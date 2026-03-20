package org.neptrueworks.lox.lexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

public final class CommentScanningTest {
    @Test
    public fun testSeparatedSlashes_ShouldNotMatchComment() {
        val comment = "/ /";
        val scanner = LexicalScanner(comment);
        
        scanner.scanNext();
        assertFalse(scanner.matchComment());
        
        assertTrue(scanner.hasNext());
    }

    @Test
    public fun testSingleLineComment_ShouldIgnoreAllChars() {
        val comment = "// Single-line Comment";
        val scanner = LexicalScanner(comment);
        
        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testSingleLineComment_ShouldIgnoreOneLine() {
        val comment = "// Single-line \n// Comment";
        val scanner = LexicalScanner(comment);
        
        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testSingleLineComment_InMultilineComment_ShouldNotSplitApart() {
        val comment = "/*//*/";
        val scanner = LexicalScanner(comment);

        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testMultilineComment_InSingleComment_ShouldIgnoreMultilineComment() {
        val comment = "// /**/";
        val scanner = LexicalScanner(comment);

        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }
    
    @Test
    public fun testSingleLineComment_InMultilineComments_ShouldIgnoreSingleLineComment() {
        val comment = "/*\n//\n*/";
        val scanner = LexicalScanner(comment);

        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }
}