package complexNumbers;

/**
 * Holds the field topQuantity, which serves as the peak
 * of the function structure hierarchy. The structure looks something like this
 *  (1 + x^2) used as an example:
 * 
 *                                     Function
 *                                        |
 *                                        |
 *                                        |
 *                           BinaryOperaton (opType = "+")
 *                           |                          |
 *                           |                          |
 *                           |                          |
 *               Complex (a = 1, b = 0)    UnaryOperation (opType = "^2")
 *                                                      |
 *                                                      |
 *                                                      |
 *                                               Variable (name = "x")
 * 
 * Complex, Variable, UnaryOperation, and BinaryOperation are all subtypes of 
 * Quantity, which is the general term for an entity in the function structure.
 * Each BinaryOperation references two "child" Quantities, and each UnaryOperation
 * references one "child" Quantity. In addition, each Quantity except the uppermost
 * one in the structure stores a reference to its "parent" Quantity in the "parent"
 * field. A Function references the uppermost Quantity in the hierarchy using the 
 * field "topQuantity," and each Quantity in the hierarchy references the Function
 * using the field "housingFunction".                
 * 
 * @author Ken
 * @version 9/28/2019
 *
 */
public class Function {
    
    private Quantity topQuantity;
    private boolean cleaned;
    
    /**
     * Creates a function with the given topQuantity
     * 
     * @param topQuantity a Quantity that will be assigned to be the topQuantity
     */
    public Function(Quantity topQuantity)
    {
        this.topQuantity = topQuantity;
        topQuantity.updateFunction(this);
        cleaned = false;
    }
    
    /**
     * Returns the value of the topQuantity field
     * 
     * @return the topQuanity field
     */
    public Quantity getTopQuantity()
    {
        return topQuantity;
    }
    
    /**
     * Sets the value of the topQuantity field
     * 
     * @param newTopQuantity new topQuantity
     */
    public void setTopQuantity(Quantity newTopQuantity)
    {
        this.topQuantity = newTopQuantity;
    }
    
    /**
     * Sets the value of the cleaned field. cleaned = true
     * means that the last time the cleanUp() method was called,
     * no unsimplified statements
     * were detected. cleaned = false means that there may be 
     * unsimplified operations in the expression.
     * 
     * @param cleaned new value of the cleaned field
     */
    public void setCleaned(boolean cleaned)
    {
        this.cleaned = cleaned;
    }
    
    /**
     * Returns the value of the clean field. See setter method javadoc for details
     * 
     * @return value of the clean field
     */
    public boolean getCleaned()
    {
        return cleaned;
    }
    
    /**
     * Expresses this object as a String
     * 
     * @param newTopQuantity new topQuantity
     */
    public String toString()
    {
        return this.topQuantity.toString();
    }
    
    /**
     * Attempts to evaluate the given UnaryOperation
     * (Does not work if operation is at the top of the hierarchy)
     * 
     * @throws UnsupportedOperationException
     */
    private void evaluateOp(UnaryOperation unOp)
    {
        //if child is a Complex object
        if (("" + unOp.getChild().getClass()).equals("class complexNumbers.Complex"))
        {
            Complex newComplex = null;
            //uses the opType to figure out how to calculate newComplex
            if (unOp.getType().equals("^2"))
            {
                newComplex = ((Complex) unOp.getChild()).square();
            }
            else if (unOp.getType().equals("sqrt"))
            {
                newComplex = ((Complex) unOp.getChild()).sqrt();
            }
            else if (unOp.getType().equals("exp"))
            {
                newComplex = ((Complex) unOp.getChild()).exp();
            }
            else if (unOp.getType().equals("ln"))
            {
                newComplex = ((Complex) unOp.getChild()).ln();
            }
            else
            {
                throw new UnsupportedOperationException();
            }
            //replaces itself in the structure with newComplex
            this.replace(unOp, newComplex);
        }
    }
    
