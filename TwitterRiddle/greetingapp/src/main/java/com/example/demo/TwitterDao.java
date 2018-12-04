package com.example.demo;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;

@Repository
@Transactional
public class TwitterDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void add(Riddles riddle) {
        entityManager.persist(riddle);
        return;
    }

    public Riddles getById(int id) {
        return entityManager.find(Riddles.class, id);

    }

    public void delete(int id) {
        entityManager.remove(entityManager.find(Riddles.class, id));
    }

    public void update(Riddles r) {
        entityManager.merge(r);
        return;
    }

}
