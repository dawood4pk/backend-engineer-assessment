package com.midas.app.services;

import com.midas.app.worker.WorkerStarter;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class TemporalWorkerInitializer {

  private final WorkerStarter workerStarter;

  public TemporalWorkerInitializer(WorkerStarter workerStarter) {
    this.workerStarter = workerStarter;
  }

  @PostConstruct
  public void initialize() {
    // Start the WorkerStarter in a new thread so it doesn't block the main application startup.
    Thread workerThread = new Thread(() -> workerStarter.startWorker());
    workerThread.start();
  }
}
