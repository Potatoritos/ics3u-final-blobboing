package potatoritos.blobboing;

import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.jar.JarEntry;

public class Player {
    // The player's position and velocity vectors
    private Vec pos;
    private Vec vel;

    // The player's position in the previous frame.
    // Is used for collision detection
    private Vec prevPos;


    // A unit vector storing the direction of gravity
    private Vec gravityDirection;

    // The strength of gravity
    private double gravity;

    // A unit vector storing the direction of gravity in the
    // previous frame
    private Vec prevGravityDirection;

    // Whether the player is able to switch gravity
    private boolean canSwitchGravity;

    // The direction of the next gravity change. Is used with
    // gravitySwitchTimer to make gravity switch timing more
    // lenient
    private Vec queuedGravityDirection;

    // The side length of the player's square hitbox
    private double size;


    // Stores the state of the currently playing ground pound
    private FrameTimer groundPoundTimer;

    // The following 4 FrameTimers are used to make timings more
    // lenient (they queue actions that can't be done right now,
    // but can be done very soon)

    // Activated when the player presses the ground pound key
    private FrameTimer groundPoundQueueTimer;

    // Activated when the player presses the jump key
    private FrameTimer jumpTimer;

    // Activated when the player touches the ground. Makes ledge
    // jumps easier
    private FrameTimer coyoteTimer;

    // Activated when the player switches gravity
    private FrameTimer gravitySwitchTimer;

    // Activated when the player dies
    private FrameTimer deathTimer;

    // Activated when the player wins
    private FrameTimer winTimer;

    // An array of all FrameTimers. Used to update all FrameTimers
    private FrameTimer[] frameTimers;


    // Activated whenever the player touches the ground.
    // Scales with impact velocity
    private AnimationSquish squishAnimation;

    // Activated whenever the player switches gravity
    private AnimationRotate rotateAnimation;

    // Activated whenever the player ground pounds
    private AnimationSpin spinAnimation;

    // An array of all Animations. Used to update all Animations
    private Animation[] animations;


    // Whether the player is in contact with the ground
    private boolean onGround;

    // Whether the player is moving right or left
    private boolean movingRight;
    private boolean movingLeft;

    // Used for logic to make it so that pressing right
    // after (but still holding) left makes the player go right,
    // and vice versa
    private boolean holdingMoveRight;
    private boolean holdingMoveLeft;
    private boolean movingLeftPriority;
    private boolean movingRightPriority;

    // Whether the player is holding the jump key
    private boolean holdingJumpKey;

    // Player render options. Changed by Animations
    private double renderHeight;
    private double renderWidth;
    private double renderOffsetX;
    private double renderOffsetY;
    private double renderRotation;

    // Whether the player is facing left. If false,
    // the player is facing right.
    private boolean facingLeft;

    // Whether the player is dead
    private boolean dead;

    // Whether the player has cleared the level
    private boolean won;

    public Player(Vec spawnPos) {
        pos = new Vec();
        pos.set(spawnPos);

        vel = new Vec();
        gravityDirection = new Vec(0, 1);

        prevGravityDirection = new Vec();

        renderHeight = 70;
        renderWidth = 70;
        renderOffsetY = 0;
        renderOffsetX = 0;

        prevPos = new Vec(pos);

        size = 50;

        jumpTimer = new FrameTimer(24);
        coyoteTimer = new FrameTimer(15);
        gravitySwitchTimer = new FrameTimer(24);
        groundPoundQueueTimer = new FrameTimer(24);
        groundPoundTimer = new FrameTimer(137);
        deathTimer = new FrameTimer(80);
        winTimer = new FrameTimer(300);

        frameTimers = new FrameTimer[] {
                jumpTimer, coyoteTimer, gravitySwitchTimer, groundPoundTimer,
                groundPoundQueueTimer, deathTimer, winTimer
        };
        squishAnimation = new AnimationSquish();
        rotateAnimation = new AnimationRotate();
        spinAnimation = new AnimationSpin();
        animations = new Animation[] {rotateAnimation, squishAnimation, spinAnimation};

        renderRotation = 0;
        canSwitchGravity = false;
        dead = false;
        won = false;
    }

