package org.neptrueworks.lox.lexing

import org.neptrueworks.lox.diagnosis.ColumnNumber
import org.neptrueworks.lox.diagnosis.LineNumber
import org.neptrueworks.lox.diagnosis.Start

public final class LexicalScanner(
    private val source: String
) {
    public var line = LineNumber.Start
        private set;
    public var column = ColumnNumber.Start
        private set;

    private var lexemeStart = -1;
    private var current = -1;

    internal final inline fun hasNext() = this.current < this.source.length - 1;
    internal fun scanNext(): Char {
        this.nextColumn();
        if (this.getCurrentChar().isNewLine()) {
            this.nextLine();
        }
        return this.getCurrentChar();
    }

    private final inline fun getCurrentChar() = this.source[this.current]
    private final inline fun lookAhead() = this.source[this.current + 1]
    private final inline fun lookBehind() = this.source[this.current - 1]

    private inline fun nextLine() {
        this.line++;
        this.column = ColumnNumber.Start;
    }

    private inline fun nextColumn() {
        this.current++;
        this.column++;
    }

    private inline fun startLexeme() { this.lexemeStart = this.current; }

    internal final inline fun getIdentifierLexeme() = this.source.substring(this.lexemeStart, this.current + 1);
    internal final inline fun getNumericLexeme() = this.source.substring(this.lexemeStart, this.current + 1);
    internal final inline fun getTextLexeme() = this.source.substring(this.lexemeStart + 1, this.current);
    
    internal fun matchEqual(): Boolean {
        if (!this.hasNext() || this.lookAhead().isNotEqual()) {
            return false;
        }

        this.nextColumn();
        return true;
    }

    internal fun matchComment(): Boolean {
        if (this.getCurrentChar().isNotSlash()) {
            return false;
        }
        if (!this.hasNext()) {
            return false;
        }

        return if (this.lookAhead().isSlash()) {
            this.matchSingleLineComment()
        } else if (this.lookAhead().isAsterisk()) {
            this.matchMultilineComment()
        } else {
            false
        }
    }

    private fun matchSingleLineComment(): Boolean {
        this.startLexeme();
        do {
            this.nextColumn();
        } while (this.hasNext() && this.getCurrentChar().isNotNewLine());
        return true;
    }

    private fun matchMultilineComment(): Boolean {
        this.startLexeme();
        do {
            this.nextColumn();
            if (this.getCurrentChar().isSlash() && this.lookBehind().isAsterisk()) {
                // /*//*/
                //   ↑
                if (this.current != this.lexemeStart + 2) {
                    break;
                }
            }
        } while (this.hasNext());
        return true;
    }

    internal fun matchText(): Boolean {
        if (this.getCurrentChar().isNotDoubleQuote()) {
            return false;
        }

        this.startLexeme();
        while (this.hasNext() && this.getCurrentChar().isNotNewLine()) {
            this.nextColumn();
            if (this.getCurrentChar().isBackslash()) {
                this.nextColumn();
            } else if (this.getCurrentChar().isDoubleQuote()) {
                return true;
            }
        }
        return false;
    }

    internal fun matchNumeric(): Boolean {
        if (this.getCurrentChar().isNotDigit()) {
            return false;
        }

        this.startLexeme();
        var digits = 1;
        var separators = 0;
        while (this.hasNext()) {
            if (this.lookAhead().isDigit()) {
                digits++;
            } else if (this.lookAhead().isDot()) {
                separators++;
                digits = 0;
            } else {
                break;
            }
            this.nextColumn();
        }
        return separators <= 1 && digits > 0;
    }

    internal fun matchIdentifier(): Boolean {
        if (this.getCurrentChar().isNotAlphabetic()) {
            return false;
        }

        this.startLexeme();
        while (this.hasNext() && this.lookAhead().isWord()) {
            this.nextColumn();
        }
        return true;
    }
}
