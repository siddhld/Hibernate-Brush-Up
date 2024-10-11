package org.hibernate.CRUD;

import org.hibernate.Session;
import org.hibernate.entity.Song;
import org.hibernate.utils.HibernateUtil;

public class Update {
    public static void main(String[] args) {
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            // Retrieving
            Song song = session.get(Song.class, 5);

            // Updating
            session.beginTransaction();
            song.setName("Late Night Talking");
            session.update(song);
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

////// Why Update happens without "session.update(song);"

//    Automatic Dirty Checking:
//    Hibernate uses a feature called "automatic dirty checking."
//    This means it automatically detects changes to managed entities and synchronizes those changes with the database during transaction commit.