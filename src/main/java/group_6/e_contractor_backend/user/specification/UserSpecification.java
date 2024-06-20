package group_6.e_contractor_backend.user.specification;

import group_6.e_contractor_backend.user.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;



public class UserSpecification {

    /*public static Specification<UserEntity> getUsersWithFilters(
            String firstName, String lastName, String email, String phoneNumber, String role) {

        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }

            if (lastName != null && !lastName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber").as(String.class), "%" + phoneNumber + "%"));
            }

            if (role != null && !role.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("role").get("role"), role));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }*/
}
