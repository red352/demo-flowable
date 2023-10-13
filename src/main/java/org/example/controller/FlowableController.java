package org.example.controller;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LuoYunXiao
 * @since 2023/10/13 15:00
 */
@RestController
public class FlowableController {

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping
    public ResponseEntity<byte[]> getProcessImage() throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("holidayRequest").singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(inputStream.readAllBytes());
    }
}
