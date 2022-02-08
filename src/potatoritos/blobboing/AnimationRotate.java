package potatoritos.blobboing;

public class AnimationRotate extends Animation {
    private double prevRotation;
    private double targetAngle;
    public AnimationRotate() {
        prevRotation = 0;
        timer = new FrameTimer(120);
    }
    // Description: Starts the animation
    // Parameters:
    //      targetAngle: the angle to rotate to
    public void start(double targetAngle) {
        timer.start();
        this.targetAngle = targetAngle;
    }

    // Description: Applies the animation to a player
    // Parameters:
    //      player: the player
    @Override
    public void applyAnimation(Player player) {
        double rotation = targetAngle;
        double diff = rotation - prevRotation;
        double smallestDiff = Math.min(2*Math.PI - Math.abs(diff), Math.abs(diff));
        double toRotate = timer.getFrame() * Math.PI / (120 * Math.PI/smallestDiff);
        if ((diff + 2*Math.PI) % (2*Math.PI) > Math.PI) {
            rotation += toRotate;
        } else {
            rotation -= toRotate;
        }
        player.setRenderRotation(rotation);
        prevRotation = rotation;
    }
}
