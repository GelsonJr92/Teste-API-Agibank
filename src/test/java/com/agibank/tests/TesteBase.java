package com.agibank.tests;

import com.agibank.config.ConfiguracaoApi;
import com.agibank.reports.ExtentReportsExtension;
import com.agibank.services.ServicoDogApi;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ExtentReportsExtension.class)
public abstract class TesteBase {

    protected ServicoDogApi servicoDogApi;
    private long inicioTeste;

    @BeforeAll
    static void configurarAmbiente() {
        RestAssured.baseURI = ConfiguracaoApi.BASE_URL;
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails()
                        .enablePrettyPrinting(true));
    }

    @BeforeEach
    void inicializarServicos(TestInfo testInfo) {
        servicoDogApi = new ServicoDogApi();
        inicioTeste = System.currentTimeMillis();
        System.out.println("Executando: " + converterNomeMetodo(testInfo.getTestMethod().get().getName()));
    }

    @AfterEach
    void finalizarTeste(TestInfo testInfo) {
        long duracaoMs = System.currentTimeMillis() - inicioTeste;
        System.out.println("Concluido: " + converterNomeMetodo(testInfo.getTestMethod().get().getName()) + " ("
                + duracaoMs + "ms)");
    }

    private String converterNomeMetodo(String nomeMetodo) {
        String resultado = nomeMetodo
                .replaceAll("([A-Z])", " $1")
                .replaceAll("\\s+", " ")
                .trim();
        return resultado.substring(0, 1).toUpperCase() + resultado.substring(1);
    }
}
