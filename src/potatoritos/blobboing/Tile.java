package potatoritos.blobboing;

import java.awt.*;

public abstract class Tile {
    static final int TILE_SIZE = 40;

    // the x and y coordinate of the tile's location in the
    // level grid
    protected int x;
    protected int y;

    // the coordinates of the tile's corners (used for
    // collision detection)
    protected int posXL;
    protected int posXR;
    protected int posYU;
    protected int posYD;

    // whether the tile is active (inactive tiles are ignored)
    protected boolean active;

    public Tile() {
        this(0, 0);
    }
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        posXL = TILE_SIZE * this.x;
        posXR = posXL + TILE_SIZE;
        posYU = TILE_SIZE * this.y;
        posYD = posYU + TILE_SIZE;
        active = true;
    }

    // Description: Getter method for active
    // Return: whether the tile is active
    public boolean isActive() { return active; }

    // Description: Updates the tile. Is called every update frame
    public abstract void update();

    // Description: Determines whether the tile collides with the player
    // Parameters:
    //      player: the player
    public abstract void processCollisions(Player player);

    // Description: Draws the tile
    // Parameters:
    //      gw: a GraphicsWrapper attached to a BufferedImage
    public abstract void render(GraphicsWrapper gw);
}
