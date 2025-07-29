package com.linreelle.saphir.dao;

import com.linreelle.saphir.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final EntityManager entityManager;

    public List<User> searchUsers(String firstname, String lastname, String email, String telephone, String id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        // select * from User
        Root<User> root = criteriaQuery.from(User.class);

        //WHERE clause with firstname as predicate
        Predicate firstnamePredicate = criteriaBuilder.like(root.get("firstname"), "%" + firstname + "%");
        Predicate lastnamePredicate = criteriaBuilder.like(root.get("lastname"), "%" + lastname + "%");
        Predicate emailPredicate = criteriaBuilder.like(root.get("email"), "%" + email + "%");
        Predicate telephonePredicate = criteriaBuilder.like(root.get("telephone"), "%" + telephone+ "%");
        Predicate idPredicate = criteriaBuilder.like(root.get("id"), "%" + id + "%");

        Predicate orPredicate = criteriaBuilder.or(firstnamePredicate, lastnamePredicate, emailPredicate, telephonePredicate , idPredicate);

        //final query
        criteriaQuery.where(orPredicate);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();

    }

    public List<User> searchByCriteria(UserSearchRequest searchRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        List<Predicate> predicates = new ArrayList<>();

        //select from user
        Root<User> root = criteriaQuery.from(User.class);

        if (searchRequest.getLastname() != null) {
            Predicate firstnamePredicate = criteriaBuilder
                    .like(root.get("firstname"), "%" + searchRequest.getFirstname() + "%");
            predicates.add(firstnamePredicate);
        }
        if (searchRequest.getLastname() != null) {
            Predicate lastnamePredicate = criteriaBuilder
                    .like(root.get("lastname"), "%" + searchRequest.getLastname() + "%");
            predicates.add(lastnamePredicate);
        }
        if (searchRequest.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder
                    .like(root.get("email"), "%" + searchRequest.getEmail() + "%");
            predicates.add(emailPredicate);
        }
        if(searchRequest.getTelephone() != null) {
            Predicate telephonePredicate = criteriaBuilder
                    .like(root.get("telephone"), "%" + searchRequest.getTelephone() + "%");
        }
        if (searchRequest.getId() != null) {
            Predicate idPredicate = criteriaBuilder
                    .like(root.get("id"), "%" + searchRequest.getId() + "%");
            predicates.add(idPredicate);
        }

        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[0]))
        );

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();

    }
}