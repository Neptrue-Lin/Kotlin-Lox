package org.neptrueworks.lox.parsing

import org.neptrueworks.lox.lexing.LexicalPattern
import org.neptrueworks.lox.lexing.LexicalPattern.*
import org.neptrueworks.lox.lexing.LexicalToken


internal inline fun LexicalToken.isLiteral() = when (this.pattern) {
    True       -> true;
    False      -> true;
    is Numeric -> true;
    is Text    -> true;
    else -> false;
}

internal inline fun LexicalToken.isSynchronizing() = when (this.pattern) {
    Semicolon  -> true;
    Switch     -> true;
    If         -> true;
    Loop       -> true;
    Var        -> true;
    Func       -> true;
    Class      -> true;
    Terminated -> true;
    else -> false;
}
internal inline fun LexicalToken.isNotSynchronizing() = !this.isSynchronizing();

internal inline fun LexicalToken.isIdentifier() = this.pattern is Identifier;
internal inline fun LexicalToken.isNotIdentifier() = !this.isIdentifier();

// Punctuator
internal inline fun LexicalToken.isSemicolon() = this.pattern is Semicolon;
internal inline fun LexicalToken.isColon() = this.pattern is Colon;
internal inline fun LexicalToken.isDot() = this.pattern is Dot;
internal inline fun LexicalToken.isComma() = this.pattern is Comma;
internal inline fun LexicalToken.isNotSemicolon() = !this.isSemicolon();
internal inline fun LexicalToken.isNotColon() = !this.isColon();
internal inline fun LexicalToken.isNotDot() = !this.isDot();
internal inline fun LexicalToken.isNotComma() = !this.isComma();

// Delimiter
internal inline fun LexicalToken.isLeftParen()   = this.pattern is LeftParen;
internal inline fun LexicalToken.isRightParen()  = this.pattern is RightParen;
internal inline fun LexicalToken.isLeftSquare()  = this.pattern is LeftSquare;
internal inline fun LexicalToken.isRightSquare() = this.pattern is RightSquare;
internal inline fun LexicalToken.isLeftCurl()    = this.pattern is LeftCurl;
internal inline fun LexicalToken.isRightCurl()   = this.pattern is RightCurl;

internal inline fun LexicalToken.isNotLeftParen()   = !this.isLeftParen();
internal inline fun LexicalToken.isNotRightParen()  = !this.isRightParen();
internal inline fun LexicalToken.isNotLeftSquare()  = !this.isLeftSquare();
internal inline fun LexicalToken.isNotRightSquare() = !this.isRightSquare();
internal inline fun LexicalToken.isNotLeftCurl()    = !this.isLeftCurl();
internal inline fun LexicalToken.isNotRightCurl()   = !this.isRightCurl();

// Equality
internal inline fun LexicalToken.isEquality() = when (this.pattern) {
    EqualEqual -> true;
    BangEqual  -> true;
    else -> false;
}
internal inline fun LexicalToken.isNotEquality() = !this.isEquality();

internal inline fun LexicalToken.isEqualEqual() = this.pattern is EqualEqual;
internal inline fun LexicalToken.isBangEqual()  = this.pattern is BangEqual;
internal inline fun LexicalToken.isNotEqualEqual() = !this.isEqualEqual();
internal inline fun LexicalToken.isNotBangEqual()  = !this.isBangEqual();

// Comparison
internal inline fun LexicalToken.isComparison() = when (this.pattern) {
    Less         -> true;
    LessEqual    -> true;
    Greater      -> true;
    GreaterEqual -> true;
    else -> false;
}
internal inline fun LexicalToken.isNotComparison() = !this.isComparison();

internal inline fun LexicalToken.isLess()         = this.pattern is Less;
internal inline fun LexicalToken.isLessEqual()    = this.pattern is LessEqual;
internal inline fun LexicalToken.isGreater()      = this.pattern is Greater;
internal inline fun LexicalToken.isGreaterEqual() = this.pattern is GreaterEqual;
internal inline fun LexicalToken.isNotLess()         = !this.isLess();
internal inline fun LexicalToken.isNotLessEqual()    = !this.isLessEqual();
internal inline fun LexicalToken.isNotGreater()      = !this.isGreater();
internal inline fun LexicalToken.isNotGreaterEqual() = !this.isGreaterEqual();

// Assignment
internal inline fun LexicalToken.isAssign() = when (this.pattern) {
    Equal         -> true;
    PlusEqual     -> true;
    MinusEqual    -> true;
    AsteriskEqual -> true;
    SlashEqual    -> true;
    PercentEqual  -> true;
    else -> false;
}
internal inline fun LexicalToken.isNotAssign() = !this.isAssign();

internal inline fun LexicalToken.isEqual()         = this.pattern is Equal;
internal inline fun LexicalToken.isPlusEqual()     = this.pattern is PlusEqual;
internal inline fun LexicalToken.isMinusEqual()    = this.pattern is MinusEqual;
internal inline fun LexicalToken.isAsteriskEqual() = this.pattern is AsteriskEqual;
internal inline fun LexicalToken.isSlashEqual()    = this.pattern is SlashEqual;
internal inline fun LexicalToken.isPercentEqual()  = this.pattern is PercentEqual;
internal inline fun LexicalToken.isNotEqual()         = !this.isEqual();
internal inline fun LexicalToken.isNotPlusEqual()     = !this.isPlusEqual();
internal inline fun LexicalToken.isNotMinusEqual()    = !this.isMinusEqual();
internal inline fun LexicalToken.isNotAsteriskEqual() = !this.isAsteriskEqual();
internal inline fun LexicalToken.isNotSlashEqual()    = !this.isSlashEqual();
internal inline fun LexicalToken.isNotPercentEqual()  = !this.isPercentEqual();

