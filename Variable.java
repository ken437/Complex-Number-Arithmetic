/**
 * 
 */
package complexNumbers;

/**
 * Represents a currently unknown Complex
 * 
 * @author kenneth
 * @version 9/23/2019
 */
public class Variable extends Quantity {
    
    private String name;
    
    /**
     * Creates a Variable object
     * Default name is "x"
     */
    public Variable()
    {
        this("x");
    }
    
    /**
     * Creates a Variable object
     * 
     * @param name the name that will be given to this variable
     */
    public Variable(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets the name
     * 
     * @return name of the variable
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Puts the variable in String form
     * 
     * @return name of the variable
     */
    public String toString()
    {
        return name;
    }
    
    /**
     * If this variable's name matches the variableName, it will replace itself
     * with the given value
     * 
     * @param variableName the name of the variable to be replaced
     * @param value the quantity that this variable will be replaced with
     */
    public void substitute(String variableName, Quantity value)
    {
        if (name == variableName)
        {
            this.replaceWith(value);
        }
    }
    
    /**
     * Creates a new copy of this Variable and returns that copy
     * 
     * @return a copy of this quantity
     */
    public Variable copy()
    {
        Variable copy = new Variable(this.name);
        return copy;
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
        //if variableName matches this variable:
        if (variableName.equals(name))
        {
            this.replaceWith(new Complex(1, 0));
        }
        //if variableName does not match this variable:
        else
        {
            this.replaceWith(new Variable("d" + name + "/d" + variableName));
        }
    }
}
