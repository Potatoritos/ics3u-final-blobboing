package potatoritos.blobboing;

public class FrameTimer {
    // Represents a timer that counts frames

    private int frame;
    private int startingFrame;

    public FrameTimer(int startingFrame) {
        this.startingFrame = startingFrame;
        frame = 0;
    }

    // Description: Getter method for frame
    // Return: the current frame
    public int getFrame() {
        return frame;
    }

    // Description: Setter method for frame
    // Parameters:
    //      val: the new value for frame
    public void setFrame(int val) {
        frame = val;
    }

    // Description: Getter method for startingFrame
    // Return: the starting frame
    public int getStartingFrame() {
        return startingFrame;
    }

    // Description: Starts the timer
    public void start() {
        frame = startingFrame;
    }

    // Description: Decreases the frame by one.
    //              This function is called every update
    public void decrementFrame() {
        if (frame > 0) frame--;
    }

    // Description: Increases the frame by one
    public void incrementFrame() {
        frame++;
    }

    // Description: Stops the timer
    public void end() {
        frame = 0;
    }

    // Description: Checks whether the timer is active
    // Return: true if the timer is active
    public boolean isActive() {
        return frame > 0;
    }
}
