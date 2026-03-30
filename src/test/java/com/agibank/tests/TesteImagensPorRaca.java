package com.agibank.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.qameta.allure.*;
import io.restassured.response.Response;
import com.agibank.models.RespostaImagensRaca;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Dog API")
@Feature("Imagens por Raca")
@DisplayName("Testes de Imagens por Raca")
public class TesteImagensPorRaca extends TesteBase {

    @Test
    @Order(1)
    @Story("Buscar imagens por raca")
    @DisplayName("Deve retornar imagens de uma raca especifica com sucesso")
    @Description("Verifica se a API retorna imagens de uma raca valida")
    @Severity(SeverityLevel.CRITICAL)
    void deveRetornarImagensDeUmaRacaEspecificaComSucesso() {
        Response resposta = servicoDogApi.buscarImagensPorRaca("beagle");

        assertAll("Validacoes da resposta de imagens por raca",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens nao deve ser nula"),
            () -> assertFalse(resposta.jsonPath().getList("message").isEmpty(), "Lista de imagens nao deve estar vazia")
        );
    }

    @Test
    @Order(2)
    @Story("Validar estrutura da resposta")
    @DisplayName("Deve validar estrutura correta da resposta JSON")
    @Description("Verifica se a estrutura da resposta JSON esta conforme esperado")
    @Severity(SeverityLevel.NORMAL)
    void deveValidarEstruturaDaRespostaJson() {
        RespostaImagensRaca resposta = servicoDogApi.buscarImagensPorRacaComoObjeto("labrador");

        assertAll("Validacoes da estrutura da resposta",
            () -> assertNotNull(resposta, "Objeto resposta nao deve ser nulo"),
            () -> assertEquals("success", resposta.getStatus(), "Status deve ser success"),
            () -> assertNotNull(resposta.getImagens(), "Lista de imagens nao deve ser nula"),
            () -> assertFalse(resposta.getImagens().isEmpty(), "Lista de imagens nao deve estar vazia")
        );
    }

    @Test
    @Order(3)
    @Story("Validar URLs das imagens")
    @DisplayName("Deve retornar URLs validas de imagens")
    @Description("Verifica se todas as URLs retornadas sao validas e contem o nome da raca")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarUrlsValidasDeImagens() {
        String raca = "labrador";
        RespostaImagensRaca resposta = servicoDogApi.buscarImagensPorRacaComoObjeto(raca);

        assertAll("Validacoes das URLs das imagens",
            () -> assertNotNull(resposta.getImagens(), "Lista de imagens nao deve ser nula"),
            () -> assertFalse(resposta.getImagens().isEmpty(), "Lista deve conter imagens"),
            () -> assertTrue(resposta.getImagens().get(0).startsWith("https://"), "URL deve comecar com https://"),
            () -> assertTrue(resposta.getImagens().get(0).contains(raca), "URL deve conter o nome da raca")
        );
    }

    @Test
    @Order(4)
    @Story("Testar diferentes racas")
    @DisplayName("Deve funcionar com diferentes racas validas")
    @Description("Verifica se o endpoint funciona corretamente com diferentes racas")
    @Severity(SeverityLevel.NORMAL)
    void deveFuncionarComDiferentesRacasValidas() {
        String[] racas = {"bulldog", "poodle", "husky"};

        for (String raca : racas) {
            Response resposta = servicoDogApi.buscarImagensPorRaca(raca);

            assertAll("Validacoes para raca: " + raca,
                () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200 para " + raca),
                () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success para " + raca),
                () -> assertFalse(resposta.jsonPath().getList("message").isEmpty(), "Deve retornar imagens para " + raca)
            );
        }
    }

    @Test
    @Order(5)
    @Story("Testar raca inexistente")
    @DisplayName("Deve tratar adequadamente raca inexistente")
    @Description("Verifica como a API lida com racas que nao existem")
    @Severity(SeverityLevel.NORMAL)
    void deveTratarAdequadamenteRacaInexistente() {
        Response resposta = servicoDogApi.buscarImagensPorRaca("racainexistente123");

        assertAll("Validacoes para raca inexistente",
            () -> assertEquals(404, resposta.getStatusCode(), "Status code deve ser 404 para raca inexistente"),
            () -> assertEquals("error", resposta.jsonPath().getString("status"), "Status deve ser error")
        );
    }

    @Test
    @Order(6)
    @Story("Validar performance")
    @DisplayName("Deve responder em tempo aceitavel")
    @Description("Verifica se a API responde em menos de 3 segundos")
    @Severity(SeverityLevel.MINOR)
    void deveResponderEmTempoAceitavel() {
        Response resposta = servicoDogApi.buscarImagensPorRaca("labrador");

        assertAll("Validacoes de performance",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertTrue(resposta.getTime() < 3000, "Resposta deve ser em menos de 3 segundos")
        );
    }
}
