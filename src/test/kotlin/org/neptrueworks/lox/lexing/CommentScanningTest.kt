package org.neptrueworks.lox.lexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

public final class CommentScanningTest {
    @Test
    public fun testSeparatedSlashes_ShouldNotMatchLineComment() {
        val comment = "/ /";
        val scanner = LexicalScanner(comment);
        
        scanner.scanNext();
        assertFalse(scanner.matchComment());
        
        assertTrue(scanner.hasNext());
    }

    @Test
    public fun testLineComment_ShouldIgnoreAllChars() {
        val comment = "// Line Comment";
        val scanner = LexicalScanner(comment);
        
        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testLineComments_ShouldIgnoreEachLine() {
        val comment = "// Line \n// Comment";
        val scanner = LexicalScanner(comment);
        
        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testLineComment_InDelimitedComment_ShouldNotSplitApart() {
        val comment = "/*//*/";
        val scanner = LexicalScanner(comment);

        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }

    @Test
    public fun testDelimitedComment_InLineComment_ShouldIgnoreDelimitedComment() {
        val comment = "// /**/";
        val scanner = LexicalScanner(comment);

        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }
    
    @Test
    public fun testLineComment_InDelimitedComments_ShouldIgnoreLineComment() {
        val comment = "/*\n//\n*/";
        val scanner = LexicalScanner(comment);

        scanner.scanNext();
        assertTrue(scanner.matchComment());
        
        assertFalse(scanner.hasNext());
    }
}