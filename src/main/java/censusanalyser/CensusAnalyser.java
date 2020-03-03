package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    Iterator<IndiaCensusCSV> censusCSVIterator=null;
        public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        CsvToBean<IndiaCensusCSV> csvToBean;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            CsvToBeanBuilder<IndiaCensusCSV> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(IndiaCensusCSV.class);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            csvToBean = csvToBeanBuilder.build();
           censusCSVIterator = csvToBean.iterator();
            Iterable<IndiaCensusCSV> csvIterable = () -> censusCSVIterator;
            int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
            return namOfEateries;

          } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}



  /* Iterator<IndiaCensusCSV> censusCSVIterator = csvToBean.iterator();
            int namOfEateries=0;
            Iterable<IndiaCensusCSV> csvIterable =() -> censusCSVIterator;
            int namOfEateries = (int) StreamSupport.stream(censusCSVIterator.spliterator(),false).count();
           while (censusCSVIterator.hasNext()) {
                namOfEateries++;
                IndiaCensusCSV censusData = censusCSVIterator.next();
            }
            return namOfEateries;*/