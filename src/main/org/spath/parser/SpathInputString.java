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


public class SpathInputString
{
    /**
     * End of input character. (Used internally).
     */
    public static final char NULL       = '\0';

    /**
     * End of line character.
     */
    public static final char EOLN       = '\n';

    /** The line of input. */
    private String line;

    /** The start of the current token */
    private int start = 0;

    /** Just past the end of the current token. */
    private int finish = 0;

    /**
     * Constructor for String Tokeniser.
     *
     */
    public SpathInputString()
    {
    }

    /**
     * Constructor for Strong Tokeniser.
     *
     * @param input
     *            the string to be tokenised.
     */
    public SpathInputString(String input)
    {
        setLine(input);
    }

    public String getLine()
    {
        return line;
    }

    public void setLine(String input)
    {
        this.line = input;
        start = 0;
        finish = 0;
    }

    public int getTokenFinish()
    {
        return finish;
    }

    public int getTokenStart()
    {
        return start;
    }

    /**
     * Get the character at the start of the current token.
     *
     * @return a character or NULL if past the end of input.
     */
    public char startChar()
    {
        start = finish;
        return getChar();
    }

    /**
     * Step over the current character.
     */
    int spanCharacter(int state)
    {
        startChar(); // Mark start of token
        incrementFinish(); // Step over character
        return state;
    }

    /**
     * Get the character at the end of the current token.
     *
     * @return a character or NULL if past the end of input.
     */
    public char nextChar()
    {
        incrementFinish();
        return getChar();
    }

    protected void incrementFinish()
    {
        if (finish < line.length()) {
            ++finish;
        }
    }

    public char getChar()
    {
        if (line == null)
            return NULL;
        else if (finish < line.length())
            return line.charAt(finish);
        else
            return eoln();
    }
    
    protected char eoln() {
        return NULL;
    }

    public String getTokenString()
    {
        // Don't read beyond the end of the string.
        return (line != null) ? line.substring(start, finish) : null;
    }

    public String peek(int lookahead) {
        if (line== null)
            return null;
        int offset = finish + lookahead;
        if (offset >= line.length())
            return line.substring(finish);
        else
            return line.substring(finish, offset);
    }
    
    public static String encode(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return "'" + ch + "'";
        } else {
            switch(ch) {
            case '\n' : return "\\n";
            case '\r' : return "\\r";
            case '\t' : return "\\t";
            }
            return "\\" + Integer.toOctalString(ch);
        }
    }

    public static String encode(String str) {
        if (str != null && str.length() > 0) {
            StringBuffer buf = null;
            int len = str.length();
            for (int index=0; index<len; index++) {
                char ch = str.charAt(index);
                if (buf != null) {
                    buf.append(Character.isISOControl(ch) ? encode(ch) : ch);
                } else if (Character.isISOControl(ch)) {
                    buf = new StringBuffer(str.substring(0, index));
                    buf.append(encode(ch));
                }
            }
            if (buf != null) {
                return buf.toString();
            }
        }
        return str;
    }
    
    public String toDollarString()
    {
        if (line == null)
            return "$$";
        StringBuffer buf = new StringBuffer();
        buf.append(encode(line.substring(0, start)));
        buf.append('$');
        buf.append(encode(line.substring(start, finish)));
        buf.append('$');
        buf.append(encode(line.substring(finish, line.length())));
        return buf.toString();
    }

    public String toString()
    {
        return toDollarString();
    }
}
