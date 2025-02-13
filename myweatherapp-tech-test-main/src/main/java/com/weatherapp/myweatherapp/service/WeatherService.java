package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//weatherService interacts with visualcrossingRepository to retrieve weather data, and provides processing for daylight hours and rain status


@Service
public class WeatherService {

  @Autowired
  private VisualcrossingRepository weatherRepo;
  /**
   * fetches forcast for city
   * @param city City name
   * @return containing weather data
   */
  public CityInfo forecastByCity(String city) {
    return weatherRepo.getByCity(city);
  }

  /**
   * Retrieves daylight duration in mins for a given city
   * @param city City name
   * @return Daylight duration in minutes
   */
  public long getDaylightHours(String city) {
    CityInfo cityInfo = weatherRepo.getByCity(city);
    if (cityInfo == null || cityInfo.currentConditions == null){
      throw new RuntimeException("City weather data not found");
    }

    String sunrise = cityInfo.currentConditions.sunrise;
    String sunset = cityInfo.currentConditions.sunset;
    return parseTime(sunset) - parseTime(sunrise); //returns daylight duration in mins
  }

  /**
   * determines if it is raining in a city
   * @param city City name
   * @return if it is raining, false otherwise
   */
  public boolean isRaining(String city) {
    CityInfo cityInfo = weatherRepo.getByCity(city);
    if (cityInfo == null || cityInfo.currentConditions == null){
      throw new RuntimeException("City weather data not found");
    }

    String conditions = cityInfo.currentConditions.conditions;
    return conditions.toLowerCase().contains("rain");
  }

  /**
   * Parses time string in HH:mm format and converts to minutes
   * @param timeStr time in HH:mm format
   * @return time in mins since midnight
   */
  private long parseTime(String timeStr) {
    String[] parts = timeStr.split(":");
    return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
  }
}
