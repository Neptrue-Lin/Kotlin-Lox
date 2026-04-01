package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.lexing.LexicalPattern

public sealed class SyntaxStatement private constructor() {
    public abstract fun <R> accept(visitor: StatementVisitor<R>) : R;
    
    public data class Expr(val expr: SyntaxExpression) : SyntaxStatement() {
        public final override fun <R> accept(visitor: StatementVisitor<R>) = visitor.visitExpr(this);
    }
    public data class VarDecl(val name: LexicalPattern.Identifier) : SyntaxStatement() {
        public final override fun <R> accept(visitor: StatementVisitor<R>) = visitor.visitVarDecl(this);
    }
}
