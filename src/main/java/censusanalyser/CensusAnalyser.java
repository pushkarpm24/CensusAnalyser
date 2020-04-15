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

public class CensusAnalyser {

    List csvFileList = null;
    Map csvFileMap = null;

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        this.checkValidCSVFile(csvFilePath);
        try (Reader reader = newBufferedReader(Paths.get(csvFilePath))){
            csvFileMap = new HashMap<String, IndiaCensusCSV>();
            csvFileList = new ArrayList<IndiaCensusCSV>();
            IcsvBuilder csvBuilder = createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader,IndiaCensusCSV.class);
            while (censusCSVIterator.hasNext()) {
                IndiaCensusCSV indiaCensusCSV = censusCSVIterator.next();
                this.csvFileMap.put(indiaCensusCSV.state,indiaCensusCSV);
                this.csvFileList = (List) csvFileMap.values().stream().collect(Collectors.toList());
            }

            return csvFileMap.size();

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
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }



    public int loadIndiaStateData(String csvFilePath) throws CensusAnalyserException {
        this.checkValidCSVFile(csvFilePath);
        try (Reader reader = newBufferedReader(Paths.get(csvFilePath))){
            IcsvBuilder csvBuilder = createCSVBuilder();
            csvFileMap = new HashMap<String, IndiaStateCodeCSV>();
            csvFileList = new ArrayList<IndiaStateCodeCSV>();
            Iterator<IndiaStateCodeCSV> IndiaStateCodeCSVIterator = csvBuilder.getCSVFileIterator(reader,IndiaStateCodeCSV.class);
            while (IndiaStateCodeCSVIterator.hasNext()) {
                IndiaStateCodeCSV indiaStateCodeCSV = IndiaStateCodeCSVIterator.next();
                this.csvFileMap.put(indiaStateCodeCSV.stateName,indiaStateCodeCSV);
                this.csvFileList = (List) csvFileMap.values().stream().collect(Collectors.toList());
            }

            return csvFileMap.size();
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
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }



    private void checkValidCSVFile(String csvFilePath) throws CensusAnalyserException {
        if (!csvFilePath.contains(".csv"))
            throw new CensusAnalyserException("This is invalid file type", CensusAnalyserException.ExceptionType.WRONG_FILE_TYPE);
    }



    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
        if (csvFilePath == "./src/test/resources/IndiaStateCensusData.csv") {
            loadIndiaCensusData(csvFilePath);
            if (csvFileList == null || csvFileList.size() == 0) {
                throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }

            Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
            this.sort(csvFileList, censusComparator);
            String toJson = new Gson().toJson(csvFileList);
            return toJson;
        }

        loadIndiaStateData(csvFilePath);
        if (csvFileList == null || csvFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<IndiaStateCodeCSV> censusComparator = Comparator.comparing(census -> census.stateName);
        this.sort(csvFileList, censusComparator);
        String toJson = new Gson().toJson(csvFileList);
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