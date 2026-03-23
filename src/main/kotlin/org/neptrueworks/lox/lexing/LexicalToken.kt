package org.neptrueworks.lox.lexing

import org.neptrueworks.lox.diagnosis.LineNumber

public data class LexicalToken(val pattern: LexicalPattern, val lineNumber: LineNumber);
