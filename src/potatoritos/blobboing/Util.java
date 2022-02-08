package potatoritos.blobboing;

public class Util {
    static final double EPSILON = 0.000001;
    // Description: Determines if a floating point number is
    // very close to another one
    // Parameters:
    //      a and b: the numbers
    // Return: true if a is very close to b
    static boolean epsilonEquals(double a, double b) {
        return Math.abs(a - b) <= Math.max(Math.abs(a), Math.abs(b)) * EPSILON;
    }
}
