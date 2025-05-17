package src.util;

import src.models.Car;
import src.models.OutputFormat;

import java.util.*;
import java.util.stream.Collectors;

public final class SortUtil {

    private SortUtil() {
    }

    /**
     * Sorts cars by specified field and order.
     * Supports sorting by price, date, or brand.
     */
    public static List<Car> sort(List<Car> cars, String sortField, String order) {
        Comparator<Car> comparator = switch (sortField.toLowerCase()) {
            case "price" -> Comparator.comparingDouble(Car::getDefaultPrice);
            case "date" -> Comparator.comparing(Car::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(Car::getBrand, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
        };

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return cars.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Sort cars by their type and preferred currency mapping.
     * Updates default price and currency according to preference, then sorts by price ascending.
     */
    public static List<Car> sortCarsByTypeCurrency(List<Car> cars, Map<String, String> typeCurrencyMap) {
        var normalizedMap = typeCurrencyMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> normalizeType(e.getKey()),
                        e -> e.getValue().toUpperCase()
                ));

        cars.forEach(car -> {
            var carType = normalizeType(car.getType());
            var preferredCurrency = normalizedMap.get(carType);
            if (preferredCurrency != null) {
                var price = car.getPricesByCurrency().get(preferredCurrency);
                if (price != null) {
                    car.setDefaultPrice(price);
                    car.setDefaultCurrency(preferredCurrency);
                }
            }
        });

        return cars.stream()
                .sorted(Comparator.comparingDouble(Car::getDefaultPrice))
                .collect(Collectors.toList());
    }

    /**
     * Interactive method to sort inventory using Scanner input.
     */
    public static void sortInventory(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        while (true) {
            PrintUtil.println("Sort Options:");
            PrintUtil.println("1. Sort by Price");
            PrintUtil.println("2. Sort by Date");
            PrintUtil.println("3. Sort by Type and Currency");
            PrintUtil.print("Enter choice: ");
            var choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    sortByPrice(scanner, allCars, outputFormat);
                    return;
                }
                case "2" -> {
                    sortByDate(scanner, allCars, outputFormat);
                    return;
                }
                case "3" -> {
                    sortByTypeAndCurrency(scanner, allCars, outputFormat);
                    return;
                }
                default -> PrintUtil.printError("Invalid input, valid inputs are: 1, 2 or 3");
            }
        }
    }

    private static void sortByPrice(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        var dir = getSortDirection(scanner);
        var sorted = sort(allCars, "price", dir);
        PrintUtil.printCarsWithDefaultCurrency(sorted, outputFormat);
    }

    private static void sortByDate(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        var dir = getSortDirection(scanner);
        var sorted = sort(allCars, "date", dir);
        PrintUtil.printCarsWithDefaultCurrency(sorted, outputFormat);
    }

    private static void sortByTypeAndCurrency(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        PrintUtil.println("Enter type and currency separated by comma (e.g. sedan,usd). Input any digit to finish.");
        PrintUtil.println("\u001B[31mInvalid entries will be ignored.\u001B[0m");

        Map<String, String> typeToCurrency = new HashMap<>();

        while (true) {
            if (typeToCurrency.size() == 3) {break;} //comment out to enable more currencies
            var input = scanner.nextLine().trim();
            if (input.matches("\\d+")) break;

            var parts = input.split(",");
            if (parts.length != 2) {
                PrintUtil.printError("Invalid input format. Expected: type,currency");
                continue;
            }

            typeToCurrency.put(parts[0].trim(), parts[1].trim().toUpperCase());
            PrintUtil.printSuccess("Input recorded. Add more or enter a digit to finish.");
        }
        var sorted = sortCarsByTypeCurrency(allCars, typeToCurrency);
        PrintUtil.printCarsWithDefaultCurrency(sorted, outputFormat);
        PrintUtil.println("");
    }

    private static String getSortDirection(Scanner scanner) {
        while (true) {
            PrintUtil.println("Enter sort direction:");
            PrintUtil.println("1 - Ascending");
            PrintUtil.println("2 - Descending");
            var input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> { return "asc"; }
                case "2" -> { return "desc"; }
                default -> PrintUtil.printError("Invalid input, valid inputs are: 1 or 2");
            }
        }
    }

    private static String normalizeType(String key) {
        if (key == null) return null;
        return switch (key.toLowerCase()) {
            case "suv" -> "SUV";
            case "truck" -> "Truck";
            case "sedan" -> "Sedan";
            default -> key.toUpperCase();
        };
    }
}
