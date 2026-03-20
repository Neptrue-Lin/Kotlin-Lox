package org.neptrueworks.lox.lexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

public final class TextScanningTest {
    @Test
    public fun testEmptyText_ShouldBeEmpty() {
        val literal = "";
        val text = "\"$literal\"";
        val scanner = LexicalScanner(text);
        
        scanner.scanNext();
        assertTrue(scanner.matchText());
        assertEquals(scanner.getTextLexeme(), literal);
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testText_ShouldEqualsLiteral() {
        val literal = "This is a text";
        val text = "\"$literal\"";
        val scanner = LexicalScanner(text);
        
        scanner.scanNext();
        assertTrue(scanner.matchText());
        assertEquals(scanner.getTextLexeme(), literal);

        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testTextWithEscapedBackslash_ShouldEqualsLiteral() {
        val literal = "\\\\";
        val text = "\"$literal\"";
        val scanner = LexicalScanner(text);
        
        scanner.scanNext();
        assertTrue(scanner.matchText());
        assertEquals(scanner.getTextLexeme(), literal);

        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testTextWithEscapedDoubleQuote_ShouldEqualsLiteral() {
        val literal = "\\\"";
        val text = "\"$literal\"";
        val scanner = LexicalScanner(text);
        
        scanner.scanNext();
        assertTrue(scanner.matchText());
        assertEquals(scanner.getTextLexeme(), literal);

        assertFalse(scanner.hasNext());
    }
    
    @Test
    public fun testTextWithEscapedBackslash_And_DoubleQuote_ShouldEqualsLiteral() {
        val literal = "\\\"";
        val text = "\"$literal\"";
        val scanner = LexicalScanner(text);
        
        scanner.scanNext();
        assertTrue(scanner.matchText());
        assertEquals(scanner.getTextLexeme(), literal);

        assertFalse(scanner.hasNext());
    }
}
