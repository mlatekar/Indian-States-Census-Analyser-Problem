package censusanalyser;

public class CensusAnalyserException extends RuntimeException {
    enum ExceptionType {
        CENSUS_FILE_PROBLEM, COUNTRY_NOT_FOUND, NO_CENSUS_DATA
        //UNABLE_TO_PASS
    }

    ExceptionType type;
    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

/*
    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
*/
}
