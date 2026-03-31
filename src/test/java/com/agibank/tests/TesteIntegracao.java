package com.agibank.tests;

import com.agibank.models.RespostaImagemAleatoria;
import com.agibank.models.RespostaImagensRaca;
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
@Tag("integracao")
@DisplayName("Testes de Integracao Completos")
public class TesteIntegracao extends TesteBase {

        @Test
        @Order(1)
        @DisplayName("Deve executar fluxo completo de busca de imagens por raca")
        @Tag("smoke")
        @Tag("positivo")
        void deveExecutarFluxoCompletoDeBuscaDeImagensPorRaca() {
                RespostaListaRacas listaRacas = servicoDogApi.buscarTodasRacasComoObjeto();
                assertEquals("success", listaRacas.getStatus(), "Busca de racas deve ter sucesso");
                assertFalse(listaRacas.getRacas().isEmpty(), "Deve ter racas disponiveis");

                String racaSelecionada = listaRacas.getRacas().keySet().iterator().next();
                assertNotNull(racaSelecionada, "Deve conseguir selecionar uma raca");

                RespostaImagensRaca imagensRaca = servicoDogApi.buscarImagensPorRacaComoObjeto(racaSelecionada);

                assertAll("Validacoes do fluxo completo",
                                () -> assertEquals("success", imagensRaca.getStatus(),
                                                "Busca de imagens deve ter sucesso"),
                                () -> assertFalse(imagensRaca.getImagens().isEmpty(), "Deve retornar imagens da raca"),
                                () -> assertTrue(imagensRaca.getImagens().get(0).contains(racaSelecionada),
                                                "URLs devem conter o nome da raca selecionada"));
        }

        @Test
        @Order(2)
        @DisplayName("Deve comparar imagens aleatorias gerais e especificas")
        @Tag("comparacao")
        void deveCompararImagemAleatoriaGeralVsEspecifica() {
                RespostaImagemAleatoria imagemGeral = servicoDogApi.buscarImagemAleatoriaComoObjeto();
                assertEquals("success", imagemGeral.getStatus(), "Imagem aleatoria geral deve ter sucesso");

                String raca = "labrador";
                Response imagemEspecifica = servicoDogApi.buscarImagemAleatoriaPorRaca(raca);
                assertEquals(200, imagemEspecifica.getStatusCode(), "Imagem de raca especifica deve ter sucesso");

                String urlGeral = imagemGeral.getImagemUrl();
                String urlEspecifica = imagemEspecifica.jsonPath().getString("message");

                assertAll("Validacoes de comparacao de imagens",
                                () -> assertTrue(urlGeral.contains("dog.ceo"), "URL geral deve ser do dog.ceo"),
                                () -> assertTrue(urlEspecifica.contains("dog.ceo"),
                                                "URL especifica deve ser do dog.ceo"),
                                () -> assertTrue(urlEspecifica.contains(raca),
                                                "URL especifica deve conter nome da raca"),
                                () -> assertTrue(urlGeral.matches(".*\\.(jpg|jpeg|png)$"), "URL geral deve ser imagem"),
                                () -> assertTrue(urlEspecifica.matches(".*\\.(jpg|jpeg|png)$"),
                                                "URL especifica deve ser imagem"));
        }

        @Test
        @Order(3)
        @DisplayName("Deve manter consistencia entre lista de racas e busca individual")
        @Tag("consistencia")
        void deveManterConsistenciaEntreDados() {
                RespostaListaRacas listaRacas = servicoDogApi.buscarTodasRacasComoObjeto();
                Map<String, List<String>> racas = listaRacas.getRacas();

                for (String raca : new String[] { "labrador", "bulldog", "beagle" }) {
                        assertTrue(racas.containsKey(raca), "Raca '" + raca + "' deve estar na lista da API");
                        Response imagensRaca = servicoDogApi.buscarImagensPorRaca(raca);

                        assertAll("Validacoes de consistencia para raca: " + raca,
                                        () -> assertEquals(200, imagensRaca.getStatusCode(),
                                                        "Raca " + raca + " listada deve retornar imagens"),
                                        () -> assertEquals("success", imagensRaca.jsonPath().getString("status"),
                                                        "Status deve ser success para raca existente"));
                }
        }

        @Test
        @Order(4)
        @DisplayName("Deve manter performance aceitavel em multiplas chamadas")
        @Tag("performance")
        void deveManterPerformanceAceitavelEmMultiplasChamadas() {
                long tempoInicio = System.currentTimeMillis();

                for (int i = 0; i < 5; i++) {
                        Response resposta = servicoDogApi.buscarImagemAleatoria();
                        assertEquals(200, resposta.getStatusCode(), "Todas as chamadas devem ter sucesso");
                }

                long tempoTotal = System.currentTimeMillis() - tempoInicio;

                assertAll("Validacoes de performance sequencial",
                                () -> assertTrue(tempoTotal < 15000,
                                                String.format("5 chamadas devem completar em menos de 15s, mas levaram %dms",
                                                                tempoTotal)),
                                () -> assertTrue(tempoTotal / 5 < 5000,
                                                "Tempo medio por chamada deve ser menor que 5s"));
        }

        @Test
        @Order(5)
        @DisplayName("Deve manter estabilidade em diferentes cenarios")
        @Tag("robustez")
        @Tag("negativo")
        void deveManterEstabilidadeEmDiferentesCenarios() {
                Response racasValidas = servicoDogApi.buscarTodasRacas();
                assertEquals(200, racasValidas.getStatusCode(), "Requisicao padrao deve funcionar");

                Response imagemAleatoria = servicoDogApi.buscarImagemAleatoria();
                assertEquals(200, imagemAleatoria.getStatusCode(), "Imagem aleatoria deve funcionar");

                Response racaEspecifica = servicoDogApi.buscarImagensPorRaca("labrador");
                assertEquals(200, racaEspecifica.getStatusCode(), "Raca especifica deve funcionar");

                Response racaInvalida = servicoDogApi.buscarImagensPorRaca("racainexistente");
                assertTrue(racaInvalida.getStatusCode() >= 400, "Raca invalida deve retornar erro");

                assertAll("Validacoes de robustez",
                                () -> assertEquals("success", racasValidas.jsonPath().getString("status")),
                                () -> assertEquals("success", imagemAleatoria.jsonPath().getString("status")),
                                () -> assertEquals("success", racaEspecifica.jsonPath().getString("status")),
                                () -> assertEquals("error", racaInvalida.jsonPath().getString("status")));
        }

        @Test
        @Order(6)
        @DisplayName("Deve retornar sempre conteudo no formato correto")
        @Tag("contrato")
        void deveRetornarSempreConteudoNoFormatoCorreto() {
                Response[] respostas = {
                                servicoDogApi.buscarTodasRacas(),
                                servicoDogApi.buscarImagemAleatoria(),
                                servicoDogApi.buscarImagensPorRaca("beagle")
                };

                for (Response resposta : respostas) {
                        assertAll("Validacoes de formato para endpoint",
                                        () -> assertEquals(200, resposta.getStatusCode(), "Status deve ser 200"),
                                        () -> assertTrue(
                                                        resposta.getHeader("Content-Type").contains("application/json"),
                                                        "Content-Type deve ser JSON"),
                                        () -> assertNotNull(resposta.jsonPath().getString("status"),
                                                        "Deve ter campo status"),
                                        () -> assertNotNull(resposta.jsonPath().get("message"),
                                                        "Deve ter campo message"),
                                        () -> assertTrue(resposta.jsonPath().getString("status").equals("success"),
                                                        "Status deve ser success"));
                }
        }
}
