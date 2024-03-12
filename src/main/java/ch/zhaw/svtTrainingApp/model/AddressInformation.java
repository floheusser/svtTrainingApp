package ch.zhaw.svtTrainingApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a response object for the api
 * https://addressvalidation.googleapis.com
 *
 * For informations about the properties see:
 * https://developers.google.com/maps/documentation/address-validation/understand-response?hl=de
 **/
@Getter
@NoArgsConstructor
@Setter
public class AddressInformation {

    private Result result;

    @Getter
    @Setter
    public static class Result {
        private Verdict verdict;
    }

    @Getter
    @Setter
    public static class Verdict {
        private String inputGranularity;
        private String validationGranularity;
        private String geocodeGranularity;
        private Boolean addressComplete;

    }
}

