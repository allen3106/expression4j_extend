package com.zlus.expession4j.extend.function;

import cn.hutool.core.convert.Convert;
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
import fr.expression4j.sample.custom.element.StringMathematicalElement;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;


/**
 * chose(index,value1,value2,value3...)
 *
 * @author j
 */
public class ChoseFunction extends AbstractFunction {

    /**
     * @param x
     * @return
     */
    private Object chose(Object... x) {
        if (x.length > 1) {
            Integer index = Convert.toInt(x[0]);
            return x.length > index ? x[index] : null;
        }
        return null;
    }

    /**
     * evaluation method called by the Expression4j when needed.
     *
     * @param parameters parameter given to the function for the evaluation.
     */
    public MathematicalElement evaluate(Parameters parameters) throws EvalException {
        try {
            Object[] args = new Object[parameters.getParameters().size()];
            int index = 0;
            while (parameters.getParameters().containsKey("var" + index)) {
                String key = "var" + index;
                MathematicalElement mex = parameters.getParameter(key);
                args[index++] = mex.getValue();
            }
            Object value = chose(args);
            if (value == null) return null;
            if (value instanceof Number) return NumberFactory.createReal(Double.parseDouble(value.toString()));
            if (value instanceof Boolean) return new BooleanMathematicalElement((Boolean) value);
            return new StringMathematicalElement(value.toString());
        } catch (ParametersException pe) {
            throw new EvalException("Cannot evaluate chose(x...). " + pe);
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
        return "chose";
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