package complexNumbers;

import java.lang.Math;

/**
 * Represents mathematical complex numbers of the form a + bi,
 * where a and b are both doubles
 * @author kenneth
 * @version 9/20/29
 */

public class Complex extends Quantity
{
    /**
     * Real part
     */
    private double a;
    /**
     * Imaginary part
     */
    private double b;
    
    /**
     * Creates a complex number equal to zero
     */
    public Complex()
    {
        a = 0;
        b = 0;
    }
    
    /**
     * Creates a complex number with the given
     * real and imaginary parts
     */
     public Complex(double a, double b)
     {
         this.a = a;
         this.b = b;
     }
    
    /**
     * returns the real part of the number
     * @return the real part of the number
     */
    public double real()
    {
        return a;
    }
    
    /**
     * returns the real part of the number
     * @return the real part of the number
     */
    public Complex realAsComplex()
    {
        return new Complex(a, 0);
    }
    
    /**
     * returns the imaginary part of the number
     * @return the imaginary part of the number
     */
    public double imag()
    {
        return b;
    }
    
    /**
     * returns the imaginary part of the number
     * @return the imaginary part of the number
     * output has an imaginary part of zero and 
     * a real part of b
     */
    public Complex imagAsComplex()
    {
        return new Complex(b, 0);
    }
    
    /**
     * Represents the complex number as a String
     * of the form a + bi
     *
     * @return a String representation of the number
     * @Override
     */
    public String toString()
    {
        if (b > 0)
        {
            return "(" + a + " + " + b + "i" + ")";
        }
        else if (b < 0)
        {
            return "(" + a + " - " + (-1) * b + "i" + ")";
        }
        else
        {
            return "" + a;
        }
    }
    
    /**
     * Tests if a complex number equals another object
     * @param other the other object
     * @return true if both real and imaginary
     * parts match, false otherwise
     */
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (this.getClass() != other.getClass())
        {
            return false;
        }
        else if (this == other)
        {
            return true;
        }
        else if ((Math.abs(this.real() - ((Complex) other).real()) < 0.0001) &&
                 Math.abs(this.imag() - ((Complex) other).imag()) < 0.0001)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**,
     * Adds two complex numbers
     * 
     * @param second the second number
     * @return the sum of the two complex numbers
     */
    public Complex add(Complex second)
    {
        Complex sum = new Complex(this.real() + second.real(), this.imag() + second.imag());
        return sum;
    }
    
    /**,
     * Subtractss two complex numbers
     * 
     * @param second the second number
     * @return the difference of the two complex numbers
     */
    public Complex subtract(Complex second)
    {
        Complex difference = new Complex(this.real() - second.real(), this.imag() - second.imag());
        return difference;
    }
    
    /**,
     * finds the conjugate of a complex number
     * 
     * @return the conjugate of this number
     */
    public Complex conj()
    {
        Complex conjugate = new Complex(this.real(), (-1) * this.imag());
        return conjugate;
    }
    
    /**
     * Finds the magnitude of a complex number
     * @return magnitude (double)
     */
    public double mag()
    {
        return Math.sqrt(a * a + b * b);
    }
    
    /**
     * Finds the magnitude of a complex number
     * @return magnitude (Complex)
     */
    public Complex magAsComplex()
    {
        return new Complex(Math.sqrt(a * a + b * b), 0);
    }
    
    /**
     * Finds the argument of a complex number
     * Outputs value between zero and 2 * pi
     * @throws ArithmeticException
     * @return argument (double)
     */
    public double arg()
    {
        //positive imaginary axis
        if (a == 0 && b > 0)
        {
            return Math.PI / 2;
        }
        //negative imaginary axis
        else if (a == 0 && b < 0)
        {
            return 3 * Math.PI / 2;
        }
        //first quadrant
        else if (a > 0 && b >= 0)
        {
            return Math.atan(b/a);
        }
        //second quadrant
        else if (a < 0 && b >= 0)
        {
            return Math.PI + Math.atan(b/a);
        }
        //third quadrant
        else if (a < 0 && b < 0)
        {
            return Math.PI + Math.atan(b/a);
        }
        //fourth quadrant
        else if (a > 0 && b < 0)
        {
            return 2 * Math.PI + Math.atan(b/a);
        }
        //exactly zero
        else
        {
            throw new ArithmeticException();
        }
            
    }
    
