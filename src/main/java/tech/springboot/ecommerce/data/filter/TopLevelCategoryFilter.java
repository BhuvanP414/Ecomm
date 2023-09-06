package tech.springboot.ecommerce.data.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class TopLevelCategoryFilter extends JPAFilter {
    @Override
    public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root) {
        return criteriaBuilder.isNull(root.get("parent"));
    }
}
