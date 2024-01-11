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

import com.zlus.expession4j.extend.constant.MathematicalElementType;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.MathematicalException;

import java.util.Properties;

/**
 * define a mathematical element who contens a Integer element.
 * @author j
 *
 */
public class IntegerMathematicalElement implements MathematicalElement {

	//parameter value used to store element in a config file.
	public final static String INTEGER_VALUE = "IntegerValue";

	private Integer integerValue;
	
	public IntegerMathematicalElement(int value) {
		super();
		this.integerValue = Integer.valueOf(value);
	}

	public double getComplexValue() throws MathematicalException {
		//no used
		return 0;
	}

	public Properties getProperties() {
		//used to store objet in xml file (used by configuration manager)
		Properties result = new Properties();
		result.put(INTEGER_VALUE, integerValue.toString());
		return result;
	}

	public double getRealValue() {
		//not used
		return integerValue;
	}

	public int getType() {
		//create a new constant for integer type.
		return MathematicalElementType.INTEGER;
	}

	public Object getValue() {
		//get the value of the String as an Object.
		return integerValue;
	}

	public void setProperties(Properties properties) {
		//used to store objet in xml file (used by configuration manager)
		integerValue = Integer.valueOf(properties.getProperty(INTEGER_VALUE));
	}

}
