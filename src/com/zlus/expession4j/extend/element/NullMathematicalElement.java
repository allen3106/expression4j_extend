package com.zlus.expession4j.extend.element;

import com.zlus.expession4j.extend.constant.MathematicalElementType;
import fr.expression4j.basic.MathematicalElement;
import fr.expression4j.basic.MathematicalException;

import java.util.Properties;

public class NullMathematicalElement implements MathematicalElement {
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public double getRealValue() {
        return 0;
    }

    @Override
    public double getComplexValue() throws MathematicalException {
        return 0;
    }

    @Override
    public int getType() {
        return MathematicalElementType.NULL;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public Properties getProperties() {
        return null;
    }
}
