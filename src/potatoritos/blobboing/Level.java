package potatoritos.blobboing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Level {
    // The amount of tiles in a horizontal / vertical slice
    public static final int SIZE_X = 48;
    public static final int SIZE_Y = 27;

    // The grid of tiles
    private Tile[][] tiles;

    // The player and spawn position of the player
    private Player player;
    private Vec spawnPos;

    // The level's image file (contains tile placements)
    private BufferedImage image;

    // A BufferedImage and its accompanying GraphicsWrapper
    // to write any "static" (without animations) tile's render
    // (Rendering all tiles every frame is too slow)
    private BufferedImage tileBackground;
    private GraphicsWrapper gwBackground;

    public Level(BufferedImage image, BufferedImage bgImage, GraphicsWrapper bgGw) {
        this.image = image;
        tiles = new Tile[SIZE_Y][SIZE_X];

        this.tileBackground = bgImage;
        this.gwBackground = bgGw;
    }

    // Description: Creates a bitmask containing information about
    //              adjacent tiles
    // Parameters:
    //      image: the image the tile is in
    //          x: the x coordinate of the tile
    //          y: the y coordinate of the tile
    // Return: the bitmask
    private static int getAdjBitmask(BufferedImage image, int x, int y) {
        int adjBitmask = 0;
        // If there is a neighbour to the left
        // or if this tile is on the left edge of the screen
        if (x == 0 || image.getRGB(x-1, y) == 0xFF000000)
            adjBitmask |= 8;
        // ... up
        if (y == 0 || image.getRGB(x, y-1) == 0xFF000000)
            adjBitmask |= 4;
        // ... right
        if (x == SIZE_X-1 || image.getRGB(x+1, y) == 0xFF000000)
            adjBitmask |= 2;
        // ... down
        if (y == SIZE_Y-1 || image.getRGB(x, y+1) == 0xFF000000)
            adjBitmask |= 1;
        return adjBitmask;
    }

    // Description: (re-)Initializes tiles and the player. Is called
    //              every time the player dies/enters the level.
    public void reset() {
        spawnPos = new Vec();
        gwBackground.setColor(new Color(0xCCCCCC));
        gwBackground.fillRect(0, 0, 1920, 1080);

        // Iterate through all tiles in image
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                Tile tile;
                // Add a tile to tiles according to the RGB value of
                // the current cell in image
                switch(image.getRGB(j, i)) {
                    // case black (representing solid tile)
                    case 0xFF000000 -> {
                        BufferedImage img;

                        // A bitmask containing information about
                        // neighbouring tiles. This is used to determine
                        // which image the TileSolid will use.
                        int adjBitmask = getAdjBitmask(image, j, i);
                        if (Main.tileImages[adjBitmask] == null) {
                            // Use the default (completely filled in) tile image
                            // if configuration of neighbours is not in tileImages
                            img = Main.tileImages[15];
                        } else {
                            img = Main.tileImages[adjBitmask];
                        }
                        tile = new TileSolid(j, i, img);

                        // Add this tile to gwBackground
                        tile.render(gwBackground);
                    }
                    // case red (representing redzone)
                    case 0xFFFF0000 -> {
                        tile = new TileRedzone(j, i);

                        // Add this tile to gwBackground
                        tile.render(gwBackground);
                    }
                    // case magenta (representing spikes)
                    case 0xFFFF00FF -> {
                        BufferedImage img;

                        // A bitmask containing information about
                        // neighbouring TileSolids. This is used to determine
                        // which image the TileSpike will use.
                        int adjBitmask = getAdjBitmask(image, j, i);

                        if (Main.spikeImages[adjBitmask] == null) {
                            // Use the default (completely filled in) tile image
                            // if configuration of neighbours is not in tileImages
                            img = Main.spikeImages[15];
                        } else {
                            img = Main.spikeImages[adjBitmask];
                        }
                        tile = new TileSpike(j, i, img, adjBitmask);

                        // Add this tile to gwBackground
                        tile.render(gwBackground);
                    }
                    // case green (representing orb tile)
                    case 0xFF00FF00 -> tile = new TileOrb(j, i);
                    // case blue (representing flag tile)
                    case 0xFF0000FF -> {
                        int adjBitmask = getAdjBitmask(image, j, i);
                        if ((adjBitmask & 8) != 0) {
                            tile = new TileFlag(j, i, TileFlag.RIGHT);
                        } else if ((adjBitmask & 4) != 0) {
                            tile = new TileFlag(j, i, TileFlag.DOWN);
                        } else if ((adjBitmask & 2) != 0) {
                            tile = new TileFlag(j, i, TileFlag.LEFT);
                        } else {
                            tile = new TileFlag(j, i, TileFlag.UP);
                        }
                    }
                    // case yellow (representing the spawn point)
                    case 0xFFFFFF00 -> {
                        spawnPos.set(j*Tile.TILE_SIZE, i*Tile.TILE_SIZE);
                        tile = new TileAir();
                    }
                    // otherwise, set the tile to an air tile
                    default -> tile = new TileAir();
                }
                tiles[i][j] = tile;
            }
        }
        // Initialize player
        player = new Player(spawnPos);
    }

    // Description: Getter method for player
    // Return: the player
    public Player getPlayer() {
        return player;
    }

    // Description: Updates the level. Is called every update frame
    public void update() {
        // Update the player
        if (!player.isDead() && !player.isWinning()) {
            player.setIsOnGround(false);
            processCollisions(player);
        }
        player.update();

        // Update all tiles
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                tiles[i][j].update();
            }
        }
    }

    // Description: Determines whether the player collides with any tile.
    // Parameters:
    //      player: the player
    public void processCollisions(Player player) {
        // Check every tile in a 4x4 grid around the player
        for (int i = Math.max((int)(player.getPos().getY() / Tile.TILE_SIZE)-1, 0);
                i <= Math.min((int)Math.ceil((player.getPos().getY()
                        + player.getSize()) / Tile.TILE_SIZE), SIZE_Y-1); i++) {
            for (int j = Math.max((int)(player.getPos().getX() / Tile.TILE_SIZE)-1, 0);
                    j <= Math.min((int)Math.ceil((player.getPos().getX()
                            + player.getSize()) / Tile.TILE_SIZE), SIZE_X-1); j++) {
                // Disregard inactive tiles
                if (tiles[i][j].isActive())
                    tiles[i][j].processCollisions(player);
            }
        }
    }

    // Description: Draws the level. Is called every render frame
    // Parameters:
    //      gw: a GraphicsWrapper
    public void render(GraphicsWrapper gw) {
        // Draw the background (containing all TileSolid renders)
        gw.drawImage(tileBackground, 0, 0, 1920, 1080);

        // Draw every other tile
        for (int i = 0; i < SIZE_Y; i++) {
            for (int j = 0; j < SIZE_X; j++) {
                if (tiles[i][j] instanceof TileOrb
                    || tiles[i][j] instanceof TileFlag)
                    tiles[i][j].render(gw);
            }
        }

        // Draw the player
        player.render(gw);
    }
}
