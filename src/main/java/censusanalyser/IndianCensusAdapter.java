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
import java.util.stream.StreamSupport;

public class IndianCensusAdapter extends CensusAdapter {
    Map<String, CensusDTO> censusCSVMap = new HashMap<>();

    @Override
    public Map<String, CensusDTO> loadCensusData(String[] csvFilePath) {
        Map<String, CensusDTO> censusCSVMap=super.loadCensusData(IndianCensusCSV.class,csvFilePath[0]);
        this.loadIndianCensusCode(censusCSVMap,csvFilePath[1]);
        return censusCSVMap;
    }
    public Map<String, CensusDTO> loadCensusData(CensusAnalyser.Country country, String[] csvFilePath) {
        if (country.equals(CensusAnalyser.Country.INDIA)) {
            return this.loadCensusData(IndianCensusCSV.class, csvFilePath);
        } else if (country.equals(CensusAnalyser.Country.US)) {
            return this.loadCensusData(USCensusCSV.class, csvFilePath);
        }else throw new CensusAnalyserException("Incorrect  country name",
                CensusAnalyserException.ExceptionType.COUNTRY_NOT_FOUND);
    }

    public <E> Map<String, CensusDTO> loadCensusData(Class<E> csvClass, String... csvFilePath) {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
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
            if (csvFilePath.length == 1) return censusCSVMap;
            this.loadIndianCensusCode(censusCSVMap, csvFilePath[1]);
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
            Iterable<IndianCensusCode> censusCodeIterable = () -> censusCSVIterator;
            StreamSupport.stream(censusCodeIterable.spliterator(), false)
                    .filter(csvState -> censusCSVMap.get(csvState.stateName) != null)
                    .forEach(csvState -> censusCSVMap.get(csvState.stateName).stateCode = csvState.stateCode);
            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}