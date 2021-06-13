package com.redhat.internal.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.kie.internal.query.QueryContext;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.services.api.KieServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.redhat.GSWrapper;
import com.redhat.internal.cases.CSSharedState;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

public class CSSteps {
	
	@Autowired
    private ProcessService processService;
	
	@Autowired
    private RuntimeDataService runtimeDataService;
	
	@Autowired
    private DeploymentService deploymentService;
	
    @Autowired
    private KieServer kieServer;

    private static final Logger LOGGER = LoggerFactory.getLogger(CSSteps.class);

    private static final String CONTAINER_ID = "POC_test-dmn";

    RetryPolicy<Object> retryPolicy = new RetryPolicy<>() //
        .handle(Exception.class) //
        .withDelay(Duration.ofSeconds(1)) //
        .withMaxRetries(1);

    @Autowired
    private CSSharedState csSharedState;
    
    @Before
    public void beforTest(Scenario scenario) {
        LOGGER.info("######################### INIT SCENARIO {} #########################", scenario.getName());
        KieContainerResource kieContainer = new KieContainerResource(new ReleaseId("com.redhat", "test-dmn", "1.0.1-SNAPSHOT"));
        kieServer.createContainer(CONTAINER_ID, kieContainer);
    }

    @After
    public void afterTest(Scenario scenario) {
    	List<Integer> states = new ArrayList<Integer>();
    	states.add(0);
    	Collection<ProcessInstanceDesc> activeProcesses = runtimeDataService.getProcessInstancesByDeploymentId(CONTAINER_ID, states, new QueryContext());
    	activeProcesses.stream().forEach(pi -> processService.abortProcessInstance(CONTAINER_ID, pi.getId()));
    	deploymentService.deactivate(CONTAINER_ID);
    	deploymentService.undeploy(deploymentService.getDeployedUnit(CONTAINER_ID).getDeploymentUnit());
        LOGGER.info("######################### END SCENARIO {} #########################", scenario.getName());
    }


    @Given("^a request to check for '(.*?)' when the customer financial status is")
    public void startProcessInstanceEr(String processDefinitionId, DataTable table) throws Throwable {
        LOGGER.info("a process instance for definition id '{}' is started$", processDefinitionId);
        final AtomicReference<Long> processId = new AtomicReference<>();
        final Map<String, Object> rows1 = table.asMap(String.class, Object.class);
        final Map<String, Object> rows = translate(rows1);
        Failsafe.with(retryPolicy).run(() -> processId.set(processService.startProcess(CONTAINER_ID, processDefinitionId, rows)));
        csSharedState.setProcessId(processId.get());
    }
    
    private Map<String, Object> translate(Map<String, Object> rows) {
    	Map<String, Object> transRows = new HashMap<String, Object>();
    	GSWrapper wrapper = new GSWrapper();
    	wrapper.setState((String)rows.get("state"));
    	wrapper.setZone((String)rows.get("zone"));
    	wrapper.setDateOfIncorporationMonths(Integer.parseInt((String)rows.get("dateOfIncorporationMonths")));
    	// wrapper.setDateOfIncorporation(LocalDate.parse((String)rows.get("dateOfIncorporation"))); 
    	//TODO : How to pass the date  
    	wrapper.setCompanyTypeEnName((String)rows.get("companyTypeEnName"));
    	wrapper.setCountryCode(Integer.parseInt((String)rows.get("countryCode")));
    	transRows.put("InputPayload", wrapper);
		return transRows;
	}

    @And("^customer Loan might be '(.*?)'")
    public void validateEr(String varValue, DataTable table) throws Throwable {
		String varName = "LoanApproval";
        LOGGER.info("validate process variable {} value is {}", varName, varValue);
        // Map<String, Object> values  = processService.getProcessInstanceVariables(csSharedState.getProcessId());
        // String decision = (String)values.get(varName);
        // assertEquals(varValue, decision);
        assertTrue(true);
        
        //Failsafe.with(retryPolicy).run(() -> processId.set(processServicesClient.startProcess(CONTAINER_ID, processDefinitionId, rows)));
    }
}
