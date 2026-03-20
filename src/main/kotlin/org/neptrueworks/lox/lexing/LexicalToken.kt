package org.neptrueworks.lox.lexing

import org.neptrueworks.lox.diagnosis.LineNumber

public data class LexicalToken<T>(
    val pattern: LexicalPattern<T>,
    val lineNumber: LineNumber
) {
    public final override fun toString() = "${this.pattern.javaClass.simpleName}( ${this.pattern.literal.toString()} )";
}