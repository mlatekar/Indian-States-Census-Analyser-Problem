package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static CensusAnalyser censusAnalyser;
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIAN_STATE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_STATE_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";

    @BeforeClass
    public static void beforeClass() throws Exception {
         censusAnalyser = new CensusAnalyser();
    }

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndiaCSVData_WhenSortedState_ShouldReturn_Sorted()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String stateWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.STATE);
        CensusDTO[] indianCensusCSV = new Gson().fromJson(stateWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals("Andhra Pradesh",indianCensusCSV[indianCensusCSV.length-1].state);
    }
    @Test
    public void givenIndiaCSVData_WhenSortedStateId_ShouldReturn_Sorted()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String stateWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.STATEID);
        CensusDTO[] indianCensusCSV = new Gson().fromJson(stateWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals("AP",indianCensusCSV[indianCensusCSV.length-1].stateCode);
    }
    @Test
    public void givenUSCensus_CSVFile_Returns_CorrectRecords() {
        try {
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
            Assert.assertEquals(51,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }
    @Test
    public void GivenIndiaCsvData_whenSortedBy_StatePopulation__ReturnLeastOne_StatePopulation() {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String statePopulationWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.POPULATION);
        CensusDTO[] indianCensusCSV = new Gson().fromJson(statePopulationWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals(199812341,indianCensusCSV[0].population);
    }

    @Test
    public void GivenIndiaCsvData_whenSortedBy_StateDensity__ReturnLeastOne_StateDensity() {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String stateDensityWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.POPULATIONDENSITY);
        CensusDTO[] indianCensusCSV = new Gson().fromJson(stateDensityWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals(1102.0,indianCensusCSV[0].populationDensity,0.0);
    }

    @Test
    public void givenUSCSVData_WhenSortedState_ShouldReturn_SortedSate()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String stateWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.STATE);
        CensusDTO[] USCensusCSV = new Gson().fromJson(stateWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals("Wyoming",USCensusCSV[0].state);
    }

    @Test
    public void givenUSCSVData_WhenSortedBy_Population_ShouldReturn_SortedPopulation()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String populationWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.POPULATION);
        CensusDTO[] USCensusCSV = new Gson().fromJson(populationWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals(37253956,USCensusCSV[0].population);
    }



    //new
    @Test
    public void givenUSCSVData_WhenSortedBy_PopulationDensity_ShouldReturn_SortedPopulationDensity()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String populationWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.POPULATIONDENSITY);
        CensusDTO[] USCensusCSV = new Gson().fromJson(populationWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals(3805.61,USCensusCSV[0].populationDensity,0.0d);
    }
    @Test
    public void givenUSCSVData_WhenSortedBy_TotalArea_ShouldReturn_SortedTotalArea()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String populationWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.TOTALAREA);
        CensusDTO[] USCensusCSV = new Gson().fromJson(populationWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals(1723338.01,USCensusCSV[0].totalArea,0.0d);
    }
    @Test
    public void givenUSCSVData_WhenSortedBy_StateCode_ShouldReturn_SortedStateCode()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String populationWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.STATEID);
        CensusDTO[] USCensusCSV = new Gson().fromJson(populationWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals("WY",USCensusCSV[0].stateCode);
    }

    @Test
    public void givenIndiaCSVData_WhenSortedBy_TotalArea_ShouldReturn_SortedTotal()  {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH);
        String populationWiseSortedCensusData = censusAnalyser.getSortedCensusData(SortField.TOTALAREA);
        CensusDTO[] IndianCensusCSV = new Gson().fromJson(populationWiseSortedCensusData, CensusDTO[].class);
        Assert.assertEquals(342239.0,IndianCensusCSV[0].totalArea,0.00d);
    }
    @Test
    public void givenCensusData_IndiaAndUS_WhenSorted_ByMostPopulationDensity_ShouldReturn_PopulusDensity() {
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIAN_STATE_CSV_FILE_PATH,US_STATE_CSV_FILE_PATH);
        String indiaSortedData = censusAnalyser.getSortedCensusData(SortField.POPULATIONDENSITY);
        CensusDTO[] indianCensusCSV = new Gson().fromJson(indiaSortedData, CensusDTO[].class);

        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV_FILE_PATH);
        String usCensusData = censusAnalyser.getSortedCensusData(SortField.POPULATIONDENSITY);
        CensusDTO[] usCensusCSV = new Gson().fromJson(usCensusData, CensusDTO[].class);
        Assert.assertEquals(false,(String.valueOf(usCensusCSV[0].populationDensity).compareToIgnoreCase(String.valueOf(indianCensusCSV[0].populationDensity))<0));

    }

}
