package com.redhat.internal.kie;

import org.jbpm.services.api.ProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.services.api.KieServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ExceptionTest {

    @Autowired
    private ProcessService processService;

    @Autowired
    private KieServer kieServer;

    private static final String CONTAINER_ID = "LoanApprovalDMN";

    private static final Logger log = LoggerFactory.getLogger(ExceptionTest.class);

    @Test
    public void exceptionTest() {

        KieContainerResource kieContainer = new KieContainerResource(new ReleaseId("com.cs", CONTAINER_ID, "1.0.0-SNAPSHOT"));
        kieServer.createContainer(CONTAINER_ID, kieContainer);
        processService.startProcess(CONTAINER_ID, "LoanApprovalDMN.loanapprovalprocess");
    }
}
