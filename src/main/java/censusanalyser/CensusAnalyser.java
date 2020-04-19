package censusanalyser;

 

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static censusanalyser.CSVBuilderFactory.createCSVBuilder;
import static java.nio.file.Files.newBufferedReader;
import static javafx.scene.input.KeyCode.T;

public class CensusAnalyser<E> {

    private CountryEnum.Country country;
    public CensusAnalyser(CountryEnum.Country country) {
        this.country = country;

    }

    public CensusAnalyser() {

    }

    List csvFileList =new ArrayList<CensusDAO>();
    Map<String, CensusDAO> csvFileMap = new HashMap<>();

    public int loadCensusData(CountryEnum.Country country, String  csvFilePath) throws CensusAnalyserException {
        csvFileMap = CensusAdapterFactory.getCensusData(country, csvFilePath);
        csvFileList = (List) csvFileMap.values().stream().collect(Collectors.toList());
        return csvFileMap.size();
    }
    public  String getStateNameWiseSortedCensusData(CountryEnum.Country country, String csvFilePath) throws CensusAnalyserException {
        loadCensusData(country, csvFilePath);
        if (csvFileList == null || csvFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(censusDAO -> censusDAO.getState());
        this.sort(censusComparator);
        String toJson = new Gson().toJson(csvFileList);
        return toJson;
    }


    public String  getPopulationWiseSortedCensusData(CountryEnum.Country country,String csvFilePath) throws CensusAnalyserException {

        loadCensusData(country,csvFilePath);
            if (csvFileList == null || csvFileList.size() == 0) {
                throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }

            Comparator<CensusDAO> censusComparator = Comparator.comparing(censusDAO -> censusDAO.getPopulation());
            this.sort(censusComparator);
            Collections.reverse(csvFileList);
            String toJson = new Gson().toJson(csvFileList);
            return toJson;
        }

    public  String getDensityWiseSortedCensusData(CountryEnum.Country country,String csvFilePath) throws CensusAnalyserException {
        loadCensusData(country,csvFilePath);
        if (csvFileList == null || csvFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<CensusDAO> censusComparator = Comparator.comparing(censusDAO -> censusDAO. getDensityPerSqKm());
        this.sort(censusComparator);
        String toJson = new Gson().toJson(csvFileList);
        return toJson;
    }

    public  String getAreaWiseSortedCensusData(CountryEnum.Country country,String csvFilePath) throws CensusAnalyserException {
        loadCensusData(country,csvFilePath);
        if (csvFileList == null || csvFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(censusDAO -> censusDAO.getAreaInSqKm());
        this.sort(censusComparator);
        Collections.reverse(csvFileList);
        String toJson = new Gson().toJson(csvFileList);
        return toJson;
    }

    private void sort(Comparator<CensusDAO> censusComparator){
            for (int i = 0; i < csvFileList.size(); i++) {
                for (int j = 0; j < csvFileList.size() - i - 1; j++) {
                    CensusDAO census1 = (CensusDAO) csvFileList.get(j);
                    CensusDAO census2 = (CensusDAO) csvFileList.get(j + 1);
                    if (censusComparator.compare(census1, census2) > 0) {
                        csvFileList.set(j, census2);
                        csvFileList.set(j + 1, census1);
                    }

                }

            }
        }
    public static void main(String[] args) throws CensusAnalyserException {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String stateNameWiseSortedCensusData = censusAnalyser.getStateNameWiseSortedCensusData(CountryEnum.Country.US, "src/test/resources/USCensusFile.csv");
        System.out.println(stateNameWiseSortedCensusData);
    }

}