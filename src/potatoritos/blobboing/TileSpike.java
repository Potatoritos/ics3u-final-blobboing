package potatoritos.blobboing;

import java.awt.image.BufferedImage;

public class TileSpike extends Tile {
    // Represents a spike obstacle

    private BufferedImage image;
    private int adjBitmask;

    public TileSpike(int x, int y, BufferedImage image, int adjBitmask) {
        super(x, y);
        this.image = image;
        this.adjBitmask = adjBitmask;
    }

    @Override
    public void update() {}

    // Description: Checks whether the player is colliding with
    //              the tile. If so, kills the player
    // Parameters:
    //      player: the player
    @Override
    public void processCollisions(Player player) {
        int count = Integer.bitCount(adjBitmask);
        boolean collides = false;

        // If the spike is attached to only one side:
        if (count == 1) {
            // Rectangular hitbox based on the spike's direction
            // (very lenient)
            if ((adjBitmask & 8) != 0
                    && player.getPos().getX() + player.getSize() >= posXL
                    && player.getPos().getX() <= posXR-32
                    && player.getPos().getY() + player.getSize() > posYU+18
                    && player.getPos().getY() < posYD-18) {
                collides = true;
            } else if ((adjBitmask & 4) != 0
                    && player.getPos().getX() + player.getSize() > posXL+18
                    && player.getPos().getX() < posXR-18
                    && player.getPos().getY() + player.getSize() >= posYU
                    && player.getPos().getY() <= posYD-32) {
                collides = true;
            } else if ((adjBitmask & 2) != 0
                    && player.getPos().getX() + player.getSize() >= posXL+32
                    && player.getPos().getX() <= posXR
                    && player.getPos().getY() + player.getSize() > posYU+18
                    && player.getPos().getY() < posYD-18) {
                collides = true;
            } else if ((adjBitmask & 1) != 0
                    && player.getPos().getX() + player.getSize() > posXL+18
                    && player.getPos().getX() < posXR-18
                    && player.getPos().getY() + player.getSize() >= posYU+32
                    && player.getPos().getY() <= posYD) {
                collides = true;
            }
            // Regular square hitbox (also very lenient)
        } else if (player.getPos().getX() + player.getSize() > posXL+18
                && player.getPos().getX() < posXR-18
                && player.getPos().getY() + player.getSize() > posYU+18
                && player.getPos().getY() < posYD-18) {
            collides = true;
        }

        if (collides) {
            player.kill();
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