    /**
     * Converts a double to a Complex
     * @param the original double
     * @return a Complex equivalent to the double
     */
    public static Complex toComplex(double decimal)
    {
        return new Complex(decimal, 0);
    }
    
    /**
     * Converts an int to a Complex
     * @param the original int
     * @return a Complex equivalent to the int
     */
    public static Complex intToComplex(int integer)
    {
        return new Complex((double) integer, 0);
    }
    
    /**
     * Returns true if the imaginary part is zero, false otherwise
     * @return true if the number is real
     */
    public boolean isReal()
    {
        return (Math.abs(this.imag()) < 0.000001);
    }
    
    /**
     * Converts a Complex to a double (imaginary part must be zero!)
     * @throws IllegalArgumentException
     * @return the converted Complex
     */
    public double toDouble()
    {
        if (!this.isReal())
        {
            throw new IllegalArgumentException();
        }
        else
        {
            return this.a;
        }
    }
    
    /**
     * Multiplies two complex numbers
     * 
     * @param second the second number
     * @return the product of the two complex numbers
     */
    public Complex multiply(Complex second)
    {
        double productReal = this.real() * second.real() - this.imag() * second.imag();
        double productImag = this.real() * second.imag() + this.imag() * second.real();
        return new Complex(productReal, productImag);
    }
        
    /**
     * Divides two complex numbers
     * 
     * @param second the second number
     * @return the quotient of the two complex numbers
     */
    public Complex divide(Complex second)
    {
        Complex numerator = this.multiply(second.conj());
        double denominator = second.mag() * second.mag();
        return new Complex(numerator.real() / denominator, numerator.imag() / denominator);
    }
    
    /**
     * Squares a complex number
     * 
     * @return the square of this complex number
     */
    public Complex square()
    {
        return this.multiply(this);
    }
    
    /**
     * Finds the square root of a complex number
     * (returns the one with the positive real part)
     * 
     * @return the square root of this complex number
     */
    public Complex sqrt()
    {
        //take care of this == 0 case first
        if (Math.abs(a) < 0.000001 && Math.abs(b) < 0.000001)
        {
            return new Complex();
        }
        double mag = this.mag();
        double arg = this.arg();
        if (Math.cos(arg/2) >= 0)
        {
            return new Complex(Math.sqrt(mag) * Math.cos(arg / 2), Math.sqrt(mag) * Math.sin(arg / 2));
        }
        else
        {
            return new Complex(-1 * Math.sqrt(mag) * Math.cos(arg / 2), -1 * Math.sqrt(mag) * Math.sin(arg / 2));
        }
    }
    
    /**
     * Creates a new copy of this Complex and returns that copy
     * 
     * @return a copy of this quantity
     */
    public Complex copy()
    {
        Complex copy = new Complex(this.real(), this.imag());
        return copy;
    }
    
    /**
     * Raises e to the power of this complex number
     * 
     * @return  e to the power of this complex number
     */
    public Complex exp()
    {
        double answerReal = Math.exp(this.real()) * Math.cos(this.imag());
        double answerImag = Math.exp(this.real()) * Math.sin(this.imag());
        return new Complex(answerReal, answerImag);
    }
    
    /**
     * Finds the natural logarithm of this complex number
     * (imaginary value is set between 0 and 2*pi)
     * 
     * @return  the natural logarithm of this complex number
     */
    public Complex ln()
    {
        double answerReal = Math.log(this.mag());
        double answerImag = this.arg();
        return new Complex(answerReal, answerImag);
    }
    
    /**
     * Finds the derivative of this quantity and replaces itself
     * in the function with this derivative
     * 
     * @param variableName the name of the variable with respect
     * to which this function will be differentiated
     */
    public void diff(String variableName)
    {
        //derivative of a constant is always zero
        Complex zero = new Complex();
        this.replaceWith(zero);
    }

    /**
     * Replaces every instance of a certain variable in the
     * function below this quantity with a given value
     * 
     * @param variableName the name of the variable to be replaced
     * @param value the quantity that this variable will be replaced with
     */
    @Override
    public void substitute(String variableName, Quantity value) {
        //does nothing because this is not a variable and
        //cannot have any variable children
    }
}
