package nl.han.ica.icss.parser;

import java.util.Properties;
import java.util.Stack;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

import javax.swing.text.Style;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast;

    //Use this to keep track of the parent nodes when recursively traversing the ast
    private Stack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new Stack<>();
    }

    public AST getAST() {
        return ast;
    }


    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet stylesheet = new Stylesheet();
        ast.root = stylesheet;
        currentContainer.push(stylesheet);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        Stylerule sr = new Stylerule();
        currentContainer.peek().addChild(sr);
        currentContainer.push(sr);
    }

    @Override
    public void exitStylerule(ICSSParser.StyleruleContext ctx) {
        currentContainer.pop();
    }


    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        VariableAssignment va = new VariableAssignment();
        currentContainer.peek().addChild(va);
        currentContainer.push(va);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        VariableReference vr = new VariableReference(ctx.getText());
        currentContainer.peek().addChild(vr);
        currentContainer.push(vr);

    }

    @Override
    public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        TagSelector tag = new TagSelector(ctx.getText());
        currentContainer.peek().addChild(tag);
        currentContainer.push(tag);
    }

    @Override
    public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        IdSelector is = new IdSelector(ctx.getText());
        currentContainer.peek().addChild(is);
        currentContainer.push(is);
    }

    @Override
    public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ClassSelector cs = new ClassSelector(ctx.getText());
        currentContainer.peek().addChild(cs);
        currentContainer.push(cs);
    }

    @Override
    public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        Declaration dec = new Declaration(ctx.getText());
        currentContainer.peek().addChild(dec);
        currentContainer.push(dec);
    }

    @Override
    public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterPropertyname(ICSSParser.PropertynameContext ctx) {
        PropertyName n = new PropertyName(ctx.getText());
        currentContainer.peek().addChild(n);
        currentContainer.push(n);
    }

    @Override
    public void exitPropertyname(ICSSParser.PropertynameContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        ColorLiteral cl = new ColorLiteral(ctx.getText());
        currentContainer.peek().addChild(cl);
        currentContainer.push(cl);
    }

    @Override
    public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        BoolLiteral bl = new BoolLiteral(ctx.getText());
        currentContainer.peek().addChild(bl);
        currentContainer.push(bl);
    }

    @Override
    public void exitBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        PercentageLiteral pl = new PercentageLiteral(ctx.getText());
        currentContainer.peek().addChild(pl);
        currentContainer.push(pl);
    }

    @Override
    public void exitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        PixelLiteral pix = new PixelLiteral(ctx.getText());
        currentContainer.peek().addChild(pix);
        currentContainer.push(pix);
    }

    @Override
    public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        ScalarLiteral sl = new ScalarLiteral(ctx.getText());
        currentContainer.peek().addChild(sl);
        currentContainer.push(sl);
    }

    @Override
    public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterOperation(ICSSParser.OperationContext ctx) {
        Operation operation = null;
        if(ctx.addoperation() != null){
            operation = new AddOperation();
        }else if(ctx.multiplyoperation() != null){
            operation = new MultiplyOperation();
        }else if(ctx.subtractoperation() != null){
            operation = new SubtractOperation();
        }
        currentContainer.peek().addChild(operation);
        currentContainer.push(operation);
    }

    @Override
    public void exitOperation(ICSSParser.OperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterIfclause(ICSSParser.IfclauseContext ctx) {
        IfClause ic = new IfClause();
        currentContainer.peek().addChild(ic);
        currentContainer.push(ic);
    }

    @Override
    public void exitIfclause(ICSSParser.IfclauseContext ctx) {
        currentContainer.pop();
    }
}