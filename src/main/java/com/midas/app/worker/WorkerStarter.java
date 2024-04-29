package com.midas.app.worker;

import com.midas.app.activities.AccountActivityImpl;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.CreateAccountWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkerStarter {

  private final AccountActivityImpl accountActivityImpl;

  @Autowired
  public WorkerStarter(AccountActivityImpl accountActivityImpl) {
    this.accountActivityImpl = accountActivityImpl;
  }

  public void startWorker() {
    // Connect to Temporal service
    WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    WorkflowClient client = WorkflowClient.newInstance(service);

    // Create a worker factory that can be used to create workers
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Create a worker that listens on the task queue
    Worker worker = factory.newWorker(CreateAccountWorkflow.QUEUE_NAME);

    // Register the workflow implementation with the worker
    worker.registerWorkflowImplementationTypes(CreateAccountWorkflowImpl.class);

    // Register activity implementations with the worker
    worker.registerActivitiesImplementations(accountActivityImpl);

    // Start all the workers created by this factory
    factory.start();
  }
}
