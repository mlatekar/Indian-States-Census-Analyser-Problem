package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class CensusAdapter {

    public abstract Map<String, CensusDTO> loadCensusData(String... csvFilePath);

    public <E> Map<String, CensusDTO> loadCensusData(Class<E> csvClass, String csvFilePath) {

        Map<String, CensusDTO> censusCSVMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            // new csvbuilderfactory();  create class add then createcsvbuilder(); create local variable
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusCSVIterator = csvBuilder.getCSVIterator(reader, csvClass);
            Iterable<E> censusCodeIterable = () -> censusCSVIterator;
            if (csvClass.getName().equals("censusanalyser.IndianCensusCSV")) {
                StreamSupport.stream(censusCodeIterable.spliterator(), false)
                        .map(IndianCensusCSV.class::cast)
                        .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDTO(censusCSV)));
            } else if (csvClass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(censusCodeIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusCSVMap.put(censusCSV.state, new CensusDTO(censusCSV)));
            }

            return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}