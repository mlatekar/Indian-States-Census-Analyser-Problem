package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

   List<CensusDTO> censusCSVList=null;
    Map<String, CensusDTO> censusCSVMap=null;
    public CensusAnalyser(){

        this.censusCSVMap=new HashMap<>();
    }
    public enum Country{INDIA,US;}
    public int loadCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {
        censusCSVMap=new CensusLoader().loadCensusData(country,csvFilePath);
        return censusCSVMap.size();
    }

    // "loadIndianCensusCode" method paste to the "censusLoader" class and in test case pass multiple file and create the local variable to it

     public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if(censusCSVMap ==null || censusCSVMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<CensusDTO> censusCodeComparator=Comparator.comparing(census -> census.state);
        censusCSVList =censusCSVMap.values().stream().collect(Collectors.toList());

        this.sort(censusCodeComparator);
        String sortedStateCensus=new Gson().toJson(censusCSVList);
        return sortedStateCensus;
    }
    private  void sort(Comparator <CensusDTO> censusCSVComparator) {
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