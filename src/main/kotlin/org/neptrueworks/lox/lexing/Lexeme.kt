package org.neptrueworks.lox.lexing

internal inline fun Char.isNewLine() = this == '\n';
internal inline fun Char.isSlash() = this == '/';
internal inline fun Char.isEqual() = this == '=';
internal inline fun Char.isDoubleQuote() = this == '"';
internal inline fun Char.isSemicolon() = this == ';';
internal inline fun Char.isDot() = this == '.';
internal inline fun Char.isUnderscore() = this == '_';
internal inline fun Char.isBackslash() = this == '\\';
internal inline fun Char.isAsterisk() = this == '*';
internal inline fun Char.isAlphabetic() = this.isLetter();
internal inline fun Char.isAlphanumeric() = this.isDigit() || this.isAlphabetic();
internal inline fun Char.isWord() = this.isAlphanumeric() || this.isUnderscore();

internal inline fun Char.isNotDigit() = !this.isDigit();
internal inline fun Char.isNotNewLine() = !this.isNewLine();
internal inline fun Char.isNotSlash() = !this.isSlash();
internal inline fun Char.isNotEqual() = !this.isEqual();
internal inline fun Char.isNotWhitespace() = !this.isWhitespace();
internal inline fun Char.isNotDoubleQuote() = !this.isDoubleQuote();
internal inline fun Char.isNotSemicolon() = !this.isSemicolon();
internal inline fun Char.isNotDot() = !this.isDot();
internal inline fun Char.isNotUnderscore() = !this.isUnderscore();
internal inline fun Char.isNotBackslash() = !this.isBackslash();
internal inline fun Char.isNotAsterisk() = !this.isAsterisk();
internal inline fun Char.isNotAlphabetic() = !this.isAlphabetic();
internal inline fun Char.isNotAlphanumeric() = !this.isAlphanumeric();
internal inline fun Char.isNotWord() = !this.isWord();

internal inline fun Char.toEscapeChar(): Char = when (this) {
    'b' -> '\b';
    'n' -> '\n';
    'r' -> '\r';
    't' -> '\t';
    else -> this;
}

internal fun String.toEscaped(): String {
    val first = this.indexOf('\\')
    if (first < 0) { 
        return this;
    }

    val builder = StringBuilder(this.length - 1);
    builder.append(this, 0, first);

    var current = first;
    while (current < this.length) {
        val char = this[current];
        if (char == '\\' && current + 1 < this.length) {
            builder.append(this[current + 1].toEscapeChar())
            current += 2;
        } else {
            builder.append(char);
            current++;
        }
    }
    return builder.toString();
}