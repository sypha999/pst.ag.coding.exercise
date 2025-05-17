package src.models;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Car {
    private String brand;
    private final String model;
    private final String type;
    private double defaultPrice;
    private String defaultCurrency;
    private final Map<String, Double> pricesByCurrency;
    private LocalDate releaseDate;

    public Car(String model, String type, double defaultPrice, Map<String, Double> pricesByCurrency) {
        this.model = Objects.requireNonNull(model, "model must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.defaultPrice = defaultPrice;
        this.pricesByCurrency = pricesByCurrency != null ? new HashMap<>(pricesByCurrency) : new HashMap<>();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Returns an unmodifiable view of the pricesByCurrency map to prevent external modification.
     */
    public Map<String, Double> getPricesByCurrency() {
        return Collections.unmodifiableMap(pricesByCurrency);
    }

}
