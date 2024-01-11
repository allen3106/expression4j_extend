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
import fr.expression4j.factory.NumberFactory;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;


/**
 * avg(1,2,3)
 *
 * @author j
 */
public class AvgFunction extends AbstractFunction {

    /**
     * @param x
     * @return
     */
    private double avg(double... x) {
        return Arrays.stream(x).average().getAsDouble();
    }

    /**
     * evaluation method called by the Expression4j when needed.
     *
     * @param parameters parameter given to the function for the evaluation.
     */
    public MathematicalElement evaluate(Parameters parameters) throws EvalException {
        try {
            double[] args = new double[parameters.getParameters().size()];
            int index = 0;
            while (parameters.getParameters().containsKey("var" + index)) {
                String key = "var" + index;
                MathematicalElement mex = parameters.getParameter(key);
//                System.err.println(key + ":" + mex.getValue());
                if (mex == null) {
                    args[index++] = 0;
                } else if (mex instanceof BooleanMathematicalElement) {
                    args[index++] = (Boolean) mex.getValue() ? 1 : 0;
                } else if (mex instanceof RealImpl) {
                    args[index++] = mex.getRealValue();
                }
            }
            return NumberFactory.createReal(avg(args));
        } catch (ParametersException pe) {
            throw new EvalException("Cannot evaluate avg(x...). " + pe);
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
        return "avg";
    }

    /**
     * get parameter list of the function.
     */
    public List getParameters() {

        List xParameters = new Vector(1);
        return xParameters;
    }

    /**
     * 是否可变参数，参数的个数不定
     *
     * @return
     */
    @Override
    public boolean isArrayParameters() {
        return true;
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