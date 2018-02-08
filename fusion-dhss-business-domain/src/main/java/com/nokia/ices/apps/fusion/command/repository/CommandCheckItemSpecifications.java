package com.nokia.ices.apps.fusion.command.repository;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem_;
import com.nokia.ices.apps.fusion.command.domain.CommandGroup;
import com.nokia.ices.apps.fusion.command.domain.types.CommandCategory;
import com.nokia.ices.apps.fusion.command.domain.types.SubtoolCmdType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;

public class CommandCheckItemSpecifications {
    public static Specification<CommandCheckItem> CommandCheckItemEqual(final String categoryStr) {
        return new Specification<CommandCheckItem>() {
            @Override
            public Predicate toPredicate(Root<CommandCheckItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(categoryStr))
                    return null;
                CommandCategory category = (CommandCategory) Enum.valueOf(CommandCategory.class, categoryStr);
                return cb.equal(root.get(CommandCheckItem_.category), category);
            }
        };        
    }
    
    public static Specification<CommandCheckItem> CommandCheckItemSubtoolCmdTypeEqual(final String subtoolCmdName) {
        return new Specification<CommandCheckItem>() {
            @Override
            public Predicate toPredicate(Root<CommandCheckItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(subtoolCmdName))
                    return null;
                SubtoolCmdType subType = (SubtoolCmdType) Enum.valueOf(SubtoolCmdType.class, subtoolCmdName);
                return cb.equal(root.get(CommandCheckItem_.cmdType), subType);
            }
        };        
    }
    
    public static Specification<CommandCheckItem> CommandCheckItemCommandGroupNeType(final Set<CommandGroup> setCom) {
        return new Specification<CommandCheckItem>() {
            @Override
            public Predicate toPredicate(Root<CommandCheckItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(setCom.isEmpty())
                    return null;
                return cb.equal(root.get(CommandCheckItem_.commandGroup), setCom);
            }
        };        
    }
    
    public static Specification<CommandCheckItem> CommandCheckItemCommandGroupUnitType(final String unitTypeName) {
        return new Specification<CommandCheckItem>() {
            @Override
            public Predicate toPredicate(Root<CommandCheckItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(unitTypeName))
                    return null;
                
                EquipmentUnitType unitType = (EquipmentUnitType) Enum.valueOf(EquipmentUnitType.class, unitTypeName);
                return cb.equal(root.get(CommandCheckItem_.commandGroup), unitType);
            }
        };        
    }
    
    
    
   
}
