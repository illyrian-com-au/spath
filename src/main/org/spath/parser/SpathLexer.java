/*
 * Created on 7/05/2008 by strongd
 *
 * Copyright (c) 2006 Department of Infrastructure (DOI)
 * State Government of Victoria, Australia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of DOI.
 */

package org.spath.parser;

import java.math.BigDecimal;
import java.util.Properties;

/**
 *
 * This tokeniser splits an input string into tokens. <br>
 * It returns these tokens:
 * <ul>
 * <li>ERROR - Invalid or unrecognised character</li>
 * <li>END - End of input</li>
 * <li>IDENTIFIER - A java style identifier</li>
 * <li>DELIMITER - ;,({[)}]</li>
 * <li>OPERATOR - "`~!@#%^&*|\:;'<,.>?/+=-</li>
 * <li>STRING - " any characters "</li>
 * <li>CHARACTER - ' a single char '</li>
 * <li>RESERVED - An Identifier with special meaning</li>
 * </ul>
 * <p>
 * Identifiers start with an alphabetic character and may include any number of
 * alphanumeric characters. <br>
 * Identifiers are case sensitive while reserved words (AND and OR) are
 * <b>not</b> case sensitive.
 *
 * @author strongd
 */
public class SpathLexer
{
   /** The last read token. Modified by nextToken() */
    private TokenType tokenType = null;

    private SpathInputString input = null;
    
    /** Whitespace before the current token */
    private String whitespace;

    /** The characters in a string or char excluding the quotes. */
    private String tokenString;

    /** The quote character that surrounded the string. */
    private char tokenDelimiter;

    /** The text of a comment. */
    private String commentString = null;

    private String errorMessage;

    /**
     * End of line character. (Used internally).
     */
    protected static final char EOL = '\n';

    /**
     * A map from reserved word to an object.
     */
    private Properties reservedWords = new Properties();

    /**
     * A map from operator to an object.
     */
    private Properties operators = null;

    /**
     * Constructor for Search Query Tokeniser.
     *
     * @param input
     *            the string to be tokenised.
     */
    public SpathLexer() {
        setInput(new SpathInputString());
    }

    /**
     * Constructor for Search Query Tokeniser.
     *
     * @param input
     *            the string to be tokenised.
     */
    public SpathLexer(SpathInputString input)
    {
        setInput(input);
    }

    public void setInput(SpathInputString input)
    {
        this.input = input;
    }

    /*
     * @see au.com.illyrian.parser.Lexer#getLexerInput()
     */
    public SpathInputString getInput()
    {
        if (input == null) {
            throw new NullPointerException("Input is null.");
        }
        return input;
    }

    public String getCommentString()
    {
        return commentString;
    }

    public void setReservedWords(Properties reservedWords)
    {
        this.reservedWords = reservedWords;
    }

    public Properties getReservedWords()
    {
        return reservedWords;
    }
    
    public void setOperators(Properties operators)
    {
        this.operators = operators;
    }

    public Properties getOperators()
    {
        if (operators == null)
            operators = new Properties();
        return operators;
    }
    
    /**
     * Get the whitespace before the current token.
     *
     * @return the whitespace before the current input token.
     */
    public String getWhitespace()
    {
        return whitespace;
    }

    public TokenType getTokenType()
    {
        return tokenType;
    }

    /*
     */
    public String getTokenValue()
    {
        String value = input.getTokenString();
        return value;
    }
    
    protected void setTokenString(String value) {
        tokenString = value;
    }

    public String getTokenString()
    {
        return tokenString;
    }

