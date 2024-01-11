package com.zlus.expession4j.extend.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.zlus.expession4j.extend.constant.MathematicalElementType;
import com.zlus.expession4j.extend.element.BooleanExpressionElement;
import com.zlus.expession4j.extend.element.IntegerMathematicalElement;
import com.zlus.expession4j.extend.element.NullExpressionElement;
import com.zlus.expession4j.extend.element.NullMathematicalElement;
import com.zlus.expession4j.extend.function.*;
import com.zlus.expession4j.extend.operator.*;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.Operator;
import fr.expression4j.basic.OperatorManager;
import fr.expression4j.basic.impl.RealImpl;
import fr.expression4j.core.Catalog;
import fr.expression4j.core.Expression;
import fr.expression4j.core.ExpressionModel;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParametersException;
import fr.expression4j.core.exception.ParsingException;
import fr.expression4j.core.predefine.AbstractFunction;
import fr.expression4j.factory.*;
import fr.expression4j.sample.custom.element.OperatorPlusStringReal;
import fr.expression4j.sample.custom.element.OperatorPlusStringString;
import fr.expression4j.sample.custom.element.StringExpressionElement;
import fr.expression4j.sample.custom.element.StringMathematicalElement;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;
import fr.expression4j.util.ParameterUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Expression4jUtil {

    private static OperatorManager operatorManager;
    private static ExpressionModel expressionModel;
    private static Catalog catalog;
    final static String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static {
        try {
            expressionModel = ExpressionModelFactory.getDefaultExpressionModel();
            // add custom expression element
            //优先级别高，先考虑字符串
            expressionModel.addExpressionElement(new StringExpressionElement(), 1);
            //其次是boolean
            expressionModel.addExpressionElement(new BooleanExpressionElement(), 2);
            //null值
            expressionModel.addExpressionElement(new NullExpressionElement(), 2);

            // add binary operator supported by the specific expression model
            Operator gt = OperatorFactory.createOperator("gt", ">", false);
            Operator ge = OperatorFactory.createOperator("ge", ">=", false);
            Operator lt = OperatorFactory.createOperator("lt", "<", false);
            Operator le = OperatorFactory.createOperator("le", "<=", false);
            Operator eq = OperatorFactory.createOperator("eq", "==", false);
            Operator ne = OperatorFactory.createOperator("ne", "!=", false);
            Operator not = OperatorFactory.createOperator("not", "!", true);

            // add binary（二进制的，数字的） operator supported by the specific expression model
            //优先级别最低 le,ge 比lt,gt优先级别高,优先级别越高越先匹配
            expressionModel.addBinaryOperator(gt, 1);
            expressionModel.addBinaryOperator(ge, 2);
            expressionModel.addBinaryOperator(lt, 1);
            expressionModel.addBinaryOperator(le, 2);
            expressionModel.addBinaryOperator(eq, 2);
            expressionModel.addBinaryOperator(ne, 2);
            expressionModel.addUnaryOperator(not);

            operatorManager = OperatorManagerFactory.getDefaultOperatorManager();

            // add operator specific to previous model
            operatorManager.addOperatorImpl(new OperatorPlusStringString());
            operatorManager.addOperatorImpl(new OperatorPlusStringReal());
            operatorManager.addOperatorImpl(new OperatorGTRealReal());
            operatorManager.addOperatorImpl(new OperatorGERealReal());
            operatorManager.addOperatorImpl(new OperatorLERealReal());
            operatorManager.addOperatorImpl(new OperatorLTRealReal());
            operatorManager.addOperatorImpl(new OperatorEQRealReal());
            operatorManager.addOperatorImpl(new OperatorNERealReal());
            operatorManager.addOperatorImpl(new OperatorNotBoolean());
            //boolean值比较
            operatorManager.addOperatorImpl(new CommonOperatorImpl("eq", MathematicalElementType.BOOLEAN, MathematicalElementType.BOOLEAN) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    return new BooleanMathematicalElement(leftElement.getValue().equals(rightElement.getValue()));
                }
            });
            //boolean值比较
            operatorManager.addOperatorImpl(new CommonOperatorImpl("ne", MathematicalElementType.BOOLEAN, MathematicalElementType.BOOLEAN) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    return new BooleanMathematicalElement(!leftElement.getValue().equals(rightElement.getValue()));
                }
            });

            //string值比较
            operatorManager.addOperatorImpl(new CommonOperatorImpl("eq", MathematicalElementType.STRING, MathematicalElementType.STRING) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    return new BooleanMathematicalElement(leftElement.getValue().equals(rightElement.getValue()));
                }
            });
            //string值比较
            operatorManager.addOperatorImpl(new CommonOperatorImpl("ne", MathematicalElementType.STRING, MathematicalElementType.STRING) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    return new BooleanMathematicalElement(!leftElement.getValue().equals(rightElement.getValue()));
                }
            });
            //与null相关的运算
            operatorManager.addOperatorImpl(new CommonOperatorImpl("eq", MathematicalElementType.NULL, -1) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    boolean result = false;
                    if (leftElement instanceof NullMathematicalElement) {
                        result = rightElement.getValue() == null;
                    }
                    if (rightElement instanceof NullMathematicalElement) {
                        result = leftElement.getValue() == null;
                    }
                    return new BooleanMathematicalElement(result);
                }
            });
            operatorManager.addOperatorImpl(new CommonOperatorImpl("ne", MathematicalElementType.NULL, -1) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    boolean result = false;
                    if (leftElement instanceof NullMathematicalElement) {
                        result = rightElement.getValue() != null;
                    }
                    if (rightElement instanceof NullMathematicalElement) {
                        result = leftElement.getValue() != null;
                    }
                    return new BooleanMathematicalElement(result);
                }
            });
            //null值比较 通配符：*:8:-1 代表任何运算符在有一方为null时的运算规则
            operatorManager.addOperatorImpl(new CommonOperatorImpl("*", MathematicalElementType.NULL, -1) {
                @Override
                public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
                    return new NullMathematicalElement();
                }
            });


            // define a specific catalog (not necessary,we can use the default catalog instead)
            catalog = ExpressionFactory.getCatalog();


            addStringMethods();

            // add the user define function to the catalog
            catalog.addExpression(new IfFunction());
            catalog.addExpression(new AndFunction());
            catalog.addExpression(new OrFunction());
            catalog.addExpression(new NotFunction());
            catalog.addExpression(new SumFunction());
            catalog.addExpression(new MaxFunction());
            catalog.addExpression(new MinFunction());
            catalog.addExpression(new ChoseFunction());
            catalog.addExpression(new AvgFunction());
            catalog.addExpression(new MatchFunction());
            catalog.addExpression(new EqFunction());
            /**
             * 四舍五入
             */
            catalog.addExpression(new CommonFunction("round", false) {
                @Override
                public MathematicalElement evaluate(Parameters parameters) throws EvalException {
                    try {
                        MathematicalElement mex = parameters.getParameter("x");
                        MathematicalElement mey = parameters.getParameter("y");
                        if (mex instanceof NullMathematicalElement || mey instanceof NullMathematicalElement) {
                            return new NullMathematicalElement();
                        }
                        double value = 0.00;
                        if (mex.getValue() == null) {
                            value = 0.00;
                        } else if (mex instanceof BooleanMathematicalElement) {
                            value = (Boolean) mex.getValue() ? 1 : 0;
                        } else if (mex instanceof RealImpl) {
                            value = mex.getRealValue();
                        } else if (mex instanceof IntegerMathematicalElement) {
                            value = (Integer) mex.getValue();
                        }

                        int scale = (int) mey.getRealValue();

                        double result = Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);

                        return NumberFactory.createReal(result);

                    } catch (ParametersException e) {
                        throw new EvalException("Cannot evaluate " + getName() + "(x...). " + e);
                    }
                }

                @Override
                public List getParameters() {
                    List xParameters = new Vector(2);
                    xParameters.add("x");
                    xParameters.add("y");
                    return xParameters;
                }
            });

            //自动增加数学函数
            addMathMethods();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        Object value = evaluateExpression("if(a>=6,'9',2)",6);
