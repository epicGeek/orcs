package com.nokia.ices.apps.fusion.system.repository.spec;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.domain.SystemUser_;
import com.nokia.ices.core.utils.Collections3;

public class SystemUserSpecifications {
    
//    private static final Logger logger = LoggerFactory.getLogger(SystemUserSpecifications.class);
    
    public static Specification<SystemUser> userNameLike(final String userName) {
        return new Specification<SystemUser>() {

            @Override
            public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(userName)){
                    return null;
                }
                String likePattern = "%"+userName+"%";
                return cb.like(root.<String>get(SystemUser_.userName),likePattern);
            }
        };
    }
    
    public static Specification<SystemUser> creatorLike(final String userName) {
        return new Specification<SystemUser>() {

            @Override
            public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(userName)){
                    return null;
                }
                String likePattern = "%"+userName+"@%";
                return cb.like(root.<String>get(SystemUser_.creator),likePattern);
            }
        };
    }

    public static Specification<SystemUser> realNameLike(final String realName) {
        return new Specification<SystemUser>() {

            @Override
            public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(realName)){
                    return null;
                }
                String likePattern = "%"+realName+"%";
                return cb.like(root.<String>get(SystemUser_.realName),likePattern);
            }
        };
    }
    
    public static Specification<SystemUser> mobileLike(final String mobile) {
        return new Specification<SystemUser>() {

            @Override
            public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(mobile)){
                    return null;
                }
                String likePattern = "%"+mobile+"%";
                return cb.like(root.<String>get(SystemUser_.mobile),likePattern);
            }
        };
    }
    
    public static Specification<SystemUser> emailLike(final String email) {
        return new Specification<SystemUser>() {

            @Override
            public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isEmpty(email)){
                    return null;
                }
                String likePattern = "%"+email+"%";
                return cb.like(root.<String>get(SystemUser_.email),likePattern);
            }
        };
    }

    public static Specification<SystemUser> roleIn(final Set<SystemRole> roleSet) {
        return new Specification<SystemUser>() {

            @Override
            public Predicate toPredicate(Root<SystemUser> user, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(Collections3.isEmpty( roleSet)){
                    return null;
                }
                SetJoin<SystemUser,SystemRole> roleJoin = user.join(SystemUser_.systemRole , JoinType.LEFT);
                query.select(null).distinct(true);
                return roleJoin.in(roleSet);
            }
        };
    }
}
