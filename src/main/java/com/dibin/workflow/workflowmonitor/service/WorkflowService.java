package com.dibin.workflow.workflowmonitor.service;

import com.dibin.workflow.workflowmonitor.dao.WorkflowDao;
import com.dibin.workflow.workflowmonitor.model.State;
import com.dibin.workflow.workflowmonitor.model.Workflow;
import com.dibin.workflow.workflowmonitor.model.WorkflowResult;
import com.dibin.workflow.workflowmonitor.rest.WorkflowRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkflowService {

    private WorkflowDao workflowDao;

    private WorkflowRestService restService;

    private Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    public void retrieveAndSaveWorkflow() {
        for(State state : State.values()) {
            try {
                WorkflowResult[] workflows = restService.getorkflows(state);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void saveWorkflow(WorkflowResult[] workflows) throws Exception{
        for(WorkflowResult workflowResult : workflows) {
            Workflow workflow = restService.getWorkflow(restService.getWorkflowUri(workflowResult.getUri()));
            int modelId = workflowDao.getModelId(workflow.getModel());
            if(modelId == -1) {
                modelId = workflowDao.saveModel(workflow.getModel());
            }

            workflow.setModelId(modelId);
            int stateId = State.valueOf(workflow.getState()).ordinal() + 1;
            long workflowId = workflowDao.isWorkflowSaved(workflow.getId());
            if(workflowId == -1) {
                workflowId = workflowDao.saveWorkflow(workflow);
            }

            workflowDao.saveWorkflowDetails(workflowId, stateId, modelId);
        }
    }
}
