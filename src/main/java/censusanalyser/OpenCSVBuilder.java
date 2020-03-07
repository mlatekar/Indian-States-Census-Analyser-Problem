package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder<E> implements ICSVBuilder{
    @Override
    public <E>Iterator<E> getCSVIterator(Reader reader, Class<E> csvClass){
       return this.getCsvBean(reader,csvClass).iterator();
    }

    @Override
    public <E> List getCSVFileList(Reader reader, Class<E> csvClass) {
       return this.getCsvBean(reader,csvClass).parse();
    }

    private <E> CsvToBean getCsvBean(Reader reader,Class<E> csvClass){
       try {
           CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
           csvToBeanBuilder.withType(csvClass);
           csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
           CsvToBean<E> csvToBean = csvToBeanBuilder.build();
           return csvToBean;
       }
       catch (IllegalStateException e){
           throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
       }
    }

}
