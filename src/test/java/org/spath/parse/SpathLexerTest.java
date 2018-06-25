package org.spath.parse;

import java.math.BigDecimal;

import org.spath.parser.SpathInputString;
import org.spath.parser.SpathLexer;
import org.spath.parser.TokenType;

import junit.framework.TestCase;

public class SpathLexerTest extends TestCase {
    SpathInputString input = new SpathInputString();
    SpathLexer lexer = new SpathLexer(input);

    public void testEmptyLexer() {
        assertEquals(input, lexer.getInput()); 
        assertNull(input.getLine());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testSimpleValue() {
        input.setLine("a");
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testSimpleLexerSequence() {
        input.setLine("a/b");
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }
    
    public void testAbsoluteSequence() {
        input.setLine("/a/b");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }
    
    public void testDecendantSequence() {
        input.setLine("/a//b");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("//", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidence() {
        input.setLine("/a[@b]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceEqualsDoubleQuotes() {
        input.setLine("/a[@b=\"c\"]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("=", lexer.getTokenValue());
        assertEquals(TokenType.STRING, lexer.nextToken());
        assertEquals("\"c\"", lexer.getTokenValue());
        assertEquals("c", lexer.getTokenString());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceEqualsSingleQuotes() {
        input.setLine("/a[@b='cat']");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("=", lexer.getTokenValue());
        assertEquals(TokenType.STRING, lexer.nextToken());
        assertEquals("'cat'", lexer.getTokenValue());
        assertEquals("cat", lexer.getTokenString());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceNotEquals() {
        input.setLine("/a[@b!='cat']");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("!=", lexer.getTokenValue());
        assertEquals(TokenType.STRING, lexer.nextToken());
        assertEquals("'cat'", lexer.getTokenValue());
        assertEquals("cat", lexer.getTokenString());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceEqualsNumber() {
        input.setLine("/a[@b=3.141]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("=", lexer.getTokenValue());
        assertEquals(TokenType.DECIMAL, lexer.nextToken());
        assertEquals("3.141", lexer.getTokenValue());
        assertEquals(new BigDecimal("3.141"), lexer.getTokenDecimal());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceNotEqualsNumber() {
        input.setLine("/a[@b!=3.141]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("!=", lexer.getTokenValue());
        assertEquals(TokenType.DECIMAL, lexer.nextToken());
        assertEquals("3.141", lexer.getTokenValue());
        assertEquals(new BigDecimal("3.141"), lexer.getTokenDecimal());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceLessThanNumber() {
        input.setLine("/a[@b<3.141]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("<", lexer.getTokenValue());
        assertEquals(TokenType.DECIMAL, lexer.nextToken());
        assertEquals("3.141", lexer.getTokenValue());
        assertEquals(new BigDecimal("3.141"), lexer.getTokenDecimal());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceLessEqualsNumber() {
        input.setLine("/a[@b<=3.141]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("<=", lexer.getTokenValue());
        assertEquals(TokenType.DECIMAL, lexer.nextToken());
        assertEquals("3.141", lexer.getTokenValue());
        assertEquals(new BigDecimal("3.141"), lexer.getTokenDecimal());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceGreaterThanNumber() {
        input.setLine("/a[@b>3.141]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals(">", lexer.getTokenValue());
        assertEquals(TokenType.DECIMAL, lexer.nextToken());
        assertEquals("3.141", lexer.getTokenValue());
        assertEquals(new BigDecimal("3.141"), lexer.getTokenDecimal());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }

    public void testPrecidenceGreaterEqualsNumber() {
        input.setLine("/a[@b>=3.141]");
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals("/", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("a", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("[", lexer.getTokenValue());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("@", lexer.getTokenValue());
        assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
        assertEquals("b", lexer.getTokenValue());
        assertEquals(TokenType.OPERATOR, lexer.nextToken());
        assertEquals(">=", lexer.getTokenValue());
        assertEquals(TokenType.DECIMAL, lexer.nextToken());
        assertEquals("3.141", lexer.getTokenValue());
        assertEquals(new BigDecimal("3.141"), lexer.getTokenDecimal());
        assertEquals(TokenType.DELIMITER, lexer.nextToken());
        assertEquals("]", lexer.getTokenValue());
        assertEquals(TokenType.END, lexer.nextToken());
    }
}
