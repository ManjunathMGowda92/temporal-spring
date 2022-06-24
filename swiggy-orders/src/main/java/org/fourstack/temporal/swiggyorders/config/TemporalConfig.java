package org.fourstack.temporal.swiggyorders.config;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TemporalConfig {

    @Value("${temporal.service-address}")
    private String temporalServiceAddress;

    @Value("${temporal.namespace}")
    private String temporalNameSpace;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        WorkflowServiceStubsOptions stubsOptions = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalServiceAddress).build();
        return WorkflowServiceStubs.newConnectedServiceStubs(
                stubsOptions,
                Duration.ofSeconds(60)
        );
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(
                workflowServiceStubs,
                WorkflowClientOptions.newBuilder()
                        .setNamespace(temporalNameSpace).build()
        );
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }
}
