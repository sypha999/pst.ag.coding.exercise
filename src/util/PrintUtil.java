package src.util;

import src.models.Car;
import src.models.OutputFormat;

import java.util.List;

public final class PrintUtil {

    private static final ViewFormatter formatter = new ViewFormatter();

    private PrintUtil() {
    }

    public static void printFilterResult(List<Car> cars, OutputFormat format) {
        if (cars.isEmpty()) {
            printError("No cars found for specified filter.");
        } else {
            printCars(cars, format);
        }
    }

    public static void printCars(List<Car> cars, OutputFormat format) {
        switch (format) {
            case TABLE -> formatter.printTable(cars);
            case JSON -> System.out.println(formatter.toJSON(cars));
            case XML -> System.out.println(formatter.toXML(cars));
            default -> printError("Invalid output format");
        }
        println("");
    }

    public static void printCarsWithDefaultCurrency(List<Car> cars, OutputFormat format) {
        switch (format) {
            case TABLE -> formatter.printTableDefaultCurrency(cars);
            case JSON -> System.out.println(formatter.toJSONDefaultCurrency(cars));
            case XML -> System.out.println(formatter.toXMLDefaultCurrency(cars));
            default -> printError("Invalid output format");
        }
        println("");
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.out.printf("\u001B[31m%s\u001B[0m%n", "Error: " + message);
    }

    public static void printSuccess(String message) {
        System.out.printf("\u001B[32m%s\u001B[0m%n", message);
    }

    public static void printHeader(String message) {
        println("");
        println("=== " + message + " ===");
        println("");
    }

    public static void printMenu() {
        println("""
                Menu Options:
                1. Change view format
                2. Filter inventory
                3. Sort inventory
                4. View inventory
                5. Exit
                Please enter your choice:\s""");
    }
}
