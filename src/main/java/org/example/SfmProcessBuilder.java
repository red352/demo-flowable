package org.example;

import org.flowable.bpmn.model.*;

import java.util.List;

/**
 * @author LuoYunXiao
 * @since 2023/10/13 9:34
 */
public class SfmProcessBuilder {

    private List<SequenceFlow> sequenceFlows;

    public static void main(String[] args) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");

        SequenceFlow sequenceFlow = new SequenceFlow();

        UserTask userTask = new UserTask();
        userTask.setId("approveTask");
        userTask.setName("Approve or reject request");
        userTask.setCandidateGroups(List.of("managers"));

        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId("decision");

        ServiceTask externalSystemCall = new ServiceTask();
        externalSystemCall.setId("externalSystemCall");
        externalSystemCall.setName("Enter holidays in external system");
//        externalSystemCall.setOperationRef();

        EndEvent approveEnd = new EndEvent();
        approveEnd.setId("approveEnd");
        EndEvent rejectEnd = new EndEvent();
        rejectEnd.setId("rejectEnd");
    }


}
