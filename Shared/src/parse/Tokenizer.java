package parse;

import static parse.TokenType.ARR;
import static parse.TokenType.ASSIGN;
import static parse.TokenType.DIV;
import static parse.TokenType.EQ;
import static parse.TokenType.GE;
import static parse.TokenType.GT;
import static parse.TokenType.LBRACE;
import static parse.TokenType.LBRACKET;
import static parse.TokenType.LE;
import static parse.TokenType.LPAREN;
import static parse.TokenType.LT;
import static parse.TokenType.MINUS;
import static parse.TokenType.MUL;
import static parse.TokenType.NE;
import static parse.TokenType.PLUS;
import static parse.TokenType.RBRACE;
import static parse.TokenType.RBRACKET;
import static parse.TokenType.RPAREN;
import static parse.TokenType.SEMICOLON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import parse.Token.EOFToken;
import parse.Token.ErrorToken;
import parse.Token.NumToken;

/**
 * A Tokenizer turns a Reader into a stream of tokens that can be iterated over
 * using a {@code for} loop.
 */
class Tokenizer implements Iterator<Token> {

    private final BufferedReader br;
    private final StringBuilder buf;
    private int lineNo;
    /**
     * {@code tokenReady} is {@code false} if a token is not immediately
     * available to be returned from {@code next()}, and {@code true} if a token
     * is immediately ready to be returned from {@code next()}.
     */
    private boolean tokenReady = false;
    private Token curTok =
            new ErrorToken("Tokenizer has not yet begun reading", -1);
    private boolean atEOF = false;

    /**
     * Create a Tokenizer that reads from the specified reader
     * 
     * @param r
     *            The source from which the Tokenizer lexes input into Tokens
     */
    Tokenizer(Reader r) {
        br = new BufferedReader(r);
        buf = new StringBuilder();
        lineNo = 1;
    }

