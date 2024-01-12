package com.zlus.expession4j.extend.function;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.zlus.expession4j.extend.element.IntegerMathematicalElement;
import com.zlus.expession4j.extend.element.NullMathematicalElement;
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
import fr.expression4j.sample.custom.element.StringMathematicalElement;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;

import java.util.List;
import java.util.Vector;


/**
 * and(boolean,boolean,boolean)
 *
 * @author j
 */
public class OrFunction extends CommonFunction {


    public  OrFunction(){
        super("or",true);
    }
    /**
     * @param x
     * @return
     */
    private boolean or(boolean... x) {
        return BooleanUtil.or(x);
    }

    /**
     * evaluation method called by the Expression4j when needed.
     *
     * @param parameters parameter given to the function for the evaluation.
     */
    public MathematicalElement evaluate(Parameters parameters) throws EvalException {
        try {
            boolean[] args = new boolean[parameters.getParameters().size()];
            int index = 0;
            while (parameters.getParameters().containsKey("var" + index)) {
                String key = "var" + index;
                MathematicalElement mex = parameters.getParameter(key);
//                System.err.println(key + ":" + mex.getValue());
                if (mex == null || mex instanceof NullMathematicalElement) {
                    args[index++] = false;
                } else if (mex instanceof BooleanMathematicalElement) {
                    args[index++] = (Boolean) mex.getValue();
                } else if (mex instanceof RealImpl) {
                    args[index++] = mex.getRealValue() > 0;
                } else if (mex instanceof IntegerMathematicalElement) {
                    args[index++] = mex.getRealValue() > 0;
                } else if (mex instanceof StringMathematicalElement) {
                    args[index++] = StrUtil.isNotEmpty((CharSequence) mex.getValue());
                } else {
//                    args[index++] = true;
                    throw new ParametersException("不支持这种参数类型：" + mex);
                }
            }
            return new BooleanMathematicalElement(or(args));
        } catch (ParametersException pe) {
            throw new EvalException("Cannot evaluate or(x...). " + pe);
        }
    }
//
//    public MathematicalElement evaluate(OperatorManager operatorManager, Parameters parameters) throws EvalException {
//        return evaluate(parameters);
//    }
//
//    public Catalog getCatalog() {
//        return ExpressionFactory.getCatalog();
//    }
//
//    /**
//     * get the function name
//     */
//    public String getName() {
//        return "or";
//    }
//
//    /**
//     * get parameter list of the function.
//     */
//    public List getParameters() {
//
//        List xParameters = new Vector(1);
//        return xParameters;
//    }
//
//    /**
//     * 是否可变参数，参数的个数不定
//     *
//     * @return
//     */
//    @Override
//    public boolean isArrayParameters() {
//        return true;
//    }
//
//    public ExpressionModel getExpressionModel() {
//
//        try {
//            return ExpressionModelFactory.getDefaultExpressionModel();
//        } catch (ModelException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}