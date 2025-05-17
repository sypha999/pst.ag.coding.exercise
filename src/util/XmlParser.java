package src.util;

import org.w3c.dom.*;
import src.models.Car;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.*;

public class XmlParser {

    public List<Car> parse(Path path) throws Exception {
        List<Car> cars = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(path.toFile());
        doc.getDocumentElement().normalize();

        NodeList carNodes = doc.getElementsByTagName("car");
        for (int i = 0; i < carNodes.getLength(); i++) {
            Element carElement = (Element) carNodes.item(i);

            String type = getTextContent(carElement, "type");
            String model = getTextContent(carElement, "model");

            // Default price and currency from single <price> element (if any)
            Element priceElement = getFirstElement(carElement, "price");
            double defaultPrice = 0;
            String defaultCurrency = null;
            if (priceElement != null) {
                defaultCurrency = priceElement.getAttribute("currency");
                defaultPrice = parseDouble(priceElement.getTextContent());
            }

            // Collect all prices from <prices> tag if present
            Map<String, Double> prices = new HashMap<>();
            if (defaultCurrency != null) {
                prices.put(defaultCurrency, defaultPrice);
            }
            Element pricesElement = getFirstElement(carElement, "prices");
            if (pricesElement != null) {
                NodeList priceList = pricesElement.getElementsByTagName("price");
                for (int j = 0; j < priceList.getLength(); j++) {
                    Element price = (Element) priceList.item(j);
                    String currency = price.getAttribute("currency");
                    double value = parseDouble(price.getTextContent());
                    prices.put(currency, value);
                }
            }

            cars.add(new Car(model, type, defaultPrice, prices));
        }
        return cars;
    }

    private static String getTextContent(Element parent, String tagName) {
        Element el = getFirstElement(parent, tagName);
        return el != null ? el.getTextContent().trim() : null;
    }

    private static Element getFirstElement(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? (Element) nodes.item(0) : null;
    }

    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