//        Object value = evaluateExpression("if(a>=6,'9',2)",2);
//        Object value = evaluateExpression("or(true,false,true,5!=6)");
//        Object value = evaluateExpression("not(true)");
//        Object value = evaluateExpression("!true");
//        Object value = evaluateExpression("not(0)");
//        Object value = evaluateExpression("sum(true,5,6,8,10)");
//        Object value = evaluateExpression("min(false,5,6,8,10,-1)");
//        Object value = evaluateExpression("chose(4,5,6,8,'t',-1)");
//        Object value = evaluateExpression("left('abc',2)");
//        Object value = evaluateExpression("right('abc',2)");
//        Object value = evaluateExpression("mid('abcdefghijk',2,2)");
//        Object value = evaluateExpression("len('abcdefghijk')");
//        Object value = evaluateExpression("len('123456a')");
//        Object value = evaluateExpression("upper('123456a')");
//        Object value = evaluateExpression("match(1>=1,1,1!=1,'3',true)");
//        Object value = evaluateExpression("if(a==true,'是','否')", true);
//        Object value = evaluateExpression("null==null");
//        Object value = evaluateExpression("null!=null");
//        Object value = evaluateExpression("5*null");
//        Object value = evaluateExpression("(1+2)*3-6/5");
//        Object value = evaluateExpression("avg(11,2,5)");
        VarMap varMap = new VarMap();

        varMap.setValue("aa", 3.123456789);
