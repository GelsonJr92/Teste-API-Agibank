package com.agibank.tests;

import com.agibank.models.RespostaImagemAleatoria;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Dog API")
@Feature("Imagens Aleatorias")
@DisplayName("Testes de Imagens Aleatorias")
public class TesteImagensAleatorias extends TesteBase {

    @Test
    @Order(1)
    @Story("Buscar imagem aleatoria")
    @DisplayName("Deve retornar uma imagem aleatoria com sucesso")
    @Description("Verifica se a API retorna uma imagem aleatoria valida")
    @Severity(SeverityLevel.CRITICAL)
    void deveRetornarUmaImagemAleatoriaComSucesso() {
        Response resposta = servicoDogApi.buscarImagemAleatoria();

        assertAll("Validacoes da resposta de imagem aleatoria",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getString("message"), "URL da imagem nao deve ser nula"),
            () -> assertFalse(resposta.jsonPath().getString("message").isEmpty(), "URL da imagem nao deve estar vazia")
        );
    }

    @Test
    @Order(2)
    @Story("Validar estrutura da resposta")
    @DisplayName("Deve validar estrutura correta da resposta JSON")
    @Description("Verifica se a estrutura da resposta JSON esta conforme esperado")
    @Severity(SeverityLevel.NORMAL)
    void deveValidarEstruturaDaRespostaJson() {
        RespostaImagemAleatoria resposta = servicoDogApi.buscarImagemAleatoriaComoObjeto();

        assertAll("Validacoes da estrutura da resposta",
            () -> assertNotNull(resposta, "Objeto resposta nao deve ser nulo"),
            () -> assertEquals("success", resposta.getStatus(), "Status deve ser success"),
            () -> assertNotNull(resposta.getImagemUrl(), "URL da imagem nao deve ser nula"),
            () -> assertFalse(resposta.getImagemUrl().isEmpty(), "URL da imagem nao deve estar vazia")
        );
    }

    @Test
    @Order(3)
    @Story("Validar URL da imagem")
    @DisplayName("Deve retornar URL valida de imagem")
    @Description("Verifica se a URL retornada e valida e aponta para uma imagem")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarUrlValidaDeImagem() {
        String urlImagem = servicoDogApi.buscarImagemAleatoriaComoObjeto().getImagemUrl();

        assertAll("Validacoes da URL da imagem",
            () -> assertNotNull(urlImagem, "URL da imagem nao deve ser nula"),
            () -> assertFalse(urlImagem.isEmpty(), "URL da imagem nao deve estar vazia"),
            () -> assertTrue(urlImagem.startsWith("https://"), "URL deve comecar com https://"),
            () -> assertTrue(urlImagem.contains("dog.ceo"), "URL deve conter dominio dog.ceo"),
            () -> assertTrue(urlImagem.matches(".*\\.(jpg|jpeg|png)$"),
                "URL deve terminar com extensao de imagem valida")
        );
    }

    @Test
    @Order(4)
    @Story("Validar aleatoriedade")
    @DisplayName("Deve retornar imagens diferentes em chamadas consecutivas")
    @Description("Verifica se multiplas chamadas retornam imagens diferentes")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarImagensDiferentesEmChamadasConsecutivas() {
        RespostaImagemAleatoria primeira = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        RespostaImagemAleatoria segunda = servicoDogApi.buscarImagemAleatoriaComoObjeto();
        RespostaImagemAleatoria terceira = servicoDogApi.buscarImagemAleatoriaComoObjeto();

        boolean todasIguais = primeira.getImagemUrl().equals(segunda.getImagemUrl()) &&
                             segunda.getImagemUrl().equals(terceira.getImagemUrl());

        assertAll("Validacoes de aleatoriedade",
            () -> assertEquals("success", primeira.getStatus(), "Primeira chamada deve ter sucesso"),
            () -> assertEquals("success", segunda.getStatus(), "Segunda chamada deve ter sucesso"),
            () -> assertEquals("success", terceira.getStatus(), "Terceira chamada deve ter sucesso"),
            () -> assertFalse(todasIguais, "Nem todas as imagens devem ser iguais")
        );
    }

    @Test
    @Order(5)
    @Story("Validar multiplas imagens aleatorias")
    @DisplayName("Deve retornar multiplas imagens aleatorias")
    @Description("Verifica se a API pode retornar multiplas imagens aleatorias de uma vez")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarMultiplasImagensAleatorias() {
        int quantidade = 3;
        Response resposta = servicoDogApi.buscarMultiplasImagensAleatorias(quantidade);

        assertAll("Validacoes de multiplas imagens aleatorias",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens nao deve ser nula"),
            () -> assertEquals(quantidade, resposta.jsonPath().getList("message").size(),
                "Deve retornar exatamente " + quantidade + " imagens")
        );
    }

    @Test
    @Order(6)
    @Story("Validar performance")
    @DisplayName("Deve responder em tempo aceitavel")
    @Description("Verifica se a API responde em menos de 3 segundos")
    @Severity(SeverityLevel.MINOR)
    void deveResponderEmTempoAceitavel() {
        Response resposta = servicoDogApi.buscarImagemAleatoria();

        assertAll("Validacoes de performance",
            () -> assertEquals(200, resposta.getStatusCode(), "Deve retornar sucesso"),
            () -> assertTrue(resposta.getTime() < 3000,
                String.format("Tempo de resposta deve ser menor que 3000ms, mas foi %dms", resposta.getTime()))
        );
    }

    @Test
    @Order(7)
    @Story("Validar headers da resposta")
    @DisplayName("Deve retornar headers corretos")
    @Description("Verifica se os headers da resposta estao conforme esperado")
    @Severity(SeverityLevel.MINOR)
    void deveRetornarHeadersCorretos() {
        Response resposta = servicoDogApi.buscarImagemAleatoria();

        assertAll("Validacoes dos headers",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertNotNull(resposta.getHeader("Content-Type"), "Header Content-Type deve estar presente"),
            () -> assertTrue(resposta.getHeader("Content-Type").contains("application/json"),
                "Content-Type deve ser application/json")
        );
    }

    @Test
    @Order(8)
    @Story("Buscar imagem aleatoria por raca")
    @DisplayName("Deve retornar imagem aleatoria de raca especifica")
    @Description("Verifica se a API retorna imagem aleatoria de uma raca especifica")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarImagemAleatoriaPorRaca() {
        String raca = "labrador";
        Response resposta = servicoDogApi.buscarImagemAleatoriaPorRaca(raca);

        assertAll("Validacoes de imagem aleatoria por raca",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getString("message"), "URL da imagem nao deve ser nula"),
            () -> assertTrue(resposta.jsonPath().getString("message").contains(raca),
                "URL deve conter o nome da raca: " + raca)
        );
    }

    @Test
    @Order(9)
    @Story("Validar multiplas imagens por raca")
    @DisplayName("Deve retornar multiplas imagens aleatorias de raca especifica")
    @Description("Verifica se a API retorna multiplas imagens aleatorias de uma raca")
    @Severity(SeverityLevel.NORMAL)
    void deveRetornarMultiplasImagensPorRaca() {
        String raca = "beagle";
        int quantidade = 5;
        Response resposta = servicoDogApi.buscarMultiplasImagensAleatoriasPorRaca(raca, quantidade);

        assertAll("Validacoes de multiplas imagens por raca",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens nao deve ser nula"),
            () -> assertTrue(resposta.jsonPath().getList("message").size() <= quantidade,
                "Nao deve retornar mais imagens que o solicitado")
        );
    }

    @Test
    @Order(10)
    @Story("Testar limite de multiplas imagens")
    @DisplayName("Deve respeitar limite maximo de imagens")
    @Description("Verifica comportamento ao solicitar quantidade excessiva de imagens")
    @Severity(SeverityLevel.MINOR)
    void deveRespeitarLimiteMaximoDeImagens() {
        Response resposta = servicoDogApi.buscarMultiplasImagensAleatorias(100);

        assertAll("Validacoes de limite maximo de imagens",
            () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
            () -> assertEquals("success", resposta.jsonPath().getString("status"), "Status deve ser success"),
            () -> assertNotNull(resposta.jsonPath().getList("message"), "Lista de imagens nao deve ser nula"),
            () -> assertFalse(resposta.jsonPath().getList("message").isEmpty(), "Deve retornar ao menos uma imagem")
        );
    }
}
