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
package com.zlus.expession4j.extend.operator;

import cn.hutool.core.util.StrUtil;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.OperatorImpl;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.sample.custom.operator.BooleanMathematicalElement;

/**
 * implement superior operation between a real and a real.
 * <p>
 * 非：！
 *
 * @author SGINER
 */

public class OperatorNotBoolean implements OperatorImpl {

    public OperatorNotBoolean() {
        super();
    }

    public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
        boolean b = false;
        switch (leftElement.getType()) {
            case 1://数字RealImpl
                b = leftElement.getRealValue() != 0;
                break;
            case 2://ComplexImpl
                b = leftElement.getRealValue() != 0;
                break;
            case 5://StringMathematicalElement
                b = StrUtil.isNotEmpty((CharSequence) leftElement.getValue());
                break;
            case 6:
                b = (boolean) leftElement.getValue();
                break;
        }

        BooleanMathematicalElement result = new BooleanMathematicalElement(!b);
        return result;
    }

    public int getLeftOperandeType() {
        return 6;
    }

    //this name must be the same as the name given in the operator.
    public String getOperatorName() {
        return "not";
    }

    public int getRightOperandeType() {
        return 6;
    }

    public boolean isUnaryOperator() {
        return true;
    }

}
