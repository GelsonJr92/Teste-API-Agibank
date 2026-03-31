package com.agibank.tests;

import com.agibank.models.RespostaListaRacas;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("listagem-racas")
@DisplayName("Testes de Listagem de Racas")
public class TesteListagemRacas extends TesteBase {

        @Test
        @Order(1)
        @DisplayName("Deve retornar lista completa de racas com sucesso")
        @Tag("smoke")
        @Tag("positivo")
        void deveRetornarListaCompletaDeRacasComSucesso() {
                Response resposta = servicoDogApi.buscarTodasRacas();

                assertAll("Validacoes da resposta de listagem de racas",
                                () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
                                () -> assertEquals("success", resposta.jsonPath().getString("status"),
                                                "Status deve ser success"),
                                () -> assertNotNull(resposta.jsonPath().getMap("message"), "Message nao deve ser nulo"),
                                () -> assertFalse(resposta.jsonPath().getMap("message").isEmpty(),
                                                "Lista de racas nao deve estar vazia"));
        }

        @Test
        @Order(2)
        @DisplayName("Deve validar estrutura correta da resposta JSON")
        @Tag("contrato")
        void deveValidarEstruturaDaRespostaJson() {
                RespostaListaRacas resposta = servicoDogApi.buscarTodasRacasComoObjeto();

                assertAll("Validacoes da estrutura da resposta",
                                () -> assertNotNull(resposta, "Objeto resposta nao deve ser nulo"),
                                () -> assertEquals("success", resposta.getStatus(), "Status deve ser success"),
                                () -> assertNotNull(resposta.getRacas(), "Map de racas nao deve ser nulo"),
                                () -> assertFalse(resposta.getRacas().isEmpty(), "Map de racas nao deve estar vazio"));
        }

        @Test
        @Order(3)
        @DisplayName("Deve conter racas conhecidas na lista")
        @Tag("dados")
        void deveConterRacasConhecidasNaLista() {
                Map<String, List<String>> racas = servicoDogApi.buscarTodasRacasComoObjeto().getRacas();

                assertAll("Validacoes de racas conhecidas",
                                () -> assertTrue(racas.containsKey("labrador"), "Deve conter a raca labrador"),
                                () -> assertTrue(racas.containsKey("bulldog"), "Deve conter a raca bulldog"),
                                () -> assertTrue(racas.containsKey("beagle"), "Deve conter a raca beagle"),
                                () -> assertTrue(racas.containsKey("retriever"), "Deve conter a raca retriever"));
        }

        @Test
        @Order(4)
        @DisplayName("Deve validar sub-racas quando disponiveis")
        @Tag("dados")
        void deveValidarSubRacasQuandoDisponiveis() {
                Map<String, List<String>> racas = servicoDogApi.buscarTodasRacasComoObjeto().getRacas();

                assertAll("Validacoes de sub-racas",
                                () -> assertTrue(racas.containsKey("terrier"), "API deve conter a raca terrier"),
                                () -> assertFalse(racas.get("terrier").isEmpty(), "Terrier deve ter sub-racas"),
                                () -> assertTrue(racas.containsKey("spaniel"), "API deve conter a raca spaniel"),
                                () -> assertFalse(racas.get("spaniel").isEmpty(), "Spaniel deve ter sub-racas"));
        }

        @Test
        @Order(5)
        @DisplayName("Deve responder em tempo aceitavel")
        @Tag("performance")
        void deveResponderEmTempoAceitavel() {
                Response resposta = servicoDogApi.buscarTodasRacas();

                assertAll("Validacoes de performance",
                                () -> assertEquals(200, resposta.getStatusCode(), "Deve retornar sucesso"),
                                () -> assertTrue(resposta.getTime() < 3000,
                                                String.format("Tempo de resposta deve ser menor que 3000ms, mas foi %dms",
                                                                resposta.getTime())));
        }

        @Test
        @Order(6)
        @DisplayName("Deve retornar headers corretos")
        @Tag("headers")
        void deveRetornarHeadersCorretos() {
                Response resposta = servicoDogApi.buscarTodasRacas();

                assertAll("Validacoes dos headers",
                                () -> assertEquals(200, resposta.getStatusCode(), "Status code deve ser 200"),
                                () -> assertNotNull(resposta.getHeader("Content-Type"),
                                                "Header Content-Type deve estar presente"),
                                () -> assertTrue(resposta.getHeader("Content-Type").contains("application/json"),
                                                "Content-Type deve ser application/json"));
        }

        @Test
        @Order(7)
        @DisplayName("Deve manter consistencia entre multiplas chamadas")
        @Tag("consistencia")
        void deveManterConsistenciaEntreMultiplasChamadas() {
                RespostaListaRacas primeiraChamada = servicoDogApi.buscarTodasRacasComoObjeto();
                RespostaListaRacas segundaChamada = servicoDogApi.buscarTodasRacasComoObjeto();

                assertAll("Validacoes de consistencia",
                                () -> assertEquals(primeiraChamada.getStatus(), segundaChamada.getStatus(),
                                                "Status deve ser consistente"),
                                () -> assertEquals(primeiraChamada.getRacas().size(), segundaChamada.getRacas().size(),
                                                "Numero de racas deve ser consistente"),
                                () -> assertEquals(primeiraChamada.getRacas().keySet(),
                                                segundaChamada.getRacas().keySet(),
                                                "Lista de racas deve ser identica"));
        }

        @Test
        @Order(8)
        @DisplayName("Deve retornar nomes de racas em formato valido")
        @Tag("contrato")
        void deveRetornarNomesDeRacasEmFormatoValido() {
                Map<String, List<String>> racas = servicoDogApi.buscarTodasRacasComoObjeto().getRacas();

                for (String nomeRaca : racas.keySet()) {
                        assertAll("Validacoes do formato do nome da raca: " + nomeRaca,
                                        () -> assertTrue(nomeRaca.equals(nomeRaca.toLowerCase()),
                                                        "Nome da raca deve estar em lowercase: " + nomeRaca),
                                        () -> assertFalse(nomeRaca.contains(" "),
                                                        "Nome da raca nao deve conter espacos: " + nomeRaca),
                                        () -> assertFalse(nomeRaca.isEmpty(),
                                                        "Nome da raca nao deve estar vazio"));
                }
        }
}
