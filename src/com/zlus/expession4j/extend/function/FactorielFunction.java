package com.zlus.expession4j.extend.function;

import java.util.List;

import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.OperatorManager;
import fr.expression4j.core.Catalog;
import fr.expression4j.core.Expression;
import fr.expression4j.core.ExpressionModel;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParametersException;
import fr.expression4j.core.predefine.AbstractFunction;
import fr.expression4j.factory.ExpressionFactory;
import fr.expression4j.factory.NumberFactory;
import fr.expression4j.util.ParameterUtil;

public class FactorielFunction extends AbstractFunction {
	/**
	 * compute the factoriel of a number
	 * 
	 * @param x
	 *            number to compute
	 * @return factoriel of the number.
	 */
	private double fact(double x) {
		if (x == 0) {
			return 1;
		}
		return x * fact(x - 1);
	}

	/**
	 * evaluation method called by the Expression4j when needed.
	 * 
	 * @param parameters
	 *            parameter given to the function for the evaluation.
	 */
	public MathematicalElement evaluate(Parameters parameters) throws EvalException {
		try {
			MathematicalElement me = parameters.getParameter("x");
			double tmpValue = me.getRealValue();
			if (tmpValue < 0) {
				throw new EvalException("Cannot evaluate fact of " + "negativ number.");
			}
			double result = fact(tmpValue);
			return NumberFactory.createReal(result);
		} catch (ParametersException pe) {
			throw new EvalException("Cannot evaluate fact(x). " + pe);
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
		return "fact";
	}

	/**
	 * get parameter list of the function.
	 */
	public List getParameters() {
		// this util method get one parameter "x".
		return ParameterUtil.generateXParameters();
	}

	public ExpressionModel getExpressionModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		try {
			// define a specific catalog (not necessary,
			// we can use the default catalog instead)
			Catalog catalog = ExpressionFactory.createCatalog("catalog");

			// add the user define function to the catalog
			catalog.addExpression(new FactorielFunction());

			// create the expression who use the user define function
			// dont forget to put the catalog of the user define function
			// if default catalog is not used.
			Expression expression1 = ExpressionFactory.createExpression("f(x)=fact(if(true,6,9))", catalog);
			System.out.println("Expression name: " + expression1.getName());
			System.out.println("Expression : " + expression1);
			System.out.println("Expression parameters: " + expression1.getParameters());

			// compute a value
			MathematicalElement me1 = NumberFactory.createReal(5);
			Parameters parameters = ExpressionFactory.createParameters();
			parameters.addParameter("x", me1);
			System.out.println("Value of expression h :" + expression1.evaluate(parameters));
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
}