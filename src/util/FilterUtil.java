package src.util;

import src.models.Car;
import src.models.OutputFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class FilterUtil {

    private FilterUtil() {
    }

    /**
     * Filters cars by optional criteria: brand (case-insensitive),
     * maxPrice (compared to any price in map), and releaseDate.
     */
    public static List<Car> filter(List<Car> cars, String brand, Double maxPrice, LocalDate releaseDate) {
        Predicate<Car> brandPredicate = car -> brand == null ||
                (car.getBrand() != null && car.getBrand().equalsIgnoreCase(brand));

        Predicate<Car> pricePredicate = car -> maxPrice == null ||
                car.getPricesByCurrency().values().stream().anyMatch(price -> price <= maxPrice);

        Predicate<Car> datePredicate = car -> releaseDate == null ||
                releaseDate.equals(car.getReleaseDate());

        return cars.stream()
                .filter(brandPredicate.and(pricePredicate).and(datePredicate))
                .collect(Collectors.toList());
    }

    public static void filterInventory(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        while (true) {
            PrintUtil.println("Filter Options:");
            PrintUtil.println("1. Filter by Brand and Price");
            PrintUtil.println("2. Filter by Brand and Release Date");
            PrintUtil.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    filterByBrandAndPrice(scanner, allCars, outputFormat);
                    return;
                }
                case "2" -> {
                    filterByBrandAndReleaseDate(scanner, allCars, outputFormat);
                    return;
                }
                default -> PrintUtil.printError("Invalid input, valid inputs are: 1 or 2");
            }
        }
    }

    private static void filterByBrandAndPrice(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        PrintUtil.print("Input Brand: ");
        String brand = scanner.nextLine().trim();

        Double price = null;
        while (price == null) {
            PrintUtil.print("Input Max Price: ");
            String input = scanner.nextLine().trim();
            price = parseDoubleInput(input);
            if (price == null) {
                PrintUtil.printError("Invalid price input, please enter a valid number.");
            }
        }

        List<Car> filtered = filter(allCars, brand, price, null);
        PrintUtil.printFilterResult(filtered, outputFormat);
    }

    private static void filterByBrandAndReleaseDate(Scanner scanner, List<Car> allCars, OutputFormat outputFormat) {
        PrintUtil.print("Input Brand: ");
        String brand = scanner.nextLine().trim();

        LocalDate releaseDate = null;
        while (releaseDate == null) {
            PrintUtil.print("Input Release Date (yyyy,dd,mm): ");
            String inputDate = scanner.nextLine().trim();

            try {
                String[] parts = inputDate.split(",");
                if (parts.length != 3) throw new IllegalArgumentException("Date format invalid");

                int year = Integer.parseInt(parts[0]);
                int day = Integer.parseInt(parts[1]);
                int month = Integer.parseInt(parts[2]);

                releaseDate = LocalDate.of(year, month, day);
            } catch (Exception e) {
                PrintUtil.printError("Invalid date format or value: " + e.getMessage() + ". Please try again.");
            }
        }

        List<Car> filtered = filter(allCars, brand, null, releaseDate);
        PrintUtil.printFilterResult(filtered, outputFormat);
    }

    private static Double parseDoubleInput(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
