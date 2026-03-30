package com.agibank.services;

import com.agibank.config.ConfiguracaoApi;
import com.agibank.models.RespostaImagemAleatoria;
import com.agibank.models.RespostaImagensRaca;
import com.agibank.models.RespostaListaRacas;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ServicoDogApi {

    private void logResposta(Response response, String endpoint) {
        System.out.println("[RESPOSTA] " + endpoint);
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Tempo: " + response.getTime() + "ms");

        String body = response.getBody().asString();
        if (body.length() > 500) {
            System.out.println("Body: " + body.substring(0, 500) + "...");
        } else {
            System.out.println("Body: " + body);
        }
    }

    @Step("Buscar todas as racas disponiveis")
    public Response buscarTodasRacas() {
        Response response = given()
                .contentType(ConfiguracaoApi.CONTENT_TYPE)
                .log().ifValidationFails()
                .when()
                .get("/breeds/list/all")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();

        logResposta(response, "GET /breeds/list/all");
        return response;
    }

    @Step("Buscar todas as racas e converter para objeto")
    public RespostaListaRacas buscarTodasRacasComoObjeto() {
        return buscarTodasRacas().as(RespostaListaRacas.class);
    }

    @Step("Buscar imagens da raca: {raca}")
    public Response buscarImagensPorRaca(String raca) {
        Response response = given()
                .contentType(ConfiguracaoApi.CONTENT_TYPE)
                .pathParam("raca", raca)
                .log().ifValidationFails()
                .when()
                .get("/breed/{raca}/images")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();

        logResposta(response, "GET /breed/" + raca + "/images");
        return response;
    }

    @Step("Buscar imagens da raca {raca} e converter para objeto")
    public RespostaImagensRaca buscarImagensPorRacaComoObjeto(String raca) {
        return buscarImagensPorRaca(raca).as(RespostaImagensRaca.class);
    }

    @Step("Buscar imagem aleatoria")
    public Response buscarImagemAleatoria() {
        Response response = given()
                .contentType(ConfiguracaoApi.CONTENT_TYPE)
                .log().ifValidationFails()
                .when()
                .get("/breeds/image/random")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();

        logResposta(response, "GET /breeds/image/random");
        return response;
    }

    @Step("Buscar imagem aleatoria e converter para objeto")
    public RespostaImagemAleatoria buscarImagemAleatoriaComoObjeto() {
        return buscarImagemAleatoria().as(RespostaImagemAleatoria.class);
    }

    @Step("Buscar {quantidade} imagens aleatorias")
    public Response buscarMultiplasImagensAleatorias(int quantidade) {
        Response response = given()
                .contentType(ConfiguracaoApi.CONTENT_TYPE)
                .pathParam("quantidade", quantidade)
                .log().ifValidationFails()
                .when()
                .get("/breeds/image/random/{quantidade}")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();

        logResposta(response, "GET /breeds/image/random/" + quantidade);
        return response;
    }

    @Step("Buscar imagem aleatoria da raca: {raca}")
    public Response buscarImagemAleatoriaPorRaca(String raca) {
        Response response = given()
                .contentType(ConfiguracaoApi.CONTENT_TYPE)
                .pathParam("raca", raca)
                .log().ifValidationFails()
                .when()
                .get("/breed/{raca}/images/random")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();

        logResposta(response, "GET /breed/" + raca + "/images/random");
        return response;
    }

    @Step("Buscar {quantidade} imagens aleatorias da raca: {raca}")
    public Response buscarMultiplasImagensAleatoriasPorRaca(String raca, int quantidade) {
        Response response = given()
                .contentType(ConfiguracaoApi.CONTENT_TYPE)
                .pathParam("raca", raca)
                .pathParam("quantidade", quantidade)
                .log().ifValidationFails()
                .when()
                .get("/breed/{raca}/images/random/{quantidade}")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();

        logResposta(response, "GET /breed/" + raca + "/images/random/" + quantidade);
        return response;
    }
}
