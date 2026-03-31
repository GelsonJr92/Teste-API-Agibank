package com.agibank.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class ExtentReportsExtension implements BeforeEachCallback, AfterAllCallback, TestWatcher {

    @Override
    public void beforeEach(ExtensionContext context) {
        String testName = context.getDisplayName();
        String description = context.getTags().isEmpty() ? "" : "Tags: " + String.join(", ", context.getTags());
        ExtentReportsManager.createTest(testName, description);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        ExtentTest test = ExtentReportsManager.getTest();
        if (test != null) {
            test.pass("Teste passou com sucesso");
        }
        ExtentReportsManager.flush();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        ExtentTest test = ExtentReportsManager.getTest();
        if (test != null) {
            test.fail(cause);
        }
        ExtentReportsManager.flush();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        ExtentTest test = ExtentReportsManager.getTest();
        if (test != null) {
            test.log(Status.WARNING, "Teste abortado: " + cause.getMessage());
        }
        ExtentReportsManager.flush();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String name = context.getDisplayName();
        ExtentReportsManager.getInstance()
                .createTest(name)
                .skip("Teste desabilitado: " + reason.orElse("sem motivo informado"));
        ExtentReportsManager.flush();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        ExtentReportsManager.flush();
    }
}
