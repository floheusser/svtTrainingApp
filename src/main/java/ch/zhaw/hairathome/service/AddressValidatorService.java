package ch.zhaw.hairathome.service;

import java.time.Duration;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ch.zhaw.hairathome.model.AddressInformation;
import ch.zhaw.hairathome.model.AddressInformation.Verdict;
import ch.zhaw.hairathome.model.dto.AddressValidationDTO;
import ch.zhaw.hairathome.model.dto.AddressValidationDTO.Address;

@Service
public class AddressValidatorService {

    private static final String API_KEY = "AIzaSyA7G4bjDnmZLHAgbFpazzmsNbtx8LZGGqE";
    private static final String GOOGLE_ADDRESS_VALIDATOR_BASE_URL = "https://addressvalidation.googleapis.com";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(AddressValidatorService.class);
    private final WebClient webClient;

    @Autowired
    public AddressValidatorService() {
        this.webClient = WebClient.builder()
                .baseUrl(GOOGLE_ADDRESS_VALIDATOR_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(ServiceUtils.logRequest(logger))
                .build();
    }

    public boolean isAddressValid(String street, String city, String postCode) {
        AddressValidationDTO aDto = new AddressValidationDTO();
        Address address = new Address();

        if (street.equals("") || street == null) {
           address.setAddressLines(Collections.singletonList("DefaultAddressLine"));
        } else {
           address.setAddressLines(Collections.singletonList(street));
        }
        address.setRegionCode("CH");
        address.setLocality(city);
        address.setPostalCode(postCode);
        
        aDto.setAddress(address);
        
        Verdict verdict = validateAdress(aDto).getResult().getVerdict();
    
        return verdict.getAddressComplete() != null && verdict.getAddressComplete()
                && "PREMISE".equals(verdict.getValidationGranularity())
                && "PREMISE".equals(verdict.getInputGranularity())
                && "PREMISE".equals(verdict.getGeocodeGranularity());
    }

    private AddressInformation validateAdress(AddressValidationDTO address) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1:validateAddress")
                        .queryParam("key", API_KEY).build())

                .bodyValue(address)
                .retrieve()
                .bodyToMono(AddressInformation.class)
                .block(REQUEST_TIMEOUT);
    }
}
