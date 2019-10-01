package org.spath.parser;

import java.math.BigDecimal;

import org.spath.SpathMatch;
import org.spath.SpathQuery;
import org.spath.query.SpathAnyName;
import org.spath.query.SpathFunction;
import org.spath.query.SpathName;
import org.spath.query.SpathQualifiedName;
import org.spath.query.SpathQueryException;
import org.spath.query.SpathQueryBuilder;
import org.spath.query.SpathPredicateOperator;
import org.spath.query.SpathPredicateAnd;
import org.spath.query.SpathPredicateBoolean;
import org.spath.query.SpathPredicateNumber;
import org.spath.query.SpathPredicateOr;
import org.spath.query.SpathPredicateString;
import org.spath.query.SpathQueryType;

public class SpathParser {
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String AND = "and";
    private static final String OR = "or";
    private static final String STAR = "*";

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

    public SpathQuery parse(String expression) {
        getInput().setLine(expression);
        
        SpathQuery expr = spath();
        
        return expr;
    }
    
    public SpathQuery parse(SpathQuery parent, String expression) {
        getInput().setLine(expression);
        
        SpathQuery expr = extension(parent);
        
        return expr;
    }
    
    SpathQuery spath() {
        SpathQuery expr = null;
        lexer.nextToken();
        while (lexer.hasNext()) {
            return root();
        }
        return expr;
    }
    
    SpathQuery root() {
        SpathQuery expr = null;
        if (accept(SpathToken.OPERATOR, "/")) {
            SpathQueryBuilder build = new SpathQueryBuilder()
                .withType(SpathQueryType.ROOT)
                .withName(name())
                .withPredicate(predicateOpt());
            expr = extension(build.build());
        } else if (accept(SpathToken.OPERATOR, "//")) {
            SpathQueryBuilder build = new SpathQueryBuilder()
                .withType(SpathQueryType.RELATIVE)
                .withName(name())
                .withPredicate(predicateOpt());
            expr = extension(build.build());
        } else {
            SpathQueryBuilder build = new SpathQueryBuilder()
                .withName(name())
                .withType(SpathQueryType.RELATIVE)
                .withPredicate(predicateOpt());
            expr = extension(build.build());
        }
        return expr;
    }
    
    SpathQuery extension(SpathQuery parent) {
        SpathQueryBuilder build = new SpathQueryBuilder(parent);
        SpathQuery expr = null;
        if (accept(SpathToken.OPERATOR, "/")) {
            build.withElement(element())
                .withPredicate(predicateOpt());
            expr = extension(build.build());
        } else if (accept(SpathToken.OPERATOR, "//")) {
            build.withType(SpathQueryType.RELATIVE)
                .withElement(element())
                .withPredicate(predicateOpt());
            expr = extension(build.build());
        } else if (match(SpathToken.END)) {
            expr = parent;
        } else {
            throw exception("Unexpected input: " + toString());
        }
        return expr;
    }
    
    SpathName element() {
        if (match(SpathToken.IDENTIFIER)) {
            String name = lexer.getTokenValue();
            nextToken();
            if (accept(SpathToken.DELIMITER, ":")) {
                String qualifier = lexer.getTokenValue();
                nextToken();
                return new SpathQualifiedName(qualifier, name);
            } else if (accept(SpathToken.DELIMITER, "(")) {
                expect(SpathToken.DELIMITER, ")", ") expected");
                return new SpathFunction(name);
            } else {
                return new SpathName(name);
            }
        } else if (accept(SpathToken.DELIMITER, "*")) {
            return new SpathAnyName();
        } else {
            throw exception("Expected a name or *");
        }
    }
    
    SpathMatch predicateOpt() {
        SpathMatch predicate = null;
        if (accept(SpathToken.DELIMITER, "[")) {
            predicate = predicateMult();
            expect(SpathToken.DELIMITER, "]", "Predicate must be closed with ']' not " + lexer.getTokenValue());
        }
        return predicate;
    }
    
