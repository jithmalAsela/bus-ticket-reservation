package com.lk.busreservation.config;

import java.io.InputStream;
import java.util.*;

/**
 * @author jithmal
 */
public class BusConfig {

    private static final BusConfig INSTANCE = new BusConfig();

    private int totalSeats;
    private int seatsPerRow;
    private final Map<String, Integer> stopOrder = new HashMap<>();
    private final Map<String, Double> priceMap = new HashMap<>();

    private BusConfig() {
        loadConfig();
    }

    private void loadConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("Config.properties NOT FOUND in resources!");
            }

            Properties props = new Properties();
            props.load(is);

            // Seat config
            this.totalSeats = Integer.parseInt(props.getProperty("totalSeats", "40"));
            this.seatsPerRow = Integer.parseInt(props.getProperty("seatsPerRow", "4"));

            if (totalSeats % seatsPerRow != 0) {
                throw new RuntimeException("totalSeats must be divisible by seatsPerRow");
            }

            // Stops
            String[] stops = props.getProperty("stops", "A,B,C,D").split(",");
            for (int i = 0; i < stops.length; i++) {
                stopOrder.put(stops[i].trim().toUpperCase(), i);
            }

            // Prices
            for (String key : props.stringPropertyNames()) {
                if (key.startsWith("price.")) {
                    String route = key.substring(6).toUpperCase();
                    priceMap.put(route, Double.parseDouble(props.getProperty(key)));
                }
            }

            System.out.println("CONFIG LOADED SUCCESSFULLY from config.properties");
            System.out.println("totalSeats = " + totalSeats);
            System.out.println("seatsPerRow = " + seatsPerRow);
            System.out.println("stops = " + stopOrder.keySet());
            System.out.println("number of prices loaded = " + priceMap.size());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static BusConfig getInstance() {
        return INSTANCE;
    }

    public int getTotalSeats() { return totalSeats; }
    public int getSeatsPerRow() { return seatsPerRow; }
    public Map<String, Integer> getStopOrder() { return Collections.unmodifiableMap(stopOrder); }
    public Map<String, Double> getPriceMap() { return Collections.unmodifiableMap(priceMap); }
}
