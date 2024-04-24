package com.midas.app.config;

import com.midas.app.services.StripeService;
import com.midas.app.workflows.impl.UserSignupWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalWorkerSetup implements CommandLineRunner {

  @Autowired private StripeService stripeService;

  @Override
  public void run(String... args) throws Exception {
    WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    WorkflowClient client = WorkflowClient.newInstance(service);
    WorkerFactory factory = WorkerFactory.newInstance(client);
    Worker worker = factory.newWorker("UserSignupTaskQueue");

    worker.registerWorkflowImplementationTypes(UserSignupWorkflowImpl.class);

    factory.start();
  }
}
