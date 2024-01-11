package com.zlus.expession4j.extend.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Vector;

import cn.hutool.core.convert.Convert;
import com.zlus.expession4j.extend.element.BooleanExpressionElement;
import com.zlus.expession4j.extend.operator.OperatorGERealReal;
import com.zlus.expession4j.extend.operator.OperatorGTRealReal;
import com.zlus.expession4j.extend.operator.OperatorLERealReal;
import com.zlus.expession4j.extend.operator.OperatorLTRealReal;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.Operator;
import fr.expression4j.basic.OperatorManager;
import fr.expression4j.core.Catalog;
import fr.expression4j.core.Expression;
import fr.expression4j.core.ExpressionModel;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ModelException;
import fr.expression4j.core.exception.ParametersException;
import fr.expression4j.core.predefine.AbstractFunction;
import fr.expression4j.factory.ExpressionFactory;
import fr.expression4j.factory.ExpressionModelFactory;
import fr.expression4j.factory.NumberFactory;
import fr.expression4j.factory.OperatorFactory;
import fr.expression4j.factory.OperatorManagerFactory;

import fr.expression4j.sample.custom.element.OperatorPlusStringReal;
import fr.expression4j.sample.custom.element.OperatorPlusStringString;
import fr.expression4j.sample.custom.element.StringExpressionElement;
import fr.expression4j.sample.custom.element.StringMathematicalElement;


/**
 * IF(boolean,valIfTrue,valIfFalse)
 * 
 * @author j
 *
 */
public class IfFunction extends AbstractFunction {
	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private Object IF(Object x, Object y, Object z) {
		return x != null && Boolean.parseBoolean(x.toString()) ? y : z;
	}

