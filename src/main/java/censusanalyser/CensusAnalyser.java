package censusanalyser;

 

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.nio.file.Files.newBufferedReader;
import static javafx.scene.input.KeyCode.T;

public class CensusAnalyser {

    List<IndiaCensusCSV> csvStateCensusFileList = null;
    List<IndiaStateCodeCSV> csvStateCodeFileList = null;

    Map<String,IndiaCensusCSV> csvStateCensusMap = new HashMap<>();
    Map<String,IndiaStateCodeCSV> csvStateCodeMap = new HashMap<>();

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = newBufferedReader(Paths.get(csvFilePath))){

            IcsvBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader,IndiaCensusCSV.class);
            while (censusCSVIterator.hasNext()) {
                IndiaCensusCSV value = censusCSVIterator.next();
                this.csvStateCensusMap.put(value.state,value);
                csvStateCensusFileList = csvStateCensusMap.values().stream().collect(Collectors.toList());
            }
            int totalRecords = csvStateCensusMap.size();
            return totalRecords;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("header!"))
                throw new CensusAnalyserException(e.getMessage(),
                        CensusAnalyserException.ExceptionType.WRONG_FILE_HEADER);
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.WRONG_FILE_DELIMETER);
        }  catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }



    public int loadIndiaStateData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = newBufferedReader(Paths.get(csvFilePath))){
            IcsvBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> IndiaStateCodeCSVIterator = csvBuilder.getCSVFileIterator(reader,IndiaStateCodeCSV.class);
            while (IndiaStateCodeCSVIterator.hasNext()) {
                IndiaStateCodeCSV value = IndiaStateCodeCSVIterator.next();
                this.csvStateCodeMap.put(value.stateName,value);
                csvStateCodeFileList = csvStateCodeMap.values().stream().collect(Collectors.toList());
            }
            int totalRecords = csvStateCodeMap.size();
            return totalRecords;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (RuntimeException e) {
            if (e.getMessage().contains("header!"))
                throw new CensusAnalyserException(e.getMessage(),
                        CensusAnalyserException.ExceptionType.WRONG_FILE_HEADER);
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.WRONG_FILE_DELIMETER);
        }
        catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }



    private void checkValidCSVFile(String csvFilePath) throws CensusAnalyserException {
        if (!csvFilePath.contains(".csv"))
            throw new CensusAnalyserException("This is invalid file type", CensusAnalyserException.ExceptionType.WRONG_FILE_TYPE);
    }

    private <E> int getCount(Iterator<E> iterator) {
               Iterable<E> iterable = () -> iterator;
        int numOfEntries = (int) StreamSupport.stream(iterable.spliterator(), false).count();

        return numOfEntries;
    }

    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
        if (csvFilePath == "./src/test/resources/IndiaStateCensusData.csv") {
            loadIndiaCensusData(csvFilePath);
            if (csvStateCensusFileList == null || csvStateCensusFileList.size() == 0) {
                throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }

            Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
            this.sort(csvStateCensusFileList, censusComparator);
            String toJson = new Gson().toJson(csvStateCensusFileList);
            return toJson;
        }

        loadIndiaStateData(csvFilePath);
        if (csvStateCodeFileList == null || csvStateCodeFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<IndiaStateCodeCSV> censusComparator = Comparator.comparing(census -> census.stateName);
        this.sort(csvStateCodeFileList, censusComparator);
        String toJson = new Gson().toJson(csvStateCodeFileList);
        return toJson;
    }

        private < T > void sort (List < T > csvFileList, Comparator < T > censusComparator){
            for (int i = 0; i < csvFileList.size(); i++) {
                for (int j = 0; j < csvFileList.size() - i - 1; j++) {
                    T census1 = csvFileList.get(j);
                    T census2 = csvFileList.get(j + 1);
                    if (censusComparator.compare(census1, census2) > 0) {
                        csvFileList.set(j, census2);
                        csvFileList.set(j + 1, census1);
                    }

                }

            }
        }


}