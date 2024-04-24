package com.midas.app.workflows;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface UserSignupWorkflow {
  @WorkflowMethod
  String createUser(String email);
}
