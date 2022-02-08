package potatoritos.blobboing;

public class AnimationSquish extends Animation {
    private double intensity;
    private int halfPeriodLength;
    private int halfPeriods;
    public AnimationSquish() {
        intensity = 0;
        halfPeriodLength = 1;
        halfPeriods = 1;
    }
    // Description: Starts the animation
    // Parameters:
    //      intensity: the intensity of the squish
    //      halfPeriodLength: the length of a half-period
    //      halfPeriods: the amount of half periods
    public void start(double intensity, int halfPeriodLength, int halfPeriods) {
        this.intensity = intensity;
        this.halfPeriodLength = halfPeriodLength;
        this.halfPeriods = halfPeriods;
        timer = new FrameTimer(halfPeriodLength * halfPeriods);
        timer.start();
    }

    // Description: Applies the animation to a player
    // Parameters:
    //      player: the player
    @Override
    public void applyAnimation(Player player) {
        double squishAmount = Math.min(4*Math.pow(intensity, 0.7), 40);
        double x = Math.PI * (halfPeriods - (double)timer.getFrame()/ halfPeriodLength + 1);
        double squish = 20 * squishAmount * Math.sin(x) / (x*x) + 70;

        player.setRenderHeight(squish);
        player.setRenderOffsetY(70 - squish);
        double width = 70 + (70 - squish)/2;
        player.setRenderWidth(width);
        player.setRenderOffsetX((70 - width)/2);
    }
}
