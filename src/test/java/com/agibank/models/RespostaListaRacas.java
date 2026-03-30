package com.agibank.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaListaRacas {

    @JsonProperty("message")
    private Map<String, List<String>> racas;

    @JsonProperty("status")
    private String status;
}
