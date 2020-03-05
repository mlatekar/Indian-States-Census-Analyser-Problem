package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusLoader {
    Map<String, CensusDTO> censusCSVMap=new HashMap<>();
    public<E> Map<String,CensusDTO> loadCensusData(Class<E> csvClass, String... csvFilePath) {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            // new csvbuilderfactory();  create class add then createcsvbuilder(); create local variable
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusCSVIterator = csvBuilder.getCSVIterator(reader, csvClass);
            Iterable<E> censusCodeIterable=() -> censusCSVIterator;
            if(csvClass.getName().equals("censusanalyser.IndianCensusCSV")) {
                StreamSupport.stream(censusCodeIterable.spliterator(), false)
                        .map(IndianCensusCSV.class::cast)
                        .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDTO(censusCSV)));
            }
            else if(csvClass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(censusCodeIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDTO(censusCSV)));
            }
            if (csvFilePath.length==1)return censusCSVMap;
            this.loadIndianCensusCode(censusCSVMap,csvFilePath[1]);
            return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
    private int loadIndianCensusCode(Map<String, CensusDTO> censusCSVMap, String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianCensusCode> censusCSVIterator = csvBuilder.getCSVIterator(reader, IndianCensusCode.class);
            Iterable<IndianCensusCode> censusCodeIterable=() -> censusCSVIterator;
            StreamSupport.stream(censusCodeIterable.spliterator(),false)
                    .filter(csvState -> censusCSVMap.get(csvState.stateName)!=null)
                    .forEach(csvState -> censusCSVMap.get(csvState.stateName).stateCode=csvState.stateName);
            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }


}
