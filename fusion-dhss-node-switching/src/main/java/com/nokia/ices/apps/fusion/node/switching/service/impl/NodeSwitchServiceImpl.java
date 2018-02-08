package com.nokia.ices.apps.fusion.node.switching.service.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.node.switching.service.NodeSwitchService;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitch;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCmd;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleCmd;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleHistory;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleStepExecute;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchStepExecute;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchCmdRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchSingleCmdRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchSingleHistoryRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchSingleStepExecuteRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchStepExecuteRepository;

@Service("nodeSwitchService")
public class NodeSwitchServiceImpl implements NodeSwitchService {
	
	public static final String APP_MODULE = "板卡倒换";

	@Autowired
	private NodeSwitchRepository nodeSwitchRepository;
	
	@Autowired
	private NodeSwitchStepExecuteRepository nodeSwitchStepExecuteRepository;
	
	@Autowired
	private NodeSwitchCmdRepository nodeSwitchCmdRepository;
	
	@Autowired
	private NodeSwitchSingleCmdRepository nodeSwitchSingleCmdRepository;
	
	@Autowired
	private NodeSwitchSingleHistoryRepository nodeSwitchSingleHistoryRepository;
	
	@Autowired
	private NodeSwitchSingleStepExecuteRepository nodeSwitchSingleStepExecuteRepository;
	
	@Override
	public List<NodeSwitchSingleStepExecute> getNodeSwitchSingleStepExecute(Map<String, Object> params) {
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<NodeSwitchSingleStepExecute> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, NodeSwitchSingleStepExecute.class);
		return nodeSwitchSingleStepExecuteRepository.findAll(spec);
	}

	@Override
	public List<NodeSwitchSingleHistory> getNodeSwitchSingleHistory(Map<String, Object> params) {
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<NodeSwitchSingleHistory> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, NodeSwitchSingleHistory.class);
		return nodeSwitchSingleHistoryRepository.findAll(spec);
	}



	@Override
	public List<NodeSwitchCmd> getStepList(Map<String, Object> params) {
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<NodeSwitchCmd> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, NodeSwitchCmd.class);
		return nodeSwitchCmdRepository.findAll(spec);
	}
	
	@Override
	public List<NodeSwitch> getNodeSwitchStepList(Map<String, Object> params) {
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<NodeSwitch> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, NodeSwitch.class);
		return nodeSwitchRepository.findAll(spec);
	}


	@Override
	public List<NodeSwitchSingleCmd> getSingleStepList(Map<String, Object> params) {
		
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<NodeSwitchSingleCmd> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, NodeSwitchSingleCmd.class);
		return nodeSwitchSingleCmdRepository.findAll(spec);
	}

	@Override
	public List<NodeSwitchStepExecute> getNodeSwitchStepExecuteList(Map<String, Object> params) {
		Map<String,SearchFilter> filter = SearchFilter.parse(params);
		Specification<NodeSwitchStepExecute> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, NodeSwitchStepExecute.class);
		return nodeSwitchStepExecuteRepository.findAll(spec);
	}

}
