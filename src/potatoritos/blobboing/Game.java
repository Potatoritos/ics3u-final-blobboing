package potatoritos.blobboing;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Game {
    private static final int STATE_MAIN_MENU = 0;
    private static final int STATE_LEVEL_MENU = 1;
    private static final int STATE_ABOUT_MENU = 2;
    private static final int STATE_IN_LEVEL = 3;

    // The amount of nanoseconds between each update / render frame
    private double nsPerUpdate;
    private double nsPerRender;

    // Whether the game loop is running
    private boolean isRunning;

    // The state of the main/level menu
    private int state;
    private int selectIndex;
    private int levelIndex;

    // The id of the selected FPS value
    private int fpsToSet;

    // Used for the main menu move animation
    private FrameTimer selectTimer;

    // The amount of updates / renders that are queued
    private double deltaUpdate;
    private double deltaRender;

    // The game's window
    private Window window;

    // All levelsq
    private Level[] levels;

    // The current level and the player belonging to the
    // current level
    private Level level;
    private Player player;

    public Game() {
        window = new Window();
        setUpdateFPS(144);
        fpsToSet = 0;
        setRenderFPS(Math.min(144, window.getRefreshRate()));

        state = STATE_MAIN_MENU;
        selectIndex = 0;
        levelIndex = 0;

        levels = new Level[Main.levelImages.length];
        for (int i = 0; i < Main.levelImages.length; i++) {
            BufferedImage img = window.createBufferedImage();
            GraphicsWrapper imgGw = new GraphicsWrapper(img.createGraphics(),
                    (double)window.getRenderHeight() / 1080);
            levels[i] = new Level(Main.levelImages[i], img, imgGw);
        }

        isRunning = true;
        deltaUpdate = 0;
        deltaRender = 0;
        selectTimer = new FrameTimer(30);
    }

    // Description: Setter method for nsPerUpdate
    // Parameters:
    //      val: the FPS, in seconds
    public void setUpdateFPS(double val) {
        nsPerUpdate = 1e9 / val;
    }

    // Description: Setter method for nsPerRender
    // Parameters:
    //      val: the FPS, in seconds
    public void setRenderFPS(double val) {
        nsPerRender = 1e9 / val;
    }

    // Description: Runs the main game loop
    public void runGameLoop() {
        long previousTime = System.nanoTime();
        long currentTime;

        long previousFPSCalcTime = System.nanoTime();
        int counterUpdate = 0, counterRender = 0;

        while (isRunning) {
            currentTime = System.nanoTime();
            deltaUpdate += (currentTime - previousTime) / nsPerUpdate;
            deltaRender += (currentTime - previousTime) / nsPerRender;
            previousTime = currentTime;

            // Update if there are >= 1 updates queued
            if (deltaUpdate >= 1) {
                update();
                counterUpdate++;
                deltaUpdate--;
            }

            // Render if there are >= 1 renders queued
            if (deltaRender >= 1) {
                render();
                counterRender++;
                deltaRender--;
            }

            // Make sure queued updates/renders don't go too high
            deltaUpdate = Math.min(100, deltaUpdate);
            deltaRender = Math.min(100, deltaRender);

            // Calculate performance stats
            if (currentTime - previousFPSCalcTime >= 1e9) {
                System.out.printf("Update FPS: %d | Render FPS: %d | "
                        + "Queued updates: %f | Queued renders: %f\n",
                        counterUpdate, counterRender, deltaUpdate,
                        deltaRender);
                previousFPSCalcTime = currentTime;
                counterUpdate = 0;
                counterRender = 0;
            }
        }
    }

    // Description: Advances the game forward by one frame
    private void update() {
        KeyListener kl = window.getKeyListener();
        switch(state) {
            case STATE_MAIN_MENU -> {
                // Handle main menu input
                selectTimer.decrementFrame();
                if (kl.isKeyDownFrame(KeyEvent.VK_UP) && selectIndex > 0) {
                    selectIndex--;
                    selectTimer.start();
                }
                if (kl.isKeyDownFrame(KeyEvent.VK_DOWN) && selectIndex < 2) {
                    selectIndex++;
                    selectTimer.start();
                }
                if (kl.isKeyDownFrame(KeyEvent.VK_ENTER)) {
                    switch (selectIndex) {
                        case 0 -> state = STATE_LEVEL_MENU;
                        case 1 -> state = STATE_ABOUT_MENU;
                        case 2 -> {
                            fpsToSet = (fpsToSet + 1) % 5;
                            final int[] fpsVals = {window.getRefreshRate(), 30, 60, 120, 144};
                            setRenderFPS(fpsVals[fpsToSet]);
                        }
                    }
                }
                if (kl.isKeyDownFrame(KeyEvent.VK_ESCAPE)) {
                    System.exit(0);
                }
            }
            case STATE_LEVEL_MENU -> {
                // Handle level select input
                if (kl.isKeyDownFrame(KeyEvent.VK_ESCAPE)) {
                    state = STATE_MAIN_MENU;
                    return;
                }
                if (levelIndex > 0 && kl.isKeyDownFrame(KeyEvent.VK_LEFT)) {
                    levelIndex--;
                }
                if (levelIndex < levels.length-1 && kl.isKeyDownFrame(KeyEvent.VK_RIGHT)) {
                    levelIndex++;
                }
                if (kl.isKeyDownFrame(KeyEvent.VK_ENTER)) {
                    level = levels[levelIndex];
                    level.reset();
                    player = level.getPlayer();
                    resetQueues();
                    state = STATE_IN_LEVEL;
                }
            }
            case STATE_ABOUT_MENU -> {
                if (kl.isKeyDownFrame(KeyEvent.VK_ESCAPE)) {
                    state = STATE_MAIN_MENU;
                }
            }
            case STATE_IN_LEVEL -> {
                player.processInput(kl);
                level.update();
                if (kl.isKeyDownFrame(KeyEvent.VK_ESCAPE) || player.hasWon()) {
                    state = STATE_LEVEL_MENU;
                    resetQueues();
                }
                if (player.isDead()) {
                    level.reset();
                    player = level.getPlayer();
                }
            }
        }
    }

    // Description: Resets the amount of updates/renders queued
    private void resetQueues() {
        deltaUpdate = 0;
        deltaRender = 0;
    }

    // Description: Renders the game
    private void render() {
        GraphicsWrapper gw = window.getGraphics();
        switch (state) {
            case STATE_MAIN_MENU -> {
                // Draw the background and logo
                gw.setColor(new Color(0xCCCCCC));
                gw.fillRect(0, 0, 1920, 1080);
                gw.drawImage(Main.images[Main.IMAGE_LOGO],
                        477, 189, 966, 108);

                // Draw the selection menu
                gw.drawImage(Main.images[Main.IMAGE_SELECT],
                        474, 472, 700, 255);
                gw.drawImage(Main.images[Main.IMAGE_FPS_N + fpsToSet*2],
                        474, 642, 700, 79);
                int animationWidth = (int)(1401.0 / 30 * (30 - selectTimer.getFrame()));
                if (selectIndex < 2) {
                    gw.drawImage(Main.images[Main.IMAGE_SELECT_N + selectIndex],
                            0, 0, animationWidth, 510,
                            474, 472, animationWidth/2, 255);
                } else {
                    gw.drawImage(Main.images[Main.IMAGE_FPS_N + fpsToSet*2 + 1],
                            0, 0, animationWidth, 158,
                            474, 642, animationWidth/2, 79);
                }

                gw.drawImage(Main.images[Main.IMAGE_KEYS_MAIN],
                        52, 1007, 563, 41);
            }
            case STATE_LEVEL_MENU -> {
                // Draw the background and logo
                gw.setColor(new Color(0xCCCCCC));
                gw.fillRect(0, 0, 1920, 1080);
                gw.drawImage(Main.images[Main.IMAGE_LOGO],
                        477, 189, 966, 108);

                // Draw boxes for each visible level
                for (int i = 0; i < 5; i++) {
                    int levelNumber = levelIndex + i - 2;
                    // Continue if the level number is out of bounds
                    if (levelNumber < 0 || levelNumber >= levels.length)
                        continue;

                    // Draw the box; the selected level has a filled in box
                    BufferedImage img;
                    Color colour;
                    if (levelNumber == levelIndex) {
                        img = Main.images[Main.IMAGE_BOX_FILLED];
                        colour = new Color(0xCCCCCC);
                    } else {
                        img = Main.images[Main.IMAGE_BOX];
                        colour = new Color(0x2F2F2F);
                    }
                    gw.drawImage(img, 100 + 350*i, 472, 300, 300);
                    gw.setColor(colour);
                    gw.drawString("LEVEL", 135 + 350*i, 570);
                    String name = "" + (levelNumber+1);
                    if (name.length() == 1) name = "0" + name;
                    gw.drawString(name, 135 + 350*i, 650);
                }
                gw.drawImage(Main.images[Main.IMAGE_TRIANGLE], 874, 800, 151, 76);
                gw.drawImage(Main.images[Main.IMAGE_KEYS_LEVELSELECT],
                        52, 1007, 563, 41);
            }
            // Draw the about menu
            case STATE_ABOUT_MENU -> gw.drawImage(Main.images[Main.IMAGE_ABOUT],
                    0, 0, 1920, 1080);
            case STATE_IN_LEVEL -> {
                // Draw the level
                level.render(gw);
            }
        }
        // Blit the contents of the window's buffered image to
        // the screen
        window.blit();
    }
}
