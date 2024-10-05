package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.CountryEntity;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    private final RestTemplate restTemplate;

    public List<Country> getAllCountries() {
        String url = "https://restcountries.com/v3.1/all";
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
        return response.stream().map(this::mapToCountry).collect(Collectors.toList());
    }

    /**
     * Agregar mapeo de campo cca3 (String)
     * Agregar mapeo campos borders ((List<String>))
     */
    public Country mapToCountry(Map<String, Object> countryData) {
        Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
        return Country.builder()
                .name((String) nameData.get("common"))
                .population(((Number) countryData.get("population")).longValue())
                .area(((Number) countryData.get("area")).doubleValue())
                .region((String) countryData.get("region"))
                .languages((Map<String, String>) countryData.get("languages"))
                .code((String) countryData.get("cca3"))
                .borders((List<String>) countryData.get("borders"))
                .build();
    }


    public CountryDTO mapToDTO(Country country) {
        return new CountryDTO(country.getCode(), country.getName());
    }


    public List<CountryDTO> getCountries(String name, String code) {
        List<Country> countries = getAllCountries();
        List<CountryDTO> countriesDTO = new ArrayList<>();
        for (Country country : countries) {
            countriesDTO.add(mapToDTO(country));
        }

        if (name != null) {
            List<CountryDTO> filteredCountries = countriesDTO.stream()
                    .filter(country -> country.getName().equals(name))
                    .collect(Collectors.toList());
            countriesDTO = filteredCountries;
        }
        if (code != null) {
            List<CountryDTO> filteredCountries = countriesDTO.stream()
                    .filter(country -> country.getCode().equals(code))
                    .collect(Collectors.toList());
            countriesDTO = filteredCountries;

        }

        return countriesDTO;

    }

    public List<CountryDTO> getCountriesByContinent(String continent) {
        List<Country> countries = getAllCountries();
        List<CountryDTO> countriesDTO = new ArrayList<>();
        List<Country> filteredCountries = countries.stream()
                .filter(country -> country.getRegion() != null &&country.getRegion().equals(continent))
                .collect(Collectors.toList());

        for (Country country : filteredCountries) {
            countriesDTO.add(mapToDTO(country));
        }
        return countriesDTO;
    }

    public List<CountryDTO> getCountriesByLanguage(String language) {
        List<Country> countries = getAllCountries();
        List<CountryDTO> countriesDTO = new ArrayList<>();
        List<Country> filteredCountries = countries.stream()
                .filter(country -> country.getLanguages() != null && country.getLanguages().containsValue(language))
                .collect(Collectors.toList());

        for (Country country : filteredCountries) {
            countriesDTO.add(mapToDTO(country));
        }
        return countriesDTO;
    }

    public CountryDTO getCountryWithMostBorders() {

        List<Country> countries = getAllCountries();
        Country countryWithMostBorders = null;
        int maxBorders = 0;
        for (Country country : countries) {
            List<String> borders = country.getBorders();

            if (borders != null && borders.size() > maxBorders) {
                maxBorders = borders.size();
                countryWithMostBorders = country;
            }
        }
        return mapToDTO(countryWithMostBorders);
    }

    public List<CountryDTO> postCountries(int amount) {
        List<Country> countries = getAllCountries();
        List<Country> selectedcountries = new ArrayList<>();
        List<CountryDTO> countriesDTO = new ArrayList<>();

        //get amount of countries that comes as parameter
        for (int i = amount; i> 0; i--) {
            countriesDTO.add(mapToDTO(countries.get(i)));
            selectedcountries.add(countries.get(i));

        }
        Collections.shuffle(countriesDTO);

        //generate entities

        int loopIndex = 1;
        for (Country country : selectedcountries) {
            CountryEntity countryEntity = new CountryEntity();
            countryEntity.setCode(country.getCode());
            countryEntity.setName(country.getName());
            countryEntity.setId(loopIndex);
            countryEntity.setPopulation(country.getPopulation());
            countryEntity.setArea(country.getArea());

            loopIndex++;
            //save entities
            countryRepository.save(countryEntity);


        }

    return countriesDTO;
    }



}