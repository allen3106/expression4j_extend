package com.zlus.expession4j.extend.constant;

/**
 * Java中的运算符优先级从高到低分为以下几个级别：
 *
 * 1. 一元运算符：包括递增（++）和递减（--）运算符，以及正号（+）和负号（-）运算符。它们的优先级最高，会先于其他运算符进行计算。 *
 * 2. 算术运算符：包括乘法（*）、除法（/）和取模（%）运算符，以及加法（+）和减法（-）运算符。它们的优先级次之，按照从左到右的顺序进行计算。 *
 * 3. 移位运算符：包括左移（<<）、右移（>>）和无符号右移（>>>）运算符。它们的优先级比算术运算符低，按照从左到右的顺序进行计算。 *
 * 4. 关系运算符：包括小于（<）、大于（>）、小于等于（<=）、大于等于（>=）、等于（==）和不等于（!=）运算符。它们的优先级比移位运算符低，按照从左到右的顺序进行计算。 *
 * 5. 逻辑运算符：包括逻辑与（&&）、逻辑或（||）和逻辑非（!）运算符。它们的优先级比关系运算符低，按照从左到右的顺序进行计算。 *
 * 6. 条件运算符：即三元运算符（?:），它的优先级比逻辑运算符低，按照从左到右的顺序进行计算。 *
 * 7. 赋值运算符：包括简单赋值（=）以及复合赋值运算符（+=、-=、*=、/=、%=等）。它们的优先级最低，按照从右到左的顺序进行计算。
 */
public interface MathematicalOperatorPriority {
    int ASSIGNMENT_OPERATOR=1;
    /**三元运算符*/
    int TERNARY_OPERATOR=2;
    /**
     * 逻辑运算符
     */
    int LOGICAL_OPERATOR=3;

    /**
     * 关系运算符
     */
    int RELATION_OPERATOR=4;
    /**
     * 移位运算符
     */
    int SHIFT_OPERATOR=5;
    /**
     * 算术运算符（加，减）：+-
     */
    int PLUS_MINUS_OPERATOR=6;
    /**
     * 算术运算符(乘法，除法)：*  /
     */
    int MULTIPLE_DIVIDE_OPERATOR=7;
    /**
     * 一元运算符
     */
    int UNARY_OPERATOR=10;

}