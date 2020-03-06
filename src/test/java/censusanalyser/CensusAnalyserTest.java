package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
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
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndiaCSVData_WhenSortedState_ShouldReturn_Sorted()  {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String stateWiseSortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
        IndianCensusCSV[] indianCensusCSV = new Gson().fromJson(stateWiseSortedCensusData, IndianCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh",indianCensusCSV[0].state);
    }
    @Test
    public void givenUSCensus_CSVFile_Returns_CorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
            Assert.assertEquals(51,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }
    @Test
    public void GivenIndiaCsvData_whenSortedBy_StatePopulation__ReturnLeastOne_StatePopulation() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String statePopulationWiseSortedCensusData = censusAnalyser.getStatePopulationWiseSortedCensusData();
        IndianCensusCSV[] indianCensusCSV = new Gson().fromJson(statePopulationWiseSortedCensusData, IndianCensusCSV[].class);
        Assert.assertEquals(607688,indianCensusCSV[0].population);
    }
    @Test
    public void GivenIndiaCsvData_whenSortedBy_StateDensity__ReturnLeastOne_StateDensity() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String stateDensityWiseSortedCensusData = censusAnalyser.getStateDensityWiseSortedCensusData();
        IndianCensusCSV[] indianCensusCSV = new Gson().fromJson(stateDensityWiseSortedCensusData, IndianCensusCSV[].class);
        Assert.assertEquals(50,indianCensusCSV[0].populationDensity,0.0);
    }

    @Test
    public void givenUSCSVData_WhenSortedState_ShouldReturn_SortedSate()  {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String stateWiseSortedCensusData = censusAnalyser.getUSStateWiseSortedCensusData();
        USCensusCSV[] USCensusCSV = new Gson().fromJson(stateWiseSortedCensusData, USCensusCSV[].class);
        Assert.assertEquals("Alabama",USCensusCSV[0].state);
    }
    @Test
    public void givenUSCSVData_WhenSortedBy_Population_ShouldReturn_SortedPopulation()  {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String populationWiseSortedCensusData = censusAnalyser.getUSPopulationWiseSortedCensusData();
        USCensusCSV[] USCensusCSV = new Gson().fromJson(populationWiseSortedCensusData, USCensusCSV[].class);
        Assert.assertEquals(563626,USCensusCSV[0].population);
    }
}
