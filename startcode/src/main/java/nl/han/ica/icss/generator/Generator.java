package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;

public class Generator {

    public String generate(AST ast) {
        return generateStyleSheet(ast.root);
    }

    public String generateStyleSheet(ASTNode node){
        StringBuilder sb = new StringBuilder();
        for(ASTNode child : node.getChildren()){
            generateStylerule(child);
        }
        return "";
    }

    public String generateStylerule(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        for(ASTNode child : node.getChildren()){
            generateDeclaration(child);
        }
return "";
    }

    private String generateDeclaration(ASTNode node) {
        return "";
    }

    public String generateSelector() {
return "";
    }





}




/*
TODO : Generator implementeren
 */