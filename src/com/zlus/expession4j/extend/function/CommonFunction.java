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
public abstract class CommonFunction extends AbstractFunction {
    private String name;
    private boolean isArrayParameters = false;

    public CommonFunction() {
        super();
    }

    public CommonFunction(String name, boolean isArrayParameters) {
        this.name = name;
        this.isArrayParameters = isArrayParameters;
    }

    /**
     * evaluation method called by the Expression4j when needed.
     *
     * @param parameters parameter given to the function for the evaluation.
     */
    public MathematicalElement evaluate(Parameters parameters) throws EvalException {
        throw new EvalException("Cannot evaluate " + name + "(x...). ");
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
        return name;
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
        return isArrayParameters;
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