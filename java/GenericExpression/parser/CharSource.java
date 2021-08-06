package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    String errorLogging(final int errorBound);
}
