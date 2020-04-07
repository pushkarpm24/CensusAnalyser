package censusanalyser;

public class CSVBuilderFactory {
    public static IcsvBuilder createCSVBuilder() {
        return new OpenCSVBuilder();
    }
}
