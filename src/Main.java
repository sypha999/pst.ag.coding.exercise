package src;

import src.interfaces.CarParser;
import src.models.Car;
import src.models.OutputFormat;
import src.parser.CarParserImplementation;
import src.util.FilterUtil;
import src.util.SortUtil;
import src.util.PrintUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static src.util.PrintUtil.printCars;

/**
 * Main application entry point for Car Inventory Management.
 * Provides interactive console menu for viewing, filtering, and sorting car data.
 */
public class Main {

    private static final String CSV_PATH = "CarsBrand.csv";
    private static final String XML_PATH = "carsType.xml";

    private static OutputFormat outputFormat = OutputFormat.TABLE;

    public static void main(String[] args) throws Exception {
        CarParser carParser = new CarParserImplementation();
        List<Car> allCars = carParser.build(Path.of(CSV_PATH), Path.of(XML_PATH));
        Scanner scanner = new Scanner(System.in);

        PrintUtil.printHeader("By default, view is set to Tabular format");
        printCars(allCars, outputFormat);

        while (true) {
            PrintUtil.printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> changeViewFormat(scanner);
                case "2" -> FilterUtil.filterInventory(scanner, allCars, outputFormat);
                case "3" -> SortUtil.sortInventory(scanner, allCars, outputFormat);
                case "4" -> printCars(allCars, outputFormat);
                case "5" -> {
                    PrintUtil.println("Exiting program. Goodbye!");
                    return;
                }
                default -> PrintUtil.printError("Invalid input, valid inputs are: 1, 2, 3, 4 or 5");
            }

            Thread.sleep(2000);
            PrintUtil.println("");
        }
    }

    /**
     * Allows user to select the output view format.
     * Repeats until valid input is received.
     */
    private static void changeViewFormat(Scanner scanner) {
        while (true) {
            PrintUtil.println("Select view format: Table | Json | Xml");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "table" -> {
                    outputFormat = OutputFormat.TABLE;
                    PrintUtil.printSuccess("View format updated to Tabular");
                    return;
                }
                case "json" -> {
                    outputFormat = OutputFormat.JSON;
                    PrintUtil.printSuccess("View format updated to JSON");
                    return;
                }
                case "xml" -> {
                    outputFormat = OutputFormat.XML;
                    PrintUtil.printSuccess("View format updated to XML");
                    return;
                }
                default -> PrintUtil.printError("Invalid input, valid inputs are: Table, Json, Xml");
            }
        }
    }

}
