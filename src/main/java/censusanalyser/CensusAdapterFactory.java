package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory {
    public CensusAdapterFactory() {
    }
    public static Map<String, CensusDAO> getCensusData( CountryEnum.Country country, String csvFilePath) throws  CensusAnalyserException {
        if (country.equals( CountryEnum.Country.INDIA)) {
            return new IndiaCensusAdapter().loadCensusData(csvFilePath);
        } else if (country.equals( CountryEnum.Country.US))
            return new USCensusAdapter().loadCensusData(csvFilePath);
        else
            throw new  CensusAnalyserException( "You Have Entered Invalid country", CensusAnalyserException. ExceptionType.INVALID_COUNTRY);
    }
}
