package com.steve.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;
import java.util.logging.Logger;

public class TestLogger implements TestWatcher {
    private static final Logger logger = Logger.getLogger(TestLogger.class.getName());

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("Test passed: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.severe("Test failed: " + context.getDisplayName() + ", Cause: " + cause.getMessage());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.warning("Test aborted: " + context.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logger.warning("Test disabled: " + context.getDisplayName() + ", Reason: " + reason.orElse("No reason provided"));
    }
}

