package com.nokia.ices.apps.fusion.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import com.nokia.ices.apps.fusion.log.domain.WorkPackage;
import com.nokia.ices.apps.fusion.log.domain.WorkType;
import com.nokia.ices.apps.fusion.log.repository.ProductRepository;
import com.nokia.ices.apps.fusion.log.repository.ProjectRepository;
import com.nokia.ices.apps.fusion.log.repository.StageRepository;
import com.nokia.ices.apps.fusion.log.repository.WorkPackageRepository;
import com.nokia.ices.apps.fusion.log.repository.WorkTypeRepository;
import com.nokia.ices.apps.fusion.log.service.SelectService;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;

@Service("selectService")
public class SelectServiceImpl implements SelectService {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	StageRepository stageRepository;
	@Autowired
	WorkPackageRepository workPackageRepository;
	@Autowired
	WorkTypeRepository workTypeRepository;
	@Autowired
	SystemUserRepository systemUserRepository;
	
	@Override
	public List<?> queryByListAll(String type) {
		// TODO Auto-generated method stub
		if("product".equals(type)){  //产品
			Order sort1 =  new Order(Direction.ASC,"orderby");
			Order sort2 = new Order(Direction.ASC,"product");
			List<Order> proSort= new ArrayList<Order>();
			proSort.add(sort1);
			proSort.add(sort2);
			Sort sort = new Sort(proSort);
			return (List<?>) productRepository.findAll(sort);
		}else if("project".equals(type)){  //项目
			Order  order1 =  new Order (Direction.ASC,"cbt");
			Order  order2 =  new Order (Direction.ASC,"projectName");
			List<Order> listSort= new ArrayList<Order>();
			listSort.add(order1);
			listSort.add(order2);
			Sort sort = new Sort(listSort);
			return (List<?>) projectRepository.findAll(sort);
		}else if("stage".equals(type)){  //阶段 
			return (List<?>) stageRepository.findAll();
		}else if("workType".equals(type)){  //工作包类型
			return (List<?>) workTypeRepository.findAll();
		}else if("user".equals(type)){
			return (List<?>) systemUserRepository.findUserNames();
		}
		return null;
	}
    
	/**
	 * 根据工作包类型查询 
	 */
	@Override
	public List<WorkPackage> queryByWorkPackageList(Long work_type_id) {
		// TODO Auto-generated method stub
		//List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
		//searchFilter.add(new SearchFilter("work_type_id", Operator.EQ, workTypeId));
		//Specification<WorkPackage> specListAnd = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,WorkPackage.class);
		//return workPackageRepository.findAll(specListAnd);
		WorkType w = new WorkType();
		w.setId(work_type_id);
		return workPackageRepository.findByWorkType(w);
	}


}
