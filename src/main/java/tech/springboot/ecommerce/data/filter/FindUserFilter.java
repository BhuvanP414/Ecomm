package tech.springboot.ecommerce.data.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Setter;

import java.util.Optional;

@Setter
public class FindUserFilter extends JPAFilter {
    private Optional<String> username;

    @Override
    public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root) {
        Predicate emailPredicate = username.map(usernameString -> equals(criteriaBuilder, root, "email", usernameString)).orElse(null);

        return emailPredicate;
    }
}
