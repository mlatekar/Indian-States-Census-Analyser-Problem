package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIAN_STATE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_STATE_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";


  /*  private  static CensusAnalyser censusAnalyser;
    @BeforeClass
    public static void setUp() throws Exception {
        censusAnalyser = new CensusAnalyser();
    }*/

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void loadThe_Indian_States_Code_Information_from_csvFile() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
            //int numOfRecords = censusAnalyser.loadIndianCensusCode(INDIAN_STATE_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) {

        }
    }

    @Test
    public void givenIndiaCSVData_WhenSortedState_ShouldReturn_Sorted()  {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String stateWiseSortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
        IndianCensusCSV[] indianCensusCSV = new Gson().fromJson(stateWiseSortedCensusData, IndianCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh",indianCensusCSV[0].state);
    }
    @Test
    public void givenUSCensus_CSVFile_Returns_CorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadUSCensusData(US_STATE_CSV_FILE_PATH);
            Assert.assertEquals(51,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }
}
