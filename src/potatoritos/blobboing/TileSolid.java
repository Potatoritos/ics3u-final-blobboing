package potatoritos.blobboing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileSolid extends Tile {
    // Represents a solid tile (a tile that the player
    // can't pass through)

    private BufferedImage image;

    public TileSolid(int x, int y, BufferedImage image) {
        super(x, y);
        this.image = image;
    }

    // Description: Checks whether the player is colliding with
    //              the horizontal sides of the tile / whether the
    //              player is touching the ground
    // Parameters:
    //      player: the player
    // Return: true if the player doesn't collide and false otherwise
    private boolean processVerticalCollisions(Player player) {
        // Check if the player is hugging the tile
        if (Util.epsilonEquals(player.getPos().getY() + player.getSize(), posYU)
                || Util.epsilonEquals(player.getPos().getY(), posYD)) {
            return true;
        }
        // Collision detection
        if (player.getPos().getX() + player.getSize() >= posXL
                && posXL >= player.getPrevPos().getX() + player.getSize()
        ) {
            if (player.getGravityDirection().getX() > 0) {
                player.onTouchGround(player.getVel().getX());
            }
            player.getPos().setX(posXL - player.getSize());
            player.getVel().setX(0);
            return false;
        }
        if (posXR >= player.getPos().getX()
                && player.getPrevPos().getX() >= posXR
        ) {
            if (player.getGravityDirection().getX() < 0) {
                player.onTouchGround(player.getVel().getX());
            }
            player.getPos().setX(posXR);
            player.getVel().setX(0);
            return false;
        }
        return true;
    }

    // Description: Checks whether the player is colliding with
    //              the horizontal sides of the tile / whether the
    //              player is touching the ground
    // Parameters:
    //      player: the player
    // Return: true if the player doesn't collide and false otherwise
    private boolean processHorizontalCollisions(Player player) {
        // Check if the player is hugging the tile
        if (Util.epsilonEquals(player.getPos().getX() + player.getSize(), posXL)
                || Util.epsilonEquals(player.getPos().getX(), posXR)) {
            return true;
        }
        // Collision detection
        if (player.getPos().getY() + player.getSize() >= posYU
                && posYU >= player.getPrevPos().getY() + player.getSize()) {
            if (player.getGravityDirection().getY() > 0) {
                player.onTouchGround(player.getVel().getY());
            }
            player.getPos().setY(posYU - player.getSize());
            player.getVel().setY(0);
            return false;
        }
        if (posYD >= player.getPos().getY()
                && player.getPrevPos().getY() >= posYD) {
            if (player.getGravityDirection().getY() < 0) {
                player.onTouchGround(player.getVel().getY());
            }
            player.getPos().setY(posYD);
            player.getVel().setY(0);
            return false;
        }
        return true;
    }

    @Override
    public void update() {}

    // Description: Checks whether the player is colliding with
    //              the tile.
    // Parameters:
    //      player: the player
    @Override
    public void processCollisions(Player player) {
        if (player.getPos().getX() + player.getSize() < posXL
                || player.getPos().getX() > posXR
                || player.getPos().getY() + player.getSize() < posYU
                || player.getPos().getY() > posYD) {
            return;
        }

        // Process collisions on sides parallel to the player's
        // gravity direction first so that walls don't protrude
        // from the ground
        if (player.getGravityDirection().getY() != 0) {
            if (processVerticalCollisions(player));
                processHorizontalCollisions(player);
        } else {
            if (processHorizontalCollisions(player));
                processVerticalCollisions(player);
        }
    }

    // Description: Draws the tile
    // Parameters:
    //      gw: a GraphicsWrapper
    @Override
    public void render(GraphicsWrapper gw) {
        gw.drawImage(image, posXL, posYU, TILE_SIZE, TILE_SIZE);
    }
}
