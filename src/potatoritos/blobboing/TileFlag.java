package potatoritos.blobboing;

public class TileFlag extends Tile {
    // Represents a flag, the player's objective
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    private FrameTimer timer;
    private int orientation;
    public TileFlag(int x, int y, int orientation) {
        super(x, y);
        timer = new FrameTimer(140);
        this.orientation = orientation;
    }
    // Description: Updates timer
    @Override
    public void update() {
        timer.decrementFrame();
    }

    // Description: Checks whether the player is colliding with
    //              the tile. If so, makes the player win
    // Parameters:
    //      player: the player
    @Override
    public void processCollisions(Player player) {
        boolean collides = false;
        switch(orientation) {
            case LEFT -> collides = (
                    player.getPos().getX() + player.getSize() >= posXL
                    && player.getPos().getX() <= posXR + TILE_SIZE
                    && player.getPos().getY() + player.getSize() >= posYU
                    && player.getPos().getY() <= posYD
            );
            case RIGHT -> collides = (
                    player.getPos().getX() + player.getSize() >= posXL - TILE_SIZE
                            && player.getPos().getX() <= posXR
                            && player.getPos().getY() + player.getSize() >= posYU
                            && player.getPos().getY() <= posYD
            );
            case UP -> collides = (
                    player.getPos().getX() + player.getSize() >= posXL
                            && player.getPos().getX() <= posXR
                            && player.getPos().getY() + player.getSize() >= posYU
                                - TILE_SIZE
                            && player.getPos().getY() <= posYD
            );
            case DOWN -> collides = (
                    player.getPos().getX() + player.getSize() >= posXL
                            && player.getPos().getX() <= posXR
                            && player.getPos().getY() + player.getSize() >= posYU
                            && player.getPos().getY() <= posYD + TILE_SIZE
            );
        }
        if (collides) {
            timer.start();
            player.win();
        }
    }

    // Description: Draws the tile
    // Parameters:
    //      gw: a GraphicsWrapper
    @Override
    public void render(GraphicsWrapper gw) {
        if (timer.isActive()) {
            // Play collision animation
            gw.setAlpha(1/140f * (timer.getFrame()));
            double radius = 120 + (140 - timer.getFrame());
            gw.drawImage(Main.images[Main.IMAGE_BLUE],
                    posXL -radius/2 + 20, posYU - radius/2 + 20,
                    radius, radius);
            gw.setAlpha(1);
        }
        switch(orientation) {
            case LEFT -> gw.drawImage(Main.images[Main.IMAGE_FLAG_LEFT],
                    posXL-TILE_SIZE+10, posYU, 2*TILE_SIZE, TILE_SIZE);
            case RIGHT -> gw.drawImage(Main.images[Main.IMAGE_FLAG_RIGHT],
                    posXL-10, posYU, 2*TILE_SIZE, TILE_SIZE);
            case UP -> gw.drawImage(Main.images[Main.IMAGE_FLAG_UP],
                    posXL, posYU-TILE_SIZE+10, TILE_SIZE, 2*TILE_SIZE);
            case DOWN -> gw.drawImage(Main.images[Main.IMAGE_FLAG_DOWN],
                    posXL, posYU-10, TILE_SIZE, 2*TILE_SIZE);
        }
    }
}
