package ch.zhaw.hairathome.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddressValidationDTO {

    private Address address;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Address {
        private String regionCode;
        private String locality;
        private String administrativeArea;
        private String postalCode;
        private List<String> addressLines;
    }
}
