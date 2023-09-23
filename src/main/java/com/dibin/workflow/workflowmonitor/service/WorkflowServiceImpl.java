package com.dibin.workflow.workflowmonitor.service;

import com.dibin.workflow.workflowmonitor.Utility;
import com.dibin.workflow.workflowmonitor.dao.WorkflowDao;
import com.dibin.workflow.workflowmonitor.model.State;
import com.dibin.workflow.workflowmonitor.model.Workflow;
import com.dibin.workflow.workflowmonitor.model.WorkflowResult;
import com.dibin.workflow.workflowmonitor.rest.WorkflowRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WorkflowServiceImpl implements WorkflowService{

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private WorkflowRestService restService;

    private Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    public void retrieveAndSaveWorkflow() {
        Arrays.stream(State.values()).forEach(Utility.throwingConsumerWrapper(this::retrieveAndSaveWorkflow));
    }

    private void retrieveAndSaveWorkflow(State state) throws Exception {
        WorkflowResult[] workflows = restService.getWorkflows(state);
        saveWorkflow(workflows);
    }

    private void saveWorkflow(WorkflowResult[] workflows){
        Arrays.stream(workflows).forEach(Utility.throwingConsumerWrapper(this::saveWorkflow));
    }

    private void saveWorkflow(WorkflowResult workflowResult) throws Exception{
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
