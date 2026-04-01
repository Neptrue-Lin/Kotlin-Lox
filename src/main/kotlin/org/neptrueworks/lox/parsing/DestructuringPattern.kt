package org.neptrueworks.lox.parsing

public sealed class DestructuringPattern private constructor() {
    public data class Constant(val expr: SyntaxExpression) : DestructuringPattern();
    public data object Else : DestructuringPattern();
}
