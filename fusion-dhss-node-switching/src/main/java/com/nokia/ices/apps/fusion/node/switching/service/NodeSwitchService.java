package com.nokia.ices.apps.fusion.node.switching.service;

import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitch;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCmd;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleCmd;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleHistory;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleStepExecute;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchStepExecute;


public interface NodeSwitchService {
	
	List<NodeSwitchSingleHistory> getNodeSwitchSingleHistory(Map<String, Object> params);
	
	List<NodeSwitchCmd> getStepList(Map<String, Object> params);
	
	List<NodeSwitchSingleCmd> getSingleStepList(Map<String, Object> params);
	
	List<NodeSwitchSingleStepExecute> getNodeSwitchSingleStepExecute(Map<String, Object> params);
	
	List<NodeSwitch> getNodeSwitchStepList(Map<String, Object> params);
	
	List<NodeSwitchStepExecute> getNodeSwitchStepExecuteList(Map<String, Object> params);


}
