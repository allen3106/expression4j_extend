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

package fr.expression4j.basic.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Predicate;

import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.OperatorImpl;
import fr.expression4j.basic.OperatorManager;
import fr.expression4j.core.exception.EvalException;

public class OperatorManagerImpl implements OperatorManager {

    private String name;
    private Map operatorMap = new HashMap(20);

    public OperatorManagerImpl(String name) {
        this.name = name;
    }

    private String computeKeyMap(String opName, int left, int right) {
        return opName + ":" + left + ":" + right;
    }

    public void addOperatorImpl(OperatorImpl operatorImpl) {
        if (operatorImpl == null) {
            return;
        }

        if (operatorImpl.isUnaryOperator()) {
            operatorMap.put(computeKeyMap(operatorImpl.getOperatorName(), operatorImpl.getLeftOperandeType(), 0), operatorImpl);
        } else {
            operatorMap.put(computeKeyMap(operatorImpl.getOperatorName(), operatorImpl.getLeftOperandeType(), operatorImpl.getRightOperandeType()), operatorImpl);
        }
    }

    public void removeOperatorImpl(OperatorImpl operatorImpl) {
        if (operatorImpl == null) {
            return;
        }

        if (operatorImpl.isUnaryOperator()) {
            operatorMap.remove(computeKeyMap(operatorImpl.getOperatorName(), operatorImpl.getLeftOperandeType(), 0));
        } else {
            operatorMap.remove(computeKeyMap(operatorImpl.getOperatorName(), operatorImpl.getLeftOperandeType(), operatorImpl.getRightOperandeType()));
        }
    }

    public MathematicalElement computeValue(String operatorName,
                                            MathematicalElement element) throws EvalException {

        OperatorImpl currentOperatorImpl = (OperatorImpl) operatorMap.get(computeKeyMap(operatorName, element.getType(), 0));
        return currentOperatorImpl.compute(element, null);
    }

    public MathematicalElement computeValue(String operatorName,
                                            MathematicalElement leftElement, MathematicalElement rightElement)
            throws EvalException {

        OperatorImpl currentOperatorImpl = (OperatorImpl) operatorMap.get(computeKeyMap(operatorName, leftElement.getType(), rightElement.getType()));
        if (currentOperatorImpl == null) {
            currentOperatorImpl = (OperatorImpl) operatorMap.get(computeKeyMap(operatorName, rightElement.getType(), leftElement.getType()));
        }

        if (currentOperatorImpl == null) {
            /**找到通配符设置的OperatorImpl对象*/
            currentOperatorImpl = findWildcardOperatorImpl(operatorName, rightElement.getType() + "", leftElement.getType() + "");
        }


        if (currentOperatorImpl == null) {
            throw new EvalException("Could not find operator implementation for " + computeKeyMap(operatorName, leftElement.getType(), rightElement.getType()));
        }
        return currentOperatorImpl.compute(leftElement, rightElement);
    }

    /**
     * 通配符：*:8:-1 代表任何运算符在有一方为null时的运算规则
     * @param operatorName
     * @param leftElement
     * @param rightElement
     * @return
     */
    private OperatorImpl findWildcardOperatorImpl(String operatorName, String leftElement, String rightElement) {
        /**取到有通配符的key*/
        Object[] keys = operatorMap.keySet().stream().filter(key -> {
            String str = (String) key;
            String[] strings = str.split(":");
            return "*".equals(strings[0]) || "-1".equals(strings[1]) || "-1".equals(strings[2]);
        }).toArray();
        /**
         * key 中的两个值与条件相同
         */
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String[] strings = key.split(":");
            int count = 0;
            if ("*".equals(strings[0])) {
                if (leftElement.equals(strings[1]) && rightElement.equals(strings[2]))
                    return (OperatorImpl) operatorMap.get(key);
                if (rightElement.equals(strings[1]) && leftElement.equals(strings[2]))
                    return (OperatorImpl) operatorMap.get(key);
            } else if (operatorName.equals(strings[0])) {
                if (leftElement.equals(strings[1]) || rightElement.equals(strings[1]))
                    return (OperatorImpl) operatorMap.get(key);
                if (rightElement.equals(strings[2]) && leftElement.equals(strings[2]))
                    return (OperatorImpl) operatorMap.get(key);
            }
        }
        /**有且只有一个条件满足*/
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String[] strings = key.split(":");
            if (operatorName.equals(strings[0]) && "-1".equals(strings[1]) && "-1".equals(strings[2]))
                return (OperatorImpl) operatorMap.get(key);
            else if ("*".equals(strings[0])) {
                if ((leftElement.equals(strings[1]) || rightElement.equals(strings[1])) && "-1".equals(strings[2]))
                    return (OperatorImpl) operatorMap.get(key);
                if ((leftElement.equals(strings[2]) || rightElement.equals(strings[2])) && "-1".equals(strings[1]))
                    return (OperatorImpl) operatorMap.get(key);
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public OperatorImpl getOperatorImpl(String name) {
        return (OperatorImpl) operatorMap.get(name);
    }

    public List getOperatorImplList() {
        List result = new Vector(10);
        Iterator iter = operatorMap.keySet().iterator();

        while (iter.hasNext()) {
            result.add(iter.next());
        }

        return result;
    }

}
