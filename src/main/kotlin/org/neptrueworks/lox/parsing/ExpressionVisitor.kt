package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.parsing.SyntaxExpression.*

public interface ExpressionVisitor<R> {
    public fun visitBinary(expr: Binary) : R;
    public fun visitUnary(expr: Unary): R;
    public fun visitGrouping(expr: Grouping): R;
    public fun visitLiteral(expr: Literal): R;
    public fun visitIdentifier(expr: Identifier): R;
    public fun visitVarDef(expr: VarDef): R;
    public fun visitVarAssign(expr: VarAssign): R;
    public fun visitBlock(expr: Block): R;
    public fun visitSwitchBlock(expr: SwitchBlock): R;
    public fun visitIf(expr: If): R;
    public fun visitSwitch(expr: Switch): R;
    public fun visitLoop(expr: Loop): R;
    public fun visitBreak(expr: Break): R;
    public fun visitContinue(expr: Continue): R;
    public fun visitReturn(expr: Return): R;
    public fun visitFuncCall(expr: FuncCall): R;
    public fun visitMemberAccess(expr: MemberAccess): R;
}
