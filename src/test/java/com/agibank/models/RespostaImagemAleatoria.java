package com.agibank.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaImagemAleatoria {

    @JsonProperty("message")
    private String imagemUrl;

    @JsonProperty("status")
    private String status;
}
