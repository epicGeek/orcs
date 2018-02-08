package com.nokia.ices.apps.fusion.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
import com.nokia.ices.core.utils.Collections3;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicSpecifications {

    private static final Logger logger = LoggerFactory.getLogger(DynamicSpecifications.class);
    
    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters,final BooleanOperator operator,
            final Class<T> entityClazz) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (Collections3.isNotEmpty(filters)) {
                    List<Predicate> predicates = Lists.newArrayList();
                    for (SearchFilter filter : filters) {
                        // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        logger.debug(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            logger.debug(names[i]);
                            expression = expression.get(names[i]);
                        }
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicates.add(builder.equal(expression, filter.value));
                                break;
                            case NOTEQ:
                                predicates.add(builder.notEqual(expression, filter.value));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case CONTAINS:
                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case STARTWITH:
                                predicates.add(builder.like(expression, filter.value + "%"));
                                break;
                            case ENDWITH:
                                predicates.add(builder.like(expression, "%" + filter.value));
                                break;
                            case GT:
                                predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                                break;
                            case LT:
                                predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                                break;
                            case GE:
                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case LE:
                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case IN:
                                List listIn = (List)filter.value;
                                predicates.add(expression.in(listIn.toArray(new Long[listIn.size()])));
                                break;
                            case ENTITYIN:
                                if(filter.value instanceof Collection)
                                    predicates.add(expression.in( (Collection) filter.value));
                                else if(filter.value instanceof Object[]){
                                    predicates.add(expression.in( (Object[]) filter.value));
                                }
                                break;
                                
                        }
                    }
                    // 将所有条件用 and/or 联合起来
                    if (!predicates.isEmpty()) {
                        if (operator.equals(BooleanOperator.AND))
                            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                        else if (operator.equals(BooleanOperator.OR)) {
                            return builder.or(predicates.toArray(new Predicate[predicates.size()]));
                        } else {
                            return null;
                        }
                    }
                }
                // 如果没条件
                return builder.conjunction();

            }
        };
    }

    public static <T> Specification<T> buildSpecification(final SearchFilter filter) {
        // TODO Auto-generated method stub
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                String[] names = StringUtils.split(filter.fieldName, ".");
                Path expression = root.get(names[0]);
                for (int i = 1; i < names.length; i++) {
                    expression = expression.get(names[i]);
                }
                List<Predicate> predicates = Lists.newArrayList();
                
                // logic operator
                switch (filter.operator) {
                    case EQ:
                        predicates.add(builder.equal(expression, filter.value));
                        break;
                    case NOTEQ:
                        predicates.add(builder.notEqual(expression, filter.value));
                        break;
                    case LIKE:
                        predicates.add(builder.like(expression, "%" + filter.value + "%"));
                        break;
                    case CONTAINS:
                        predicates.add(builder.like(expression, "%" + filter.value + "%"));
                        break;
                    case STARTWITH:
                        predicates.add(builder.like(expression, filter.value + "%"));
                        break;
                    case ENDWITH:
                        predicates.add(builder.like(expression, "%" + filter.value));
                        break;
                    case GT:
                        predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                        break;
                    case LT:
                        predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                        break;
                    case GE:
                        predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                        break;
                    case LE:
                        predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                        break;
                    case IN:
                        List listIn = (List)filter.value;
                        predicates.add(expression.in(listIn.toArray(new Long[listIn.size()])));
                        break;
                    case ENTITYIN:
                        if(filter.value instanceof Collection)
                            predicates.add(expression.in((Collection)filter.value));
                        else{
                            predicates.add(expression.in(filter.value));
                        }
                        break;
                        
                }
                if (!predicates.isEmpty()) {
                    return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
                // 如果没条件
                return builder.conjunction();

            }
        };
    }

//    public static <T> Specification<T> concatSpecification(final BooleanOperator operator,final Specification<T>... spec) {
//        return new Specification<T>() {
//            @Override
//            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//                
//                List<Predicate> predicates = Lists.newArrayList();
//
//                for (int i = 0; i < spec.length; i++) {
//                    Predicate p = spec[i].toPredicate(root, query, builder);
//                    predicates.add(p);
//                }
//
//                // 将所有条件用 and/or 联合起来
//                if (!predicates.isEmpty()) {
//                    if (operator.equals(BooleanOperator.AND))
//                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//                    else if (operator.equals(BooleanOperator.OR)) {
//                        return builder.or(predicates.toArray(new Predicate[predicates.size()]));
//                    } else {
//                        return null;
//                    }
//                }
//                //无条件
//                return builder.conjunction();
//            }
//        };
//    }
    
    
    
}