    /**
     * Returns {@code true} if the iteration has more meaningful elements. (In
     * other words, returns {@code true} if {@link #next} would return a non-EOF
     * element rather than throwing an exception or returning EOF.)
     *
     * @return {@code true} if the iteration has more meaningful elements
     */
    @Override
    public boolean hasNext() {
        if (!tokenReady) {
            try {
                lexOneToken();
            }
            catch (IOException e) {
                throw new TokenizerIOException(e);
            }
            catch (EOFException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Token next() throws TokenizerIOException {
        Token tok = peek();
        tokenReady = false;
        return tok;
    }

    /**
     * Return the next token in the program without consuming the token.
     * 
     * @return the next token, without consuming it
     * @throws IOException
     *             if an IOException was thrown while trying to read from the
     *             source Reader
     * @throws EOFException
     *             if EOF was encountered while trying to lex the next token
     */
    Token peek() throws TokenizerIOException {
        if (!tokenReady && !atEOF) try {
            lexOneToken();
        }
        catch (IOException e) {
            throw new TokenizerIOException(e);
        }
        catch (EOFException e) {
            // EOFException is thrown by encounteredEOF(), which should set
            // curTok to an EOFToken
        }
        return curTok;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Close the reader opened by this tokenizer.
     */
    void close() {
        try {
            br.close();
        }
        catch (IOException e) {
            System.out.println("IOException:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read one token from the reader. One token is always produced if the end
     * of file is not encountered, but that token may be an error token.
     * 
     * @throws IOException
     *             if an IOException was thrown when trying to read from the
     *             source Reader
     * @throws EOFException
     *             if EOF is encountered and a token cannot be produced.
     */
    private void lexOneToken() throws IOException, EOFException {
        setBufToFirstMeaningfulChar();
        char c = buf.charAt(0);

        switch (c) {
        case '[':
            setNextTokenAndReset(LBRACKET);
            break;
        case ']':
            setNextTokenAndReset(RBRACKET);
            break;
        case '(':
            setNextTokenAndReset(LPAREN);
            break;
        case ')':
            setNextTokenAndReset(RPAREN);
            break;
        case '{':
            setNextTokenAndReset(LBRACE);
            break;
        case '}':
            setNextTokenAndReset(RBRACE);
            break;
        case ';':
            setNextTokenAndReset(SEMICOLON);
            break;
        case '=':
            setNextTokenAndReset(EQ);
            break;
        case '+':
            setNextTokenAndReset(PLUS);
            break;
        case '*':
            setNextTokenAndReset(MUL);
            break;
        case '/':
            lexSlash();
            break;
        case '<':
            lexLAngle();
            break;
        case '>':
            lexRAngle();
            break;
        case '-':
            lexDash();
            break;
        case ':':
            if (consume('=')) setNextTokenAndReset(ASSIGN);
            break;
        case '!':
            if (consume('=')) setNextTokenAndReset(NE);
            break;
        default:
            if (Character.isLetter(c))
                lexIdentifier();
            else if (Character.isDigit(c))
                lexNum();
            else unexpected();
        }
    }

    /**
     * Consumes whitespace up until the first non-whitespace character, and sets
     * the buffer to that character
     * 
     * @throws IOException
     *             if an IOException is encountered while reading from the
     *             source Reader
     */
    private void setBufToFirstMeaningfulChar() throws IOException, EOFException {
        // Make sure there isn't any leftover from a previous lexing operation
        assert buf.length() <= 1;
        char c = buf.length() == 1 ? c = buf.charAt(0) : getNextCharAndAppend();

        // consume whitespaces
        while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            if (c == '\n') lineNo++;
            c = getNextCharAndAppend();
        }

        resetBufferWith(c);
    }

    private void lexLAngle() throws IOException, EOFException {
        int c = nextChar(false);
        if (c == -1)
            setNextTokenAndReset(LT);
        else {
            char cc = (char) c;
            buf.append(cc);
            if (cc == '=')
                setNextTokenAndReset(LE);
            else setNextTokenAndResetWith(LT, cc);
        }
    }

    private void lexRAngle() throws IOException, EOFException {
        int c = nextChar(false);
        if (c == -1)
            setNextTokenAndReset(GT);
        else {
            char cc = (char) c;
            buf.append(cc);
            if (cc == '=')
                setNextTokenAndReset(GE);
            else setNextTokenAndResetWith(GT, cc);
        }
    }

    private void lexDash() throws IOException, EOFException {
        final int c = nextChar(false);
        if (c == -1)
            setNextTokenAndReset(MINUS);
        else {
            final char cc = (char) c;
            buf.append(cc);
            if (cc == '-') {
                if (consume('>'))
                    setNextTokenAndReset(ARR);
            } else
                setNextTokenAndResetWith(MINUS, cc);
        }
    }
    
    /**Improved to deal with comments */
    private void lexSlash() throws IOException, EOFException {
    	int cc = nextChar(false);
    	if (cc == 32) {
    		setNextTokenAndReset(DIV);
    	}
    	//else is a comment
    	else { 
    		assert buf.length() <= 1;
            char c = buf.length() == 1 ? c = buf.charAt(0) : getNextCharAndAppend();

            // consume whitespaces
            while (c != '\n') {
                c = getNextCharAndAppend();
            }
            c = getNextCharAndAppend();
            lineNo++;
            resetBufferWith(c);
    	}
    }

    private void lexIdentifier() throws IOException, EOFException {
        int c;
        for (c = nextChar(false); c != -1 && Character.isLetter(c); c =
                nextChar(false))
            buf.append((char) c);

        String id = buf.toString();
        TokenType tt = TokenType.getTypeFromString(id);
        if (tt != null) {
            setNextTokenAndReset(tt);
        }
        else {
            unexpected();
        }

        if (c != -1) buf.append((char) c);
    }

    private void lexNum() throws IOException, EOFException {
        int c;
        for (c = nextChar(false); c != -1 && Character.isDigit(c); c =
                nextChar(false))
            buf.append((char) c);

        try {
            String num = buf.toString();
            int val = Integer.parseInt(num);
            curTok = new NumToken(val, lineNo);
            tokenReady = true;
            buf.setLength(0);
            if (c != -1) buf.append((char) c);
        }
        catch (NumberFormatException e) {
            unexpected();
        }
    }

    /**
     * Read the next character from the reader, treating EOF as an error. If
     * successful, append the character to the buffer.
     * 
     * @return The next character
     * @throws IOException
     *             if an IOException was thrown when trying to read the next
     *             char
     * @throws EOFException
     *             if EOF is encountered
     */
    private char getNextCharAndAppend() throws IOException, EOFException {
        char c = (char) nextChar(true);
        buf.append(c);
        return c;
    }

    /**
     * Read the next character from the reader. If isEOFerror, treat EOF as an
     * error. If successful, append the character to the buffer.
     * 
     * @param exceptionOnEOF
     * @return The integer representation of the next character
     * @throws IOException
     *             if an {@code IOException} is thrown when trying to read from
     *             the source Reader
     * @throws EOFException
     *             if EOF is encountered and isEOFerror is true
     */
    private int nextChar(boolean exceptionOnEOF) throws IOException,
            EOFException {
        int c = br.read();
        if (exceptionOnEOF && c == -1) encounteredEOF();
        return c;
    }

    private void setNextTokenAndReset(TokenType tokenType) {
        curTok = new Token(tokenType, lineNo);
        tokenReady = true;
        buf.setLength(0);
    }

    private void setNextTokenAndResetWith(TokenType tokenType, char c) {
        setNextTokenAndReset(tokenType);
        buf.append(c);
    }

    private void resetBufferWith(char c) {
        buf.setLength(0);
        buf.append(c);
    }

    /**
     * Read the next character and determine whether it is the expected
     * character. If not, the current buffer is an error
     * 
     * @param expected
     *            The expected next character
     * @return true if the next character is as expected
     * @throws IOException
     *             if an IOException was thrown when trying to read from the
     *             source Reader
     * @throws EOFException
     *             if EOF is encountered
     */
    private boolean consume(char expected) throws IOException, EOFException {
        int c = getNextCharAndAppend();
        if (c != expected) {
            unexpected();
            return false;
        }
        return true;
    }

    /**
     * Make the current token an error token with the current contents of the
     * buffer
     */
    private void unexpected() {
        curTok = new ErrorToken(buf.toString(), lineNo);
        tokenReady = true;
        buf.setLength(0);
    }

    /**
     * Make the contents of the current buffer into an EOFToken, clearing the
     * buffer in the process, set atEOF to true, and set the current token to
     * the newly generated EOFToken, setting tokenReady in the process
     */
    private void encounteredEOF() throws EOFException {
        curTok = new EOFToken(buf.toString(), lineNo);
        buf.setLength(0);
        atEOF = true;
        tokenReady = true;
        throw new EOFException();
    }

    /**
     * "Helper" exception to indicate that EOF was reached
     */
    static class EOFException extends Exception {
        private static final long serialVersionUID = -7333947165525391472L;
    }

    static class TokenizerIOException extends RuntimeException {
        private static final long serialVersionUID = 8014027094822746940L;

        TokenizerIOException(Throwable cause) {
            super(cause);
        }
    }
}
