package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.lexing.LexicalPattern
import org.neptrueworks.lox.lexing.LexicalToken
import org.neptrueworks.lox.parsing.SyntaxExpression as Expr
import org.neptrueworks.lox.parsing.SyntaxStatement as Stmt

public final class SyntaxParser(
    private val tokens: List<LexicalToken>,
) {
    public val errors: List<Exception> 
        field = mutableListOf();
    private var current = 0;

    private final inline fun getCurrentToken() = this.tokens[this.current];

    private inline fun nextToken() {
        if (this.getCurrentToken().isNotTerminated()) {
            this.current++;
        }
    }

    private fun synchronize() {
        while (this.getCurrentToken().isNotSynchronizing()) {
            this.nextToken();
        }
    }


    private fun <T> parenthesize(parseBody: () -> T): T {
        if (this.getCurrentToken().isNotLeftParen()) {
            throw Exception("Missing left parenthesis")
        }
        this.nextToken();
        if (this.getCurrentToken().isTerminated()) {
            throw Exception("Missing right parenthesis")
        }
        val value = parseBody();
        if (this.getCurrentToken().isNotRightParen()) {
            throw Exception("Missing right parenthesis")
        }
        this.nextToken();
        return value;
    }

    private fun <T> squareBracket(parseBody: () -> T): T {
        if (this.getCurrentToken().isNotLeftSquare()) {
            throw Exception("Missing left square bracket")
        }
        this.nextToken();
        if (this.getCurrentToken().isTerminated()) {
            throw Exception("Missing right square bracket")
        }
        val value = parseBody();
        if (this.getCurrentToken().isNotRightSquare()) {
            throw Exception("Missing right square bracket")
        }
        this.nextToken();
        return value;
    }

    private fun <T> curlBrace(parseBody: () -> T): T {
        if (this.getCurrentToken().isNotLeftCurl()) {
            throw Exception("Missing left curl brace")
        }
        this.nextToken();
        if (this.getCurrentToken().isTerminated()) {
            throw Exception("Missing right curl brace")
        }
        val value = parseBody();
        if (this.getCurrentToken().isNotRightCurl()) {
            throw Exception("Missing right curl brace")
        }
        this.nextToken();
        return value;
    }


    internal fun parse(): MutableList<Stmt> {
        return this.parseProgram();
    }

    private fun parseProgram(): MutableList<Stmt> {
        val stmts = mutableListOf<Stmt>();
        while (this.getCurrentToken().isNotTerminated()) {
            try {
                stmts += this.parseStatement();
            } catch (e: Exception) {
                this.errors += e;
                this.synchronize();
            }
        }
        return stmts;
    }

    private fun parseStatement(): Stmt = when (this.getCurrentToken().pattern) {
        else -> this.parseExprStmt()
    }


    private inline fun parseConditionExpr(): Expr = this.parenthesize {
        if (this.getCurrentToken().isRightParen()) {
            throw Exception("Missing condition")
        }
        this.parseSimpleExpr();
    }

    private fun parseIfExpr(): Expr.If {
        if (this.getCurrentToken().isNotIf()) {
            throw Exception()
        }
        this.nextToken();

        val condition = this.parseConditionExpr();
        val then = this.parseIfThenClause();
        val `else` = this.parseElseClause();
        return Expr.If(condition, then, `else`);
    }
    
    private fun parseIfThenClause() : IfThenClause {
        return if (this.getCurrentToken().isLeftCurl()) {
            IfThenClause.AnonymousBlock(this.parseBlock())
        } else if (this.getCurrentToken().isSwitch()) {
            IfThenClause.Switch(this.parseSwitchExpr())
        } else {
            throw Exception("Unknown then clause")
        }
    }

    private fun parseElseClause(): ElseClause {
        if (this.getCurrentToken().isNotElse()) {
            return ElseClause.None;
        }
        this.nextToken();
        val then = when (this.getCurrentToken().pattern) {
            LexicalPattern.If -> this.parseIfExpr()
            LexicalPattern.Switch -> this.parseSwitchExpr()
            LexicalPattern.Loop -> this.parseLoopExpr()
            LexicalPattern.LeftCurl -> this.parseBlock()
            LexicalPattern.Break -> this.parseBreakExpr()
            LexicalPattern.Continue -> this.parseContinueExpr()
            LexicalPattern.Return -> this.parseReturnExpr()
            else -> throw Exception("Unknown else clause")
        }
        return ElseClause.Else(then);
    }


    private inline fun parseSubject(): Expr = this.parenthesize {
        if (this.getCurrentToken().isRightParen()) {
            throw Exception("Missing subject")
        }
        if (this.getCurrentToken().isNotVar()) {
            return@parenthesize this.parseSimpleExpr();
        }
        this.nextToken();
        if (this.getCurrentToken().isNotIdentifier()) {
            throw Exception("Missing variable name")
        }
        val name = this.getCurrentToken().pattern as LexicalPattern.Identifier;
        this.nextToken();

        val type = if (this.getCurrentToken().isNotColon()) {
            TypeAnnotation.None
        } else {
            this.nextToken();
            if (this.getCurrentToken().isNotIdentifier()) {
                throw Exception("Missing type annotation")
            }
            // TODO Type Member
            val typeName = Expr.Identifier(this.getCurrentToken().pattern as LexicalPattern.Identifier);
            this.nextToken();
            TypeAnnotation.Annotated(typeName);
        }

        if (this.getCurrentToken().isNotEqual()) {
            throw Exception("Missing assignment operator")
        }
        this.nextToken();
        val value = this.parseSimpleExpr();

        Expr.VarDef(name, type, value);
    }

    private fun parseGuardClause(): GuardClause {
        if (this.getCurrentToken().isNotIf()) {
            return GuardClause.None;
        }
        this.nextToken();
        val condition = this.parenthesize { this.parseSimpleExpr(); }
        return GuardClause.Guarded(condition);
    }

    private fun parseDestructuringPattern(): DestructuringPattern {
        if (this.getCurrentToken().isElse()) {
            this.nextToken();
            return DestructuringPattern.Else;
        }

        if (this.getCurrentToken().isNotCase()) {
            throw Exception("Unknown case clause")
        }
        this.nextToken();
        val expr = this.parseSimpleExpr();
        return DestructuringPattern.Constant(expr);
    }

    private fun parseSwitchExpr(): Expr.Switch {
        if (this.getCurrentToken().isNotSwitch()) {
            throw Exception("Missing switch")
        }
        this.nextToken();

        val subject = this.parseSubject();
        val cases = mutableListOf<CaseClause>();
        this.curlBrace {  
            while (this.getCurrentToken().isNotTerminated()) {
                if (this.getCurrentToken().isRightCurl()) {
                    break;
                }
                cases += this.parseCaseClause();
            }
        }
        return Expr.Switch(subject, cases);
    }

    private fun parseCaseClause(): CaseClause {
        val pattern = this.parseDestructuringPattern();
        val guard = this.parseGuardClause();
        val then = this.parseBlock();
        return CaseClause(pattern, guard, then);
    }


    private fun parseLoopExpr(): Expr {
        if (this.getCurrentToken().isNotLoop()) {
            throw Exception("Missing loop")
        }
        this.nextToken();

        val label = if (this.getCurrentToken().isIdentifier()) {
            val id = this.getCurrentToken().pattern as LexicalPattern.Identifier;
            this.nextToken();
            SyntaxLabel.Labeled(id)
        } else {
            SyntaxLabel.Anonymous
        }

        val clause = when (this.getCurrentToken().pattern) {
            LexicalPattern.For -> throw Exception("Unimplemented")
            LexicalPattern.While -> this.parseWhileClause()
            LexicalPattern.LeftCurl -> LoopClause.None;
            else -> throw Exception("Unknown loop clause")
        }

        val then = this.parseBlock();
        val `else` = this.parseElseClause();
        return Expr.Loop(label, clause, then, `else`);
    }

    private inline fun parseWhileClause(): LoopClause.While {
        if (this.getCurrentToken().isNotWhile()) {
            throw Exception("Missing while")
        }
        this.nextToken();

        val condition = this.parseConditionExpr();
        return LoopClause.While(condition)
    }

    private fun parseBlock(): Expr.Block = this.curlBrace {
        val stmts = mutableListOf<Stmt.Expr>();
        var isYielded = false;
        while (this.getCurrentToken().isNotTerminated()) {
            if (this.getCurrentToken().isRightCurl()) {
                break;
            }

            val expr = when (this.getCurrentToken().pattern) {
                LexicalPattern.Break -> this.parseBreakExpr();
                LexicalPattern.Continue -> this.parseContinueExpr();
                LexicalPattern.Return -> this.parseReturnExpr();
                else -> this.parseExpression()
            }

            stmts += Stmt.Expr(expr);
            isYielded = this.getCurrentToken().isNotSemicolon();
            if (this.getCurrentToken().isSemicolon()) {
                this.nextToken();
            }
        }
        val yield = if (isYielded) {
            val stmt = stmts.removeLast();
            SyntaxYield.Yielded(stmt.expr)
        } else {
            SyntaxYield.None
        }
        Expr.Block(stmts, yield);
    }

    private fun parseBreakExpr(): Expr.Break {
        if (this.getCurrentToken().isNotBreak()) {
            throw Exception("Missing break")
        }
        this.nextToken();

        val yield = if (this.getCurrentToken().isSemicolon()) {
            SyntaxYield.None
        } else if (this.getCurrentToken().isNotTo()) {
            SyntaxYield.Yielded(this.parseSimpleExpr());
        } else {
            SyntaxYield.None
        }

        val label = this.parseJumpToClause();
        return Expr.Break(yield, label);
    }


    private fun parseContinueExpr(): Expr.Continue {
        if (this.getCurrentToken().isNotContinue()) {
            throw Exception("Missing continue")
        }
        this.nextToken();

        val label = this.parseJumpToClause();
        return Expr.Continue(label);
    }

    private fun parseReturnExpr(): Expr.Return {
        if (this.getCurrentToken().isNotReturn()) {
            throw Exception("Missing return")
        }
        this.nextToken();

        val yield = if (this.getCurrentToken().isSemicolon()) {
            SyntaxYield.None
        } else if (this.getCurrentToken().isNotTo()) {
            SyntaxYield.Yielded(this.parseSimpleExpr());
        } else {
            SyntaxYield.None
        }

        val label = this.parseJumpToClause();
        return Expr.Return(yield, label);
    }

    private fun parseJumpToClause(): SyntaxLabel {
        return if (this.getCurrentToken().isNotTo()) {
            SyntaxLabel.Anonymous
        } else {
            this.nextToken();

            if (this.getCurrentToken().isNotIdentifier()) {
                throw Exception("Missing label")
            }
            val id = this.getCurrentToken().pattern as LexicalPattern.Identifier;
            this.nextToken();

            SyntaxLabel.Labeled(id)
        }
    }

    private fun parseExprStmt(): Stmt {
        val expr = this.parseExpression();
        if (this.getCurrentToken().isSemicolon()) {
            this.nextToken();
        }
        return Stmt.Expr(expr);
    }

    private fun parseExpression(): Expr = when (this.getCurrentToken().pattern) {
        is LexicalPattern.If -> this.parseIfExpr();
        is LexicalPattern.Switch -> this.parseSwitchExpr();
        is LexicalPattern.Loop -> this.parseLoopExpr()
        is LexicalPattern.LeftCurl -> this.parseBlock();
        is LexicalPattern.Var -> this.parseVarDefExpr();
        else -> this.parseSimpleExpr();
    }



    private fun parseName(): Expr {
        if (this.getCurrentToken().isNotIdentifier()) {
            throw Exception("Missing identifier")
        }
        val id = this.getCurrentToken().pattern as LexicalPattern.Identifier;
        this.nextToken();
        return Expr.Identifier(id);
    }

    private fun parseVarDefExpr(): Expr {
        if (this.getCurrentToken().isNotVar()) {
            throw Exception("Missing var")
        }
        this.nextToken();

        if (this.getCurrentToken().isNotIdentifier()) {
            throw Exception("Missing variable name")
        }
        val name = this.getCurrentToken().pattern as LexicalPattern.Identifier;
        this.nextToken();
        
        val type = if (this.getCurrentToken().isNotColon()) {
            TypeAnnotation.None
        } else {
            this.nextToken();
            if (this.getCurrentToken().isNotIdentifier()) {
                throw Exception("Missing type annotation")
            }
            val typeName = Expr.Identifier(this.getCurrentToken().pattern as LexicalPattern.Identifier);
            this.nextToken();
            TypeAnnotation.Annotated(typeName);
        }

        if (this.getCurrentToken().isNotEqual()) {
            throw Exception("Missing assignment operator")
        }
        this.nextToken();
        val value = this.parseExpression();

        return Expr.VarDef(name, type, value);
    }


    private fun parseSimpleExpr(): Expr {
        return this.parseVarAssignExpr();
    }


    private fun parseVarAssignExpr(): Expr {
        val expr = this.parseDisjunctionExpr();

        if (this.getCurrentToken().isAssign()) {
            val operator = this.getCurrentToken();
            this.nextToken();

            val value = this.parseExpression();
            return Expr.VarAssign(expr, operator.pattern, value);
        }
        
        return expr;
    }

    private fun parseDisjunctionExpr(): Expr {
        var left = this.parseConjunctionExpr();
        while (this.getCurrentToken().isOr()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val right = this.parseConjunctionExpr();
            left = Expr.Binary(left, operator, right);
        }
        return left;
    }

    private fun parseConjunctionExpr(): Expr {
        var left = this.parseEqualityExpr();
        while (this.getCurrentToken().isAnd()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val right = this.parseEqualityExpr();
            left = Expr.Binary(left, operator, right);
        }
        return left;
    }

    private fun parseEqualityExpr(): Expr {
        var left = this.parseComparisonExpr();
        while (this.getCurrentToken().isEquality()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val right = this.parseComparisonExpr();
            left = Expr.Binary(left, operator, right);
        }
        return left;
    }

    private fun parseComparisonExpr(): Expr {
        var left = this.parseAdditiveExpr();
        while (this.getCurrentToken().isComparison()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val right = this.parseAdditiveExpr();
            left = Expr.Binary(left, operator, right);
        }
        return left;
    }

    private fun parseAdditiveExpr(): Expr {
        var left = this.parseMultiplicativeExpr();
        while (this.getCurrentToken().isAdditive()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val right = this.parseMultiplicativeExpr();
            left = Expr.Binary(left, operator, right);
        }
        return left;
    }

    private fun parseMultiplicativeExpr(): Expr {
        var left = this.parseUnaryExpr();
        while (this.getCurrentToken().isMultiplicative()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val right = this.parseUnaryExpr();
            left = Expr.Binary(left, operator, right);
        }
        return left;
    }

    private fun parseUnaryExpr(): Expr {
        if (this.getCurrentToken().isUnary()) {
            val operator = this.getCurrentToken();
            this.nextToken();
            val expr = this.parseUnaryExpr();
            return Expr.Unary(operator, expr);
        }
        return this.parseFuncCallExpr();
    }

    private fun parseFuncCallExpr() : Expr {
        var expr = this.parsePrimaryExpr();
        while (this.getCurrentToken().isLeftParen()) {
            val valArgs = this.parseValueArgs();
            expr = Expr.FuncCall(expr, valArgs);
        }
        return expr;
    }

    private fun parseValueArgs() : ArgumentList = this.parenthesize {
        // TODO Named arguments and Default arguments, No mixing of named and positional arguments
        // TODO Error UnknownParameterName, DuplicateParameterName, MissingParameterValue
        val args = mutableListOf<Expr>();
        while (this.getCurrentToken().isNotTerminated()) {
            if (this.getCurrentToken().isRightParen()) {
                break;
            }
            if (args.size >= 255) {
                throw Exception("Maximum arguments exceeded");
            }
            args += this.parseSimpleExpr();
            if (this.getCurrentToken().isComma()) {
                this.nextToken();
            } else if (this.getCurrentToken().isNotRightParen()) {
                throw Exception("Missing comma or right parenthesis");
            }
        }
        ArgumentList(args)
    }

    private fun parsePrimaryExpr(): Expr {
        if (this.getCurrentToken().isLeftParen()) {
            return this.parseGroupingExpr();
        }
        val token = this.getCurrentToken();
        return when {
            token.isLiteral() -> {
                this.nextToken(); 
                Expr.Literal(token.pattern)
            }
            // TODO Name indexer memberAccess functionInvocation
            token.isIdentifier() -> this.parseName();
            token.isRightParen() -> throw Exception("Redundant parenthesis")
            else -> throw Exception("Unknown expression")
        }
    }

    private inline fun parseGroupingExpr(): Expr.Grouping = this.parenthesize {
        if (this.getCurrentToken().isRightParen()) {
            throw Exception("Missing expression");
        }
        Expr.Grouping(this.parseSimpleExpr());
    }
}
