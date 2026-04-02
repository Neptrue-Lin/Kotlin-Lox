package org.neptrueworks.lox.parsing

public sealed class TypeAnnotation private constructor() {
    public data object None : TypeAnnotation();
    public data class Annotated(val name: SyntaxExpression) : TypeAnnotation();
}
