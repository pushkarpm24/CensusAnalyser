package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class stateCodeAnalyserTest {
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCode.csv";
    private static final String WRONG_CSV_FILE_TYPE = "./src/test/resources/StateCodeTypeWrong.txt";
    private static final String WRONG_CSV_FILE_DELIMETER = "./src/test/resources/StateCodeDelimeterWrong.csv";
    private static final String WRONG_CSV_FILE_HEADER = "./src/test/resources/StateCodeHeaderWrong.csv";

    @Test
    public void givenIndianStateCodeCSVFile_ReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaStateData(INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(37, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndianStateCodeCSVFilePath_whenInCorrectPath_shouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaStateData(WRONG_CSV_FILE_PATH);

        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeCSVFileType_whenInCorrectType_shouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaStateData(WRONG_CSV_FILE_TYPE);

        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_FILE_TYPE, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeCSVFileDelimeter_whenInCorrectDelimeter_shouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaStateData(WRONG_CSV_FILE_DELIMETER);

        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_FILE_DELIMETER, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeCSVFileHeader_whenInCorrectHeader_shouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaStateData(WRONG_CSV_FILE_HEADER);

        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_FILE_HEADER, e.type);
        }
    }

    @Test
    public void givenIndiaStateCodeData_withRandomStateName_ShouldReturnIndiaStateNameSortedOrder() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            String stateWiseSortedStateCodeCensusData = censusAnalyser.getStateWiseSortedStateCodeCensusData(INDIA_STATE_CODE_CSV_FILE_PATH);
            System.out.println(stateWiseSortedStateCodeCensusData);
            IndiaStateCodeCSV[] censusCSV = new Gson().fromJson(stateWiseSortedStateCodeCensusData, IndiaStateCodeCSV[].class);
            Assert.assertEquals("AD",censusCSV[0].stateCode);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }
}