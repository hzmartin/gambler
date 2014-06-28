package gambler.commons.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <code>Persistence</code> provides some useful data access operations
 * 
 * @author Martin
 * 
 */
@SuppressWarnings("rawtypes")
public interface IPersistence {

	/**
	 * persist the given transient instance
	 * 
	 * @param entity
	 *            a persistent class
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public void save(Object entity) throws PersistenceException;

	/**
	 * update the given persistent instance, according to its id
	 * 
	 * @param entity
	 *            a persistent instance
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public void update(Object entity) throws PersistenceException;

	/**
	 * save or update the given persistent instance, according to its id
	 * 
	 * @param entity
	 *            a persistent instance
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public void saveOrUpdate(Object entity) throws PersistenceException;

	/**
	 * save or update all the given persistent instances
	 * 
	 * @param entities
	 *            persistent instances
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public void saveOrUpdateAll(Collection entities)
			throws PersistenceException;

	/**
	 * delete the given persistent instance
	 * 
	 * @param entity
	 *            a persistent instance
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public void delete(Object entity) throws PersistenceException;

	/**
	 * delete all the given persistent instances
	 * 
	 * @param entities
	 *            persistent instances
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public void deleteAll(Collection entities) throws PersistenceException;

	/**
	 * return all persistent instances of the given entity class
	 * 
	 * @param entityClass
	 *            a persistent class
	 * @return return all persistent instances of the given entity class
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public List loadAll(Class entityClass) throws PersistenceException;

	/**
	 * return the persistent instance of the given entity class with the given
	 * identifier, throwing an exception if not found
	 * 
	 * @param entityClass
	 *            a persistent class
	 * @param key
	 *            id of a persistent instance
	 * @return entity whoes id is the given key or null
	 */
	public Object load(Class entityClass, Serializable key)
			throws PersistenceException;

	/**
	 * @see IPersistence#find(String, Object[])
	 */
	public List find(String queryString) throws PersistenceException;

	/**
	 * @see IPersistence#find(String, Object[])
	 */
	public List find(String queryString, Object value)
			throws PersistenceException;

	/**
	 * execute a query for persistent instances, binding more values to "?"
	 * parameters in the query string.
	 * 
	 * @param queryString
	 *            a query expressed in query language
	 * @param values
	 *            the values of the parameter
	 * @return a List containing 0 or more persistent instances
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public List find(String queryString, Object[] values)
			throws PersistenceException;

	/**
	 * execute a query for persistent instances by specified from index and max count, binding more values to "?"
	 * parameters in the query string.
	 * 
	 * @param queryString
	 *            a query expressed in query language
	 * @param values
	 *            the values of the parameter
	 * @param from
	 * 			  first result index, numbered from 0
	 * @param count
	 * 			  max result number
	 * @return a List containing 0 or more persistent instances
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public List find(String queryString, Object[] values, int from, int count)
			throws PersistenceException;

	/**
	 * @see IPersistence#find(String, Object[], int, int)
	 */
	public List find(String queryString, Object values, int from, int count)
			throws PersistenceException;

	/**
	 * @see IPersistence#find(String, Object[], int, int)
	 */
	public List find(String queryString, int from, int count)
			throws PersistenceException;

	/**
	 * get the queryString's result which only return one object
	 * 
	 * @param queryString
	 *            a query expressed in query language
	 * @param values
	 *            the values of the parameters
	 * @return unique result or null
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public Object unique(String queryString) throws PersistenceException;

	/**
	 * get the queryString's result which only return one object, binding one
	 * value to "?" parameters in the query string.
	 * 
	 * @param queryString
	 *            a query expressed in query language
	 * @param values
	 *            the values of the parameters
	 * @return unique result or null
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public Object unique(String queryString, Object value)
			throws PersistenceException;

	/**
	 * get the queryString's result which only return one object, binding a
	 * number of values to "?" parameters in the query string.
	 * 
	 * @param queryString
	 *            a query expressed in query language
	 * @param values
	 *            the values of the parameters
	 * @return unique result or null
	 * @throws PersistenceException
	 *             when database connection error etc
	 */
	public Object unique(String queryString, Object[] values)
			throws PersistenceException;
}
