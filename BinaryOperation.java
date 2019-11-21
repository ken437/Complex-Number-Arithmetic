/**
 * 
 */
package complexNumbers;

/**
 * An operation that can be performed on two Quantites
 * 
 * @author kenneth
 * @version 9/23/2019
 *
 */
public class BinaryOperation extends Quantity {
    
    private String opType;
    private Quantity leftChild;
    private Quantity rightChild;
    
    /**
     * Creates a UnaryOperation object
     * 
     * @param type a String representing the operation
     * this object will be able to perform
     */
    public BinaryOperation(String type)
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
        if (opType.equals("+"))
        {
            return "(" + leftChild.toString() + " + " + rightChild.toString() + ")";
        }
        else if (opType.equals("-"))
        {
            return "(" + leftChild.toString() + " - " + rightChild.toString() + ")";
        }
        else if (opType.equals("*"))
        {
            return "(" + leftChild.toString() + " * " + rightChild.toString() + ")";
        }
        else if (opType.equals("/"))
        {
            return "(" + leftChild.toString() + " / " + rightChild.toString() + ")";
        }
        else
        {
            return "<unrecognized opType>";
        }
    }
    
    /**
     * Sets the left child to another Quantity
     */
    public void setLeftChild(Quantity newChild)
    {
        //this object now links down to the child
        this.leftChild = newChild;
        //child now links up to this object
        this.leftChild.setParent((Quantity) this);
    }
    
    /**
     * Gets left child
     * 
     * @return left child
     */
    public Quantity getLeftChild()
    {
        return leftChild;
    }
    
    /**
     * Gets right child
     * 
     * @return right child
     */
    public Quantity getRightChild()
    {
        return rightChild;
    }
    
    /**
     * Sets the right child to another Quantity
     */
    public void setRightChild(Quantity newChild)
    {
        //this object now links down to the child
        this.rightChild = newChild;
        //child now links up to this object
        this.rightChild.setParent((Quantity) this);
    }
    
