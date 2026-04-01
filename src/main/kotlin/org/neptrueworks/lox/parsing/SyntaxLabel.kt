package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.lexing.LexicalPattern

public sealed class SyntaxLabel private constructor() {
    public data object Anonymous : SyntaxLabel();
    public data class Labeled(val label: LexicalPattern.Identifier) : SyntaxLabel();
}