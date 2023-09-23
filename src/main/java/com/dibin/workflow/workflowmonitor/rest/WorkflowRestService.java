package com.dibin.workflow.workflowmonitor.rest;

import com.dibin.workflow.workflowmonitor.Constants;
import com.dibin.workflow.workflowmonitor.model.State;
import com.dibin.workflow.workflowmonitor.model.Workflow;
import com.dibin.workflow.workflowmonitor.model.WorkflowResult;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowRestService {

    private Logger logger = LoggerFactory.getLogger(WorkflowRestService.class);

    public WorkflowResult[] getWorkflows(State state) throws Exception {
        String url = getUrl(state);
        logger.debug("url = {}", url);
        String stateResponse = HttpUtility.sendGet(url);
        logger.debug("stateResponse = {}", stateResponse);
        Gson gson = new Gson();
        WorkflowResult[] workflowResults = gson.fromJson(stateResponse, WorkflowResult[].class);
        return workflowResults;
    }

    public Workflow getWorkflow(String workflowUrl) throws Exception {
        String response = HttpUtility.sendGet(workflowUrl);
        Gson gson = new Gson();
        logger.debug("response = {}", response);
        Workflow wf = gson.fromJson(response, Workflow.class);
        return wf;
    }

    public String getWorkflowUri(String workflowId) {
        return Constants.AEM_DOMAIN + "/" + workflowId + ".json";
    }

    private String getUrl(State state) {
        String url = Constants.WORKFLOW_URL;
        return url.replace("${STATE}", state.toString());
    }
}
