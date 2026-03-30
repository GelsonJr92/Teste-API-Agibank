package com.agibank.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaImagensRaca {

    @JsonProperty("message")
    private List<String> imagens;

    @JsonProperty("status")
    private String status;
}