    /**
     * Attempts to evaluate the given BinaryOperation
     * 
     * @param quantity the BinaryOperation that will be evaluated
     */
    private void evaluateOp(BinaryOperation binOp)
    {
        //used for testing class type
        Complex testingComplex = new Complex();
        //if both children are Complex objects
        if (binOp.getLeftChild().getClass() == testingComplex.getClass() &&
            binOp.getRightChild().getClass() == testingComplex.getClass())
        {
            Complex newComplex = null;
            //uses the opType to figure out how to calculate newComplex
            if (binOp.getType().equals("+"))
            {
                newComplex = ((Complex) binOp.getLeftChild()).add((Complex) binOp.getRightChild());
            }
            else if (binOp.getType().equals("-"))
            {
                newComplex = ((Complex) binOp.getLeftChild()).subtract((Complex) binOp.getRightChild());
            }
            else if (binOp.getType().equals("*"))
            {
                newComplex = ((Complex) binOp.getLeftChild()).multiply((Complex) binOp.getRightChild());
            }
            else if (binOp.getType().equals("/"))
            {
                newComplex = ((Complex) binOp.getLeftChild()).divide((Complex) binOp.getRightChild());
            }
            else
            {
                throw new UnsupportedOperationException();
            }
            //replaces itself in the structure with newComplex
            this.replace(binOp, newComplex);
        }
    }
    
    /**
     * Replaces one Quantity in the structure with another Quantity
     * 
     * @param oldQuantity the quantity being replaced
     * @param newQuantity the quantity that will take oldQuantity's place in the structure
     */
    private void replace(Quantity oldQuantity, Quantity newQuantity)
    {
        //newQuantity links up to oldQuantity's parent
        newQuantity.setParent(oldQuantity.getParent());
        //this Quantity's parent no longer links down to this object,
        //and instead links to newQuantity       
        Quantity parent = oldQuantity.getParent();
        if (parent == null)
        {
            topQuantity = newQuantity;
        }
        else if (("" + parent.getClass()).equals("class complexNumbers.UnaryOperation")) //if the parent is a UnaryOperation:
        {
            ((UnaryOperation) parent).setChild(newQuantity);           
        }
        //if the parent is a BinaryOperation and this is the leftChild:
        else if (((BinaryOperation) parent).getLeftChild().equals(oldQuantity))
        {
            ((BinaryOperation) parent).setLeftChild(newQuantity); 
        }
        else
        {
            ((BinaryOperation) parent).setRightChild(newQuantity);
        }
    }
    
    /**
     * Attempts to evaluate the given Quantity. If the Quantity is
     * a Variable or a Complex, this does nothing. If it is a BinaryOperation
     * or a UnaryOperation, it runs the corresponding evaluateOp method.
     * 
     * @param quantity the quantity that this function will attempt to evaluate
     */
    public void evaluate(Quantity quantity)
    {
        //used for testing class type
        BinaryOperation binOp = new BinaryOperation("+");
        UnaryOperation unOp = new UnaryOperation("sqrt");
        
        //if quantity is a UnaryOperation
        if (quantity.getClass() == unOp.getClass())
        {
            this.evaluateOp((UnaryOperation) quantity);
        }
        //if quantity is a BinaryOperation
        else if (quantity.getClass() == binOp.getClass())
        {
            this.evaluateOp((BinaryOperation) quantity);
        }
    }
    
    /**
     * Attempts to evaluate the entire function. Will reduce the 
     * function down to a single Complex if no unsubstituted variables
     * are remaining.
     */
    public void fullySimplify()
    {
        this.fullySimplify(topQuantity);
    }
    
