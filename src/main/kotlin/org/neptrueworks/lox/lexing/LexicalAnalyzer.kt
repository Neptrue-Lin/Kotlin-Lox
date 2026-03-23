package org.neptrueworks.lox.lexing

import org.neptrueworks.lox.diagnosis.LineNumber
import org.neptrueworks.lox.results.LexicalAnalysisResult
import org.neptrueworks.lox.results.LexicalAnalysisResult.*
import org.neptrueworks.lox.lexing.LexicalPattern.*

public final class LexicalAnalyzer(
    private val scanner: LexicalScanner,
) {
    public val tokens: List<LexicalToken> 
        field = mutableListOf();
    public val errors: List<LexicalAnalysisResult>
        field = mutableListOf();

    public final override fun toString() = this.tokens.toString() + this.errors.toString();

    public fun analyze() {
        while (this.scanner.hasNext()) {
            val char = this.scanner.scanNext();
            if (char.isWhitespace()) {
                continue;
            }
            if (this.scanner.matchComment()) {
                continue;
            }
            val result = this.tokenize(char);
            this.analyzeToken(result, this.scanner.line);
        }
        this.tokens += LexicalToken(Terminated, this.scanner.line);
    }

    private fun analyzeToken(result: LexicalAnalysisResult, line: LineNumber) {
        if (result is Tokenized) {
            this.tokens += LexicalToken(result.pattern, line);
        } else {
            this.errors += result;
        }
    }

    private fun tokenize(char: Char): LexicalAnalysisResult = when (char) {
        '(' -> Tokenized(LeftParen)
        ')' -> Tokenized(RightParen)
        '[' -> Tokenized(LeftSquare)
        ']' -> Tokenized(RightSquare)
        '{' -> Tokenized(LeftCurl)
        '}' -> Tokenized(RightCurl)
        ',' -> Tokenized(Comma)
        '.' -> Tokenized(Dot)
        ';' -> Tokenized(Semicolon)
        ':' -> Tokenized(Colon)
        '+' -> if (this.scanner.matchEqual()) Tokenized(PlusEqual)     else Tokenized(Plus)
        '-' -> if (this.scanner.matchEqual()) Tokenized(MinusEqual)    else Tokenized(Minus)
        '*' -> if (this.scanner.matchEqual()) Tokenized(AsteriskEqual) else Tokenized(Asterisk)
        '/' -> if (this.scanner.matchEqual()) Tokenized(SlashEqual)    else Tokenized(Slash)
        '%' -> if (this.scanner.matchEqual()) Tokenized(PercentEqual)  else Tokenized(Percent)
        '=' -> if (this.scanner.matchEqual()) Tokenized(EqualEqual)    else Tokenized(Equal)
        '<' -> if (this.scanner.matchEqual()) Tokenized(LessEqual)     else Tokenized(Less)
        '>' -> if (this.scanner.matchEqual()) Tokenized(GreaterEqual)  else Tokenized(Greater)
        '!' -> if (this.scanner.matchEqual()) Tokenized(BangEqual)     else UnexpectedBang(this.scanner.line)
        else if char.isQuotation()  -> if (this.scanner.matchText())    Tokenized(this.tokenizeText())    else UnterminatedString(this.scanner.line)
        else if char.isDigit()      -> if (this.scanner.matchNumeric()) Tokenized(this.tokenizeNumeric()) else MultipleDecimalSeparator(this.scanner.line)
        else if char.isAlphabetic() -> { this.scanner.matchIdentifier(); Tokenized(this.tokenizeIdentifier()) }
        else -> UnknownCharacter(char, this.scanner.line)
    }

    private final fun tokenizeText(): Text {
        val literal = this.scanner.getTextLexeme().toEscaped();
        return Text(Txt(literal));
    }

    private final fun tokenizeNumeric(): Numeric {
        val literal = this.scanner.getNumericLexeme().toDouble();
        return Numeric(Num(literal));
    }

    private final fun tokenizeIdentifier(): LexicalPattern {
        return when (val lexeme = this.scanner.getIdentifierLexeme()) {
            True.Lexeme        -> True;
            False.Lexeme       -> False;

            And.Lexeme         -> And;
            Or.Lexeme          -> Or;
            Not.Lexeme         -> Not;
            Is.Lexeme          -> Is;

            If.Lexeme          -> If;
            Else.Lexeme        -> Else;
            Switch.Lexeme      -> Switch;
            Case.Lexeme        -> Case;

            Loop.Lexeme        -> Loop;
            For.Lexeme         -> For;
            While.Lexeme       -> While;
            Until.Lexeme       -> Until;

            Break.Lexeme       -> Break;
            Continue.Lexeme    -> Continue;
            FallThrough.Lexeme -> FallThrough;
            Return.Lexeme      -> Return;
            To.Lexeme          -> To;

            Var.Lexeme         -> Var;
            Func.Lexeme        -> Func;
            Class.Lexeme       -> Class;
            This.Lexeme        -> This;
            Base.Lexeme        -> Base;
            else               -> Identifier(Id(lexeme));
        }
    }
}