//    /**
//     * Attempts to evaluate the operation
//     * (Does not work if the function is at the top of the hierarchy)
//     * 
//     * @throws UnsupportedOperationException
//     */
//    public void evaluate()
//    {
//        //if both children are Complex objects
//        if (("" + leftChild.getClass()).equals("class complexNumbers.Complex") &&
//            ("" + rightChild.getClass()).equals("class complexNumbers.Complex"))
//        {
//            Complex newComplex = null;
//            //uses the opType to figure out how to calculate newComplex
//            if (opType.equals("+"))
//            {
//                newComplex = ((Complex) leftChild).add((Complex) rightChild);
//            }
//            else if (opType.equals("-"))
//            {
//                newComplex = ((Complex) leftChild).subtract((Complex) rightChild);
//            }
//            else if (opType.equals("*"))
//            {
//                newComplex = ((Complex) leftChild).multiply((Complex) rightChild);
//            }
//            else if (opType.equals("/"))
//            {
//                newComplex = ((Complex) leftChild).divide((Complex) rightChild);
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
        leftChild.substitute(variableName, value);
        rightChild.substitute(variableName, value);
    }
    
    /**
     * Creates a new copy of this BinaryOperation, along with all
     * Quantities linked below it, and returns that copy
     * 
     * @return a copy of this quantity
     */
    public BinaryOperation copy()
    {
        //copy the children
        Quantity copyOfLeftChild = leftChild.copy();
        Quantity copyOfRightChild = rightChild.copy();
        //create a new BinaryOperation identical to this one
        BinaryOperation copyOfBinOp = new BinaryOperation(this.opType);
        //link the copies of the children to the copy of this operation
        copyOfBinOp.setLeftChild(copyOfLeftChild);
        copyOfBinOp.setRightChild(copyOfRightChild);
        return copyOfBinOp;    
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
        if (opType.equals("+"))
        {
            //(a + b)' = a' + b'
            this.getLeftChild().diff(variableName);
            this.getRightChild().diff(variableName);
        }
        else if (opType.equals("-"))
        {
            //(a - b)' = a' - b'
            this.getLeftChild().diff(variableName);
            this.getRightChild().diff(variableName);
        }
        else if (opType.equals("*"))
        {
            //(ab)' = a'b + b'a
            BinaryOperation plus = new BinaryOperation("+");
            BinaryOperation times1 = new BinaryOperation("*");
            BinaryOperation times2 = new BinaryOperation("*");
            Quantity copyLeftChild = this.getLeftChild().copy();
            Quantity copyRightChild = this.getRightChild().copy();
            plus.setLeftChild(times1);
            plus.setRightChild(times2);
            times1.setLeftChild(copyLeftChild);
            times1.getLeftChild().diff(variableName);
            times1.setRightChild(this.getRightChild());
            times2.setLeftChild(copyRightChild);
            times2.getLeftChild().diff(variableName);
            times2.setRightChild(this.getLeftChild());
            this.replaceWith(plus);
        }
        else if (opType.equals("/"))
        {
            //(a/b)' = (a'b - b'a)/b^2
            BinaryOperation divide = new BinaryOperation("/");
            BinaryOperation minus = new BinaryOperation("-");
            BinaryOperation times1 = new BinaryOperation("*");
            BinaryOperation times2 = new BinaryOperation("*");
            UnaryOperation square = new UnaryOperation("^2");
            Quantity copyLeftChild = this.getLeftChild().copy();
            Quantity copyRightChild = this.getRightChild().copy();
            divide.setLeftChild(minus);
            divide.setRightChild(square);
            minus.setLeftChild(times1);
            minus.setRightChild(times2);
            times1.setLeftChild(copyLeftChild);
            times1.getLeftChild().diff(variableName);
            times1.setRightChild(this.getRightChild());
            times2.setLeftChild(copyRightChild);
            times2.getLeftChild().diff(variableName);
            times2.setRightChild(this.getLeftChild());
            square.setChild(this.getRightChild());
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
        if (leftChild != null)
        {
            leftChild.updateFunction(newHousingFunction);
        }
        if (rightChild != null)
        {
        rightChild.updateFunction(newHousingFunction);
        }
    }
    

    /**
     * Removes any unnecessarily complicated
     * expressions from this quantity and/or its subquantities
     * (such as 1 * (0 + x))
     * 
     * @return the housingFunction of this operation
     */
    public Function cleanUp()
    {
        //looks for unsimplified expressions
        
        //for testing purposes
        //<no test objects initialized yet>
        
       //if it finds 0 + a, where a is any quantity:
       if (this.getType().equals("+") && this.getLeftChild().equals(new Complex()) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace 0 + a with a
           this.replaceWith(this.getRightChild());
       }
       //if it finds a + 0, where a is any quantity:
       else if (this.getType().equals("+") && this.getRightChild().equals(new Complex()) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace a + 0 with a
           this.replaceWith(this.getLeftChild());
       }
       //if it finds 0 - a, where a is any quantity:
       else if (this.getType().equals("-") && this.getLeftChild().equals(new Complex()) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace 0 - a with (-1) * a
           BinaryOperation mult = new BinaryOperation("*");
           mult.setLeftChild(new Complex(-1, 0));
           mult.setRightChild(this.getRightChild());
           this.replaceWith(mult);
       }
       //if it finds a - 0, where a is any quantity:
       else if (this.getType().equals("-") && this.getRightChild().equals(new Complex()) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace a - 0 with a
           this.replaceWith(this.getLeftChild());
       }
       //if it finds 0 * a, where a is any quantity:
       else if (this.getType().equals("*") && this.getLeftChild().equals(new Complex()) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace 0 * a with 0
           this.replaceWith(new Complex());
       }
       //if it finds a * 0, where a is any quantity:
       else if (this.getType().equals("*") && this.getRightChild().equals(new Complex()) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace a * 0 with 0
           this.replaceWith(new Complex());
       }
       //if it finds a * 1, where a is any quantity:
       else if (this.getType().equals("*") && this.getRightChild().equals(new Complex(1, 0)) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace a * 1 with a
           this.replaceWith(this.getLeftChild());
       }
       //if it finds 1 * a, where a is any quantity:
       else if (this.getType().equals("*") && this.getLeftChild().equals(new Complex(1, 0)) )
       {
           this.getHousingFunction().setCleaned(false);
           //replace 1 * a with a
           this.replaceWith(this.getRightChild());
       }      
       else
       {
           this.getLeftChild().cleanUp();
           this.getRightChild().cleanUp();
       }
       return this.getHousingFunction();
    }
        
}
