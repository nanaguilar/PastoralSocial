package pastoralsocial.dao;

import java.util.List;

public interface DAO<T> {
	
	List<T> getAll();

	T getById(int id);

	void save(T t);

	void update(T t);

	void delete(T t);

}
