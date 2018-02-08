package com.nokia.ices.apps.fusion.equipment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentWebInterface;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNumberSectionRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitSpecifications;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentWebInterfaceRepository;
import com.nokia.ices.apps.fusion.equipment.service.EquipmentService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@Service("equipmentService")
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	EquipmentNeRepository equipmentNeRepository;


	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;


	@Autowired
	EquipmentNumberSectionRepository equipmentNumberSectionRepository;

	@Autowired
	EquipmentWebInterfaceRepository equipmentWebInterfaceRepository;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Page<EquipmentNe> findEquipmentNePageBySearchFilter(Map<String,Object> paramMap,SystemRole currentUserRole, Pageable pageable) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
		Specification<EquipmentNe> specList = DynamicSpecifications.bySearchFilter(searchFilter, BooleanOperator.OR,EquipmentNe.class);
		return equipmentNeRepository.findAll(specList, pageable);
	}

	@Override
	public Page<EquipmentUnit> findEquipmentUnitPageBySearchFilter(Map<String,Object> paramMap,SystemRole currentUserRole, Pageable pageable) {
		List<SearchFilter> searchByRole = new ArrayList<SearchFilter>();
        Specification<EquipmentUnit> specNeEQ = DynamicSpecifications.bySearchFilter(searchByRole, BooleanOperator.AND,EquipmentUnit.class);
        String neName = paramMap.get("neName").toString();
        String unitType = paramMap.get("unitType").toString();
        String unitName = paramMap.get("unitName").toString();
        Specification<EquipmentUnit> neNameEqual = EquipmentUnitSpecifications.neNameEqual(neName);
        Specification<EquipmentUnit> unitNameLike = EquipmentUnitSpecifications.unitNameLike(unitName);
        Specification<EquipmentUnit> unitTypeEqual = EquipmentUnitSpecifications.unitTypeEqual(unitType);

        return equipmentUnitRepository.findAll(Specifications.where(specNeEQ).
                and(neNameEqual).and(unitNameLike).and(unitTypeEqual), pageable);

	}

	@Override
	public List<EquipmentUnit> findEquipmentUnitListBySearchFilter(SystemRole currentUserRole,Sort sort) {
        List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();  
        Specification<EquipmentUnit> specListOR = DynamicSpecifications.bySearchFilter(searchFilterOR, BooleanOperator.OR,EquipmentUnit.class);
        return equipmentUnitRepository.findAll(Specifications.where(specListOR));
	}

    @Override
    public List<EquipmentNumberSection> findEquipmentNumberSectionByAreaID(String searchField, String type,
            Long area_id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EquipmentWebInterface> findEquipmentWebInterfaceByUnitID(Long unit_id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EquipmentNumberSection> findEquipmentNumberSectionListBySearchFilter(SystemRole currentUserRole,
            Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<EquipmentNumberSection> findEquipmentNumberSectionPageBySearchFilter(Map<String,Object> paramMap,SystemRole currentUserRole,
            Pageable pageable) {
        Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
        Specification<EquipmentNumberSection> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.OR, EquipmentNumberSection.class);
        return equipmentNumberSectionRepository.findAll(spec, pageable);
    }

    @Override
    public List<EquipmentNe> findEquipmentNeListBySearchFilter(SystemRole currentUserRole, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EquipmentUnit> findEquipmentUnitListBySearchFilter(Map<String, Object> searchParams,
            SystemRole currentUserRole, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EquipmentUnit> findEquipmentUnitListBySearchFilter(Map<String, Object> searchParams,
            ShiroUser shiroUser, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Map<String, List<String>> getOptionMap(String dhssName, String nodeName, String neType, String unitType,
			String neName) {
		Map<String, List<String>> optionMap = new HashMap<String, List<String>>();
		List<String> options = new ArrayList<String>();
		List<Map<String,Object>> neList = new ArrayList<>();
		List<Map<String,Object>> unitList = new ArrayList<>();
		neList = jdbcTemplate.queryForList("select * from equipment_ne");
		unitList = jdbcTemplate.queryForList("select * from equipment_unit");
		options.add("this is a test");
		optionMap.put("test", options);
		return optionMap;
	}
}
