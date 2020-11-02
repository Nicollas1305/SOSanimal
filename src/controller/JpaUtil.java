/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author carto
 */
public class JpaUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOSanimalPU");
    private static final EntityManager em = emf.createEntityManager();
    
    public static EntityManagerFactory getEmf() {
        return emf;
    }
    
    public static EntityManager getEm() {
        return em;
    }
}
