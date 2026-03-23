package org.neptrueworks.lox.results

import org.neptrueworks.lox.diagnosis.LineNumber
import org.neptrueworks.lox.lexing.LexicalPattern

public sealed class LexicalAnalysisResult : Result() {
    public data class UnknownCharacter(val char: Char, val line: LineNumber) : LexicalAnalysisResult(), Error 
    public data class UnexpectedBang(val line: LineNumber) : LexicalAnalysisResult(), Error 
    public data class UnterminatedString(val line: LineNumber) : LexicalAnalysisResult(), Error
    public data class MultipleDecimalSeparator(val line: LineNumber) : LexicalAnalysisResult(), Error
    public data class Tokenized(val pattern: LexicalPattern) : LexicalAnalysisResult(), Success
}