    /**
     * Attempts to evaluate everything below this quantity in the hierarchy. 
     * Will reduce this quantity down to a single Complex if no unsubstituted variables
     * are remaining below it.
     * 
     * @param quantity the quantity that this function will attempt to reduce to a single
     * Complex
     */
    public void fullySimplify(Quantity quantity)
    {
      //used for testing class type
        BinaryOperation binOp = new BinaryOperation("+");
        UnaryOperation unOp = new UnaryOperation("sqrt");
        Complex num = new Complex();
        
        //if quantity is a UnaryOperation
        if (quantity.getClass() == unOp.getClass())
        {
            Quantity child = ((UnaryOperation) quantity).getChild();
            //if the child is a Complex
            if (child.getClass() == num.getClass())
            {
                this.evaluate(quantity);
            }
            //if the child is a BinaryOperation or UnaryOperation
            else if (child.getClass() == binOp.getClass() ||
                (child.getClass() == unOp.getClass()))
            {
                this.fullySimplify(child);
                this.evaluate(quantity);
            }
        }
        //if quantity is a BinaryOperation
        else if (quantity.getClass() == binOp.getClass())
        {
            Quantity leftChild = ((BinaryOperation) quantity).getLeftChild();
            Quantity rightChild = ((BinaryOperation) quantity).getRightChild();
            //if the leftChild is a BinaryOperation or UnaryOperation
            if (leftChild.getClass() == binOp.getClass() ||
                (leftChild.getClass() == unOp.getClass()))
            {
                this.fullySimplify(leftChild);
            }
            //if the rightChild is a BinaryOperation or UnaryOperation
            if (rightChild.getClass() == binOp.getClass() ||
                (rightChild.getClass() == unOp.getClass()))
            {
                this.fullySimplify(rightChild);
            }
            //calculate leftChild and rightChild again
            leftChild = ((BinaryOperation) quantity).getLeftChild();
            rightChild = ((BinaryOperation) quantity).getRightChild();
            //if both children are Complex
            if (leftChild.getClass() == num.getClass() &&
                rightChild.getClass() == num.getClass())
            {
                this.evaluate(quantity);
            }                
        }
    }
    
    /**
     * Replaces every instance of a certain variable in the
     * function with a given value
     * 
     * @param variableName the name of the variable to be replaced
     * @param value the quantity that this variable will be replaced with
     */
    public void substitute(String variableName, Quantity value)
    {
        topQuantity.substitute(variableName, value);
        //substitution may create unsimplified expressions
        cleaned = false;
    }
    
