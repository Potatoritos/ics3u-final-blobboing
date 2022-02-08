package potatoritos.blobboing;

public class TileRedzone extends Tile {
    // Represents a tile that kills the player

    public TileRedzone(int x, int y) {
        super(x, y);
    }
    @Override
    public void update() {}

    // Description: Checks whether the player is colliding with
    //              the tile. If so, kills the player
    // Parameters:
    //      player: the player
    @Override
    public void processCollisions(Player player) {
        // Somewhat lenient collision detection
        if (player.getPos().getX() + player.getSize() < posXL+10
                || player.getPos().getX() > posXR-10
                || player.getPos().getY() + player.getSize() < posYU+10
                || player.getPos().getY() > posYD-10) {
            return;
        }
        player.kill();
    }

    // Description: Draws the tile
    // Parameters:
    //      gw: a GraphicsWrapper
    @Override
    public void render(GraphicsWrapper gw) {
        gw.drawImage(Main.images[Main.IMAGE_REDZONE], posXL, posYU, TILE_SIZE, TILE_SIZE);
    }
}
