package org.hibernate.CRUD;

import org.hibernate.Session;
import org.hibernate.entity.Song;
import org.hibernate.utils.HibernateUtil;

public class Delete {
    public static void main(String[] args) {
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            // Retrieving
            Song song = session.get(Song.class, 4);

            // Deleting
            session.beginTransaction();
            session.remove(song);
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
