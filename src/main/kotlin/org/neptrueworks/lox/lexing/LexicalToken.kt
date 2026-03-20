package org.neptrueworks.lox.lexing

import org.neptrueworks.lox.diagnosis.LineNumber

public final class LexicalToken<T>(
    private val pattern: LexicalPattern<T>,
    private val lineNumber: LineNumber
) {
    public final override fun toString() = "${this.pattern.javaClass.simpleName}( ${this.pattern.literal.toString()} )";
}