package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory {

    public static Map<String, CensusDTO> getCensusData(CensusAnalyser.Country country, String[] csvFilePath) {
        if (country.equals(CensusAnalyser.Country.INDIA)) {
            return new IndianCensusAdapter().loadCensusData(csvFilePath);
        }
        if (country.equals(CensusAnalyser.Country.US)) {
            return new USCensusAdapter().loadCensusData(csvFilePath);
        }
        throw new CensusAnalyserException("Incorrect  country name",
                CensusAnalyserException.ExceptionType.COUNTRY_NOT_FOUND);
    }
}
