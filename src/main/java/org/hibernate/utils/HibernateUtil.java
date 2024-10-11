package org.hibernate.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static SessionFactory sessionFactory = null;

    // Singleton
    public static SessionFactory getSessionFactory(){
        try {
            if(sessionFactory == null){
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                sessionFactory = configuration.buildSessionFactory();
            }
        }catch (Exception e){
            System.err.println("Exception occurred while Creating SessionFactory instance");
            e.printStackTrace();
        }
        return sessionFactory;
    }

    // Prototype
    public static Session getSession() {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
        } catch (Exception ex) {
            System.out.println("Exception occurred while opening a Session");
            ex.printStackTrace();
        }
        return session;
    }
}