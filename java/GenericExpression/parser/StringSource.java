package expression.parser;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public String errorLogging(final int errorBound) {
        return "Position: " + pos + " in " + data.substring(Math.max(0, pos - errorBound), Math.min(data.length(), pos + errorBound));
    }
}
