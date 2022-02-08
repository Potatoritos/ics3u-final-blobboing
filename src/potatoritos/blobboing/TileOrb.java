package potatoritos.blobboing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TileOrb extends Tile {
    // Represents a tile that resets the player's gravity charge

    private FrameTimer inactiveTimer;
    public TileOrb(int x, int y) {
        super(x, y);
        inactiveTimer = new FrameTimer(300);
    }

    // Description: Updates inactiveTimer and active
    @Override
    public void update() {
        inactiveTimer.decrementFrame();
        if (!inactiveTimer.isActive())
            active = true;
    }

    // Description: Checks whether the player is colliding with
    //              the tile. If so, resets the player's gravity charge
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
        // Don't consume if the player is still able to change gravity
        if (player.hasGravityCharge())
            return;

        // Reset the player's ability to change gravity
        player.resetGravityCharge();
        inactiveTimer.start();
        active = false;
    }

    // Description: Draws the tile
    // Parameters:
    //      gw: a GraphicsWrapper
    @Override
    public void render(GraphicsWrapper gw) {
        double shrink;
        // If the orb has recently been consumed:
        if (inactiveTimer.getFrame() > 30) {
            if (inactiveTimer.getFrame() > 220) {
                // Play an explosion animation
                gw.setAlpha(1/80f * (inactiveTimer.getFrame() - 220));
                gw.drawImage(Main.images[Main.IMAGE_DIAMOND],
                        posXL-20, posYU-20, 80, 80);
                gw.setAlpha(1);
            }
            // Make the orb become smaller
            shrink = Math.min((300 - inactiveTimer.getFrame()), 30);
        } else {
            // Make the orb become bigger right before it fully regenerates
            shrink = inactiveTimer.getFrame();
        }
        gw.drawImage(Main.images[Main.IMAGE_DIAMOND], posXL+shrink/2,
                posYU+shrink/2, 40-shrink, 40-shrink);
    }
}
