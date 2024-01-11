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

import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.OperatorImpl;
import fr.expression4j.core.exception.EvalException;

/**
 * 通用操作符，只需覆写compute方法，其它属性通过构造函数赋值
 *
 * @author j
 */

public class CommonOperatorImpl implements OperatorImpl {

    private String operatorName;
    private int rightOperandeType;
    private int leftOperandeType;
    private boolean isUnaryOperator = false;

    public CommonOperatorImpl() {
        super();
    }

    public CommonOperatorImpl(String operatorName, int leftOperandeType, int rightOperandeType) {
       this(operatorName,leftOperandeType,rightOperandeType,false);
    }

    public CommonOperatorImpl(String operatorName, int leftOperandeType, int rightOperandeType, boolean isUnaryOperator) {
        super();
        this.operatorName = operatorName;
        this.leftOperandeType = leftOperandeType;
        this.rightOperandeType = rightOperandeType;
        this.isUnaryOperator = isUnaryOperator;
    }

    public MathematicalElement compute(MathematicalElement leftElement, MathematicalElement rightElement) throws EvalException {
        return null;
    }

    public int getLeftOperandeType() {
        return leftOperandeType;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public int getRightOperandeType() {
        return rightOperandeType;
    }

    public boolean isUnaryOperator() {
        return isUnaryOperator;
    }

}
