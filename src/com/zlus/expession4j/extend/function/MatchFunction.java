package com.zlus.expession4j.extend.function;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zlus.expession4j.extend.element.IntegerMathematicalElement;
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
 * match(value,compareValue1,compareValue2,compareValue3....)  返回序号
 *
 * @author j
 */
public class MatchFunction extends AbstractFunction {

    /**
     * @param x
     * @return
     */
    private int match(Object... x) {
        if (ArrayUtil.isEmpty(x) || x.length == 1)
            return -1;
        Object value = x[0];

        for (int i = 1; i < x.length; i++) {
            if (value == null) {
                if (x[i] == null)
                    return i;
                else
                    continue;
            }

            if (x[i] instanceof Number && value instanceof Number) {
                if (NumberUtil.equals((Number) value, (Number) x[i])) {
                    return i;
                }
            } else if (ObjectUtil.equals(x[i], value)) {
                return i;
            }
        }
        return -1;
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
            return new IntegerMathematicalElement(match(args));
        } catch (ParametersException pe) {
            throw new EvalException("Cannot evaluate match(x...). " + pe);
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
        return "match";
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