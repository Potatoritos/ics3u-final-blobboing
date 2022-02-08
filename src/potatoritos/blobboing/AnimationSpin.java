package potatoritos.blobboing;

public class AnimationSpin extends Animation {
    public AnimationSpin() {
        timer = new FrameTimer(31);
    }
    // Description: Starts the animation
    public void start() {
        timer.start();
    }

    // Description: Applies the animation to a player
    // Parameters:
    //      player: the player
    @Override
    public void applyAnimation(Player player) {
        player.setRenderRotation(player.getGravityDirection().rot90Right().getAngle()
                + (31-timer.getFrame()) * 12 * Math.PI / 180);
    }
}