    /**
     * Makes a copy of this function, substitutes the given value
     * in for the given variableName in the copy,
     * then completely evaluates the copy.
     * 
     * @return If this operation reduces the function to a single
     * Complex, return that Complex. Otherwise, return null
     */
    public Complex subAndEval(String variableName1, Complex value1)
    {
        Function copy = this.copy();
        copy.substitute(variableName1, value1);
        copy.fullySimplify();
        //is the topQuantity a Complex?
        Complex testComplex = new Complex();
        if (copy.getTopQuantity().getClass() == testComplex.getClass())
        {
            return (Complex) copy.getTopQuantity();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Makes a copy of this function, substitutes the given values 
     * in for the given variableNames in the copy,
     * then completely evaluates the copy.
     * 
     * @return If this operation reduces the function to a single
     * Complex, return that Complex. Otherwise, return null
     */
    public Complex subAndEval(String variableName1, Complex value1, String variableName2, Complex value2)
    {
        Function copy = this.copy();
        copy.substitute(variableName1, value1);
        copy.substitute(variableName2, value2);
        copy.fullySimplify();
        //is the topQuantity a Complex?
        Complex testComplex = new Complex();
        if (copy.getTopQuantity().getClass() == testComplex.getClass())
        {
            return (Complex) copy.getTopQuantity();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Creates a copy of this function
     * 
     * @return a copy of this function
     */
    public Function copy()
    {
        Quantity newTopQuantity = this.getTopQuantity().copy();
        Function newFunction = new Function(newTopQuantity);
        return newFunction;
    }
    
    /**
     * Approximates the derivative of this function with respect to a given
     * variable at a given location
     * 
     * @param variableName the variable with respect to which the function will be differentiated
     * @param location the location at which the derivative is being calculated 
     * (for example, if variableName == "x" and location == 2, the derivative is taken at x == 2)
     * @param precision an integer specifying how precise the calculation is. Higher precision = more accurate
     * 
     * @return Complex an approximation of the derivative at the given point
     */
    public Complex approxDeriv(String variableName, Complex location, int precision)
    {
        Complex reciprocalPrecision = new Complex(1 / ((double) precision), 0);
        Complex numerator = this.subAndEval(variableName, location.add(reciprocalPrecision)).subtract(this.subAndEval(variableName, location));
        Complex denominator = reciprocalPrecision;
        return numerator.divide(denominator);
    }
    
    /**
     * Approximates the derivative of this function with respect to a given
     * variable at a given location. Default precision is 1000000
     * 
     * @param variableName the variable with respect to which the function will be differentiated
     * @param location the location at which the derivative is being calculated 
     * (for example, if variableName == "x" and location == 2, the derivative is taken at x == 2)
     * 
     * @return Complex an approximation of the derivative at the given point
     */
    public Complex approxDeriv(String variableName, Complex location)
    {
        return this.approxDeriv(variableName, location, 1000000);
    }
    
    /**
     * Approximates the integral of this function with respect to a given
     * variable between two given bounds
     * 
     * @param variableName the variable with respect to which the function will be differentiated
     * @param start the lower bound of integration
     * @param end the upper bound of integration
     * @param precision an integer specifying how precise the calculation is. Higher precision = more accurate
     * 
     * @return Complex an approximation of the derivative at the given point
     */
    public Complex approxIntegral(String variableName, Complex start, Complex end, int precision)
    {
        Complex dx = (end.subtract(start)).divide(new Complex(precision, 0));
        Complex total = new Complex();
        //this program essentially finds the Riemann sum over this interval
        for (int i = 0; i < precision; i++)
        {
            total = total.add(this.subAndEval(variableName, start.add(dx)));
        }
        return total.multiply(dx);      
    }
    
    /**
     * Approximates the integral of this function with respect to a given
     * variable between two given bounds. Default precision is 1000000
     * 
     * @param variableName the variable with respect to which the function will be differentiated
     * @param start the lower bound of integration
     * @param end the upper bound of integration
     * 
     * @return Complex an approximation of the derivative at the given point
     */
    public Complex approxIntegral(String variableName, Complex start, Complex end)
    {
        int defaultPrecision = 1000000;
        Complex dx = (end.subtract(start)).divide(new Complex(defaultPrecision, 0));
        Complex total = new Complex();
        //this program essentially finds the Riemann sum over this interval
        for (int i = 0; i < defaultPrecision; i++)
        {
            total = total.add(this.subAndEval(variableName, start.add(dx.multiply(new Complex(i, 0)))));
        }
        return total.multiply(dx);      
    }

    
    /**
     * Creates a copy of this function, differentiates the copy,
     * and returns that copy
     * 
     * @param variableName the name of the variable with respect
     * to which this function will be differentiated
     * 
     * @return a differentiated version of the original function
     */
    public Function diff(String variableName)
    {
        Function copy = this.copy();
        copy.getTopQuantity().diff(variableName);
        //differentiation may create unsimplified expressions
        cleaned = false;
        return copy;
        
    }
    
    /**
     * Creates a copy of this function, composes another function
     * within it, and then returns the modified copy
     * (e.g) (x^2).compose(1 + x) = -> (1 + x)^2
     * 
     * @param variableName the name of the variable that will be replaced
     * @function2 the function that will replace this variable
     * 
     * @return the result of the function composition operation
     */
    public Function compose(String variableName, Function function2)
    {
        Function copy = this.copy();
        copy.substitute(variableName, function2.getTopQuantity());
        //update all housingFunction fields of every quantity in copy
        copy.getTopQuantity().updateFunction(copy);
        //composition may create unsimplified expressions
        cleaned = false;
        return copy;
    }
    
    /**
     * Creates a copy of this function, removes any unnecessarily complicated
     * expressions from the copy (such as 1 * (0 + x)), and returns the copy
     */
    public Function cleanUp()
    {
        //create a copy of this function
        Quantity newTopQuantity = this.getTopQuantity().copy();
        Function newFunction = new Function(newTopQuantity);
        //this method will continue to run until no unsimplified expressions are
        //detected
        if (!newFunction.getCleaned())
        {
            //assume that unsimplified expressions are absent until one is found
            cleaned = true;
            newFunction = newTopQuantity.cleanUp();
            //update topQuantity
        }
        return newFunction;
    }
}
