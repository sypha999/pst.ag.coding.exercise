package src.interfaces;

import src.models.Car;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for parsing car data from given CSV and XML file paths.
 */
public interface CarParser {

    /**
     * Parses car data from the specified CSV and XML files.
     *
     * @param csvPath path to the CSV file containing car data
     * @param xmlPath path to the XML file containing car data
     * @return list of parsed Car objects
     * @throws Exception if any parsing error occurs
     */
    List<Car> build(Path csvPath, Path xmlPath) throws Exception;
}
