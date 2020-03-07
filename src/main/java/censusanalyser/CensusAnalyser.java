package censusanalyser;

import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

   List<CensusDTO> censusCSVList=null;
    Map<String, CensusDTO> censusCSVMap=null;
    Map<SortField, Comparator<CensusDTO>> sortMap=null;
    public enum Country {INDIA,US};

   public CensusAnalyser(){
        this.sortMap=new HashedMap();
        this.sortMap.put(SortField.STATE,Comparator.comparing(census -> census.state));
        this.sortMap.put(SortField.POPULATION,Comparator.comparing(census -> census.population));
        this.sortMap.put(SortField.POPULATIONDENSITY,Comparator.comparing(census -> census.populationDensity));
        this.sortMap.put(SortField.TOTALAREA,Comparator.comparing(census -> census.totalArea));
        this.sortMap.put(SortField.STATEID,Comparator.comparing(census -> census.stateCode));
    }

    /*public CensusAnalyser(){

        this.censusCSVMap=new HashMap<>();
    }*/
    //public enum Country{INDIA,US;}
    public int loadCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {
        censusCSVMap=CensusAdapterFactory.getCensusData(country,csvFilePath);
        return censusCSVMap.size();
    }

    // "loadIndianCensusCode" method paste to the "censusLoader" class and in test case pass multiple file and create the local variable to it

    public String getSortedCensusData(SortField sortField) throws CensusAnalyserException {
        if(censusCSVMap ==null || censusCSVMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

       // Comparator<CensusDTO> censusCodeComparator=Comparator.comparing(census -> census.state);
        censusCSVList =censusCSVMap.values().stream().collect(Collectors.toList());

        this.sort(censusCSVList,this.sortMap.get(sortField).reversed());
        String sortedStateCensus=new Gson().toJson(censusCSVList);
        return sortedStateCensus;
    }
     private  void sort(List<CensusDTO> censusDTOList,Comparator <CensusDTO> censusCSVComparator) {
        for (int i=0; i<censusCSVList.size()-1; i++){
            for (int j=0; j<censusCSVList.size()-1; j++){
                CensusDTO census1=censusCSVList.get(j);
                CensusDTO census2=censusCSVList.get(j+1);
                if (censusCSVComparator.compare(census1,census2)>0) {
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }
    }
}