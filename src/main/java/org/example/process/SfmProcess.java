package org.example.process;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;

import java.util.List;

/**
 * @author LuoYunXiao
 * @since 2023/10/19 16:06
 */
public class SfmProcess {

    public static BpmnModel create() {


        // 开始
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");

        //用户提交审批
        UserTask userTask = new UserTask();
        userTask.setId("approveTask");
        userTask.setName("Approve or reject request");
        userTask.setCandidateGroups(List.of("managers"));

        // 判断gateway
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId("decision");
        exclusiveGateway.setName("排他网关");

        SequenceFlow decisionAllow = new SequenceFlow("decision", "externalSystemCall");
        decisionAllow.setConditionExpression("${approved}");

        SequenceFlow decisionDeny = new SequenceFlow("decision", "sendRejectionMail");
        decisionDeny.setConditionExpression("${!approved}");


        // 通过任务
        ServiceTask externalSystemCall = new ServiceTask();
        externalSystemCall.setId("externalSystemCall");
        externalSystemCall.setName("Enter holidays in external system");
        externalSystemCall.setImplementation("org.flowable.CallExternalSystemDelegate");
        externalSystemCall.setImplementationType("class");

        UserTask holidayApprovedTask = new UserTask();
        holidayApprovedTask.setId("holidayApprovedTask");
        holidayApprovedTask.setName("Holiday approved");
        holidayApprovedTask.setAssignee("${employee}");


        // 不通过任务
        ServiceTask sendRejectionMail = new ServiceTask();
        sendRejectionMail.setId("sendRejectionMail");
        sendRejectionMail.setName("Send out rejection email");
        sendRejectionMail.setImplementation("org.flowable.SendRejectionMail");
        sendRejectionMail.setImplementationType("class");

        // 结束事件
        EndEvent approveEnd = new EndEvent();
        approveEnd.setId("approveEnd");
        approveEnd.setName("结束");

        // 结束事件
        EndEvent rejectEnd = new EndEvent();
        rejectEnd.setId("rejectEnd");
        rejectEnd.setName("结束");


        Process process = new Process();
        process.setId("holiday-java");
        process.setName("使用java自定义流程");
        process.setExecutable(true);


        return CustomProcessFactory.builder()
                .process(process)
                .bpmnModel(new BpmnModel())
                .addFlowElement(startEvent)
                .addFlowElement(new SequenceFlow("startEvent", "approveTask"))
                .addFlowElement(userTask)
                .addFlowElement(new SequenceFlow("approveTask", "decision"))
                .addFlowElement(exclusiveGateway)
                .addFlowElement(decisionAllow)
                .addFlowElement(decisionDeny)
                .addFlowElement(externalSystemCall)
                .addFlowElement(new SequenceFlow("externalSystemCall", "holidayApprovedTask"))
                .addFlowElement(approveEnd)
                .addFlowElement(holidayApprovedTask)
                .addFlowElement(new SequenceFlow("holidayApprovedTask", "approveEnd"))
                .addFlowElement(sendRejectionMail)
                .addFlowElement(rejectEnd)
                .addFlowElement(new SequenceFlow("sendRejectionMail", "rejectEnd"))
                .build();
    }
}
