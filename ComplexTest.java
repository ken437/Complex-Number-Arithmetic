/**
 * 
 */
package complexNumbers;

import junit.framework.TestCase;

/**
 * @author Ken
 *
 */
public class ComplexTest extends TestCase {
    
    private Complex num1;
    private Complex num2;
    
    /**
     * Runs before each test case
     */
    public void setUp()
    {
        num1 = new Complex(1.0, 1.0);
        num2 = new Complex(1.0, -1.0);
    }
    
    /**
     * Tests add
     */
    public void testAdd()
    {
        Complex sum = new Complex(2.0, 0.0);
        assertTrue(sum.equals(num1.add(num2)));
    }
    
    /**
     * Tests subtract
     */
    public void testSubtract()
    {
        Complex difference = new Complex(0, 2);
        assertTrue(difference.equals(num1.subtract(num2)));
    }
    
    /**
     * Tests real and realAsComplex
     */
    public void testReal()
    {
        Complex one = new Complex(1.0, 0);
        assertEquals(1.0, num1.real(), 0.01);
        assertEquals(1.0, num2.real(), 0.01);
        assertEquals(one, num1.realAsComplex());
    }
    
    /**
     * Tests imag and imagAsComplex
     */
    public void testImag()
    {
        Complex one = new Complex(1.0, 0);
        assertEquals(1.0, num1.imag(), 0.01);
        assertEquals(-1.0, num2.imag(), 0.01);
        assertEquals(one, num1.imagAsComplex());
    }
    
    /**
     * Tests toString
     */
    public void testToString()
    {
        assertEquals("(1.0 + 1.0i)", num1.toString());
        assertEquals("(1.0 - 1.0i)", num2.toString());
        assertEquals("5.0", (new Complex(5.0, 0.0)).toString());
    }

    /**
     * Tests equals
     */
    public void testEquals()
    {
        Complex nullNum = null;
        assertFalse(num1.equals(nullNum));
        String nonNum = "I am not a complex";
        assertFalse(num1.equals(nonNum));
        assertTrue(num1.equals(num1));
        assertFalse(num1.equals(num2));
        Complex equalToNum1 = new Complex(1.0, 1.0);
        assertTrue(num1.equals(equalToNum1));
    }
    
    /**
     * Tests mag and magAsComplex
     */
    public void testMag()
    {
        assertEquals(Math.sqrt(2), num1.mag(), 0.01);
        assertEquals(Math.sqrt(2), num2.mag(), 0.01);
        Complex pythagoreanComplex = new Complex(3.0, 4.0);
        assertEquals(5.0, pythagoreanComplex.mag(), 0.01);
        Complex complexMag = new Complex(Math.sqrt(2), 0);
        assertEquals(complexMag, num1.magAsComplex());
    }
    
    /**
     * Tests arg
     */
    public void testArg()
    {
        assertEquals(0.0, (new Complex(1.0, 0.0)).arg(), 0.01);
        assertEquals(Math.PI / 4, (new Complex(1.0, 1.0)).arg(), 0.01);
        assertEquals(Math.PI / 2, (new Complex(0.0, 1.0)).arg(), 0.01);
        assertEquals(3 * Math.PI / 4, (new Complex(-1.0, 1.0)).arg(), 0.01);
        assertEquals(Math.PI, (new Complex(-1.0, 0.0)).arg(), 0.01);
        assertEquals(5 * Math.PI / 4, (new Complex(-1.0, -1.0)).arg(), 0.01);
        assertEquals(3 * Math.PI / 2, (new Complex(0.0, -1.0)).arg(), 0.01);
        assertEquals(7 * Math.PI / 4, (new Complex(1.0, -1.0)).arg(), 0.01);
        try
        {
            (new Complex(0.0, 0.0)).arg();
        }
        catch (ArithmeticException e)
        {
            assertNotNull(e);
        }
    }
    
    /**
     * Tests conj
     */
    public void testConj()
    {
        assertEquals(num1, num2.conj());
        assertEquals(num2, num1.conj());
    }
    
    /**
     * Tests square and sqrt
     */
    public void testSquare()
    {
        Complex negativeOne = new Complex(-1.0, 0.0);
        Complex i = new Complex(0.0, 1.0);
        assertEquals(negativeOne, i.square());
        assertEquals(i, negativeOne.sqrt());
        Complex arbitraryComplex = new Complex(12.6, -13.7);
        assertEquals(arbitraryComplex, arbitraryComplex.square().sqrt());
        assertEquals(arbitraryComplex, arbitraryComplex.sqrt().square());
        assertEquals(new Complex(4.0, 0.0), (new Complex(2.0, 0.0)).square());
    }
    
    /**
     * Tests exp
     */
    public void testExp()
    {
        Complex PiI = new Complex(0, Math.PI);
        Complex HalfPiI = new Complex(0, Math.PI / 2);
        //Euler's identity should work
        assertEquals(new Complex(-1.0, 1.2246467991473532E-16), PiI.exp());
        assertEquals(new Complex(6.123233995736766E-17, 1.0), HalfPiI.exp());
        //Try it on another Complex
        Complex otherComplex = new Complex(-4.5, 6.8);
        assertEquals(new Complex(0.009658133710652758, 0.005489103507298103), otherComplex.exp());   
    }
    
    /**
     * Tests ln using exp
     */
    public void testln()
    {
        Complex PiI = new Complex(0, Math.PI);
        Complex HalfPiI = new Complex(0, Math.PI / 2);
        //Euler's identity should work
        assertEquals(PiI, (new Complex(-1, 0)).ln());
        assertEquals(HalfPiI, (new Complex(0, 1)).ln());
        //It should be an inverse operation with exp()
        Complex otherComplex = new Complex(-4.5, 1.8);
        assertEquals(otherComplex, otherComplex.ln().exp());
        assertEquals(otherComplex, otherComplex.exp().ln());   
    }

}
