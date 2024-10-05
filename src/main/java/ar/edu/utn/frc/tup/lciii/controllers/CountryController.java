package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountriesRequest;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class CountryController {

    private final CountryService countryService;
    private final RestTemplate restTemplate;

    @GetMapping("/countries")
    public List<CountryDTO> getCountries(@RequestParam(name="name", required = false) String name,
                                         @RequestParam(name="code", required = false) String code) {
        List<CountryDTO> countries = countryService.getCountries(name,code);
        return countries;
    }

    @GetMapping("countries/{continent}")
    public List<CountryDTO> getCountriesByContinent(@PathVariable String continent) {
        return countryService.getCountriesByContinent(continent);
    }

    @GetMapping("countries/language/{language}")
    public List<CountryDTO> getCountriesByLanguage(@PathVariable String language) {
        return countryService.getCountriesByLanguage(language);
    }

    @GetMapping("/countries/most-borders")
    public CountryDTO getCountryWithMostBorders() {
        return countryService.getCountryWithMostBorders();

    }

    @PostMapping("/countries")
        public List<CountryDTO> saveCountries(@RequestBody PostCountriesRequest req) {
        return countryService.postCountries(req.getAmountOfCountriesToSave());
    }


}

