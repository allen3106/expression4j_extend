package com.zlus.expession4j.extend.function;

import cn.hutool.core.util.ObjectUtil;
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
 * eq(valuex,valuey)
 *
 * @author j
 */
public class EqFunction extends AbstractFunction {



    /**
     * evaluation method called by the Expression4j when needed.
     *
     * @param parameters parameter given to the function for the evaluation.
     */
    public MathematicalElement evaluate(Parameters parameters) throws EvalException {
        try {
            MathematicalElement mex = parameters.getParameter("x");
            MathematicalElement mey = parameters.getParameter("y");
           return  new BooleanMathematicalElement(ObjectUtil.equal(mex.getValue(),mey.getValue()));
        } catch (ParametersException pe) {
            throw new EvalException("Cannot evaluate eq(x,y). " + pe);
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
        return "eq";
    }

    /**
     * get parameter list of the function.
     */
    public List getParameters() {

        List xParameters = new Vector(2);
        xParameters.add("x");
        xParameters.add("y");
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