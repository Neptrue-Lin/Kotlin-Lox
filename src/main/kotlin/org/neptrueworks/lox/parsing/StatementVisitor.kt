package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.parsing.SyntaxStatement.*

public interface StatementVisitor<R> {
    public fun visitExpr(stmt: Expr) : R;
    public fun visitVarDecl(stmt: VarDecl) : R;
}
