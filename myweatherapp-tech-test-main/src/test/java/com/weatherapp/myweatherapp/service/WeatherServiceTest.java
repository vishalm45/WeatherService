package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.model.CityInfo.CurrentConditions;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

class WeatherServiceTest {

  // TODO: 12/05/2023 write unit tests
  @Mock
  private VisualcrossingRepository weatherRepo;

  @InjectMocks
  private WeatherService weatherService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetDaylightHours(){
    CityInfo cityInfo = new CityInfo();
    CurrentConditions conditions = new CurrentConditions();
    conditions.sunrise = "06:00";
    conditions.sunset = "18:00";
    cityInfo.currentConditions = conditions;

    when(weatherRepo.getByCity("London")).thenReturn(cityInfo);

    long daylightHours = weatherService.getDaylightHours("London");
    assertEquals(720, daylightHours); //12 hours * 60 minutes 
  }

  @Test
  void testIsRaining() {
    CityInfo cityInfo = new CityInfo();
    CurrentConditions conditions = new CurrentConditions();
    conditions.conditions = "Rain";
    cityInfo.currentConditions = conditions;

    when(weatherRepo.getByCity("Seattle")).thenReturn(cityInfo);

    boolean raining = weatherService.isRaining("Seattle");
    assertTrue(raining);
  }

  @Test
  void testIsNotRaining() {
    CityInfo cityInfo = new CityInfo();
    CurrentConditions conditions = new CurrentConditions();
    conditions.conditions = "Sunny";
    cityInfo.currentConditions = conditions;

    when(weatherRepo.getByCity("Los Angeles")).thenReturn(cityInfo);

    boolean raining = weatherService.isRaining("Los Angeles");
    assertFalse(raining);
}

  @Test
  void testGetDaylightHoursWithNullCity() {
      when(weatherRepo.getByCity("Unknown")) .thenReturn(null);

      Exception exception = assertThrows(RuntimeException.class, () -> {
          weatherService.getDaylightHours("Unknown");
      });

      assertEquals("City weather data not found", exception.getMessage());
  }

}

