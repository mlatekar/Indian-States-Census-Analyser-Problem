package censusanalyser;

public class IndianCensusDTO {

    public  String state;
    public  int population;
    public  int areaInSqKm;
    public  int densityPerSqKm;
    public String stateCode;
    public IndianCensusDTO(IndianCensusCSV indianCensusCSV) {

         state = indianCensusCSV.state;
         population = indianCensusCSV.population;
         areaInSqKm = indianCensusCSV.areaInSqKm;
         densityPerSqKm = indianCensusCSV.densityPerSqKm;

    }
}
