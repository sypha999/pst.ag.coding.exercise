package src.util;

import src.models.Car;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewFormatter {

    private static final String TITLE = "CAR INVENTORY";
    private static final int COLUMN_WIDTH = 15;

    public void printTable(List<Car> cars) {
        Set<String> currencies = collectCurrencies(cars);
        int columnCount = 4 + currencies.size();
        int tableWidth = COLUMN_WIDTH * columnCount;
        int padding = (tableWidth - TITLE.length()) / 2;

        printLine(tableWidth);
        System.out.printf("%" + (padding + TITLE.length()) + "s%n", TITLE);
        printLine(tableWidth);

        // Header
        System.out.printf("%-15s %-15s %-15s %-15s", "Brand", "Model", "Type", "ReleaseDate");
        currencies.forEach(cur -> System.out.printf(" %-15s", "Price(" + cur + ")"));
        System.out.println();
        printLine(tableWidth);

        // Rows
        for (Car car : cars) {
            System.out.printf("%-15s %-15s %-15s %-15s",
                    car.getBrand(),
                    car.getModel(),
                    car.getType(),
                    car.getReleaseDate() != null ? car.getReleaseDate() : "N/A");

            for (String currency : currencies) {
                double price = car.getPricesByCurrency().getOrDefault(currency, 0.0);
                System.out.printf(" %-15.2f", price);
            }
            System.out.println();
        }

        printLine(tableWidth);
    }

    public void printTableDefaultCurrency(List<Car> cars) {
        int columnCount = 5;
        int tableWidth = COLUMN_WIDTH * columnCount;
        int padding = (tableWidth - TITLE.length()) / 2;

        printLine(tableWidth);
        System.out.printf("%" + (padding + TITLE.length()) + "s%n", TITLE);
        printLine(tableWidth);

        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n",
                "Brand", "Model", "Type", "Price", "ReleaseDate");
        printLine(tableWidth);

        for (Car car : cars) {
            String priceWithCurrency = String.format("%.2f %s",
                    car.getDefaultPrice(), car.getDefaultCurrency());
            System.out.printf("%-15s %-15s %-15s %-15s %-15s%n",
                    car.getBrand(),
                    car.getModel(),
                    car.getType(),
                    priceWithCurrency,
                    car.getReleaseDate() != null ? car.getReleaseDate() : "N/A");
        }

        printLine(tableWidth);
    }

    public String toXML(List<Car> cars) {
        return toXML(cars, false);
    }

    public String toXMLDefaultCurrency(List<Car> cars) {
        return toXML(cars, true);
    }

    private String toXML(List<Car> cars, boolean defaultCurrencyOnly) {
        StringBuilder sb = new StringBuilder("<cars>\n");
        for (Car car : cars) {
            sb.append("  <car>\n")
                    .append("    <brand>").append(escapeXml(car.getBrand())).append("</brand>\n")
                    .append("    <model>").append(escapeXml(car.getModel())).append("</model>\n")
                    .append("    <type>").append(escapeXml(car.getType())).append("</type>\n");

            if (defaultCurrencyOnly) {
                sb.append("    <price>").append(String.format("%.2f", car.getDefaultPrice())).append("</price>\n")
                        .append("    <currency>").append(escapeXml(car.getDefaultCurrency())).append("</currency>\n");
            } else {
                sb.append("    <pricesInDifferentCurrencies>\n");
                car.getPricesByCurrency().forEach((cur, price) ->
                        sb.append("      <price currency=\"").append(escapeXml(cur)).append("\">")
                                .append(String.format("%.2f", price))
                                .append("</price>\n"));
                sb.append("    </pricesInDifferentCurrencies>\n");
            }

            if (car.getReleaseDate() != null) {
                sb.append("    <releaseDate>").append(car.getReleaseDate()).append("</releaseDate>\n");
            }
            sb.append("  </car>\n");
        }
        sb.append("</cars>");
        return sb.toString();
    }

    public String toJSON(List<Car> cars) {
        return toJSON(cars, false);
    }

    public String toJSONDefaultCurrency(List<Car> cars) {
        return toJSON(cars, true);
    }

    private String toJSON(List<Car> cars, boolean defaultCurrencyOnly) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            sb.append("  {\n")
                    .append("    \"brand\": \"").append(escapeJson(car.getBrand())).append("\",\n")
                    .append("    \"model\": \"").append(escapeJson(car.getModel())).append("\",\n")
                    .append("    \"type\": \"").append(escapeJson(car.getType())).append("\",\n");

            if (defaultCurrencyOnly) {
                sb.append("    \"price\": ").append(car.getDefaultPrice()).append(",\n")
                        .append("    \"currency\": \"").append(escapeJson(car.getDefaultCurrency())).append("\",\n");
            } else {
                sb.append("    \"price in different currencies\": {\n");
                var entrySet = car.getPricesByCurrency().entrySet();
                int count = 0;
                for (var entry : entrySet) {
                    sb.append("      \"").append(escapeJson(entry.getKey())).append("\": ").append(entry.getValue());
                    if (++count < entrySet.size()) sb.append(",");
                    sb.append("\n");
                }
                sb.append("    },\n");
            }

            sb.append("    \"releaseDate\": ").append(car.getReleaseDate() != null ? "\"" + car.getReleaseDate() + "\"" : null).append("\n")
                    .append("  }");

            if (i < cars.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private Set<String> collectCurrencies(List<Car> cars) {
        return cars.stream()
                .flatMap(car -> car.getPricesByCurrency().keySet().stream())
                .collect(Collectors.toSet());
    }

    private void printLine(int width) {
        System.out.println("=".repeat(width));
    }

    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}