    public char getTokenDelimiter()
    {
        return tokenDelimiter;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public Integer getTokenInteger()
    {
        if (tokenType == TokenType.NUMBER) {
            return Integer.valueOf(input.getTokenString());
        } else {
            throw new NumberFormatException("Token is not an Integer");
        }
    }

    public BigDecimal getTokenDecimal() throws NumberFormatException
    {
        try {
            return new BigDecimal(input.getTokenString());
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Could not parse a decimal number: " + input.getTokenString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.com.illyrian.parser.Lexer#getTokenString()
     */
    public Object getTokenOperator()
    {
        if (tokenType == TokenType.OPERATOR) {
            return getOperators().get(input.getTokenString());
        } else {
            throw new IllegalStateException("Token is not an Operator");
        }
    }

    public boolean hasNext() {
        return getTokenType() != TokenType.END;
    }
    
    public TokenType nextToken()
    {
        getInput();
        do {
            // Move over any whitespace before the token.
            spanWhiteSpace();
            // Read the next token
            tokenType = spanNextToken();

        } while (tokenType == TokenType.COMMENT);
        return tokenType;
    }

    /**
     * Read the next token.
     */
    private TokenType spanNextToken()
    {
        TokenType token;
        // Now examine the start character.
        char ch = input.startChar();
        if (ch == SpathInputString.NULL) {
            token = TokenType.END;
        } else if (isIdentifierStartChar(ch)) {
            token = spanIdentifier();
        } else if (isQuote(ch)) {
            token = spanStringLiteral();
        } else if (isDelimiter(ch)) {
            token = spanDelimiter();
        } else if (isOperator(ch)) {
            token = spanOperator();
        } else if (isDigitChar(ch)) {
            token = spanNumber();
        } else {
            token = error("Unrecognised input character: " + SpathInputString.encode(ch));
        }
        return token;
    }

    /**
     * Step over the current delimiter character.
     * Spans a single character, e.g. <code>;</code> or <code>(</code>.
     * The delimiter character is available from <code>getDelimiter()</code> 
     * and <code>getTokenValue()</code>.
     * @returns Lexer.DELIMITER
     */
    private TokenType spanDelimiter()
    {
        input.startChar(); // Mark start of token
        tokenDelimiter = input.nextChar();
        return TokenType.DELIMITER;
    }

    /**
     * Step over the current quoted string.
     * Recognises a token of the form "Hello world". 
     * The string without quotes is available from <code>getString()</code>.
     * The quote delimiter is available from <code>getDelimiter()</code>.
     * The entire token including quotes is available from <code>getTokenValue()</code>.
     * @returns Lexer.CHARACTER
     */
    private TokenType spanStringLiteral()
    {
        StringBuffer buf = new StringBuffer();
        char ch = input.startChar(); // Mark start of token
        if (isQuote(ch)) {
            tokenDelimiter = ch;
            ch = input.nextChar();
            while (!isQuote(ch)) {
                if (ch == SpathInputString.NULL)
                    break;
                buf.append(ch);
                ch = input.nextChar();
            }
            if (ch == tokenDelimiter) {
                ch = input.nextChar();
                tokenString = buf.toString();
                return TokenType.STRING;
            }
            return error("Missing quote at end of String: " + tokenDelimiter);
        }
        return error("Missing quote at start of character.");
    }

    /**
     * Skip over the whitespace at the start of the current token.
     */
    private void spanWhiteSpace()
    {
        char ch = input.startChar();
        if (isWhitespace(ch)) {
            while (isWhitespace(ch)) {
                ch = input.nextChar();
            }
            whitespace = getTokenValue();
        } else {
            whitespace = "";
        }
    }

    /**
     * Span the current identifier in the input string. Determine whether the
     * identifier is a reserved word. The text for the token will be available
     * through getTokenString().
     *
     * @return the code for the identifier or reserved word.
     */
    private TokenType spanIdentifier()
    {
        char ch = input.startChar();
        {
            // Move the finish pointer just past the end of the identifier.
            while (isIdentifierChar(ch)) {
                ch = input.nextChar();
            }
        }
        // Examine the identifier to determine if it is a reserved word.
        if (reservedWords != null) {
            String identifier = convertIdentifier(getTokenValue());
            if (getReservedWords().getProperty(identifier) != null) {
                if (getOperators().getProperty(identifier) != null) {
                    return TokenType.OPERATOR;
                }
                return TokenType.RESERVED;
            }
        }
        return TokenType.IDENTIFIER;
    }
    
    private String convertIdentifier(String identifier) {
        if (identifier != null) {
            return identifier.toLowerCase();
        }
        return null;
    }

    /**
     * Span a sequence of digits in the input string. Determine whether the
     * number is valid. The text for the token will be available through
     * getTokenValue().
     *
     * @return the code for the operator or delimiter.
     */
    private TokenType spanNumber()
    {
        char ch = input.startChar();
        while (isDigitChar(ch)) {
            ch = input.nextChar();
        }
        if (ch != '.') {
            return TokenType.NUMBER;
        }
        ch = input.nextChar();
        while (isDigitChar(ch)) {
            ch = input.nextChar();
        }
        return TokenType.DECIMAL;
    }

    /**
     * Span a sequence of operators in the input string. Determine whether the
     * operator is valid. The text for the token will be available through
     * getTokenString().
     *
     * @return the code for the operator or delimiter.
     */
    private TokenType spanOperator()
    {
        char ch = input.startChar();
        while (isOperator(ch)) {
            ch = input.nextChar();
        }
        return TokenType.OPERATOR;
    }

    private TokenType error(String message)
    {
        errorMessage = message;
        return TokenType.ERROR;
    }

    // #### character tests ####

    /**
     * Determine whether the character is a white space character.
     *
     * @param ch
     *            the character to be tested.
     * @return true if the character is a digit.
     */
    private boolean isWhitespace(char ch)
    {
        return Character.isWhitespace(ch);
    }

    /**
     * Determine whether the character is a digit.
     *
     * @param ch
     *            - the character to be tested.
     * @return true if the character is a digit.
     */
    private boolean isDigitChar(char ch)
    {
        return ('0' <= ch && ch <= '9');
    }

    /**
     * Determine whether the character is suitable in an identifier.
     *
     * @param ch
     *            - the character to be tested.
     * @return true if the character is a letter or digit.
     */
    private boolean isIdentifierChar(char ch)
    {
        return isIdentifierStartChar(ch) || isDigitChar(ch)|| ch == ':';
    }

    /**
     * Determine whether the character is suitable as the first character of an
     * identifier.
     *
     * @param ch
     *            - the character to be tested.
     * @return true if the character is a letter.
     */
    private boolean isIdentifierStartChar(char ch)
    {
        return ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z');
    }

    private boolean isDelimiter(char ch)
    {
        switch (ch) {
//        case ',' :
        case '@' :
        case '(' :
        case ')' :
        case '[' :
        case ']' :
//          case '{' :
//        case '}' :
            return true;
        default:
            return false;
        }
    }

    private boolean isQuote(char ch)
    {
        return ch == '\"' || ch == '\'';
    }

    private boolean isOperator(char ch)
    {
        switch (ch) {
        case '/':
        case '=':
        case '*':
        case '!':
        case '+':
        case '-':
//        case '%':
//        case '&':
        case '|':
        case '<':
        case '>':
//        case '.':
//        case '?':
        case '~':
        case '^':
//        case ':':
            return true;
        default:
            return false;
        }
    }
    
    public String toString()
    {
        switch (tokenType) {
        case END:
            return "END";
        case IDENTIFIER:
        case DELIMITER:
        case RESERVED:
        case OPERATOR:
        case NUMBER:
        case DECIMAL:
        case CHARACTER:
        case STRING:
            return tokenType + "= " + getTokenValue();
        case COMMENT:
            return "COMMENT= " + getCommentString();
        case ERROR:
            return "ERROR= " + getErrorMessage();
        }
        return "Unknown token index : " + tokenType;
    }
}
