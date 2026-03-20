package org.neptrueworks.lox.lexing

import org.neptrueworks.lox.diagnosis.LineNumber
import org.neptrueworks.lox.results.LexicalAnalysisResult
import org.neptrueworks.lox.results.LexicalAnalysisResult.*
import org.neptrueworks.lox.lexing.LexicalPattern.*

public final class LexicalAnalyzer(
    private val scanner: LexicalScanner,
) {
    private val tokens: MutableList<LexicalToken<*>> = mutableListOf();
    private val errors: MutableList<LexicalAnalysisResult> = mutableListOf();

    public override fun toString() = this.tokens.toString() + this.errors.toString();

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
    }

    private fun tokenize(char: Char): LexicalAnalysisResult = when (char) {
        '(' -> Tokenized(LeftParen)
        ')' -> Tokenized(RightParen)
        '[' -> Tokenized(LeftBracket)
        ']' -> Tokenized(RightBracket)
        '{' -> Tokenized(LeftBrace)
        '}' -> Tokenized(RightBrace)
        ',' -> Tokenized(Comma)
        '.' -> Tokenized(Dot)
        ';' -> Tokenized(Semicolon)
        ':' -> Tokenized(Colon)
        '+' -> Tokenized(Plus)
        '-' -> Tokenized(Minus)
        '*' -> Tokenized(Asterisk)
        '/' -> Tokenized(Slash)
        '%' -> Tokenized(Percent)
        '=' -> if (this.scanner.matchEqual()) Tokenized(EqualEqual) else Tokenized(Equal)
        '<' -> if (this.scanner.matchEqual()) Tokenized(LessEqual) else Tokenized(Less)
        '>' -> if (this.scanner.matchEqual()) Tokenized(GreaterEqual) else Tokenized(Greater)
        '!' -> if (this.scanner.matchEqual()) Tokenized(BangEqual) else UnexpectedBang(this.scanner.line)
        '"' -> if (this.scanner.matchText()) Tokenized(this.tokenizeText()) else UnterminatedString(this.scanner.line)
        else if char.isDigit() -> if (this.scanner.matchNumeric()) Tokenized(this.tokenizeNumeric()) else MultipleDecimalSeparator(this.scanner.line)
        else if char.isAlphabetic() -> { this.scanner.matchIdentifier(); Tokenized(this.tokenizeIdentifier()) }
        else -> UnknownCharacter(char, this.scanner.line)
    }

    private fun analyzeToken(result: LexicalAnalysisResult, line: LineNumber) {
        if (result is Tokenized<*>) {
            this.tokens += LexicalToken(result.pattern, line);
        } else {
            this.errors += result;
        }
    }

    private final fun tokenizeText(): Text {
        val literal = this.scanner.getTextLexeme().toEscaped();
        return Text(literal);
    }

    private final fun tokenizeNumeric(): Numeric {
        val literal = this.scanner.getNumericLexeme().toDouble();
        return Numeric(literal);
    }

    private final fun tokenizeIdentifier(): LexicalPattern<Id> {
        val lexeme = this.scanner.getIdentifierLexeme();
        return when (lexeme) {
            True.literal        -> True;
            False.literal       -> False;
            Nil.literal         -> Nil;

            And.literal         -> And;
            Or.literal          -> Or;
            Not.literal         -> Not;
            Is.literal          -> Is;

            If.literal          -> If;
            Else.literal        -> Else;
            Switch.literal      -> Switch;
            Case.literal        -> Case;

            Loop.literal        -> Loop;
            For.literal         -> For;
            While.literal       -> While;
            Until.literal       -> Until;

            Break.literal       -> Break;
            Continue.literal    -> Continue;
            FallThrough.literal -> FallThrough;
            Return.literal      -> Return;

            Func.literal        -> Func;
            Class.literal       -> Class;
            This.literal        -> This;
            Base.literal        -> Base;
            else                -> Identifier(lexeme);
        }
    }
}