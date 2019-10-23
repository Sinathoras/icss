package nl.han.ica.icss.checker;

import java.nio.charset.MalformedInputException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.errorprone.annotations.Var;
import com.sun.javafx.collections.MappingChange;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    public static Map<String, Expression> assignments = new HashMap<>();


    public LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        checkNode(ast.root);

    }

    private void checkNode(ASTNode node) {
        addVariables(node);
        checkVariablesDefined(node);
        checkOperations(node);
        checkDeclarations(node);
        for (ASTNode nodes : node.getChildren()) {
            checkNode(nodes);
        }
    }

    private void addVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            VariableMap.assignments.put(((VariableAssignment) node).name.name, ((VariableAssignment) node).expression);
        }
    }

    private void checkVariablesDefined(ASTNode node) {
        if (node instanceof VariableReference) {
            if (VariableMap.assignments.get(((VariableReference) node).name) == null) {
                node.setError("this variable is not assigned");
            }
        }
    }

    private void checkOperations(ASTNode node) {
        if (node instanceof MultiplyOperation) {
            checkMulOperations(node);
        }
        if (node instanceof AddOperation || node instanceof SubtractOperation) {
            checkAddMinOperations(node);
        }
    }

    /*
     */
    // checks MulOperations for scalar values
    private void checkMulOperations(ASTNode node) {
        if (((Operation) node).lhs.getType() != ExpressionType.SCALAR && ((Operation) node).rhs.getType() != ExpressionType.SCALAR) {
            node.setError("multiplying can only be done with scalar values.");
        }
        if (((Operation) node).lhs instanceof Operation) {
            checkOperations(((Operation) node).lhs);
        }
        if (((Operation) node).rhs instanceof Operation) {
            checkOperations(((Operation) node).rhs);
        }
        if (((Operation) node).lhs instanceof ColorLiteral || ((Operation) node).rhs instanceof ColorLiteral) {
            node.setError("colors are not allowed in operations");
        }
    }

    // checkt of beide waarden in een + of - operatie gelijk zijn.
    private void checkAddMinOperations(ASTNode node) {
        String addSubstractError = "Add and Subtract operations need the same values.";
        // alleen uitvoeren als node een + of - operatie is.
        if (((Operation) node).lhs.getType() != ((Operation) node).rhs.getType()) {
            node.setError(addSubstractError);
        }
        if (((Operation) node).lhs instanceof Operation) {
            checkOperations(((Operation) node).lhs);
        }
        if (((Operation) node).rhs instanceof Operation) {
            checkOperations(((Operation) node).rhs);
        }
        if (((Operation) node).lhs instanceof ColorLiteral || ((Operation) node).rhs instanceof ColorLiteral) {
            node.setError("colors are not allowed in operations");
        }
    }

    /*
    werkt ervanuitgaand dat alle variabelen gedefinieerd zijn.
     */
    private void checkDeclarations(ASTNode node) {
        if (node instanceof Declaration) {
            if (((Declaration) node).property.name.contains("width") || ((Declaration) node).property.name.contains("height")) {
                if (((Declaration) node).expression.getType() != ExpressionType.PIXEL) {
                    if (((Declaration) node).expression instanceof VariableReference) {
                        if (VariableMap.assignments.get(((VariableReference) ((Declaration) node).expression).name).getType() != ExpressionType.PIXEL) {
                            node.setError("Width or height declarations require pixel values.");
                        }
                    }
                    node.setError("Width or height declarations require pixel values.");
                }
            }
            if (((Declaration) node).property.name.contains("color")) {
                if (((Declaration) node).expression.getType() != ExpressionType.COLOR) {
                    if (((Declaration) node).expression instanceof VariableReference) {
                        if (VariableMap.assignments.get(((VariableReference) ((Declaration) node).expression).name) != null) {
                            if (VariableMap.assignments.get(((VariableReference) ((Declaration) node).expression).name).getType() != ExpressionType.COLOR) {
                                node.setError("Color Declarations require color values.");
                            }
                        }
                    }
                    node.setError("Color Declarations require Color values.");
                }
            }
        }
    }
}


