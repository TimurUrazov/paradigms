package expression.parser;

public class BaseParser {
    public static final char END = '\0';
    private final int errorBound = 7;
    private final CharSource source;
    protected char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        nextChar();
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean expect(final char c) {
        if (ch != c) {
            return false;
        }
        nextChar();
        return true;
    }

    public String errorLogging() {
        return source.errorLogging(errorBound);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected void skipWhitespace() {
        while ((test(' ') || test('\r') || test('\n') || test('\t')) && !test('\0')) {

        }
    }
}
