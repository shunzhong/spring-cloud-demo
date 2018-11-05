package com.learn.springcloud.activiti.cmd;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventDispatcher;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.bpmn.listener.ListenerNotificationHelper;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.Activiti5Util;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLinkType;

import java.util.List;
import java.util.Map;

/**
 * activiti 任意节点跳转功能
 */
public class Jump2TargetFlowNodeCommand extends NeedsActiveTaskCmd<Void> {
    private static final long serialVersionUID = 6722836649624248068L;


    /** 工作流类型 */
    public static final String WORK_FLOW_TYPE_AGREE = "agree";
    public static final String WORK_FLOW_TYPE_REJECT = "reject";
    public static final String WORK_FLOW_TYPE_BACK = "back";
    public static final String WORK_FLOW_TYPE_JUMP = "jump";

    protected Map<String, Object> variableMap;

    protected String targetId;

    protected boolean localScope;

    protected Map<String, Object> transientVariableMap;

    /** 任务类型：（jump-任务通过， reject-任务拒绝, 默认为“jump”-通过） */
    private String taskType = WORK_FLOW_TYPE_JUMP;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Jump2TargetFlowNodeCommand(String taskId, Map<String, Object> variableMap, String targetId, Map<String, Object> transientVariableMap, String taskType) {
        super(taskId);
        this.variableMap = variableMap;
        this.targetId = targetId;
        this.localScope = false;
        this.transientVariableMap = transientVariableMap;
        this.taskType = taskType;
    }

    public Jump2TargetFlowNodeCommand(String taskId, Map<String, Object> variableMap, String targetId, String taskType) {
        super(taskId);
        this.localScope = false;
        this.variableMap = variableMap;
        this.targetId = targetId;
        this.taskType = taskType;
    }

    public Jump2TargetFlowNodeCommand(String taskId, Map<String, Object> variableMap, String taskType) {
        super(taskId);
        this.localScope = false;
        this.variableMap = variableMap;
        this.taskType = taskType;
    }

    @Override
    protected Void execute(CommandContext commandContext, TaskEntity taskEntity) {
        //判断是否Activiti5的流程
        if(isActiviti5(commandContext, taskEntity.getProcessDefinitionId())){
            Activiti5Util.getActiviti5CompatibilityHandler().completeTask(taskEntity, variableMap, localScope);
            return null;
        }

        setVariableMap(taskEntity);
        jumpTask(commandContext, taskEntity, getTaskType());
        return null;
    }

    /**
     * 判断是否Activiti5的流程
     * @param commandContext command上下文
     * @param processDefinitionId 流程定义ID
     * @return
     */
    private boolean isActiviti5(CommandContext commandContext, String processDefinitionId){
        if(null == processDefinitionId){
            return false;
        }
        return Activiti5Util.isActiviti5ProcessDefinitionId(commandContext, processDefinitionId);
    }

    private void setVariableMap(TaskEntity taskEntity){
        setTaskVariableMap(taskEntity);
        setTransientVariableMap(taskEntity);
    }

    private void setTaskVariableMap(TaskEntity taskEntity){
        if(variableMap == null || taskEntity == null){
            return;
        }

        if(localScope){
            taskEntity.setVariablesLocal(variableMap);
        }else if(taskEntity.getExecutionId() != null){
            taskEntity.setExecutionVariables(variableMap);
        }else {
            taskEntity.setVariables(variableMap);
        }
        return;
    }

    private void setTransientVariableMap(TaskEntity taskEntity){
        if (transientVariableMap == null || taskEntity == null){
            return;
        }

        if (localScope) {
            taskEntity.setTransientVariablesLocal(transientVariableMap);
        } else {
            taskEntity.setTransientVariables(transientVariableMap);
        }
    }

    protected void jumpTask(CommandContext commandContext, TaskEntity taskEntity, String taskType){
        // Task complete logic
        if(DelegationState.PENDING == taskEntity.getDelegationState()){
            throw new ActivitiException("A delegated task cannot be completed, but should be resolved instead.");
        }

        //获取流程引擎配置
        ProcessEngineConfigurationImpl configuration = commandContext.getProcessEngineConfiguration();

        ListenerNotificationHelper listenerNotificationHelper = configuration.getListenerNotificationHelper();
        listenerNotificationHelper.executeTaskListeners(taskEntity, TaskListener.EVENTNAME_COMPLETE);

        String authenticateUserId = Authentication.getAuthenticatedUserId();

        // 用户权限校验
        if(null != authenticateUserId && null != taskEntity.getProcessInstanceId()){
            ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
            ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getProcessInstanceId());

            IdentityLinkEntityManager identityLinkEntityManager = commandContext.getIdentityLinkEntityManager();
            identityLinkEntityManager.involveUser(executionEntity, authenticateUserId, IdentityLinkType.PARTICIPANT);
        }


        // activiti 事件派发
        ActivitiEventDispatcher activitiEventDispatcher = configuration.getEventDispatcher();
        if(activitiEventDispatcher.isEnabled()){
            ActivitiEvent activitiEvent;
            if(variableMap == null){
                activitiEvent = ActivitiEventBuilder.createEntityEvent(ActivitiEventType.TASK_COMPLETED, taskEntity);
            }else {
                activitiEvent = ActivitiEventBuilder.createEntityWithVariablesEvent(ActivitiEventType.TASK_COMPLETED, taskEntity, variableMap, localScope);
            }
            activitiEventDispatcher.dispatchEvent(activitiEvent);
        }

        commandContext.getTaskEntityManager().deleteTask(taskEntity, null, false, false);

        //continue process(if it is not a standalone task)
        if(taskEntity.getExecutionId() != null){
            ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
            ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());

            UserTask userTask = (UserTask) executionEntity.getCurrentFlowElement();
            List<SequenceFlow> outGoingFlows = userTask.getOutgoingFlows();

            if(WORK_FLOW_TYPE_REJECT.equals(taskType)){
                targetId = "end";
            }

            SequenceFlow sequenceFlow = new SequenceFlow(outGoingFlows.get(0).getSourceRef(), targetId);
            sequenceFlow.setTargetFlowElement(getFlowElement(taskEntity.getProcessDefinitionId(), targetId));
            sequenceFlow.setSourceFlowElement(userTask);
            executionEntity.setCurrentFlowElement(sequenceFlow);

            // 先后驱动流程
//            commandContext.getAgenda()
            Context.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);
        }
    }

    private FlowElement getFlowElement(String processDefinitionId, String flowElementId){
        if(null == processDefinitionId){
            return null;
        }

        Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        return process.getFlowElement(flowElementId, true);
    }
}
