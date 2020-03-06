package censusanalyser;

public class CensusDTO {

    public  String state;
    public  int population;
    public double totalArea;
    public double populationDensity;
    public String stateCode;

    public CensusDTO(IndianCensusCSV indianCensusCSV) {

         state = indianCensusCSV.state;
         population = indianCensusCSV.population;
         totalArea = indianCensusCSV.areaInSqKm;
         populationDensity = indianCensusCSV.populationDensity;
    }
    public CensusDTO(USCensusCSV usCensusCSV) {

        state = usCensusCSV.state;
        population = usCensusCSV.population;
        totalArea = usCensusCSV.totalArea;
        populationDensity = usCensusCSV.populationDensity;
        stateCode=usCensusCSV.state_id;
    }
}