// Arithmetic
internal inline fun LexicalToken.isMultiplicative() = when (this.pattern) {
    Asterisk -> true;
    Slash    -> true;
    Percent  -> true
    else -> false;
}
internal inline fun LexicalToken.isAdditive() = when (this.pattern) {
    Plus  -> true;
    Minus -> true;
    else -> false;
}
internal inline fun LexicalToken.isUnary() = when (this.pattern) {
    Plus  -> true;
    Minus -> true;
    Not   -> true;
    else -> false;
}
internal inline fun LexicalToken.isNotMultiplicative() = !this.isMultiplicative();
internal inline fun LexicalToken.isNotAdditive()       = !this.isAdditive();
internal inline fun LexicalToken.isNotUnary()          = !this.isUnary();

internal inline fun LexicalToken.isPlus()     = this.pattern is Plus;
internal inline fun LexicalToken.isMinus()    = this.pattern is Minus;
internal inline fun LexicalToken.isAsterisk() = this.pattern is Asterisk;
internal inline fun LexicalToken.isSlash()    = this.pattern is Slash;
internal inline fun LexicalToken.isPercent()  = this.pattern is Percent;
internal inline fun LexicalToken.isNotPlus()     = !this.isPlus();
internal inline fun LexicalToken.isNotMinus()    = !this.isMinus();
internal inline fun LexicalToken.isNotAsterisk() = !this.isAsterisk();
internal inline fun LexicalToken.isNotSlash()    = !this.isSlash();
internal inline fun LexicalToken.isNotPercent()  = !this.isPercent();

// Logical
internal inline fun LexicalToken.isOr()  = this.pattern == Or;
internal inline fun LexicalToken.isAnd() = this.pattern == And;
internal inline fun LexicalToken.isNot() = this.pattern == Not;
internal inline fun LexicalToken.isIs()  = this.pattern == Is;
internal inline fun LexicalToken.isNotOr()  = !this.isOr();
internal inline fun LexicalToken.isNotAnd() = !this.isAnd();
internal inline fun LexicalToken.isNotNot() = !this.isNot();
internal inline fun LexicalToken.isNotIs()  = !this.isIs();

// Selection 
internal inline fun LexicalToken.isIf()     = this.pattern == If;
internal inline fun LexicalToken.isElse()   = this.pattern == Else;
internal inline fun LexicalToken.isSwitch() = this.pattern == Switch;
internal inline fun LexicalToken.isCase()   = this.pattern == Case;
internal inline fun LexicalToken.isNotIf()     = !this.isIf();
internal inline fun LexicalToken.isNotElse()   = !this.isElse();
internal inline fun LexicalToken.isNotSwitch() = !this.isSwitch();
internal inline fun LexicalToken.isNotCase()   = !this.isCase();

// Loop
internal inline fun LexicalToken.isLoop()  = this.pattern == Loop;
internal inline fun LexicalToken.isFor()   = this.pattern == For;
internal inline fun LexicalToken.isWhile() = this.pattern == While;
internal inline fun LexicalToken.isNotLoop()  = !this.isLoop();
internal inline fun LexicalToken.isNotFor()   = !this.isFor();
internal inline fun LexicalToken.isNotWhile() = !this.isWhile();

// Jump
internal inline fun LexicalToken.isJump() = when (this.pattern) {
    Break       -> true;
    Continue    -> true;
    Return      -> true;
    else -> false;
}
internal inline fun LexicalToken.isNotJump() = !this.isJump();
internal inline fun LexicalToken.isBreak()       = this.pattern == Break;
internal inline fun LexicalToken.isContinue()    = this.pattern == Continue;
internal inline fun LexicalToken.isReturn()      = this.pattern == Return;
internal inline fun LexicalToken.isTo()          = this.pattern == To;
internal inline fun LexicalToken.isNotBreak()       = !this.isBreak();
internal inline fun LexicalToken.isNotContinue()    = !this.isContinue();
internal inline fun LexicalToken.isNotReturn()      = !this.isReturn();
internal inline fun LexicalToken.isNotTo()          = !this.isTo();

// Declaration
internal inline fun LexicalToken.isVar()   = this.pattern is Var;
internal inline fun LexicalToken.isFunc()  = this.pattern is Func;
internal inline fun LexicalToken.isClass() = this.pattern is LexicalPattern.Class;
internal inline fun LexicalToken.isNotVar()   = !this.isVar();
internal inline fun LexicalToken.isNotFunc()  = !this.isFunc();
internal inline fun LexicalToken.isNotClass() = !this.isClass();

internal inline fun LexicalToken.isTerminated() = this.pattern is Terminated;
internal inline fun LexicalToken.isNotTerminated() = !this.isTerminated();
