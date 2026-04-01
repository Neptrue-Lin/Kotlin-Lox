package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.lexing.LexicalPattern
import org.neptrueworks.lox.lexing.LexicalToken

public sealed class SyntaxExpression private constructor() {
    public abstract fun <R> accept(visitor: ExpressionVisitor<R>): R;

    public data class Binary(val left: SyntaxExpression, val operator: LexicalToken, val right: SyntaxExpression) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitBinary(this);
    }
    public data class Unary(val operator: LexicalToken, val expr: SyntaxExpression) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitUnary(this);
    }
    public data class Grouping(val expr: SyntaxExpression) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitGrouping(this);
    }
    public data class Literal(val pattern: LexicalPattern) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitLiteral(this);
    }
    public data class Identifier(val pattern: LexicalPattern.Identifier) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitIdentifier(this);
    }
    public data class VarDef(val name: LexicalPattern.Identifier, val value: SyntaxExpression) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitVarDef(this);
    }
    public data class VarAssign(val name: SyntaxExpression, val operator: LexicalPattern, val value: SyntaxExpression) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitVarAssign(this);
    }
    
    public data class Block(val stmts: List<SyntaxStatement>, val yield: SyntaxYield) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitBlock(this);
    }
    public data class SwitchBlock(val cases: List<CaseClause>) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitSwitchBlock(this);
    }

    public data class If(val condition: SyntaxExpression, val then: IfThenClause, val `else`: ElseClause) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitIf(this);
    }
    public data class Loop(val label: SyntaxLabel, val clause: LoopClause, val then: Block, val `else`: ElseClause) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitLoop(this);
    }
    public data class Switch(val subject: SyntaxExpression, val then: SwitchBlock) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitSwitch(this);
    }
    
    public data class Break(val yield: SyntaxYield, val to: SyntaxLabel) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitBreak(this);
    }
    public data class Continue(val to: SyntaxLabel) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitContinue(this);
    }
    public data class Return(val yield: SyntaxYield, val to: SyntaxLabel) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitReturn(this);
    }
    public data object FallThrough : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitFallThrough(this);
    }
    
    public data class FuncCall(val callee: SyntaxExpression, val valueArgs: ArgumentList) : SyntaxExpression() {
        public final override fun <R> accept(visitor: ExpressionVisitor<R>) = visitor.visitFuncCall(this);
    }
}