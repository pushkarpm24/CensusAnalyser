package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface IcsvBuilder {
    public abstract <E> Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CSVBuilderException  ;
}
