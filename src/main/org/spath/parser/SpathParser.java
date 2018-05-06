package org.spath.parser;

import java.math.BigDecimal;

import org.spath.SpathException;
import org.spath.SpathMatch;
import org.spath.SpathName;
import org.spath.SpathNameElement;
import org.spath.SpathNameRelative;
import org.spath.SpathNameStart;
import org.spath.SpathOperator;
import org.spath.SpathPredicateBoolean;
import org.spath.SpathPredicateNumber;
import org.spath.SpathPredicateString;

public class SpathParser {
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    SpathLexer lexer = null;
    
    public SpathParser() {
        this(new SpathLexer());
    }
    
    public SpathParser(SpathLexer lexer) {
        this.lexer = lexer;
    }
    
    public SpathLexer getLexer() {
        return lexer;
    }
    
    public SpathInputString getInput() {
        return getLexer().getInput();
    }

    public SpathName parse(String expression) {
        getInput().setLine(expression);
        
        SpathName expr = spath();
        
        return expr;
    }
    
    SpathName spath() {
        SpathName expr = null;
        lexer.nextToken();
        while (lexer.hasNext()) {
            return root();
        }
        return expr;
    }
    
    SpathName root() {
        SpathName expr = null;
        if (accept(TokenType.OPERATOR, "/")) {
            String value =  name();
            SpathNameStart ext = new SpathNameStart(value);
            ext.add(predicateOpt());
            expr = extension(ext);
        } else if (accept(TokenType.OPERATOR, "//")) {
            String value =  name();
            SpathNameRelative ext = new SpathNameRelative(value);
            ext.add(predicateOpt());
            expr = extension(ext);
        } else {
            String value = name();
            SpathNameRelative ext = new SpathNameRelative(value);
            ext.add(predicateOpt());
            expr = extension(ext);
        }
        return expr;
    }
    
    SpathName extension(SpathName parent) {
        SpathName expr = null;
        if (accept(TokenType.OPERATOR, "/")) {
            String value =  name();
            SpathNameElement ext = new SpathNameElement(parent, value);
            ext.add(predicateOpt());
            expr = extension(ext);
        } else if (accept(TokenType.OPERATOR, "//")) {
            String value =  name();
            SpathNameRelative ext = new SpathNameRelative(parent, value);
            ext.add(predicateOpt());
            expr = extension(ext);
        } else {
            expr = parent;
        }
        return expr;
    }
    
    String name() {
        if (match(TokenType.IDENTIFIER)) {
            String value = lexer.getTokenValue();
            nextToken();
            return value;
        } else {
            throw exception("Expected a name");
        }
    }
    
    SpathMatch predicateOpt() {
        SpathMatch predicate = null;
        if (accept(TokenType.DELIMITER, "[")) {
            if (accept(TokenType.DELIMITER, "@")) {
                String name = name();
                if (match(TokenType.OPERATOR)) {
                    SpathOperator operator = predicateOperator();
                    if (match(TokenType.STRING)) {
                        String value = quotedString();
                        predicate = new SpathPredicateString(name, operator, value);
                    } else if (match(TokenType.DECIMAL)) {
                        BigDecimal value = decimalNumber();
                        predicate = new SpathPredicateNumber(name, operator, value);
                    } else if (match(TokenType.IDENTIFIER) 
                            && TRUE.equalsIgnoreCase(lexer.getTokenValue())) {
                        predicate = new SpathPredicateBoolean(name, operator, Boolean.TRUE);
                        nextToken();
                    } else if (match(TokenType.IDENTIFIER) 
                            && FALSE.equalsIgnoreCase(lexer.getTokenValue())) {
                        predicate = new SpathPredicateBoolean(name, operator, Boolean.FALSE);
                        nextToken();
                    } else {
                        throw exception("Predicate value expected.");
                    }
                } else {
                    predicate = new SpathPredicateString(name);
                }
            } else {
                throw exception("Attribute symbol '@' expected.");
            }
            expect(TokenType.DELIMITER, "]", "Predicate must be closed with ']'");
        }
        return predicate;
    }
    
    SpathOperator predicateOperator() {
        if (accept(TokenType.OPERATOR, "=")) {
            return SpathOperator.EQ;
        } else if (accept(TokenType.OPERATOR, "!=")) {
            return SpathOperator.NE;
        } else if (accept(TokenType.OPERATOR, "<")) {
            return SpathOperator.LT;
        } else if (accept(TokenType.OPERATOR, "<=")) {
            return SpathOperator.LE;
        } else if (accept(TokenType.OPERATOR, ">")) {
            return SpathOperator.GT;
        } else if (accept(TokenType.OPERATOR, ">=")) {
            return SpathOperator.GE;
        } else {
            throw exception("Predicate operator expected, e.g. = != < <= > >=");
        }
    }
    
    String quotedString() {
        if (match(TokenType.STRING)) {
            String value = lexer.getTokenString();
            nextToken();
            return value;
        } else {
            throw exception("Expected a quoted string");
        }
    }
    
    BigDecimal decimalNumber() {
        if (match(TokenType.DECIMAL)) {
            BigDecimal value = lexer.getTokenDecimal();
            nextToken();
            return value;
        } else {
            throw exception("Expected a decimal number");
        }
    }
    
    public boolean match(TokenType s)
    {
        TokenType token = lexer.getTokenType();
        return (token == s);
    }

    public boolean match(TokenType s, String value)
    {
        TokenType token = lexer.getTokenType();
        String tokenValue;
        if (s == TokenType.STRING || s == TokenType.CHARACTER) {
            tokenValue = Character.toString(lexer.getTokenDelimiter());
        } else {
            tokenValue = lexer.getTokenValue();
        }
        return (token == s && (value == null || value.equals(tokenValue)));
    }
    
    public boolean accept(TokenType s, String value)
    {
        if (match(s, value)) 
        {
            nextToken();
            return true;
        }
        return false;
    }
    
    public void expect(TokenType s, String value, String message)
    {
        if (!accept(s, value)) {
            throw exception(message);
        }
    }

    /** Advance to the next token. */
    public TokenType nextToken()
    {
        TokenType token = lexer.nextToken();
        if (token == TokenType.ERROR)
        {
            throw exception(lexer.getErrorMessage());
        }
        return token;
    }

    /**
     * Throw an appropriate Exception.
     *
     * @param message -
     *            the error message to include in the Exception.
     * @throws Exception -
     *             with details of the error.
     */
    public SpathException exception(String message) {
        SpathException ex = new SpathException(message);
        return ex;
    }

    public SpathMatch error(String message) {
        throw exception(message);
    }

    public String toString() {
        if (getInput() == null) {
            return "$$ - no input";
        }
        return getInput().toString();
    }
}
