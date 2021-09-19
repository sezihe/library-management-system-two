package com.danielezihe.hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 19/09/2021
 */
public class HibernateUtil {
    private static StandardServiceRegistry mRegistry;
    private static SessionFactory mFactory;

    public static SessionFactory getSessionFactory() {
        // make sure only one instance is returned
        if(mFactory == null) {
            try {
                mRegistry = new StandardServiceRegistryBuilder().configure().build();

                MetadataSources metadataSources = new MetadataSources(mRegistry);

                Metadata metadata = metadataSources.getMetadataBuilder().build();

                mFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if(mRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(mRegistry);
                }
            }
        }
        return mFactory;
    }

    public static void shutDown() {
        if(mRegistry != null) {
            StandardServiceRegistryBuilder.destroy(mRegistry);
        }
    }
}
