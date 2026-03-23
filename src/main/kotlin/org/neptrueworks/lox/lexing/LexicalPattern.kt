package org.neptrueworks.lox.lexing

public sealed class LexicalPattern private constructor() {
    public final inline class Id(val id: String);
    public final inline class Txt(val txt: String);
    public final inline class Num(val num: Double);
    
    public data class Identifier(val id: Id) : LexicalPattern()
    
    // Literal keyword
    public data object True  : LexicalPattern() { const val Lexeme = "true"; const val Literal = true }
    public data object False : LexicalPattern() { const val Lexeme = "false"; const val Literal = false }
    
    // Literal
    public data class Text(val literal: Txt) : LexicalPattern()
    public data class Numeric(val literal: Num) : LexicalPattern()
    
    // Delimiter
    public data object LeftParen   : LexicalPattern() { const val Lexeme = "(" }
    public data object RightParen  : LexicalPattern() { const val Lexeme = ")" }
    public data object LeftSquare  : LexicalPattern() { const val Lexeme = "[" }
    public data object RightSquare : LexicalPattern() { const val Lexeme = "]" }
    public data object LeftCurl    : LexicalPattern() { const val Lexeme = "{" }
    public data object RightCurl   : LexicalPattern() { const val Lexeme = "}" }
    
    // Punctuator
    public data object Comma      : LexicalPattern() { const val Lexeme = "," }
    public data object Dot        : LexicalPattern() { const val Lexeme = "." }
    public data object Colon      : LexicalPattern() { const val Lexeme = ":" }
    public data object Semicolon  : LexicalPattern() { const val Lexeme = ";" }
    public data object Underscore : LexicalPattern() { const val Lexeme = "_" }
    
    // Arithmetic Operator
    public data object Minus    : LexicalPattern() { const val Lexeme = "-" }
    public data object Plus     : LexicalPattern() { const val Lexeme = "+" }
    public data object Asterisk : LexicalPattern() { const val Lexeme = "*" }
    public data object Slash    : LexicalPattern() { const val Lexeme = "/" }
    public data object Percent  : LexicalPattern() { const val Lexeme = "%" }
    public data object PlusEqual     : LexicalPattern() { const val Lexeme = "+=" }
    public data object MinusEqual    : LexicalPattern() { const val Lexeme = "-=" }
    public data object AsteriskEqual : LexicalPattern() { const val Lexeme = "*=" }
    public data object SlashEqual    : LexicalPattern() { const val Lexeme = "/=" }
    public data object PercentEqual  : LexicalPattern() { const val Lexeme = "%=" }
    
    
    // Equality Operator
    public data object EqualEqual   : LexicalPattern() { const val Lexeme = "==" }
    public data object BangEqual    : LexicalPattern() { const val Lexeme = "!=" }
    
    // Comparison Operator
    public data object Equal        : LexicalPattern() { const val Lexeme = "=" }
    public data object Less         : LexicalPattern() { const val Lexeme = "<" }
    public data object Greater      : LexicalPattern() { const val Lexeme = ">"}
    public data object LessEqual    : LexicalPattern() { const val Lexeme = "<=" }
    public data object GreaterEqual : LexicalPattern() { const val Lexeme = ">=" }
    
    // Logical Keyword 
    public data object And : LexicalPattern() { const val Lexeme = "and" }
    public data object Or  : LexicalPattern() { const val Lexeme = "or" }
    public data object Not : LexicalPattern() { const val Lexeme = "not" }
    public data object Is  : LexicalPattern() { const val Lexeme = "is" }
    
    // If 
    public data object If   : LexicalPattern() { const val Lexeme = "if" }
    public data object Else : LexicalPattern() { const val Lexeme = "else" }
    
    // Switch
    public data object Switch : LexicalPattern() { const val Lexeme = "switch" }
    public data object Case   : LexicalPattern() { const val Lexeme = "case" }
    
    // Loop
    public data object Loop  : LexicalPattern() { const val Lexeme = "loop" }
    public data object For   : LexicalPattern() { const val Lexeme = "for" }
    public data object While : LexicalPattern() { const val Lexeme = "while" }
    public data object Until : LexicalPattern() { const val Lexeme = "until" }
   
    // Jump
    public data object Break       : LexicalPattern() { const val Lexeme = "break" }
    public data object Continue    : LexicalPattern() { const val Lexeme = "continue" }
    public data object FallThrough : LexicalPattern() { const val Lexeme = "fallthrough" }
    public data object Return      : LexicalPattern() { const val Lexeme = "return" }
    public data object To          : LexicalPattern() { const val Lexeme = "to" }
    
    // Declaration
    public data object Var   : LexicalPattern() { const val Lexeme = "var" }
    public data object Func  : LexicalPattern() { const val Lexeme = "func" }
    public data object Class : LexicalPattern() { const val Lexeme = "class" }
    public data object This  : LexicalPattern() { const val Lexeme = "this" }
    public data object Base  : LexicalPattern() { const val Lexeme = "base" }
    
    public data object Terminated : LexicalPattern()
}