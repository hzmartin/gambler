package gambler.commons.persistence.hibernate;

import gambler.commons.persistence.IPersistence;
import gambler.commons.persistence.PersistenceException;

import java.util.List;

import org.hibernate.Session;

/**
 * This interface provides some hibernate feature data access operations based
 * on <code>Persistence</code>.
 * 
 * @author Martin
 * 
 */
@SuppressWarnings("rawtypes")
public interface IHibernatePersistence extends IPersistence {

	/**
	 * execute the given sql query
	 * 
	 * @param queryString - SQL query string
	 * @return query result
	 * @throws PersistenceException
	 */
	public List findBySQLQuery(String queryString) throws PersistenceException;
	
	/**
	 * Execute a named query A named query is defined in a Hibernate mapping
	 * file.
	 * 
	 * @param queryName
	 *            - the name of a Hibernate query in a mapping file
	 * @return a <code>List</code> containing the results of the query execution
	 * @exception PersistenceException
	 *                in case of Hibernate Error
	 * @see org.hibernate.Session#getNamedQuery(java.lang.String)
	 */
	public List findByNamedQuery(String queryName) throws PersistenceException;

	/**
	 * Execute a named query binding one value to a "?" parameter in the query
	 * string. A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName
	 *            - the name of a Hibernate query in a mapping file
	 * @param value
	 *            - the value of the parameter
	 * @return a List containing the results of the query execution
	 * @exception PersistenceException
	 *                - in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(java.lang.String)
	 */
	public List findByNamedQuery(String queryName, Object value)
			throws PersistenceException;

	/**
	 * Execute a named query binding a number of values to "?" parameters in the
	 * query string. A named query is defined in a Hibernate mapping file.
	 * 
	 * @param queryName
	 *            - the name of a Hibernate query in a mapping file
	 * @param values
	 *            - the values of the parameters
	 * @return a List containing the results of the query execution
	 * @exception PersistenceException
	 *                - in case of Hibernate errors
	 * @see org.hibernate.Session#getNamedQuery(java.lang.String)
	 */
	public List findByNamedQuery(String queryName, Object[] values)
			throws PersistenceException;

	/**
	 * Execute a named query binding a number of values to "?" parameters in the
	 * query string. A named query is defined in a Hibernate mapping file.
	 * Convenience method to return a single instance that matches the query, or
	 * null if the query returns no results.
	 * 
	 * @param queryName
	 *            - the name of a Hibernate query in a mapping file
	 * @param values
	 *            - the values of the parameters
	 * @return the single result or null
	 * @throws PersistenceException
	 *             - in case of errors
	 */
	public Object uniqueWithNamedQuery(String queryName, Object[] values)
			throws PersistenceException;

	/**
	 * Execute a named query binding one value to a "?" parameter in the query
	 * string. A named query is defined in a Hibernate mapping file. Convenience
	 * method to return a single instance that matches the query, or null if the
	 * query returns no results.
	 * 
	 * @param queryName
	 *            - the name of a Hibernate query in a mapping file
	 * @param values
	 *            - the values of the parameters
	 * @return the single result or null
	 * @throws PersistenceException
	 *             - in case of errors
	 */
	public Object uniqueWithNamedQuery(String queryName, Object value)
			throws PersistenceException;

	/**
	 * Execute a named query A named query is defined in a Hibernate mapping
	 * file. Convenience method to return a single instance that matches the
	 * query, or null if the query returns no results.
	 * 
	 * @param queryName
	 *            - the name of a Hibernate query in a mapping file
	 * @return the single result or null
	 * @throws PersistenceException
	 *             - in case of errors
	 */
	public Object uniqueWithNamedQuery(String queryName)
			throws PersistenceException;

	/**
	 * get hibernate session
	 * 
	 * @return hibernate session
	 * @throws PersistenceException
	 */
	public Session getHibernateSession() throws PersistenceException;

}