//        Object value = evaluateExpression("aa*(1+2)*3-6/5+5e2",varMap);
//        Object value = evaluateExpression("round(aa)",varMap);

//        Object value = evaluateExpression("round(aa,4)", varMap);
//        Object value = evaluateExpression("pow(aa,4)", varMap);
        Object value = evaluateExpression("cos(aa)", varMap);

        System.err.println(value);
    }

    /**
     * 解析式中的参数名称从26个小写字母开始使用：视为位置参数吧。
     * 如：对于解析式"if(a>=b,'9',2)"，args[0]=2,这个2代表参数a的值，args[1] = 5,这个5代表b的值 生成的函数：f(a,b)=if(a>=b,'9',2)
     * 实际使用中可以首先把a,b的值直接替换的实际值，即"if(2>=5,'9',2)"，对应生成的函数：f()=if(2>=5,'9',2)，用于无参函数的求值
     *
     * @param expression
     * @param args
     * @return
     */
    public static Object evaluateExpression(String expression, Object... args) {
        try {
            Parameters parameters = ExpressionFactory.createParameters();

            StringBuilder stringBuilder = new StringBuilder("f(");
            if (ArrayUtil.isNotEmpty(args)) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (i != 0) {
                        stringBuilder.append(",");
                    }
                    if (arg == null) {
                        MathematicalElement me1 = new NullMathematicalElement();
                        parameters.addParameter(abc.charAt(i) + "", me1);
                    } else if (arg instanceof Boolean) {
                        MathematicalElement me1 = new BooleanMathematicalElement((Boolean) arg);
                        parameters.addParameter(abc.charAt(i) + "", me1);
                    } else if (arg instanceof Number) {
                        double doubleValue = ((Number) arg).doubleValue();
                        MathematicalElement me1 = NumberFactory.createReal(doubleValue);
                        parameters.addParameter(abc.charAt(i) + "", me1);
                    } else if (arg instanceof String) {
                        MathematicalElement me1 = new StringMathematicalElement((String) arg);
                        parameters.addParameter(abc.charAt(i) + "", me1);
                    }
                    stringBuilder.append(abc.charAt(i));
                }
            }
            stringBuilder.append(")=");
            Expression expressionElement = ExpressionFactory.createExpression(stringBuilder.toString() + expression, catalog,
                    expressionModel);
            Object value = expressionElement.evaluate(parameters).getValue();
            return value;
        } catch (EvalException e) {
            throw new RuntimeException(e);
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * evaluateExpression 命名参数【key：string,value：object】
     *
     * @param expression
     * @param varMap
     * @return
     */
    public static Object evaluateExpression(String expression, VarMap varMap) {
        try {
            Parameters parameters = ExpressionFactory.createParameters();

            StringBuilder stringBuilder = new StringBuilder("f(");
            if (varMap != null && varMap.isNotEmpty()) {
                String[] variableNames = varMap.getVariableNames();
                for (int i = 0; i < variableNames.length; i++) {
                    String argName = variableNames[i];
                    Object value = varMap.getValue(argName);
                    if (i != 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(argName);
                    if (value == null) {
                        MathematicalElement me1 = new NullMathematicalElement();
                        parameters.addParameter(argName, me1);
                    } else if (value instanceof Boolean) {
                        MathematicalElement me1 = new BooleanMathematicalElement((Boolean) value);
                        parameters.addParameter(argName, me1);
                    } else if (value instanceof Number) {
                        double doubleValue = ((Number) value).doubleValue();
                        MathematicalElement me1 = NumberFactory.createReal(doubleValue);
                        parameters.addParameter(argName, me1);
                    } else if (value instanceof String) {
                        MathematicalElement me1 = new StringMathematicalElement((String) value);
                        parameters.addParameter(argName, me1);
                    }
                }
            }
            stringBuilder.append(")=");
            Expression expressionElement = ExpressionFactory.createExpression(stringBuilder.toString() + expression, catalog,
                    expressionModel);
            Object value = expressionElement.evaluate(parameters).getValue();
            return value;
        } catch (EvalException e) {
            throw new RuntimeException(e);
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }
    }


    private static void addStringMethods() {

        Arrays.stream(new String[]{"left", "right", "mid", "substr", "lower", "upper", "len", "trim"}).forEach(s1 -> {
            ExpressionFactory.getCatalog().addExpression(new CommonFunction(s1, true) {
                @Override
                public MathematicalElement evaluate(Parameters parameters) throws EvalException {
                    try {
                        Object[] args = new Object[parameters.getParameters().size()];
                        int index = 0;
                        while (parameters.getParameters().containsKey("var" + index)) {
                            String key = "var" + index;
                            MathematicalElement mex = parameters.getParameter(key);

                            if (mex instanceof RealImpl) {
                                args[index++] = (int) mex.getRealValue();
                            } else {
                                args[index++] = (String) mex.getValue();
                            }
                        }
                        String str = (String) args[0];
                        int start = 0;
                        int end = 0;
                        switch (s1) {
                            case "len"://1个参数
                                if (args.length > 1)
                                    throw new EvalException("Cannot evaluate " + s1 + "(x). 参数数量错误");
                                return new IntegerMathematicalElement(StrUtil.length(str));
                            case "lower"://1个参数
                                if (args.length > 1)
                                    throw new EvalException("Cannot evaluate " + s1 + "(x). 参数数量错误");
                                return new StringMathematicalElement((str).toLowerCase());
                            case "upper"://1个参数
                                if (args.length > 1)
                                    throw new EvalException("Cannot evaluate " + s1 + "(x). 参数数量错误");
                                return new StringMathematicalElement((str).toUpperCase());
                            case "trim"://1个参数
                                if (args.length > 1)
                                    throw new EvalException("Cannot evaluate " + s1 + "(x). 参数数量错误");
                                return new StringMathematicalElement((str).trim());
                            case "mid"://可变参数
                                start = (int) args[1];
                                end = args.length > 2 ? ((int) args[2] + start) : str.length();
                                String substring = str.substring(start, end);
                                return new StringMathematicalElement(substring);
                            case "substr"://可变参数
                                start = (int) args[1];
                                end = args.length > 2 ? (int) args[2] : str.length();
                                String substring2 = str.substring(start, end);
                                return new StringMathematicalElement(substring2);
                            case "right":
                                if (args.length > 2)
                                    throw new EvalException("Cannot evaluate " + s1 + "(x，y). 参数数量错误");
                                start = (int) args[1];
                                start = str.length() - start;
                                return new StringMathematicalElement(str.substring(start));
                            case "left":
                                if (args.length > 2)
                                    throw new EvalException("Cannot evaluate " + s1 + "(x,y). 参数数量错误");
                                start = (int) args[1];
                                return new StringMathematicalElement(str.substring(0, start));
                        }
                    } catch (ParametersException pe) {
                        throw new EvalException("Cannot evaluate " + s1 + "(x...). " + pe);
                    }
                    return null;
                }
            });
        });

    }

    /**
     * 把Math.class的一些常用方法导入。
     * atan2
     * log10
     * pow
     * addExact
     * decrementExact
     * incrementExact
     * multiplyExact
     * negateExact
     * subtractExact
     * scalb
     * copySign
     * getExponent
     * signum
     * IEEEremainder
     * cbrt
     * expm1
     * floorDiv
     * floorMod
     * hypot
     * log1p
     * nextAfter
     * nextDown
     * nextUp
     * rint
     * tanh
     * toDegrees
     * toIntExact
     * toRadians
     * ulp
     */
    private static void addMathMethods() {
        Arrays.stream(Math.class.getMethods()).filter(method -> {
            if (!Modifier.isStatic(method.getModifiers())) {
                return false;
            }
            if (ExpressionFactory.getCatalog().listExpression().contains(method.getName()))
                return false;
            return true;
        }).forEach(method -> {
//            System.err.println(method.getName());
            final int parameterCount = method.getParameterCount();
            ExpressionFactory.getCatalog().addExpression(new CommonFunction(method.getName(), false) {
                @Override
                public MathematicalElement evaluate(Parameters parameters) throws EvalException {
                    try {
                        Object[] params = new Object[parameterCount];
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (int i = 0; i < params.length; i++) {
                            MathematicalElement parameter = parameters.getParameter(abc.charAt(i) + "");
                            Class<?> paramType = parameterTypes[i];
                            Object v = Convert.convert(paramType, parameter.getValue());
                            params[i] = v;
                        }
                        Object invoke = method.invoke(null, params);
                        Double convert = Convert.convert(double.class, invoke);
                        return NumberFactory.createReal(convert);
                    } catch (ParametersException | IllegalAccessException | IllegalArgumentException |
                             InvocationTargetException e) {
                        throw new EvalException("Cannot evaluate " + method.getName() + "(" + getParameters().toString() + "). " + e);
                    }
                }

                @Override
                public List getParameters() {
                    List xParameters = new Vector(parameterCount);
                    for (int i = 0; i < parameterCount; i++) {
                        xParameters.add(abc.charAt(i) + "");
                    }
                    return xParameters;
                }

            });
        });

//        for (final Method method : Math.class.getMethods()) {
//            if (!Modifier.isStatic(method.getModifiers())) {
//                continue;
//            }
//            if (ExpressionFactory.getCatalog().listExpression().contains(method.getName()))
//                continue;
//            // System.err.println(method.getName());
//            final int parameterCount = method.getParameterCount();
//            ExpressionFactory.getCatalog().addExpression(new AbstractFunction() {
//                @Override
//                public List getParameters() {
//                    Vector<Object> vector = new Vector<>(parameterCount);
//                    for (int i = 0; i < parameterCount; i++) {
//                        vector.add(abc.charAt(i) + "");
//                    }
//                    return vector;
//                }
//
//                @Override
//                public String getName() {
//                    return method.getName();
//                }
//
//                @Override
//                public Catalog getCatalog() {
//                    return ExpressionFactory.getCatalog();
//                }
//
//                @Override
//                public MathematicalElement evaluate(OperatorManager operatorManager, Parameters parameters) throws EvalException {
//                    return evaluate(parameters);
//                }
//
//                @Override
//                public MathematicalElement evaluate(Parameters parameters) throws EvalException {
//                    try {
//                        Object[] params = new Object[parameterCount];
//                        Class<?>[] parameterTypes = method.getParameterTypes();
//                        for (int i = 0; i < params.length; i++) {
//                            MathematicalElement parameter = parameters.getParameter(abc.charAt(i) + "");
//                            Class<?> paramType = parameterTypes[i];
//                            Object v = Convert.convert(paramType, parameter.getValue());
//                            params[i] = v;
//                        }
//                        Object invoke = method.invoke(null, params);
//                        Double convert = Convert.convert(double.class, invoke);
//                        return NumberFactory.createReal(convert);
//                    } catch (ParametersException | IllegalAccessException | IllegalArgumentException |
//                             InvocationTargetException e) {
//                        throw new EvalException("Cannot evaluate " + method.getName() + "(" + getParameters().toString() + "). " + e);
//                    }
//                }
//            });
//        }

    }
}
