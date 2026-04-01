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

public final class AstPrinter : ExpressionVisitor<String>, StatementVisitor<String> {
    public final fun print(stmt: SyntaxStatement) : String {
        return stmt.accept(this);
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
        return "( var ${expr.name.id.id} = ${expr.value.accept(this)} )"
    }

    public final override fun visitVarAssign(expr: VarAssign): String {
        return "( ${expr.name.accept(this)} ${expr.operator} ${expr.value.accept(this)})";
    }

    public final override fun visitBlock(expr: Block): String {
        val builder = StringBuilder();
        builder.append("{\n");
        for (stmt in expr.stmts) {
            builder.append(stmt.accept(this));
            builder.append("\n");
        }
        if (expr.yield is SyntaxYield.Yielded) {
            builder.append(expr.yield.expr.accept(this));
            builder.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    public final override fun visitSwitchBlock(expr: SwitchBlock): String {
        val builder = StringBuilder();
        builder.append("{");
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
        builder.append("\n}\n");
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
        builder.append(" ");
        builder.append(expr.then.accept(this));
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
            is LoopClause.For -> TODO()
            is LoopClause.While -> {
                builder.append("while (");
                builder.append(expr.clause.condition);
                builder.append(") ");
            }
            is LoopClause.None -> {}
        }
        builder.append(expr.then.accept(this));
        if (expr.`else` is ElseClause.Else) {
            builder.append(" else ");
            builder.append(expr.`else`.then.accept(this));
        }
        builder.append("\n");
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
            builder.append(expr.to.label);
            builder.append(" ");
        }
        return builder.toString();
    }

    public final override fun visitContinue(expr: Continue): String {
        val builder = StringBuilder();
        builder.append("continue ");
        if (expr.to is SyntaxLabel.Labeled) {
            builder.append("to ");
            builder.append(expr.to.label);
            builder.append(" ");
        }
        return builder.toString();
    }

    public final override fun visitFallThrough(expr: FallThrough): String {
        return "fallthrough";
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
            builder.append(expr.to.label);
            builder.append(" ");
        }
        return builder.toString();
    }

    public final override fun visitExpr(stmt: SyntaxStatement.Expr): String {
        return stmt.expr.accept(this)
    }

    public final override fun visitVarDecl(stmt: SyntaxStatement.VarDecl): String {
        return "( var ${stmt.name.id} );";
    }

    override fun visitFuncCall(expr: FuncCall): String {
        val builder = StringBuilder();
        builder.append("( call ");
        builder.append(expr.callee.accept(this));
        builder.append(" ");
        when (val args = expr.valueArgs) {
            is ArgumentList.Positional -> for (arg in args.args) {
                builder.append(arg.accept(this));
                builder.append(", ");
            }
            is ArgumentList.Named -> for (arg in args.args) {
                builder.append(arg.key.accept(this));
                builder.append(" = ")
                builder.append(arg.value.accept(this));
                builder.append(", ");
            }
        }
        builder.append(")")
        return builder.toString();
    }
}