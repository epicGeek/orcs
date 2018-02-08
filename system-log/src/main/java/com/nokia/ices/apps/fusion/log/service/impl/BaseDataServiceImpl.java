package com.nokia.ices.apps.fusion.log.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nokia.ices.apps.fusion.log.domain.Product;
import com.nokia.ices.apps.fusion.log.repository.ProductRepository;
import com.nokia.ices.apps.fusion.log.repository.ProjectRepository;
import com.nokia.ices.apps.fusion.log.repository.StageRepository;
import com.nokia.ices.apps.fusion.log.repository.WorkPackageRepository;
import com.nokia.ices.apps.fusion.log.repository.WorkTypeRepository;
import com.nokia.ices.apps.fusion.log.service.BaseDataService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;

@Service("baseDataService")
public class BaseDataServiceImpl implements BaseDataService {

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
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=true)
	public Page<?> queryByListAll(Map<String, Object> searchParams, SystemRole systemRole, Integer page,
			Integer pageSize, List<String> sortType) {
		
		PageRequest pageable = buildPageRequest(page, pageSize, sortType);   
		
		String type = searchParams.get("type")!=null?searchParams.get("type").toString():"";
	 	
		if("product".equals(type)){  //产品
			return productRepository.findAll(pageable);
			
		}else if("project".equals(type)){  //项目
			return projectRepository.findAll(pageable);
			
		}else if("stage".equals(type)){  //阶段 
			return stageRepository.findAll(pageable);
			
		}else if("workType".equals(type)){  //工作包类型
			return  workTypeRepository.findAll(pageable);
			
		}else if("user".equals(type)){
			return (Page<?>) systemUserRepository.findUserNames();
		}
		return null;
	}

	@Override
	public Page<?> findListByCreator(Map<String, Object> searchParams) throws ParseException {
		
		if("product".equals(searchParams)){  //产品
			return (Page<?>) productRepository.findAll();
			
		}else if("project".equals(searchParams)){  //项目
			return (Page<?>) projectRepository.findAll();
			
		}else if("stage".equals(searchParams)){  //阶段 
			return (Page<?>) stageRepository.findAll();
			
		}else if("workType".equals(searchParams)){  //工作包类型
			return  (Page<?>) workTypeRepository.findAll();
			
		}else if("user".equals(searchParams)){
			return  (Page<?>) systemUserRepository.findUserNames();
		}
		return null;
	}

	/**
	 * 删除  产品
	 */
	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void deleteByProduct(Long id) {
		
		productRepository.delete(id);
	}

	/**
	 * 添加
	 */
	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void addProduct(Product product) {
		productRepository.save(product);
	}
	
	/**
	 * 编辑
	 * @param Product
	 */
	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void updateProduct(Product product) {
		
		//productRepository.updateProduct(product.getProduct(),product.getOrderby(),product.getId());
		
	}
	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=true)
	public Page<Product> findProductDataPageBySearch(SystemRole systemRole,Integer iDisplayStart,
			Integer iDisplayLength, List<String> sortType) throws ParseException {
		
		PageRequest pageable = buildPageRequest(iDisplayStart, iDisplayLength, sortType);   
		return productRepository.findAll(pageable);
	}
	/**
     * 创建排序请求.
     */
    private Sort buildSort(List<String> sortType) {
    	List<Order> listSort= new ArrayList<Order>();
        for (String orderStr : sortType) {
            String[] order = orderStr.split(",");
           if (order[1].equalsIgnoreCase("asc")){
            	listSort.add(new Order (Direction.ASC,order[0]));
            }else{
            	listSort.add(new Order (Direction.DESC,order[0]));
            }
        }
        return new Sort(listSort);
    }
	  /**
     * 创建分页.
     */
    private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
        Sort sort = buildSort(sortType);
        return new PageRequest(page-1, size, sort);
    }

	@Override
	public List<Product> findProductDataListByCreator(Map<String, Object> searchParams) throws ParseException {
		
	 //	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
	 	 
	//	Specification<Product> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,Product.class);            		
		return productRepository.findAll();
	}


}
