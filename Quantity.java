package complexNumbers;

/**
 * Has a numerical value that may depend on unknowns, and
 * can be evaluated once all those unknowns are substituted
 * in.
 * 
 * Has four subtypes: Complex, Variable, UnaryOperation, and 
 * BinaryOperation
 * 
 * @author Ken
 * @version 9/23/2019
 *
 */
public abstract class Quantity {
    
    private Quantity parent;
    private Function housingFunction;
    
    /**
     * Gets the Quantity's parent
     * 
     * @return the parent of this Quantity
     */
    public Quantity getParent()
    {
        return parent;
    }
    
    /**
     * Gets the Quantity's housingFunction
     * 
     * @return the housingFunction of this Quantity
     */
    public Function getHousingFunction()
    {
        return housingFunction;
    }
    
    /**
     * Sets the Quantity's parent to the given
     * value
     * 
     * @param newParent the new parent of this quantity
     */
    public void setParent(Quantity newParent)
    {
        this.parent = newParent;
    }
    
    /**
     * Sets the Quantity's parent to the given
     * value
     * 
     * @param newParent the new parent of this quantity
     */
    public void setHousingFunction(Function newHousingFunction)
    {
        this.housingFunction = newHousingFunction;
    }
    
//    /**
//     * Tries to simplify the expression
//     */
//    public void evaluate()
//    {
//        //blank for this class, but subclasses can override this method
//    }
    
    /**
     * Replaces this Quantity in the structure with another Quantity
     * 
     * @param otherQuantity the quantity that will replace this object in the structure
     */
    public void replaceWith(Quantity otherQuantity)
    {
        //otherQuantity links up to this Quantity's parent
        otherQuantity.setParent(this.getParent());
        //this Quantities's parent no longer links down to this object,
        //and instead links to newQuantity       
        Quantity parent = this.getParent();
        if (parent == null)
        {
            housingFunction.setTopQuantity(otherQuantity);
        }
        else if (("" + parent.getClass()).equals("class complexNumbers.UnaryOperation")) //if the parent is a UnaryOperation:
        {
            ((UnaryOperation) parent).setChild(otherQuantity);           
        }
        //if the parent is a BinaryOperation and this is the leftChild:
        else if (((BinaryOperation) parent).getLeftChild().equals(this))
        {
            ((BinaryOperation) parent).setLeftChild(otherQuantity); 
        }
        else
        {
            ((BinaryOperation) parent).setRightChild(otherQuantity);
        }
    }
    
    /**
     * Replaces every instance of a certain variable in the
     * function below this quantity with a given value
     * 
     * @param variableName the name of the variable to be replaced
     * @param value the quantity that this variable will be replaced with
     */
    public abstract void substitute(String variableName, Quantity value);
    
    /**
     * Creates a new copy of this Quantity, along with all
     * Quantities linked below it, and returns that copy
     * 
     * @return a copy of this quantity
     */
    public abstract Quantity copy();
    
    /**
     * Finds the derivative of this quantity and replaces itself
     * in the function with this derivative
     * 
     * @param variableName the name of the variable with respect
     * to which this function will be differentiated
     */
    public void diff(String variableName)
    {
        //does nothing useful by default, but is overridden by the subclasses
    }
    
    /**
     * Removes any unnecessarily complicated
     * expressions from this quantity and/or its subquantities
     * (such as 1 * (0 + x))
     */
    public Function cleanUp()
    {
        //the default is for the Quantity to do nothing
        return this.getHousingFunction();
    }
    
    /**
     * Updates the housing function field of this quantity and all subquantities
     * 
     * @param newHousingFunction the new housing function that the quantities will be assigned to
     */
    public void updateFunction(Function newHousingFunction)
    {
        this.setHousingFunction(newHousingFunction);
    }

}
