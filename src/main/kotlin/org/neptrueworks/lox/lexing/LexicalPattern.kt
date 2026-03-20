package org.neptrueworks.lox.lexing

public sealed class LexicalPattern<T> {
    public abstract val literal: T
    
    public typealias Id = String;
    public typealias Keyword = Id;
    public typealias Txt = String;
    public typealias Num = Double;
    public typealias Delimiter = String;
    public typealias Punctuator = String;
    public typealias Operator = String;
    

    public data class Identifier(override val literal: Id) : LexicalPattern<Id>()
    
    // Literal keyword
    public data object True  : LexicalPattern<Keyword>() { override val literal = "true" }
    public data object False : LexicalPattern<Keyword>() { override val literal = "false" }
    public data object Nil   : LexicalPattern<Keyword>() { override val literal = "nil" }
    
    // Literal
    public data class Text(override val literal: Txt) : LexicalPattern<Txt>()
    public data class Numeric(override val literal: Num) : LexicalPattern<Num>()
    
    // Delimiter
    public data object LeftParen    : LexicalPattern<Delimiter>() { override val literal = "(" }
    public data object RightParen   : LexicalPattern<Delimiter>() { override val literal = ")" }
    public data object LeftBracket  : LexicalPattern<Delimiter>() { override val literal = "[" }
    public data object RightBracket : LexicalPattern<Delimiter>() { override val literal = "]" }
    public data object LeftBrace    : LexicalPattern<Delimiter>() { override val literal = "{" }
    public data object RightBrace   : LexicalPattern<Delimiter>() { override val literal = "}" }
    
    // Punctuator
    public data object Comma      : LexicalPattern<Punctuator>() { override val literal = "," }
    public data object Dot        : LexicalPattern<Punctuator>() { override val literal = "." }
    public data object Colon      : LexicalPattern<Punctuator>() { override val literal = ":" }
    public data object Semicolon  : LexicalPattern<Punctuator>() { override val literal = ";" }
    public data object Underscore : LexicalPattern<Punctuator>() { override val literal = "_" }
    
    // Arithmetic Operator
    public data object Minus    : LexicalPattern<Operator>() { override val literal = "-" }
    public data object Plus     : LexicalPattern<Operator>() { override val literal = "+" }
    public data object Asterisk : LexicalPattern<Operator>() { override val literal = "*" }
    public data object Slash    : LexicalPattern<Operator>() { override val literal = "/" }
    public data object Percent  : LexicalPattern<Operator>() { override val literal = "%" }
    
    // Comparison Operator
    public data object Equal        : LexicalPattern<Operator>() { override val literal = "=" }
    public data object Less         : LexicalPattern<Operator>() { override val literal = "<" }
    public data object Greater      : LexicalPattern<Operator>() { override val literal = ">" }
    public data object EqualEqual   : LexicalPattern<Operator>() { override val literal = "==" }
    public data object BangEqual    : LexicalPattern<Operator>() { override val literal = "!=" }
    public data object LessEqual    : LexicalPattern<Operator>() { override val literal = "<=" }
    public data object GreaterEqual : LexicalPattern<Operator>() { override val literal = ">=" }
    
    // Logical Keyword 
    public data object And : LexicalPattern<Keyword>() { override val literal = "and" }
    public data object Or  : LexicalPattern<Keyword>() { override val literal = "or" }
    public data object Not : LexicalPattern<Keyword>() { override val literal = "not" }
    public data object Is  : LexicalPattern<Keyword>() { override val literal = "is" }
    
    // If 
    public data object If   : LexicalPattern<Keyword>() { override val literal = "if" }
    public data object Else : LexicalPattern<Keyword>() { override val literal = "else" }
    
    // Switch
    public data object Switch : LexicalPattern<Keyword>() { override val literal = "switch" }
    public data object Case   : LexicalPattern<Keyword>() { override val literal = "case" }
    
    // Loop
    public data object Loop  : LexicalPattern<Keyword>() { override val literal = "loop" }
    public data object For   : LexicalPattern<Keyword>() { override val literal = "for" }
    public data object While : LexicalPattern<Keyword>() { override val literal = "while" }
    public data object Until : LexicalPattern<Keyword>() { override val literal = "until" }
   
    // Jump
    public data object Break       : LexicalPattern<Keyword>() { override val literal = "break" }
    public data object Continue    : LexicalPattern<Keyword>() { override val literal = "continue" }
    public data object FallThrough : LexicalPattern<Keyword>() { override val literal = "fallthrough" }
    public data object Return      : LexicalPattern<Keyword>() { override val literal = "return" }
    
    // Declaration
    public data object Var   : LexicalPattern<Keyword>() { override val literal = "var" }
    public data object Func  : LexicalPattern<Keyword>() { override val literal = "func" }
    public data object Class : LexicalPattern<Keyword>() { override val literal = "class" }
    public data object This  : LexicalPattern<Keyword>() { override val literal = "this" }
    public data object Base  : LexicalPattern<Keyword>() { override val literal = "base" }
}
