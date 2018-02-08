package com.nokia.ices.apps.fusion.equipment.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe_;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit_;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;

public class EquipmentUnitSpecifications {
    public static Specification<EquipmentUnit> unitTypeEqual(final String unitTypeStr) {
        return new Specification<EquipmentUnit>() {
            @Override
            public Predicate toPredicate(Root<EquipmentUnit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(unitTypeStr))
                    return null;
                EquipmentUnitType unitType = (EquipmentUnitType) Enum.valueOf(EquipmentUnitType.class, unitTypeStr);
                return cb.equal(root.get(EquipmentUnit_.unitType), unitType);
            }
        };        
    }
    public static Specification<EquipmentUnit> neNameEqual(final String neName) {
        return new Specification<EquipmentUnit>() {
            @Override
            public Predicate toPredicate(Root<EquipmentUnit> unit, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(neName))
                    return null;
                Join<EquipmentUnit,EquipmentNe> ne = unit.join(EquipmentUnit_.ne , JoinType.LEFT);
                query.select(null).distinct(true);
                return cb.equal(ne.get(EquipmentNe_.neName), neName);
            }
        };        
    }

    public static Specification<EquipmentUnit> unitNameLike(final String unitName) {
        return new Specification<EquipmentUnit>() {
            @Override
            public Predicate toPredicate(Root<EquipmentUnit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(unitName))
                    return null;
                Predicate unitNameLike =  cb.like(root.get(EquipmentUnit_.unitName), unitName);
                Predicate serverAddreasLike =  cb.like(root.get(EquipmentUnit_.serverIp), unitName);
                return cb.or(unitNameLike,serverAddreasLike);
            }
        };        
    }
}
