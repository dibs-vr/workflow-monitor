package com.dibin.workflow.workflowmonitor.dao;

import com.dibin.workflow.workflowmonitor.model.Workflow;

public interface WorkflowDao {

    public long saveWorkflow(Workflow workflow);

    public int getModelId(String modelName);

    public int saveModel(String modelName);

    public void saveWorkflowDetails(long workflowID, int stateId, int modelId);

    public int isWorkflowSaved(String workflowId);

    public boolean isWorkflowDetailsSaved(long workflowId, int stateId);

}
