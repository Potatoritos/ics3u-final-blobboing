package potatoritos.blobboing;

public class Vec {
    // Represents a 2-D vector

    // The x and y components
    private double x;
    private double y;

    public Vec() {
        this.x = 0;
        this.y = 0;
    }
    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vec(Vec vec) {
        this.x = vec.getX();
        this.y = vec.getY();
    }

    // Description: Getter method for x
    // Return: the x component of the vector
    public double getX() {
        return x;
    }

    // Description: Getter method for y
    // Return: the y component of the vector
    public double getY() {
        return y;
    }

    // Description: Setter method for x
    // Parameters:
    //      val: the new value of the x component
    public void setX(double val) {
        this.x = val;
    }

    // Description: Setter method for y
    // Parameters:
    //      val: the new value of the y component
    public void setY(double val) {
        this.y = val;
    }

    // Description: Adds a vector to self
    // Parameters:
    //      x: the x value to add
    //      y: the y value to add
    // Return: self
    public Vec addSelf(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }
    // Description: Adds a vector to self
    // Parameters:
    //      vec: the vector to add
    // Return: self
    public Vec addSelf(Vec vec) {
        return addSelf(vec.getX(), vec.getY());
    }

    // Description: Implements vector addition
    // Parameters:
    //      x: the x value to add
    //      y: the y value to add
    // Return: a new vector with values after adding
    public Vec add(double x, double y) {
        return new Vec(this.x + x, this.y + y);
    }

    // Description: Implements vector addition
    // Parameters:
    //      vec: the vector to add
    // Return: A new vector with values after adding
    public Vec add(Vec vec) {
        return new Vec(this.x + vec.getX(), this.y + vec.getY());
    }

    // Description: Setter method for x and y
    // Parameters:
    //      x: the new value for the x component
    //      y: the new value for the y component
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // Description: Setter method for x and y
    // Parameters:
    //      vec: the vector to set values to
    public void set(Vec vec) {
        set(vec.getX(), vec.getY());
    }

    // Description: Scales the vector by some amount
    // Parameters:
    //      amount: the scaling factor
    // Return: self
    public Vec scaleSelf(double amount) {
        x *= amount;
        y *= amount;
        return this;
    }
    // Description: Returns a scaled version of the vector
    // Parameters:
    //      amount: the scaling factor
    // Return: a new vector with scaled values
    public Vec scale(double amount) {
        return new Vec(x * amount, y * amount);
    }

    // Description: Non-uniformly scales the vector
    // Parameters:
    //      vec: the scaling factors
    // Return: self
    public Vec scaleSelf(Vec vec) {
        x *= vec.getX();
        y *= vec.getY();
        return this;
    }
    // Description: Returns a non-uniformly scaled version of the vector
    // Parameters:
    //      vec: the scaling factors
    // Return: a new vector with scaled values
    public Vec scale(Vec vec) {
        return new Vec(x * vec.getX(), y * vec.getY());
    }
    // Description: Non-uniformly scales the vector
    // Parameters:
    //      x: the amount to scale the x component
    //      y: the amount to scale the y component
    // Return: self
    public Vec scaleSelf(double x, double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }
    // Description: Returns a non-uniformly scaled version of the vector
    // Parameters:
    //      x: the amount to scale the x component
    //      y: the amount to scale the y component
    // Return: a new vector with scaled values
    public Vec scale(double x, double y) {
        return new Vec(this.x * x, this.y * y);
    }

    // Description: Gets the length of the vector
    // Return: the length of the vector
    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

    // Description: Gets the angle between the vector and the positive x-axis
    // Return: The angle between the vector and the positive x-axis
    public double getAngle() {
        return Math.atan2(y, x);
    }

    // Description: Rotates the vector by an angle
    // Parameters:
    //      angle: The angle to rotate the vector by
    // Return: self
    public Vec rotate(double angle) {
        return new Vec(getLength()*Math.cos(getAngle() + angle),
                getLength()*Math.sin(getAngle() + angle));
    }
    // Description: Rotates the vector by 90° left
    // Return: self
    public Vec rot90Left() {
        return new Vec(-y, x);
    }
    // Description: Rotates the vector by 90° right
    // Return: self
    public Vec rot90Right() {
        return new Vec(y, -x);
    }
    // Description: Rotates the vector by 180°
    // Return: self
    public Vec rot180() {
        return new Vec(-x, -y);
    }

    // Description: Gets the sum of x and y
    // Return: the sum of x and y
    public double getSum() {
        return x + y;
    }

    // Description: Gets a representation of the vector in string form
    // Return: The vector in string form
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
