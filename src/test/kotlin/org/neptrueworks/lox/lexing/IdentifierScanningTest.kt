package org.neptrueworks.lox.lexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

public final class IdentifierScanningTest {
    @Test
    public fun testIdentifierBeforeNumber_ShouldMatchNumber() {
        val identifier = "Identifier0";
        val scanner = LexicalScanner(identifier);

        scanner.scanNext();
        assertTrue(scanner.matchIdentifier());
        assertEquals(scanner.getIdentifierLexeme(), identifier);
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testIdentifierBeforeUnderscore_ShouldMatchIdentifier() {
        val identifier = "Identifier_";
        val scanner = LexicalScanner(identifier);

        scanner.scanNext();
        assertTrue(scanner.matchIdentifier());
        assertEquals(scanner.getIdentifierLexeme(), identifier);
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testIdentifierAfterNumber_ShouldNotMatchLeadingNumber() {
        val number = "0";
        val identifier = "Identifier";
        val source = "$number$identifier";
        val scanner = LexicalScanner(source);

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());

        scanner.scanNext();
        assertTrue(scanner.matchIdentifier());
        assertEquals(scanner.getIdentifierLexeme(), identifier);

        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testIdentifierAfterLeadingUnderscore_ShouldNotMatchUnderscore() {
        val underscore = "_";
        val identifier = "Identifier";
        val source = "$underscore$identifier";
        val scanner = LexicalScanner(source);

        val underline = scanner.scanNext();
        assertEquals(underline, '_');

        scanner.scanNext();
        assertTrue(scanner.matchIdentifier());
        assertEquals(scanner.getIdentifierLexeme(), identifier);
        
        assertFalse(scanner.hasNext());
    }
}