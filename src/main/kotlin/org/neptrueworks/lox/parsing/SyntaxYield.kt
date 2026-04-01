package org.neptrueworks.lox.parsing

public sealed class SyntaxYield private constructor() {
    public data object None : SyntaxYield();
    public data class Yielded(val expr: SyntaxExpression) : SyntaxYield();
}