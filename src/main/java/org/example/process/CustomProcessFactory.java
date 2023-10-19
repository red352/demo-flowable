package org.example.process;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LuoYunXiao
 * @since 2023/10/13 9:34
 */
@Slf4j
public class CustomProcessFactory {

    private CustomProcessFactory() {
    }

    public static CustomProcessBuilder builder() {
        return new CustomProcessBuilder();
    }


    public static class CustomProcessBuilder {
        private BpmnModel bpmnModel;

        private Process process;
        private final Set<FlowElement> flowElements = new HashSet<>();

        public CustomProcessBuilder process(Process process) {
            this.process = process;
            return this;
        }

        public CustomProcessBuilder bpmnModel(BpmnModel bpmnModel) {
            this.bpmnModel = bpmnModel;
            return this;
        }

        public CustomProcessBuilder addFlowElement(FlowElement flowElement) {
            flowElements.add(flowElement);
            return this;
        }

        // build
        public BpmnModel build() {
            for (FlowElement flowElement : this.flowElements) {
                process.addFlowElement(flowElement);
            }
            bpmnModel.addProcess(process);
            return bpmnModel;
        }
    }


}
