package potatoritos.blobboing;

public abstract class Animation {
    // Used to animate player movements

    protected FrameTimer timer;
    public Animation() {
        timer = new FrameTimer(0);
    }

    // Description: Decrements the internal FrameTimer
    public void decrementFrame() {
        timer.decrementFrame();
    }

    // Description: Ends the animation
    public void end() {
        timer.end();
    }

    // Description: Applies the animation to a player
    //              (if the animation is active)
    // Parameters:
    //      player: the player
    public void apply(Player player) {
        if (timer.isActive()) {
            applyAnimation(player);
        }
    }

    // Description: Applies the animation to a player
    // Parameters:
    //      player: the player
    public abstract void applyAnimation(Player player);
}
