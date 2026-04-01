package org.neptrueworks.lox.parsing

public sealed class ArgumentList private constructor() {
    public data class Positional(val args: List<SyntaxExpression>) : ArgumentList();
    public data class Named(val args: Map<SyntaxExpression.Identifier, SyntaxExpression>) : ArgumentList();
}