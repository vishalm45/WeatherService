package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

//weatherController handles requests for weather features, comparing daylight hours and rain status

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
  }

  // TODO: given two city names, compare the length of the daylight hours and return the city with the longest day

  /**
   * Compares daylight hours between two cities, returning longest day
   * @param city1 first city name
   * @param city2 second city name
   * @return ResponseEntity containing result of comparison
   */
  @GetMapping("/compare-daylight")
  public ResponseEntity<Map<String, Object>> compareDaylightHours(@RequestParam String city1, @RequestParam String city2) {
    try {
      long daylightCity1 = weatherService.getDaylightHours(city1);
      long daylightCity2 = weatherService.getDaylightHours(city2);

      String result = daylightCity1 > daylightCity2 ? city1 : city2;

      Map<String, Object> response = new HashMap<>();
      response.put("cityWithLongestDay", result);
      response.put(city1, daylightCity1 + " minutes");
      response.put(city2, daylightCity2 + " minutes");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("error", "Error comparing daylight hours"));
    }
  }

  // TODO: given two city names, check which city its currently raining in


  @GetMapping("/rain-check")
  public ResponseEntity<Map<String, Object>> rainCheck(@RequestParam String city1, @RequestParam String city2) {
    try {
      boolean isRainingCity1 = weatherService.isRaining(city1);
      boolean isRainingCity2 = weatherService.isRaining(city2);

      Map<String, Object> response = new HashMap<>();
      response.put(city1, isRainingCity1 ? "is raining" : "no rain");
      response.put(city2, isRainingCity2 ? "is raining" : "no rain");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("error", "Error checking rain status"));
    }
  }

}
