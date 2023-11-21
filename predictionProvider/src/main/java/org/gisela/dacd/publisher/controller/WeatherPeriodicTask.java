package org.gisela.dacd.publisher.controller;

import org.gisela.dacd.publisher.model.Location;
import org.gisela.dacd.publisher.model.WeatherRepository;
import org.gisela.dacd.publisher.model.WeatherRepositorySQLite;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TimerTask;

public class WeatherPeriodicTask extends TimerTask {
    private final OpenWeatherProvider weatherProvider;
    private final ArrayList<Location> locations;

    public WeatherPeriodicTask(OpenWeatherProvider weatherProvider, ArrayList<Location> locations) {
        this.weatherProvider = weatherProvider;
        this.locations = locations;
    }

    @Override
    public void run() {
        try {
            SQLiteConnector sqLiteConnector = new SQLiteConnector();
            WeatherRepository weatherRepository = new WeatherRepositorySQLite(sqLiteConnector.getConnection());
            WeatherController weatherController = new WeatherController(weatherProvider, weatherRepository, locations);
            weatherController.execute();
            sqLiteConnector.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}