package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
   void mapToDTO() {
       Country country = new Country();
       country.setName("South Georgia");
       CountryDTO expectedDTO = new CountryDTO("1", "South Georgia");

        CountryDTO actualDTO = countryService.mapToDTO(country);
    }

    @Test
    void getAllCountries() {
        Map<String, Object> country1 = new HashMap<>();
        country1.put("name", Map.of("common", "Country A"));
        country1.put("population", 1000000);
        country1.put("area", 50000.0);
        country1.put("cca2", "CA"); // Country code
        country1.put("region", "Region A");
        country1.put("borders", Arrays.asList("Country B"));
        country1.put("languages", Map.of("en", "English"));

        Map<String, Object> country2 = new HashMap<>();
        country2.put("name", Map.of("common", "Country B"));
        country2.put("population", 2000000);
        country2.put("area", 70000.0);
        country2.put("cca2", "CB"); // Country code
        country2.put("region", "Region B");
        country2.put("borders", Arrays.asList("Country A"));
        country2.put("languages", Map.of("es", "Spanish"));

        List<Map<String, Object>> mockedApiResponse = Arrays.asList(country1, country2);


        when(restTemplate.getForObject("https://restcountries.com/v3.1/all", List.class))
                .thenReturn(mockedApiResponse);


        List<Country> countries = countryService.getAllCountries();


        assertNotNull(countries);
        assertEquals(2, countries.size());


    }

    @Test
    void getCountries() {

    }

    @Test
    void getCountryWithMostBorders() {
        Map<String, Object> country1 = new HashMap<>();
        country1.put("name", Map.of("common", "Country A"));
        country1.put("population", 1000000);
        country1.put("area", 50000.0);
        country1.put("cca2", "CA"); // Country code
        country1.put("region", "Region A");
        country1.put("borders", Arrays.asList("Country B", "Country C"));
        country1.put("languages", Map.of("en", "English"));

        Map<String, Object> country2 = new HashMap<>();
        country2.put("name", Map.of("common", "Country B"));
        country2.put("population", 2000000);
        country2.put("area", 70000.0);
        country2.put("cca2", "CB"); // Country code
        country2.put("region", "Region B");
        country2.put("borders", Arrays.asList("Country A"));
        country2.put("languages", Map.of("es", "Spanish"));

        List<Map<String, Object>> mockedApiResponse = Arrays.asList(country1, country2);


        when(restTemplate.getForObject("https://restcountries.com/v3.1/all", List.class))
                .thenReturn(mockedApiResponse);


        CountryDTO countryActual = countryService.getCountryWithMostBorders();


        assertNotNull(countryActual);

        assertEquals("Country A", countryActual.getName());
    }
}