    // Description: Updates the player. Is called every update frame
    public void update() {
        // Update all FrameTimers and Animations
        for (FrameTimer timer : frameTimers) {
            timer.decrementFrame();
        }
        for (Animation animation : animations) {
            animation.decrementFrame();
        }
        // Freeze upon level clear
        if (winTimer.isActive()) {
            if (winTimer.getFrame() == 1) {
                won = true;
            }
            // Make the player continue moving for a bit
            pos.addSelf(vel);
            vel.scaleSelf(0.97);
            return;
        }

        // Freeze upon death
        if (deathTimer.isActive()) {
            if (deathTimer.getFrame() == 1) {
                dead = true;
            }
            return;
        }

        prevPos.set(pos);

        // Start coyoteTimer if the player is touching the ground
        if (onGround) {
            coyoteTimer.start();
        }

        // Handle gravity switching
        if (gravitySwitchTimer.isActive() && canSwitchGravity) {
            prevGravityDirection.set(gravityDirection);
            gravityDirection.set(queuedGravityDirection);
            canSwitchGravity = false;
            rotateAnimation.start(gravityDirection.rot90Right().getAngle());
            gravitySwitchTimer.end();
        }

        // Handle jumping
        if (coyoteTimer.isActive() && jumpTimer.isActive() && !groundPoundTimer.isActive()) {
            vel.addSelf(gravityDirection.rot180().scale(5.5));
            jumpTimer.end();
            coyoteTimer.end();
        }

        // Handle moving left/right
        if (movingRight) {
            facingLeft = false;
            double sum = vel.scale(gravityDirection.rot90Right()).getSum();
            if (sum <= 3) {
                vel.addSelf(gravityDirection.rot90Right().scale(Math.min(0.2, 3 - sum)));
            }
        } else if (movingLeft) {
            facingLeft = true;
            double sum = vel.scale(gravityDirection.rot90Right()).getSum();
            if (sum >= -3) {
                vel.addSelf(gravityDirection.rot90Right().scale(Math.max(-0.2, -3 - sum)));
            }
        }

        // Handle friction
        double sum = vel.scale(gravityDirection.rot90Right()).getSum();
        double friction;
        if (onGround) {
            friction = 0.1;
        } else {
            friction = 0.03;
        }
        if (sum > 0) {
            vel.addSelf(gravityDirection.rot90Left().scale(Math.min(friction, sum)));
        } else if (sum < 0) {
            vel.addSelf(gravityDirection.rot90Right().scale(Math.min(friction, -sum)));
        }

        // Handle gravity
        if (coyoteTimer.isActive() || onGround || groundPoundTimer.getFrame() > 97) {
            gravity = 0;
        } else if (holdingJumpKey && vel.scale(gravityDirection).getSum() <= 1) {
            gravity = 0.08;
        } else {
            gravity = 0.15;
        }

        // Handle ground pounding
        if (!groundPoundTimer.isActive() && groundPoundQueueTimer.isActive()) {
            vel.set((new Vec(2,0)).rotate(gravityDirection.rot180().getAngle()));
            rotateAnimation.end();
            spinAnimation.start();
            groundPoundTimer.start();
        }
        if (groundPoundTimer.isActive()) {
            if (groundPoundTimer.getFrame() == 67) {
                vel.addSelf((new Vec(6, 6)).scale(gravityDirection));
            } else if (groundPoundTimer.getFrame() == 65 && !onGround) {
                // Stall the ground pound FrameTimer until the player
                // hits the ground
                groundPoundTimer.incrementFrame();
            } else if (groundPoundTimer.getFrame() == 16 && onGround) {
                // Make the player jump at the end of the ground pound
                // (higher than a regular jump)
                // unless the player is in the air
                vel.addSelf((new Vec(6, 6)).scale(gravityDirection.rot180()));
            }
        }

        vel.addSelf(gravityDirection.scale(gravity));
        pos.addSelf(vel);
    }

