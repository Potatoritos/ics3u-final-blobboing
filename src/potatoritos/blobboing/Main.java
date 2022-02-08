/*
Elliott Cheng
Cumulative Project - Create your own game
2022-1-25
An original game called "Blob Boing!!!"
 */

package potatoritos.blobboing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    // Stores image assets
    public static BufferedImage[] images;
    public static BufferedImage[] tileImages;
    public static BufferedImage[] spikeImages;
    public static BufferedImage[] levelImages;
    public static BufferedImage[] flagImages;

    public static final int IMAGE_BLOB = 0;
    public static final int IMAGE_BLOB_TIRED = 1;
    public static final int IMAGE_DIAMOND = 2;
    public static final int IMAGE_LOGO = 3;
    public static final int IMAGE_ABOUT = 4;
    public static final int IMAGE_BOX = 5;
    public static final int IMAGE_BOX_FILLED = 6;
    public static final int IMAGE_KEYS_MAIN = 7;
    public static final int IMAGE_KEYS_LEVELSELECT = 8;
    public static final int IMAGE_SELECT = 9;
    public static final int IMAGE_SELECT_N = 10;
    public static final int IMAGE_FPS_N = 12;
    public static final int IMAGE_CLEAR_BG = 22;
    public static final int IMAGE_REDZONE = 23;
    public static final int IMAGE_YELLOW = 24;
    public static final int IMAGE_BLUE = 25;
    public static final int IMAGE_FLAG_LEFT = 26;
    public static final int IMAGE_FLAG_RIGHT = 27;
    public static final int IMAGE_FLAG_UP = 28;
    public static final int IMAGE_FLAG_DOWN = 29;
    public static final int IMAGE_CLEAR_TEXT = 30;
    public static final int IMAGE_TRIANGLE = 31;

    public static void main(String[] args) {
        // Load assets
        String[] imageNames = new String[] {
                "blobowo", "blobowo_tired", "diamond", "logo", "about",
                "box", "box_filled", "keys_main", "keys_levelselect",
                "select", "select_0", "select_1", "fps_vsync",
                "fps_vsync_s", "fps_30", "fps_30_s", "fps_60", "fps_60_s",
                "fps_120", "fps_120_s", "fps_144", "fps_144_s", "clear_bg_down",
                "redzone", "yellow", "blue", "flag_left", "flag_right",
                "flag_up", "flag_down", "clear_text", "triangle"
        };
        images = new BufferedImage[imageNames.length];

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        for (int i = 0; i < images.length; i++) {
            try {
                InputStream inp = cl.getResourceAsStream(
                        "assets/" + imageNames[i] + ".png");
                images[i] = ImageIO.read(inp);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        HashSet<Integer> tileNums = new HashSet<>(Arrays.asList(
            0, 1, 2, 3, 4, 6, 8, 9, 12, 15
        ));
        HashSet<Integer> spikeNums = new HashSet<>(Arrays.asList(
                1, 2, 3, 4, 6, 8, 9, 12, 15
        ));
        tileImages = new BufferedImage[16];
        spikeImages = new BufferedImage[16];
        flagImages = new BufferedImage[16];
        for (int i = 0; i < 16; i++) {
            if (tileNums.contains(i)) {
                try {
                    InputStream inp = cl.getResourceAsStream(
                            "assets/tile_" + ((i >> 3) & 1) + ((i >> 2) & 1)
                                    + ((i >> 1) & 1) + (i & 1) + ".png"
                    );
                    tileImages[i] = ImageIO.read(inp);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (spikeNums.contains(i)) {
                try {
                    InputStream inp = cl.getResourceAsStream(
                            "assets/spikes_" + ((i >> 3) & 1) + ((i >> 2) & 1)
                                    + ((i >> 1) & 1) + (i & 1) + ".png"
                    );
                    spikeImages[i] = ImageIO.read(inp);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        levelImages = new BufferedImage[5];
        for (int i = 0; i < levelImages.length; i++) {
            try {
                InputStream inp = cl.getResourceAsStream("levels/level_" + (i + 1) + ".png");
                System.out.println("levels/level_" + (i + 1) + ".png");
                levelImages[i] = ImageIO.read(inp);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Run the game
        Game game = new Game();
        game.runGameLoop();
    }
}
