package org.tinyg.service;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import java.util.Collections;
import java.util.List;

public class CircuitBreakerFallback implements FallbackHandler<List<String>> {
    @Override
    public List<String> handle(ExecutionContext executionContext) {
        return getValidTrackingIds();
    }

    public List<String> getValidTrackingIds() {
        return Collections.singletonList("Has reached fallback for circuit breaker. You can provide and alternative " +
                "here.");
    }
}
