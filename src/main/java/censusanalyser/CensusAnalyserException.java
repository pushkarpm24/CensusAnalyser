package censusanalyser;

public class CensusAnalyserException extends Exception {
    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type = ExceptionType.valueOf(name);
    }
    enum ExceptionType {
        CENSUS_FILE_PROBLEM,
        WRONG_FILE_TYPE,
        WRONG_FILE_DELIMETER,
        WRONG_FILE_HEADER,
        UNABLE_TO_PARSE
    }

    ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
