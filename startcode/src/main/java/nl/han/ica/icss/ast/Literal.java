package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;

public abstract class Literal extends Expression {
    public int getValue(){
    return 0;
    }

    @Override
    public ExpressionType getType() {
        return null;
    }
}
