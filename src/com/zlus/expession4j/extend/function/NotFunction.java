package com.zlus.expession4j.extend.function;

import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.OperatorManager;
import fr.expression4j.basic.impl.RealImpl;
import fr.expression4j.core.Catalog;
import fr.expression4j.core.ExpressionModel;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ModelException;
import fr.expression4j.core.exception.ParametersException;
import fr.expression4j.core.predefine.AbstractFunction;
import fr.expression4j.factory.ExpressionFactory;
import fr.expression4j.factory.ExpressionModelFactory;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;

import java.util.List;
import java.util.Vector;


/**
 * not(boolean)
 *
 * @author j
 */
public class NotFunction extends AbstractFunction {

    /**
     * @param x
     * @return
     */
    private boolean not(boolean x) {
        return !x;
    }

    /**
     * evaluation method called by the Expression4j when needed.
     *
     * @param parameters parameter given to the function for the evaluation.
     */
    public MathematicalElement evaluate(Parameters parameters) throws EvalException {
        try {
            MathematicalElement mex = parameters.getParameter("x");
            boolean[] args = new boolean[1];
            if (mex == null) {
                args[0] = false;
            } else if (mex instanceof BooleanMathematicalElement) {
                args[0] = (Boolean) mex.getValue();
            } else if (mex instanceof RealImpl) {
                args[0] = mex.getRealValue() > 0;
            } else {
                args[0] = true;
            }

            return new BooleanMathematicalElement(not(args[0]));
        } catch (ParametersException pe) {
            throw new EvalException("Cannot evaluate not(x). " + pe);
        }
    }

    public MathematicalElement evaluate(OperatorManager operatorManager, Parameters parameters) throws EvalException {
        return evaluate(parameters);
    }

    public Catalog getCatalog() {
        return ExpressionFactory.getCatalog();
    }

    /**
     * get the function name
     */
    public String getName() {
        return "not";
    }

    /**
     * get parameter list of the function.
     */
    public List getParameters() {

        List xParameters = new Vector(1);
        xParameters.add("x");
        return xParameters;
    }



    public ExpressionModel getExpressionModel() {

        try {
            return ExpressionModelFactory.getDefaultExpressionModel();
        } catch (ModelException e) {
            e.printStackTrace();
        }
        return null;
    }

}