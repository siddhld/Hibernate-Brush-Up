package org.hibernate.CRUD;

import org.hibernate.Session;
import org.hibernate.entity.Song;
import org.hibernate.utils.HibernateUtil;

public class Read {
    public static void main(String[] args) {
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            // Retrieving
            Song song = session.get(Song.class, 1);

            System.out.println(song);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
