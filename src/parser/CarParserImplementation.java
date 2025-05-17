package src.parser;

import src.interfaces.CarParser;
import src.models.Car;
import src.util.CsvReader;
import src.util.DateFormatter;
import src.util.XmlParser;

import java.nio.file.Path;
import java.util.List;

public class CarParserImplementation implements CarParser {

    private static final XmlParser xmlParser = new XmlParser();

    @Override
    public List<Car> build(Path csvPath, Path xmlPath) throws Exception {
        // Parse cars from XML
        List<Car> cars = xmlParser.parse(xmlPath);
        // Read CSV data
        List<List<String>> csvInfo = CsvReader.read(csvPath);

        for (int i = 0; i < cars.size() && i < csvInfo.size(); i++) {
            Car car = cars.get(i);
            String[] info = csvInfo.get(i).get(0).split(",");
            if (info.length >= 2) {
                car.setBrand(info[0]);
                car.setReleaseDate(DateFormatter.formatDate(info[1]));
            }
            car.setDefaultCurrency("USD");
        }
        return cars;
    }
}