    // Description: Draws the player. Is called every render frame
    // Parameters:
    //      gw: a GraphicsWrapper
    public void render(GraphicsWrapper gw) {
        if (winTimer.isActive()) {
            // Draw win animation
            gw.drawImage(Main.images[Main.IMAGE_CLEAR_BG],
                    443, 370 - 60*Math.log(300-winTimer.getFrame()),
                    1034, 317);
            gw.drawImage(Main.images[Main.IMAGE_CLEAR_TEXT],
                    443, 370 - 75*Math.log(300-winTimer.getFrame()),
                    1034, 317);
            gw.setAlpha(1/300f * winTimer.getFrame());
        }
        if (deathTimer.isActive()) {
            // Draw death animation
            gw.setAlpha(1/80f * (deathTimer.getFrame()));
            double radius = 70 + (80 - deathTimer.getFrame());
            gw.drawImage(Main.images[Main.IMAGE_YELLOW],
                    pos.getX() -radius/2 + 20, pos.getY() - radius/2 + 20,
                    radius, radius);
            gw.setAlpha(1);
            return;
        }
        AffineTransform prevTransform = gw.getTransform();

        // Apply animations
        renderHeight = 70;
        renderWidth = 70;
        renderOffsetY = 0;
        renderOffsetX = 0;
        renderRotation = gravityDirection.rot90Right().getAngle();
        for (Animation animation : animations) {
            animation.apply(this);
        }

        // Rotate gw (centered on the player)
        gw.rotate(renderRotation, pos.getX() + size/2, pos.getY() + size/2);

        // Draw the player
        int srcx1, srcy1, srcx2, srcy2;
        if (facingLeft) {
            srcx1 = 0; srcy1 = 0; srcx2 = 128; srcy2 = 128;
        } else {
            srcx1 = 128; srcy1 = 0; srcx2 = 0; srcy2 = 128;
        }
        BufferedImage img;
        if (canSwitchGravity) img = Main.images[Main.IMAGE_BLOB];
        else img = Main.images[Main.IMAGE_BLOB_TIRED];
        gw.drawImage(img, srcx1, srcy1, srcx2, srcy2,
                pos.getX()-10+renderOffsetX, pos.getY()-5+renderOffsetY,
                renderWidth, renderHeight);

        // Reset any transformations applied to gw
        gw.setTransform(prevTransform);
        gw.setAlpha(1);
    }

    // Description: Checks whether the player has won / is about to win
    // Return: true if the player is winning
    public boolean isWinning() {
        return winTimer.isActive() || won;
    }

    // Description: Checks whether the player has won
    // Return: true if the player has won
    public boolean hasWon() {
        return won;
    }

    // Description: Plays the win animation. Is called when the player
    //              clears a level
    public void win() {
        winTimer.start();
    }

    // Description: Checks whether the player is dead
    // Return: true if the player is dead
    public boolean isDead() {
        return dead;
    }

    // Description: Plays the death animation
    public void kill() {
        if (deathTimer.isActive())
            return;
        deathTimer.start();
    }

    // Description: Getter method for prevPos
    // Return: the player's position in the previous frame
    public Vec getPrevPos() {
        return prevPos;
    }

    // Description: Getter method for pos
    // Return: the player's position
    public Vec getPos() {
        return pos;
    }

    // Description: Getter method for vel
    // Return: the player's velocity
    public Vec getVel() {
        return vel;
    }

    // Description: Getter method for size
    // Return: the player's size
    public double getSize() { return size; }

    // Description: Getter method for gravityDirection
    // Return: the player's gravity direction
    public Vec getGravityDirection() {
        return gravityDirection;
    }

    // Description: Getter method for canSwitchGravity
    // Return: true if the player is able to switch gravity
    public boolean hasGravityCharge() {
        return canSwitchGravity;
    }

    // Description: Setter method for onGround
    // Parameters:
    //      val: the new value for onGround
    public void setIsOnGround(boolean val) {
        onGround = val;
    }

    // Description: Indicates that the player is in contact with the ground
    // Parameters:
    //      impactSpeed: the speed at which the player hit the ground
    public void onTouchGround(double impactSpeed) {
        onGround = true;
        canSwitchGravity = true;
        coyoteTimer.start();

        // Play a squish animation if the impact speed is high enough
        impactSpeed = Math.abs(impactSpeed);
        if (impactSpeed <= 0.16) {
            return;
        }
        squishAnimation.start(impactSpeed*1.5, 60, 6);
    }

