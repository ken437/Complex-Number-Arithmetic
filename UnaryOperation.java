/**
 * 
 */
package complexNumbers;

/**
 * An operation that can be performed on one Quantity
 * 
 * @author kenneth
 * @version 9/23/2019
 *
 */
public class UnaryOperation extends Quantity {
    
    private String opType;
    private Quantity child;
    
    /**
     * Creates a UnaryOperation object
     * 
     * @param type a String representing the operation
     * this object will be able to perform
     */
    public UnaryOperation(String type)
    {
        this.opType = type;
    }
    
    /**
     * Gets the type of this operation
     * 
     * @return the opType field
     */
    public String getType()
    {
        return opType;
    }
    
    
    /**
     * Represents this object as a String
     */
    public String toString()
    {
        if (opType.equals("^2"))
        {
            return "(" + child.toString() + "^2" + ")";
        }
        else if (opType.equals("sqrt"))
        {
            return "(" + child.toString() + ".sqrt()" + ")";
        }
        else if (opType.equals("exp"))
        {
            return "(" + child.toString() + ".exp()" + ")";
        }
        else if (opType.equals("ln"))
        {
            return "(" + child.toString() + ".ln()" + ")";
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Sets the child to another Quantity
     */
    public void setChild(Quantity newChild)
    {
        //this object now links down to the child
        this.child = newChild;
        //child now links up to this object
        this.child.setParent(this);
    }
    
    /**
     * Returns the child
     * 
     * @return the child
     */
    public Quantity getChild()
    {
        return child;
    }
    
//    /**
//     * Attempts to evaluate the operation
//     * (Does not work if operation is at the top of the hierarchy)
//     * 
//     * @throws UnsupportedOperationException
//     */
//    private void evaluate()
//    {
//        //if child is a Complex object
//        if (("" + child.getClass()).equals("class complexNumbers.Complex"))
//        {
//            Complex newComplex = null;
//            //uses the opType to figure out how to calculate newComplex
//            if (opType.equals("^2"))
//            {
//                newComplex = ((Complex) child).square();
//            }
//            else if (opType.equals("sqrt"))
//            {
//                newComplex = ((Complex) child).sqrt();
//            }
//            else if (opType.equals("exp"))
//            {
//                newComplex = ((Complex) child).exp();
//            }
//            else if (opType.equals("ln"))
//            {
//                newComplex = ((Complex) child).ln();
//            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
//            //replaces itself in the structure with newComplex
//            this.replaceWith(newComplex);
//        }
//    }
    
    /**
     * Replaces every instance of a certain variable in the
     * function with a given value
     * 
     * @param variableName the name of the variable to be replaced
     * @param value the quantity that this variable will be replaced with
     */
    public void substitute(String variableName, Quantity value)
    {
        child.substitute(variableName, value);
    }
    
    /**
     * Creates a new copy of this UnaryOperation, along with all
     * Quantities linked below it, and returns that copy
     * 
     * @return a copy of this quantity
     */
    public UnaryOperation copy()
    {
        //copy the child
        Quantity copyOfChild = child.copy();
        //create a new UnaryOperation identical to this one
        UnaryOperation copyOfUnOp = new UnaryOperation(this.opType);
        //link the copy of the child to the copy of this operation
        copyOfUnOp.setChild(copyOfChild);
        return copyOfUnOp;    
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
        //what to do next depends on what operation this object represents
        if (opType.equals("^2"))
        {
            //(y^2)' = 2*(y*y')
            BinaryOperation times1 = new BinaryOperation("*");
            BinaryOperation times2 = new BinaryOperation("*");
            times1.setLeftChild(new Complex(2, 0));
            times1.setRightChild(times2);
            times2.setLeftChild(this.getChild());
            Quantity childCopy = this.getChild().copy();
            times2.setRightChild(childCopy);
            times2.getRightChild().diff(variableName);
            this.replaceWith(times1);
        }
        else if (opType.equals("sqrt"))
        {
            //(sqrt(y))' = y'/(2 * sqrt(y))
            BinaryOperation divide = new BinaryOperation("/");
            Quantity childCopy = this.getChild().copy();
            divide.setLeftChild(childCopy);
            divide.getLeftChild().diff(variableName);
            BinaryOperation times = new BinaryOperation("*");
            divide.setRightChild(times);
            times.setLeftChild(new Complex(2, 0));
            UnaryOperation sqrt = new UnaryOperation("sqrt");
            times.setRightChild(sqrt);
            sqrt.setChild(this.getChild());
            this.replaceWith(divide);
        }
        else if (opType.equals("exp"))
        {
            //(exp(y))' = y' * exp(y)
            BinaryOperation times = new BinaryOperation("*");
            Quantity copyChild = this.getChild().copy();
            times.setLeftChild(copyChild);
            times.getLeftChild().diff(variableName);
            UnaryOperation exp = new UnaryOperation("exp");
            times.setRightChild(exp);
            exp.setChild(this.getChild());
            this.replaceWith(times);
        }
        else if (opType.equals("ln"))
        {
            //(ln(y))' = y' / y
            BinaryOperation divide = new BinaryOperation("/");
            Quantity copyChild = this.getChild().copy();
            divide.setLeftChild(copyChild);
            divide.getLeftChild().diff(variableName);
            divide.setRightChild(this.getChild());
            this.replaceWith(divide);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Updates the housing function field of this quantity and all subquantities
     * 
     * @param newHousingFunction the new housing function that the quantities will be assigned to
     */
    public void updateFunction(Function newHousingFunction)
    {
        this.setHousingFunction(newHousingFunction);
        if (child != null)
        {
            child.updateFunction(newHousingFunction);
        }
    }
    
    /**
     * Removes any unnecessarily complicated
     * expressions from this quantity and/or its subquantities
     * (such as 1 * (0 + x))
     */
    public Function cleanUp()
    {
        this.getHousingFunction().setCleaned(true);
        
        //looks for unsimplified expressions
        
        //for class type testing purposes
        UnaryOperation unOp = new UnaryOperation("sqrt");
        Complex num = new Complex();
        
        //if it finds e^(lnx), where x is any quantity:
        if (this.getType().equals("exp") && this.getChild().getClass() == unOp.getClass()
            && ((UnaryOperation) this.getChild()).getType().equals("ln"))
        {
            this.getHousingFunction().setCleaned(false);
            //replace e^(lnx) with x
            this.replaceWith(((UnaryOperation) this.getChild()).getChild());
        }
        //if it finds ln(e^x), where x is any quantity:
        else if (this.getType().equals("ln") && this.getChild().getClass() == unOp.getClass()
            && ((UnaryOperation) this.getChild()).getType().equals("exp"))
        {
            this.getHousingFunction().setCleaned(false);
            //replace ln(e^x) with x
            this.replaceWith(((UnaryOperation) this.getChild()).getChild());
        }
        //if it finds (sqrt(x))^2, where x is any quantity:
        else if (this.getType().equals("^2") && this.getChild().getClass() == unOp.getClass()
            && ((UnaryOperation) this.getChild()).getType().equals("sqrt"))
        {
            this.getHousingFunction().setCleaned(false);
            //replace (sqrt(x))^2 with x
            this.replaceWith(((UnaryOperation) this.getChild()).getChild());
        }
        //if it finds a UnaryOperation performed on any Complex:
        else if (this.getChild().getClass() == num.getClass())
        {
            this.getHousingFunction().setCleaned(false);
            //evaluate the operation
            this.getHousingFunction().evaluate(this);
        }
        else
        {
            this.getChild().cleanUp();
        }
        return this.getHousingFunction();
    }

}
