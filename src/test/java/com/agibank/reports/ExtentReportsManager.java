package com.agibank.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportsManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();
    private static final String REPORT_DIR = "target/extent-reports/";

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = REPORT_DIR + "DogApi-Report-" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Dog API - Relatório de Testes");
            spark.config().setReportName("Agibank - Dog API Automation Tests");
            spark.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

            extentReports = new ExtentReports();
            extentReports.attachReporter(spark);
            extentReports.setSystemInfo("Aplicação", "Dog API");
            extentReports.setSystemInfo("Ambiente", "https://dog.ceo/api");
            extentReports.setSystemInfo("Executor", System.getProperty("user.name"));
            extentReports.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extentReports;
    }

    public static ExtentTest createTest(String name, String description) {
        ExtentTest test = getInstance().createTest(name, description);
        currentTest.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return currentTest.get();
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
