package com.example.pets.proxy;

import com.example.pets.api.ManagesPetsApi;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterClientHeaders
@CircuitBreaker
@RegisterRestClient(configKey = "bird-service-client")
public interface ManageBirdProxy extends ManagesBirdApi {
}