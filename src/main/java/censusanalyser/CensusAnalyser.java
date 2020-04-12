package censusanalyser;

 

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<IndiaCensusCSV> csvFileList = null;
    List<IndiaStateCodeCSV> csvStateCodeFileList = null;

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        this.checkValidCSVFile(csvFilePath);
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))){

            IcsvBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            csvFileList = csvBuilder.getCSVFileList(reader,IndiaCensusCSV.class);
            return csvFileList.size();
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
        this.checkValidCSVFile(csvFilePath);
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))){

            IcsvBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            csvStateCodeFileList = csvBuilder.getCSVFileList(reader,IndiaStateCodeCSV.class);
            return csvStateCodeFileList.size();
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
        loadIndiaCensusData(csvFilePath);
        if (csvFileList == null || csvFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
        this.sortName(csvFileList, censusComparator);
        String toJson = new Gson().toJson(csvFileList);
        return toJson;
    }

    public String getStateWiseSortedStateCodeCensusData(String csvFilePath) throws CensusAnalyserException {
        loadIndiaStateData(csvFilePath);
        if (csvStateCodeFileList == null || csvStateCodeFileList.size() == 0) {
            throw new CensusAnalyserException("NO_CENSUS_DATA", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<IndiaStateCodeCSV> censusComparator = Comparator.comparing(census -> census.stateCode);
        this.sortCode(csvStateCodeFileList, censusComparator);
        String toJson = new Gson().toJson(csvStateCodeFileList);
        return toJson;
    }

    private void sortName(List<IndiaCensusCSV> csvFileList, Comparator<IndiaCensusCSV> censusComparator) {
        for (int i = 0; i < csvFileList.size(); i++) {
            for (int j = 0; j < csvFileList.size() - i - 1; j++) {
                IndiaCensusCSV census1 = csvFileList.get(j);
                IndiaCensusCSV census2 = csvFileList.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    csvFileList.set(j, census2);
                    csvFileList.set(j + 1, census1);
                }

            }

        }
    }

    private void sortCode(List<IndiaStateCodeCSV> csvStateCodeFileList, Comparator<IndiaStateCodeCSV> censusComparator) {
        for (int i = 0; i < csvStateCodeFileList.size(); i++) {
            for (int j = 0; j < csvStateCodeFileList.size() - i - 1; j++) {
                IndiaStateCodeCSV census1 = csvStateCodeFileList.get(j);
                IndiaStateCodeCSV census2 = csvStateCodeFileList.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    csvStateCodeFileList.set(j, census2);
                    csvStateCodeFileList.set(j + 1, census1);
                }

            }

        }
    }
}