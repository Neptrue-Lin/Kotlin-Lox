package org.neptrueworks.lox.diagnosis

import org.neptrueworks.lox.lexing.LexicalPattern
import org.neptrueworks.lox.parsing.ArgumentList
import org.neptrueworks.lox.parsing.DestructuringPattern
import org.neptrueworks.lox.parsing.ElseClause
import org.neptrueworks.lox.parsing.SyntaxExpression.*
import org.neptrueworks.lox.parsing.ExpressionVisitor
import org.neptrueworks.lox.parsing.GuardClause
import org.neptrueworks.lox.parsing.IfThenClause
import org.neptrueworks.lox.parsing.LoopClause
import org.neptrueworks.lox.parsing.StatementVisitor
import org.neptrueworks.lox.parsing.SyntaxLabel
import org.neptrueworks.lox.parsing.SyntaxStatement
import org.neptrueworks.lox.parsing.SyntaxYield
import org.neptrueworks.lox.parsing.TypeAnnotation

public final class AstPrinter : ExpressionVisitor<String>, StatementVisitor<String> {
    public final fun print(stmt: SyntaxStatement) : String {
        return stmt.accept(this);
    }

    public final override fun visitExpr(stmt: SyntaxStatement.Expr): String {
        return "${stmt.expr.accept(this)};\n";
    }

    public final override fun visitVarDecl(stmt: SyntaxStatement.VarDecl): String {
        return "( var ${stmt.name.id} );\n";
    }

    public final override fun visitBinary(expr: Binary): String {
        return "( ${expr.operator.pattern} ${expr.left.accept(this)} ${expr.right.accept(this)} )"
    }

    public final override fun visitUnary(expr: Unary): String {
        return "( ${expr.operator.pattern} ${expr.expr.accept(this)} )"
    }

    public final override fun visitGrouping(expr: Grouping): String {
        return "( group ${expr.expr.accept(this)} )"
    }

    public final override fun visitLiteral(expr: Literal): String = when(val pattern = expr.pattern) {
        is LexicalPattern.True       -> "${pattern.Literal}"
        is LexicalPattern.False      -> "${pattern.Literal}"
        is LexicalPattern.Numeric    -> "${pattern.literal.num}"
        is LexicalPattern.Text       -> "\"${pattern.literal.txt}\""
        else -> throw Exception();
    }

    public final override fun visitIdentifier(expr: Identifier): String {
        return expr.pattern.id.id;
    }

    public final override fun visitVarDef(expr: VarDef): String {
        val builder = StringBuilder();
        builder.append("( var ");
        builder.append(expr.name.id.id);
        if (expr.type is TypeAnnotation.Annotated) {
            builder.append(": ");
            builder.append(expr.type.name.accept(this));
        } 
        builder.append(" = ");
        builder.append(expr.value.accept(this));
        builder.append(" )");
        return builder.toString();
    }

    public final override fun visitVarAssign(expr: VarAssign): String {
        return "( ${expr.name.accept(this)} = ${expr.value.accept(this)})";
    }

    public final override fun visitBlock(expr: Block): String {
        val builder = StringBuilder();
        builder.append("{\n");
        for (stmt in expr.stmts) {
            builder.append(stmt.accept(this));
        }
        if (expr.yield is SyntaxYield.Yielded) {
            builder.append(expr.yield.expr.accept(this));
        }
        builder.append("\n}");
        return builder.toString();
    }

    public final override fun visitIf(expr: If): String {
        val builder = StringBuilder();
        builder.append("if (");
        builder.append(expr.condition.accept(this));
        builder.append(") ");
        when (val then = expr.then) {
            is IfThenClause.AnonymousBlock -> builder.append(then.then.accept(this));
            is IfThenClause.Switch -> builder.append(then.switch.accept(this));
        }
        if (expr.`else` is ElseClause.Else) {
            builder.append(" else ");
            builder.append(expr.`else`.then.accept(this));
        }
        return builder.toString();
    }

    public final override fun visitSwitch(expr: Switch): String {
        val builder = StringBuilder();
        builder.append("switch ");
        builder.append(expr.subject.accept(this));
        builder.append(" {");
        for (case in expr.cases) {
            builder.append("\n");
            when (val pattern = case.pattern) {
                is DestructuringPattern.Constant -> builder.append("case " + pattern.expr.accept(this))
                is DestructuringPattern.Else -> builder.append("else")
            }
            if (case.guard is GuardClause.Guarded) {
                builder.append(" if ");
                builder.append(case.guard.condition.accept(this));
            }
            builder.append(" => ");
            builder.append(case.then.accept(this));
        }
        builder.append("\n}");
        return builder.toString();
    }

    public final override fun visitLoop(expr: Loop): String {
        val builder = StringBuilder();
        builder.append("loop ");
        if (expr.label is SyntaxLabel.Labeled) {
            builder.append(expr.label.label);
            builder.append(" ");
        } 
        when (expr.clause) {
            is LoopClause.For -> {
                builder.append("for( var ")
                builder.append(expr.clause.subject.id.id);
                builder.append(" in ");
                builder.append(expr.clause.range.accept(this));
                builder.append(") ");
            }
            is LoopClause.While -> {
                builder.append("while (");
                builder.append(expr.clause.condition.accept(this));
                builder.append(") ");
            }
            is LoopClause.None -> {}
        }
        builder.append(expr.then.accept(this));
        if (expr.`else` is ElseClause.Else) {
            builder.append(" else ");
            builder.append(expr.`else`.then.accept(this));
        }
        return builder.toString();
    }

    public final override fun visitBreak(expr: Break): String {
        val builder = StringBuilder();
        builder.append("break ");
        if (expr.yield is SyntaxYield.Yielded) {
            builder.append(expr.yield.expr.accept(this));
            builder.append(" ");
        }
        if (expr.to is SyntaxLabel.Labeled) {
            builder.append("to ");
            builder.append(expr.to.label.id);
            builder.append(" ");
        }
        return builder.toString();
    }

    public final override fun visitContinue(expr: Continue): String {
        val builder = StringBuilder();
        builder.append("continue ");
        if (expr.to is SyntaxLabel.Labeled) {
            builder.append("to ");
            builder.append(expr.to.label.id);
            builder.append(" ");
        }
        return builder.toString();
    }

    public final override fun visitReturn(expr: Return): String {
        val builder = StringBuilder();
        builder.append("return ");
        if (expr.yield is SyntaxYield.Yielded) {
            builder.append(expr.yield.expr.accept(this));
            builder.append(" ");
        }
        if (expr.to is SyntaxLabel.Labeled) {
            builder.append("to ");
            builder.append(expr.to.label.id);
            builder.append(" ");
        }
        return builder.toString();
    }

    override fun visitFuncCall(expr: FuncCall): String {
        val builder = StringBuilder();
        builder.append("( call ");
        builder.append(expr.callee.accept(this));
        builder.append(" ");
        for (arg in expr.valueArgs.args) {
            builder.append(arg.accept(this));
            builder.append(", ");
        }
        builder.append(")")
        return builder.toString();
    }

    public final override fun visitMemberAccess(expr: MemberAccess): String {
        return "${expr.receiver.pattern.id}.${expr.path.accept(this)}";
    }
}