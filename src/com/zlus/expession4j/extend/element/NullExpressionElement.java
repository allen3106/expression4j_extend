//Copyright 2006 Stephane GINER
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package com.zlus.expession4j.extend.element;

import fr.expression4j.basic.ExpressionElement;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.OperatorManager;
import fr.expression4j.basic.ParseInfo;
import fr.expression4j.core.Catalog;
import fr.expression4j.core.ExpressionModel;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParsingException;
import fr.expression4j.core.impl.TreeElement;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;
import fr.expression4j.util.ExpressionElementUtil;

import java.util.List;

/**
 * 
 * null
 * 
 */
public class NullExpressionElement implements ExpressionElement {

	public NullExpressionElement() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.expression4j.basic.ExpressionElement#parseElement(java.lang.String,
	 * fr.expression4j.basic.ParseInfo)
	 */
	public boolean parseElement(String expression, ExpressionModel expressionModel, ParseInfo parseInfo,
			Catalog catalog, List functionParameters, int priorityOperatorLevel) throws ParsingException {
		int startPos = parseInfo.getEndPos();
		int currentPos = parseInfo.getEndPos();

		String str_null = "null";


		// 检查是否开始为字符t
		if (ExpressionElementUtil.checkPosition(expression, currentPos) && expression.charAt(currentPos) == 'n') {
			currentPos++;

			// lokk for the end '
			while (ExpressionElementUtil.checkPosition(expression, currentPos)) {
				if (str_null.charAt(currentPos - startPos) != expression.charAt(currentPos)) {
					return false;
				}
				if (currentPos - startPos == str_null.length() - 1) {
					break;
				}
				currentPos++;
			}

			// found the ending '
			// create the corresponding TreeElement of the
			// StringExpressionElement
			MathematicalElement value = new NullMathematicalElement();
			TreeElement result = new TreeElement(TreeElement.TREE_ELEMENT_TYPE_VALUE, null, null, value, getName(),
					null, null);
			parseInfo.setTreeElement(result);
			parseInfo.setEndPos(currentPos + 1);
			return true;
		}
		return false;

	}

	public String getName() {
		return "NullExpressionElement";
	}

	public MathematicalElement evaluate(TreeElement element, Catalog catalog, OperatorManager operatorManager,
			Parameters parameters, ExpressionModel model) throws EvalException {
		// throw an exception.
		// evaluate method is reserved for function or specific custom
		// expression element
		// like "if" operator.
		throw new EvalException("Cannot evaluate " + getName());
	}

	public String toString(TreeElement element, ExpressionModel expressionModel) {
		return "<" + getName() + ">";
	}

}
