package pastoralsocial.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.*;

public class JPAUtil {

	private static EntityManagerFactory entityManagerFactory;

	private JPAUtil() {

	}

	public static EntityManager getEntityManager() {

		if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
			entityManagerFactory = Persistence.createEntityManagerFactory("pastoralsocial");
		}

		return entityManagerFactory.createEntityManager();
	}

	public static void closeEntityManagerFactory() {

		if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}

	}

}