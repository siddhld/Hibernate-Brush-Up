package org.hibernate.CRUD;

import org.hibernate.Session;
import org.hibernate.entity.Song;
import org.hibernate.utils.HibernateUtil;

public class Create {
    public static void main(String[] args) {
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            Song song = new Song("Peaches", "Justin");

            // Saving
            session.beginTransaction();
            session.persist(song);
            session.getTransaction().commit();

        } catch (Exception ex) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

