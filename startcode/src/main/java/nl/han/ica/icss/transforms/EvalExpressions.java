package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.HashMap;
import java.util.LinkedList;

public class EvalExpressions implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;
    private HashMap<String, Expression> variables;

    public EvalExpressions() {
        variables = new HashMap<>();
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        removeVariableAssign(ast.root);
        evalExpression(ast.root, ast.root);
        variableValues = new LinkedList<>();
    }

    /*
    TODO : variablen uitrekenen.
    TODO: uitreken methode.
     */

    /*
    Main method that replaces nodes in Tree.
     */
    private void evalExpression(ASTNode child, ASTNode parent) {
        if (child instanceof Expression) {
            Literal result = calculateExpression((Expression) child);
            parent.removeChild(child);
            parent.addChild(result);
        }
        for (ASTNode nodes : child.getChildren()) {
            evalExpression(nodes, child);
        }
    }

    /*
    method for deciding what to replace a node with.
     */
    private Literal calculateExpression(Expression exp) {
        if (exp instanceof Literal) {
            return (Literal) exp;
        }
        if (exp instanceof VariableReference) {
            Expression var = variables.get(((VariableReference) exp).name);
            return calculateExpression(var);
        }
        if (exp instanceof Operation) {
            Literal left = calculateExpression(((Operation) exp).lhs);
            Literal right = calculateExpression(((Operation) exp).rhs);
            return calculateOperation((Operation) exp, left, right);
        }
        return null;
    }

    /*
       Removes variable assignments ands puts them in a map for references to use.
     */
    private void removeVariableAssign(ASTNode node) {
        if (node instanceof VariableAssignment) {
            variables.put(((VariableAssignment) node).name.name, ((VariableAssignment) node).expression);
            node.removeChild(node);
        }
        for (ASTNode child : node.getChildren()) {
            removeVariableAssign(child);
        }
    }

    /*
    Method for calculating Operations, calls Submethods based on what operation is needed.
     */
    private Literal calculateOperation(Operation op, Expression left, Expression right) {
        if (op instanceof AddOperation) {
            return calculateAdd(left, right);
        }
        if (op instanceof SubtractOperation) {
            return calculateMin(left, right);
        }
        if (op instanceof MultiplyOperation) {
            return calculateMul(left, right);
        }
        return null;
    }

    // multiple operation, only allowed with Scalar's
    private ScalarLiteral calculateMul(Expression left, Expression right) {
        ScalarLiteral litLeft = (ScalarLiteral) left;
        ScalarLiteral litRight = (ScalarLiteral) right;
        int result = litLeft.value * litRight.value;
        return new ScalarLiteral(result);
    }

    /*
    Only check left because values have to be equal to pass the checker.
     */
    private Literal calculateAdd(Expression left, Expression right) {
        if (left instanceof ScalarLiteral) {
            ScalarLiteral litLeft = (ScalarLiteral) left;
            ScalarLiteral litRight = (ScalarLiteral) right;
            int result = litLeft.value + litRight.value;
            return new ScalarLiteral(result);
        }
        if (left instanceof PixelLiteral) {
            PixelLiteral litLeft = (PixelLiteral) left;
            PixelLiteral litRight = (PixelLiteral) right;
            int result = litLeft.value + litRight.value;
            return new PixelLiteral(result);
        }
        if (left instanceof PercentageLiteral) {
            PercentageLiteral litLeft = (PercentageLiteral) left;
            PercentageLiteral litRight = (PercentageLiteral) right;
            int result = litLeft.value + litRight.value;
            return new PercentageLiteral(result);
        }
        return null;
    }

    /*
    Only check left because values have to be equal to pass the checker.
     */
    private Literal calculateMin(Expression left, Expression right) {
        if (left instanceof ScalarLiteral) {
            ScalarLiteral litLeft = (ScalarLiteral) left;
            ScalarLiteral litRight = (ScalarLiteral) right;
            int result = litLeft.value - litRight.value;
            return new ScalarLiteral(result);
        }
        if (left instanceof PixelLiteral) {
            PixelLiteral litLeft = (PixelLiteral) left;
            PixelLiteral litRight = (PixelLiteral) right;
            int result = litLeft.value - litRight.value;
            return new PixelLiteral(result);
        }
        if (left instanceof PercentageLiteral) {
            PercentageLiteral litLeft = (PercentageLiteral) left;
            PercentageLiteral litRight = (PercentageLiteral) right;
            int result = litLeft.value - litRight.value;
            return new PercentageLiteral(result);
        }
        return null;
    }


}