    SpathMatch predicateMult() {
        SpathMatch predicate = predicate();
        while (!match(SpathToken.DELIMITER, "]")) {
            if (match(SpathToken.IDENTIFIER) 
                    && AND.equalsIgnoreCase(lexer.getTokenValue())) {
                nextToken();
                SpathMatch operand2 = predicate();
                predicate = new SpathPredicateAnd(predicate, operand2);
            } else  if (match(SpathToken.IDENTIFIER) 
                    && OR.equalsIgnoreCase(lexer.getTokenValue())) {
                nextToken();
                SpathMatch operand2 = predicate();
                predicate = new SpathPredicateOr(predicate, operand2);
            }
        }
        return predicate;
    }
    
    SpathMatch predicate() {
        SpathMatch predicate = null;
        if (accept(SpathToken.DELIMITER, "@")) {
            String name = name();
            if (match(SpathToken.OPERATOR)) {
                SpathPredicateOperator operator = predicateOperator();
                if (match(SpathToken.STRING)) {
                    String value = quotedString();
                    predicate = new SpathPredicateString(name, operator, value);
                } else if (match(SpathToken.DECIMAL) || match(SpathToken.NUMBER)) {
                    BigDecimal value = decimalNumber();
                    predicate = new SpathPredicateNumber(name, operator, value);
                } else if (match(SpathToken.IDENTIFIER) 
                        && TRUE.equalsIgnoreCase(lexer.getTokenValue())) {
                    predicate = new SpathPredicateBoolean(name, operator, Boolean.TRUE);
                    nextToken();
                } else if (match(SpathToken.IDENTIFIER) 
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
        return predicate;
    }
    
    String name() {
        if (match(SpathToken.IDENTIFIER)) {
            String value = lexer.getTokenValue();
            nextToken();
            return value;
        } else if (accept(SpathToken.DELIMITER, "*")) {
            return STAR;
        } else {
            throw exception("Expected a name or *");
        }
    }
    
    SpathPredicateOperator predicateOperator() {
        if (accept(SpathToken.OPERATOR, "=")) {
            return SpathPredicateOperator.EQ;
        } else if (accept(SpathToken.OPERATOR, "!=")) {
            return SpathPredicateOperator.NE;
        } else if (accept(SpathToken.OPERATOR, "<")) {
            return SpathPredicateOperator.LT;
        } else if (accept(SpathToken.OPERATOR, "<=")) {
            return SpathPredicateOperator.LE;
        } else if (accept(SpathToken.OPERATOR, ">")) {
            return SpathPredicateOperator.GT;
        } else if (accept(SpathToken.OPERATOR, ">=")) {
            return SpathPredicateOperator.GE;
        } else {
            throw exception("Predicate operator expected, e.g. = != < <= > >=");
        }
    }
    
    String quotedString() {
        if (match(SpathToken.STRING)) {
            String value = lexer.getTokenString();
            nextToken();
            return value;
        } else {
            throw exception("Expected a quoted string");
        }
    }
    
    BigDecimal decimalNumber() {
        if (match(SpathToken.DECIMAL) || match(SpathToken.NUMBER)) {
            BigDecimal value = lexer.getTokenDecimal();
            nextToken();
            return value;
        } else {
            throw exception("Expected a decimal number");
        }
    }
    
    public boolean match(SpathToken s)
    {
        SpathToken token = lexer.getTokenType();
        return (token == s);
    }

    public boolean match(SpathToken s, String value)
    {
        SpathToken token = lexer.getTokenType();
        String tokenValue;
        if (s == SpathToken.STRING || s == SpathToken.CHARACTER) {
            tokenValue = Character.toString(lexer.getTokenDelimiter());
        } else {
            tokenValue = lexer.getTokenValue();
        }
        return (token == s && (value == null || value.equals(tokenValue)));
    }
    
    public boolean accept(SpathToken s, String value)
    {
        if (match(s, value)) 
        {
            nextToken();
            return true;
        }
        return false;
    }
    
    public void expect(SpathToken s, String value, String message)
    {
        if (!accept(s, value)) {
            throw exception(message);
        }
    }

    /** Advance to the next token. */
    public SpathToken nextToken()
    {
        SpathToken token = lexer.nextToken();
        if (token == SpathToken.ERROR)
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
    public SpathQueryException exception(String message) {
        SpathQueryException ex = new SpathQueryException(message);
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
