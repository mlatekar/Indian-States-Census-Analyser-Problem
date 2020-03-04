package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<IndianCensusCSV> censusCSVList=null;
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        CsvToBean<IndianCensusCSV> csvToBean;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            // new csvbuilderfactory();  create class add then createcsvbuilder(); create local variable
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            censusCSVList = csvBuilder.getCSVFileList(reader, IndianCensusCSV.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianCensusCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianCensusCode> censusCSVIterator = csvBuilder.getCSVIterator(reader, IndianCensusCode.class);
            return this.getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    private <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return namOfEateries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
       if(censusCSVList ==null || censusCSVList.size() == 0) {
           throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
       }
            Comparator<IndianCensusCSV> censusCodeComparator=Comparator.comparing(census -> census.state);
            this.sort(censusCSVList,censusCodeComparator);
            String sortedStateCensus=new Gson().toJson(censusCSVList);
            return sortedStateCensus;

    }
    private  void sort(List<IndianCensusCSV> censusCSVList,Comparator <IndianCensusCSV> censusCSVComparator){
        for (int i=0; i<censusCSVList.size()-1; i++){
            for (int j=0; j<censusCSVList.size()-1; j++){
                IndianCensusCSV census1=censusCSVList.get(j);
                IndianCensusCSV census2=censusCSVList.get(j+1);
                if (censusCSVComparator.compare(census1,census2)>0){
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }
    }
}