	/**
	 * evaluation method called by the Expression4j when needed.
	 * 
	 * @param parameters
	 *            parameter given to the function for the evaluation.
	 */
	public MathematicalElement evaluate(Parameters parameters) throws EvalException {
		try {
			MathematicalElement mex = parameters.getParameter("x");
			MathematicalElement mey = parameters.getParameter("y");
			MathematicalElement mez = parameters.getParameter("z");

			Object result = IF(mex.getValue(), mey.getValue(), mez.getValue());
			if (result == null) {
				return null;
			}
			return result instanceof String ? new StringMathematicalElement(result.toString())
					: NumberFactory.createReal(Double.parseDouble(result.toString()));
		} catch (ParametersException pe) {
			throw new EvalException("Cannot evaluate if(x). " + pe);
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
		return "if";
	}

	/**
	 * get parameter list of the function.
	 */
	public List getParameters() {

		List xParameters = new Vector(3);
		xParameters.add("x");
		xParameters.add("y");
		xParameters.add("z");

		return xParameters;
	}

	public ExpressionModel getExpressionModel() {
		// TODO Auto-generated method stub
		try {
			return ExpressionModelFactory.getDefaultExpressionModel();
		} catch (ModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * To use this user define function in Expression4j, you must put it in the
	 * classpath and use it like this:
	 */

	public static void main(String[] args) {
		try {

			ExpressionModel stringExpressionModel = ExpressionModelFactory.getDefaultExpressionModel();
			// add binary operator supported by the specific expression model
			// stringExpressionModel.addBinaryOperator(new OperatorPlus(), 1);
			Operator greaterThen = OperatorFactory.createOperator("gt", ">", false);
			Operator greaterOrEq = OperatorFactory.createOperator("ge", ">=", false);
			Operator lt = OperatorFactory.createOperator("lt", "<", false);
			Operator le = OperatorFactory.createOperator("le", "<=", false);

			// add custom expression element
			//优先级别高，先考虑字符串
			stringExpressionModel.addExpressionElement(new StringExpressionElement(), 1);
			//其次是boolean
			stringExpressionModel.addExpressionElement(new BooleanExpressionElement(), 2);
			// add binary（二进制的，数字的） operator supported by the specific expression model
			//优先级别最低 le,ge 比lt,gt优先级别高,优先级别越高越先匹配
			stringExpressionModel.addBinaryOperator(greaterThen, 1);
			stringExpressionModel.addBinaryOperator(greaterOrEq, 2);
			stringExpressionModel.addBinaryOperator(lt, 1);
			stringExpressionModel.addBinaryOperator(le, 2);

			// add standardexpression element
			// stringExpressionModel.addExpressionElement(new
			// ComplexOrRealExpressionElement(), 2);
			// stringExpressionModel.addExpressionElement(new
			// FunctionExpressionElement(), 3);
			// stringExpressionModel.addExpressionElement(new
			// ConstantOrVariableExpressionElement(), 4);
			// stringExpressionModel.addExpressionElement(new
			// ParenthesisExpressionElement(), 5);

			OperatorManager operatorManager = OperatorManagerFactory.getDefaultOperatorManager();

			// add operator specific to previous model
			operatorManager.addOperatorImpl(new OperatorPlusStringString());
			operatorManager.addOperatorImpl(new OperatorPlusStringReal());
			operatorManager.addOperatorImpl(new OperatorGTRealReal());
			operatorManager.addOperatorImpl(new OperatorGERealReal());
			operatorManager.addOperatorImpl(new OperatorLERealReal());
			operatorManager.addOperatorImpl(new OperatorLTRealReal());

			// define a specific catalog (not necessary,
			// we can use the default catalog instead)
			Catalog catalog = ExpressionFactory.getCatalog();// ExpressionFactory.createCatalog("catalog");

			// add the user define function to the catalog
			catalog.addExpression(new IfFunction());
			addMathMethods();

			// create the expression who use the user define function
			// dont forget to put the catalog of the user define function
			// if default catalog is not used.
//			Expression expression1 = ExpressionFactory.createExpression("f()=if(8>6,'5/1',min(8,9))", catalog,
//					stringExpressionModel);
			// System.out.println("Expression name: " + expression1.getName());
			// System.out.println("Expression : " + expression1);
			// System.out.println("Expression parameters: " +
			// expression1.getParameters());

			// compute a value
			// MathematicalElement me1 = NumberFactory.createReal(0);
			// Parameters parameters = ExpressionFactory.createParameters();
			// parameters.addParameter("x", me1);
//			System.out.println("Value of expression h :" + expression1.evaluate(null));
			
			System.err.println( ExpressionFactory.createExpression("f()=6>=6", catalog,
					stringExpressionModel).evaluate(null).getValue());
			System.err.println( ExpressionFactory.createExpression("f()=min(1,5)", catalog,
					stringExpressionModel).evaluate(null).getValue());
			System.err.println( ExpressionFactory.createExpression("f()=if(6>=6,'9',2)", catalog,
					stringExpressionModel).evaluate(null));
			System.err.println( ExpressionFactory.createExpression("f()=if(false,'9',2)", catalog,
					stringExpressionModel).evaluate(null));

			System.err.println( ExpressionFactory.createExpression("f()=true", catalog,
			stringExpressionModel).evaluate(null).getValue());
			System.err.println( ExpressionFactory.createExpression("f()='true'", catalog,
					stringExpressionModel).evaluate(null).getType());
			
			System.err.println( ExpressionFactory.createExpression("f()='true'", catalog,
			stringExpressionModel).evaluate(null).getType());
		} catch (Exception e) {
			// System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}

	private static void addMathMethods() {
		final String abc = "abcdefghijklmnopqrstuvwxyz";
		for (final Method method : Math.class.getMethods()) {
			if (!Modifier.isStatic(method.getModifiers())) {
				continue;
			}
			// System.err.println(method.getName());
			final int parameterCount = method.getParameterCount();
			ExpressionFactory.getCatalog().addExpression(new AbstractFunction() {
				@Override
				public List getParameters() {
					Vector<Object> vector = new Vector<>(parameterCount);
					for (int i = 0; i < parameterCount; i++) {
						vector.add(abc.charAt(i) + "");
					}

					return vector;
				}

				@Override
				public String getName() {
					return method.getName();
				}

				@Override
				public Catalog getCatalog() {
					return ExpressionFactory.getCatalog();
				}

				@Override
				public MathematicalElement evaluate(OperatorManager operatorManager, Parameters parameters)
						throws EvalException {
					return evaluate(parameters);
				}

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
					} catch (ParametersException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						throw new EvalException(
								"Cannot evaluate " + method.getName() + "(" + getParameters().toString() + "). " + e);
					}
				}
			});
		}

	}
}