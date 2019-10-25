package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

public class Generator {

    public String generate(AST ast) {
        return generateStylesheet(ast.root);
    }

    // main method calling the other generate's and returning this as a String
    private String generateStylesheet(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Stylerule) {
                sb.append(generateStylerule(child));
            }
        }
        return sb.toString();
    }

    // generates the stylerules
    private StringBuilder generateStylerule(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Selector) {
                sb.append(generateSelector(child));
                sb.append(" ");
                sb.append("{");
                sb.append(System.lineSeparator());
            }
            if (child instanceof Declaration) {
                sb.append(generateDeclaration(child));
            }
        }
        sb.append("}");
        sb.append(System.lineSeparator());
        return sb;
    }

    /*
    Generates the selector
     */
    private StringBuilder generateSelector(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        if (node instanceof TagSelector) {
            sb.append(((TagSelector) node).tag);
        }
        if (node instanceof ClassSelector) {
            sb.append(((ClassSelector) node).cls);
        }
        if (node instanceof IdSelector) {
            sb.append(((IdSelector) node).id);
        }
        return sb;
    }

    /*
    Generates a declaration when this is found.
     */
    private StringBuilder generateDeclaration(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        if (node instanceof Declaration) {
            sb.append("\t");
            sb.append(((Declaration) node).property.name);
            sb.append(": ");
            sb.append(generateExpression(((Declaration) node).expression));
            sb.append(";");
            sb.append(System.lineSeparator());
        }
        return sb;
    }

    /*
    Generates the correct Expression when a expression is found.
     */
    private StringBuilder generateExpression(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        if (node instanceof BoolLiteral) {
            sb.append(((BoolLiteral) node).value);
        } else if (node instanceof ColorLiteral) {
            sb.append(((ColorLiteral) node).value);
        } else if (node instanceof PercentageLiteral) {
            sb.append(((PercentageLiteral) node).value);
            sb.append("%");
        } else if (node instanceof PixelLiteral) {
            sb.append(((PixelLiteral) node).value);
            sb.append("px");
        } else if (node instanceof ScalarLiteral) {
            sb.append(((ScalarLiteral) node).value);
        }
        return sb;
    }

}

