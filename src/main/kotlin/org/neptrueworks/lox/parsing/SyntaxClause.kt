package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.lexing.LexicalPattern

public sealed class GuardClause private constructor() {
    public data object None : GuardClause();
    public data class Guarded(val condition: SyntaxExpression) : GuardClause();
}

public sealed class IfThenClause private constructor() {
    public data class AnonymousBlock(val then: SyntaxExpression.Block) : IfThenClause();
    public data class Switch(val switch: SyntaxExpression.Switch) : IfThenClause();
}

public sealed class ElseClause private constructor() {
    public data object None : ElseClause();
    public data class Else(val then: SyntaxExpression) : ElseClause();
}

public data class CaseClause(val pattern: DestructuringPattern, val guard: GuardClause, val then: SyntaxExpression.Block);

public sealed class LoopClause private constructor() {
    public data object None : LoopClause();
    public data class While(val condition: SyntaxExpression) : LoopClause();
    public data class For(val subject: LexicalPattern.Identifier, val range: SyntaxExpression) : LoopClause();
}
