package com.nokia.ices.apps.fusion.system.repository.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemRole_;
import com.nokia.ices.core.utils.Collections3;

public class SystemRoleSpecifications {
    /**
     * admin@210.30.96.204:/1 -> /1
     * @param myLoginString
     * @return
     */
    public static Specification<SystemRole> createdByMe(final String pathString) {
        return new Specification<SystemRole>() {

            @Override
            public Predicate toPredicate(Root<SystemRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get(SystemRole_.path), pathString+"%");
            }
        };
    }

    public static Specification<SystemRole> createdByMyGroup(final Collection<SystemRole> roleSet) {
        return new Specification<SystemRole>() {
            @Override
            public Predicate toPredicate(Root<SystemRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (Collections3.isEmpty(roleSet)) {
                    return null;
                }
                List<Predicate> predicates = new ArrayList<Predicate>();
                Set<String> rolePathSet = new HashSet<String>();
                for (SystemRole systemRole : roleSet) {
                    String pathString = systemRole.getPath();
                    rolePathSet.add(pathString);
                }
                //过滤相同的
                for (String pathString : rolePathSet) {
                    predicates.add(createdByMe(pathString).toPredicate(root, query, cb));
                }
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));

            }
        };
    }
}
