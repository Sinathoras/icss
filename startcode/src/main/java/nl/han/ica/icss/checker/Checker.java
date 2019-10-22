package nl.han.ica.icss.checker;

import java.nio.charset.MalformedInputException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.sun.javafx.collections.MappingChange;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.*;

public class Checker {
    private Map<String, Expression> assignments = new HashMap<>();

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        checkNode(ast.root);

    }

    private void checkNode(ASTNode node) {
        checkVariablesDefined(node);
        checkOperations(node);
        for (ASTNode nodes : node.getChildren()) {
            checkNode(nodes);
        }
    }

    private void checkVariablesDefined(ASTNode node) {
        if (node instanceof VariableAssignment) {
            assignments.put(((VariableAssignment) node).name.name, ((VariableAssignment) node).expression);
        }
        if (node instanceof VariableReference) {
            if (!assignments.containsKey(((VariableReference) node).name)) {
                node.setError("this variable is not assigned");
            }
        }

    }

    private void checkOperations(ASTNode node) {
        checkMulOperations(node);
        checkAddMinOperations(node);
    }

    /*
     */
    // checks MulOperations for scalar values
    private void checkMulOperations(ASTNode node) {
        if (node instanceof MultiplyOperation) {
            if (node.getChildren().get(0) instanceof VariableReference || node.getChildren().get(1) instanceof VariableReference) {
                if (!(assignments.get(((VariableReference) node.getChildren().get(0)).name) instanceof ScalarLiteral) ||
                        !(assignments.get(((VariableReference) node.getChildren().get(1)).name) instanceof ScalarLiteral)) {
                    node.setError("Multiplying can only be done with Scalar values.");
                }
            }
            if (!(node.getChildren().get(0) instanceof ScalarLiteral)) {
                node.setError("multiplying can only be done with Scalar values");
            }
            if (node.getChildren().get(0) instanceof ScalarLiteral) {
                if (!(node.getChildren().get(1) instanceof ScalarLiteral)) {
                    node.setError("Values must be of the same type");
                }
            }
        }
    }

    /*
    TODO : overleggen met michel code kwaliteit.
     */
    // checkt of beide waarden in een + of - operatie gelijk zijn.
    private void checkAddMinOperations(ASTNode node) {
        // alleen uitvoeren als node een + of - operatie is
        if (node instanceof AddOperation || node instanceof SubtractOperation) {
            String addSubstractError = "Add and Subtract operations need the same values.";
            // als er een variable referentie links staat.
            if (node.getChildren().get(0) instanceof VariableReference) {
                if ((assignments.get(((VariableReference) node.getChildren().get(0)).name).getClass()) != node.getChildren().get(1).getClass()) {
                    node.setError(addSubstractError);
                }
            }
            // als variable referentie rechts staat.
            if (node.getChildren().get(1) instanceof VariableReference) {
                if ((assignments.get(((VariableReference) node.getChildren().get(1)).name).getClass()) != node.getChildren().get(0).getClass()) {
                    node.setError(addSubstractError);
                }
            }
            // beide kanten variable referentie's
            if (node.getChildren().get(0) instanceof VariableReference && node.getChildren().get(1) instanceof VariableReference) {
                if ((assignments.get(((VariableReference) node.getChildren().get(0)).name).getClass()) != (assignments.get(((VariableReference) node.getChildren().get(1)).name).getClass())) {
                    node.setError(addSubstractError);
                }
            }
            // geen variable referenties.
            if (!(node.getChildren().get(0) instanceof VariableReference) && !(node.getChildren().get(1) instanceof VariableReference)) {
                if (node.getChildren().get(0).getClass() != node.getChildren().get(1).getClass()) {
                    node.setError(addSubstractError);
                }
            }
        }
    }






    /*
    TODO : Declaratie check werkend maken
     */
//    private void checkDeclaration(ASTNode node) {
//        if (node instanceof Declaration) {
//            checkWidthHeight(node);
//            checkColor(node);
//        }
//    }
//
//    private void checkWidthHeight(ASTNode node) {
//        if (((Declaration)node).property.name.contains("width")){
//            if (!(((Declaration) node).expression instanceof PixelLiteral)) {
//                System.out.println("width checked");
//                node.setError("Height,Width or size declarations require pixels.");
//            }
//        }
//    }
//
//    private void checkColor(ASTNode node) {
//        if (node.getNodeLabel().contains("color")) {
//            System.out.println("color checked!");
//            if (!(((Declaration) node).expression instanceof ColorLiteral)) {
//                node.setError("Color declartions require Color values.");
//            }
//        }
//    }


}
