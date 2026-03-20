package org.neptrueworks.lox.lexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

public final class NumericScanningTest {
    @Test
    public fun testInteger() {
        val numeric = "0";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());
        assertEquals(scanner.getNumericLexeme(), numeric)
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testDecimal_ShouldNotSplitToInts() {
        val numeric = "0.0";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());
        assertEquals(scanner.getNumericLexeme(), numeric)
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testIntegerWithTrailingSeparator_ShouldNotMatch() {
        val numeric = "0.";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertFalse(scanner.matchNumeric());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testIntegerWithLeadingSeparator_ShouldNotMatch() {
        val numeric = ".0";
        val scanner = LexicalScanner(numeric);

        val dot = scanner.scanNext();
        assertEquals(dot, '.');

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());
        assertEquals(scanner.getNumericLexeme(), "0");
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testDecimalWithLeadingZero_ShouldNotSplitToIntAndDecimal() {
        val numeric = "00.0";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());
        assertEquals(scanner.getNumericLexeme(), numeric)
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testDecimalWithTrailingZero_ShouldNotSplitToDecimalAndInt() {
        val numeric = "0.00";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());
        assertEquals(scanner.getNumericLexeme(), numeric)
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testDecimalWithLeadingAndTrailingZero_ShouldNotSplitToInts() {
        val numeric = "00.00";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertTrue(scanner.matchNumeric());
        assertEquals(scanner.getNumericLexeme(), numeric)
        
        assertFalse(scanner.hasNext());
    }
    
    @Test
    public fun testDecimalWithTwoSeparators_ShouldNotMatch() {
        val numeric = "0.0.0";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertFalse(scanner.matchNumeric());
        
        assertFalse(scanner.hasNext());
    }
    
    @Test
    public fun testDecimalWithTwoContiguousSeparators_ShouldNotMatch() {
        val numeric = "0..0";
        val scanner = LexicalScanner(numeric);

        scanner.scanNext();
        assertFalse(scanner.matchNumeric());
        
        assertFalse(scanner.hasNext());
    }
}