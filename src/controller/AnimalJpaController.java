/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.Animal;
import Entity.Cliente;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author carto
 */
public class AnimalJpaController implements Serializable {

    public AnimalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Animal animal){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(animal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Animal animal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            animal = em.merge(animal);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = animal.getNome();
                if (findAnimal(id) == null) {
                    throw new NonexistentEntityException("The animal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Animal animal;
            try {
                animal = em.getReference(Animal.class, id);
                animal.getNome();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The animal with id " + id + " no longer exists.", enfe);
            }
            em.remove(animal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Animal> findAnimalEntities() {
        return findAnimalEntities(true, -1, -1);
    }

    public List<Animal> findAnimalEntities(int maxResults, int firstResult) {
        return findAnimalEntities(false, maxResults, firstResult);
    }

    private List<Animal> findAnimalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Animal.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Cliente> findAnimalEntitiesFetchID(int id){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c WHERE c.id = :id", Cliente.class);
            q.setParameter("id", id);
            return q.getResultList();
        } finally{
            em.close();
        }
    }
    
    public List<Animal> findAnimalList(int id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Animal> an = em.createQuery("SELECT c FROM Animal c WHERE c.iDcliente = :IDcliente", Animal.class);
            an.setParameter("IDcliente", id);
            return an.getResultList();
        } finally {
            em.close();
        }
    }

    public Animal findAnimal(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Animal.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnimalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Animal> rt = cq.from(Animal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
