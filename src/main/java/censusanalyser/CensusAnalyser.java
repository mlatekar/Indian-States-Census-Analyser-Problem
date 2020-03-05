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
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadCensusData(csvFilePath,IndianCensusCSV.class);
    }

    private <E> int loadCensusData(String csvFilePath, Class<E> csvClass) {
        // CsvToBean<IndianCensusCSV> csvToBean;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
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

           /* while (censusCSVIterator.hasNext()){
                IndianCensusCSV indianCensusCSV= censusCSVIterator.next();
                this.censusCSVMap.put(indianCensusCSV.state, new CensusDTO(indianCensusCSV));
            }*/

            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianCensusCode(String csvFilePath) throws CensusAnalyserException {
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

    public int loadUSCensusData(String csvFilePath) {
        return loadCensusData(csvFilePath,USCensusCSV.class);

       /* try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<USCensusCSV> censusCSVIterator = csvBuilder.getCSVIterator(reader, USCensusCSV.class);
            while (censusCSVIterator.hasNext()){
                    USCensusCSV censusCSV= censusCSVIterator.next();
                    this.censusCSVMap.put(censusCSV.state, new CensusDTO(censusCSV));
            }
            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }*/
    }
    private <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return namOfEateries;
    }

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
    private  void sort(Comparator <CensusDTO> censusCSVComparator){
        for (int i=0; i<censusCSVList.size()-1; i++){
            for (int j=0; j<censusCSVList.size()-1; j++){
                CensusDTO census1=censusCSVList.get(j);
                CensusDTO census2=censusCSVList.get(j+1);
                if (censusCSVComparator.compare(census1,census2)>0){
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }
    }
}