    // Description: Makes the player move left
    public void startMovingLeft() {
        if (!holdingMoveLeft) {
            movingLeftPriority = true;
            movingRightPriority = false;
            holdingMoveLeft = true;
        }
        if (movingRightPriority) movingLeft = false;
        else movingLeft = true;
    }
    // Description: Makes the player stop moving left
	public void stopMovingLeft() {
		holdingMoveLeft = false;
		movingLeft = false;
		movingLeftPriority = false;
	}
    // Description: Makes the player move right
	public void startMovingRight() {
		if (!holdingMoveRight) {
			movingRightPriority = true;
			movingLeftPriority = false;
			holdingMoveRight = true;
		}
		if (movingLeftPriority) movingRight = false;
		else movingRight = true;
	}
    // Description: Makes the player stop moving right
	public void stopMovingRight() {
		holdingMoveRight = false;
		movingRight = false;
		movingRightPriority = false;
	}

    // Description: Makes the player jump
    public void startJumping() {
        if (holdingJumpKey)
            return;
        holdingJumpKey = true;
        jumpTimer.start();
    }
    // Description: Indicates that the jump key has released
    public void stopJumping() {
        holdingJumpKey = false;
    }

    // Description: Sets the direction of gravity
    // Parameters:
    //      x: the x component of the gravity direction
    //      y: the y component of the gravity direction
    private void setGravityDirection(int x, int y) {
        if (gravityDirection.getX() == x && gravityDirection.getY() == y) {
            groundPoundQueueTimer.start();
            return;
        }
        queuedGravityDirection = new Vec(x, y);
        gravitySwitchTimer.start();
    }

    // Description: Allows the player to switch gravity
    public void resetGravityCharge() {
        canSwitchGravity = true;
    }

    // Description: Setter method for renderWidth
    // Parameters:
    //      val: the new value for renderWidth
    public void setRenderWidth(double val) { renderWidth = val; }
    // Description: Setter method for renderHeight
    // Parameters:
    //      val: the new value for renderHeight
    public void setRenderHeight(double val) { renderHeight = val; }
    // Description: Setter method for renderOffsetX
    // Parameters:
    //      val: the new value for renderOffsetX
    public void setRenderOffsetX(double val) { renderOffsetX = val; }
    // Description: Setter method for renderOffsetY
    // Parameters:
    //      val: the new value for renderOffsetY
    public void setRenderOffsetY(double val) { renderOffsetY = val; }
    // Description: Setter method for renderRotation
    // Parameters:
    //      val: the new value for renderRotation
    public void setRenderRotation(double val) { renderRotation = val; }

    // Description: Moves the player based on keyboard input
    // Parameters:
    //      kl: a KeyListener
    public void processInput(KeyListener kl) {
        // Move right if L is held
        if (kl.isKeyDown(KeyEvent.VK_L)) {
            startMovingRight();
        } else {
            stopMovingRight();
        }
        // Move left if J is held
        if (kl.isKeyDown(KeyEvent.VK_J)) {
            startMovingLeft();
        } else {
            stopMovingLeft();
        }
        // Jump if I is pressed; jump higher (reduce gravity
        // on the way up) if I is held
        if (kl.isKeyDown(KeyEvent.VK_I)) {
            startJumping();
        } else {
            stopJumping();
        }
        // Shift gravity left if A is pressed
        if (kl.isKeyDownFrame(KeyEvent.VK_A) || kl.isKeyDownFrame(KeyEvent.VK_LEFT)) {
            setGravityDirection(-1, 0);
        }
        // Shift gravity right if D is pressed
        if (kl.isKeyDownFrame(KeyEvent.VK_D) || kl.isKeyDownFrame(KeyEvent.VK_RIGHT)) {
            setGravityDirection(1, 0);
        }
        // Shift gravity up if W is pressed
        if (kl.isKeyDownFrame(KeyEvent.VK_W) || kl.isKeyDownFrame(KeyEvent.VK_UP)) {
            setGravityDirection(0, -1);
        }
        // Shift gravity down if S is pressed
        if (kl.isKeyDownFrame(KeyEvent.VK_S) || kl.isKeyDownFrame(KeyEvent.VK_DOWN)) {
            setGravityDirection(0, 1);
        }
    }
}
