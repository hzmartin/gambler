package gambler.commons.persistence.hibernate;

import gambler.commons.persistence.PersistenceException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementation of <code>IHibernatePersistence</code>, it provides simple dao
 * methods.
 * 
 * @author Martin
 * 
 */
@SuppressWarnings("rawtypes")
public class SimpleHibernatePersistence extends HibernateDaoSupport implements
		IHibernatePersistence {

	@Override
	public void save(Object entity) throws PersistenceException {
		try {
			getHibernateTemplate().save(entity);
		} catch (Exception ex) {
			throw new PersistenceException("create entity error", ex);
		}
	}

	@Override
	public void update(Object entity) throws PersistenceException {
		try {
			getHibernateTemplate().update(entity);
		} catch (Exception ex) {
			throw new PersistenceException("update entity error", ex);
		}
	}

	@Override
	public void saveOrUpdate(Object entity) throws PersistenceException {
		try {
			getHibernateTemplate().saveOrUpdate(entity);
		} catch (Exception ex) {
			throw new PersistenceException("saveOrUpdate entity error", ex);
		}

	}

	@Override
	public void saveOrUpdateAll(Collection entities)
			throws PersistenceException {
		try {
			getHibernateTemplate().saveOrUpdateAll(entities);
		} catch (Exception ex) {
			throw new PersistenceException("saveOrUpdateAll entities error", ex);
		}
	}

	@Override
	public void delete(Object entity) throws PersistenceException {
		try {
			getHibernateTemplate().delete(entity);
		} catch (Exception ex) {
			throw new PersistenceException("delete entity error", ex);
		}
	}

	@Override
	public void deleteAll(Collection entities) throws PersistenceException {
		try {
			Iterator iterator = entities.iterator();
			while (iterator.hasNext()) {
				getHibernateTemplate().delete(iterator.next());
			}
		} catch (Exception ex) {
			throw new PersistenceException("deleteAll entities error", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object load(Class entityClass, Serializable key)
			throws PersistenceException {
		try {
			return getHibernateTemplate().get(entityClass, key);
		} catch (Exception ex) {
			throw new PersistenceException("load query error", ex);
		}
	}

	@Override
	public List loadAll(Class entityClass) throws PersistenceException {
		try {
			Criteria criteria = getHibernateSession().createCriteria(
					entityClass);
			return criteria.list();
		} catch (Exception ex) {
			throw new PersistenceException("loadAll query error", ex);
		}
	}

	@Override
	public List find(String queryString) throws PersistenceException {
		return find(queryString, (Object[]) null);
	}

	@Override
	public List find(String queryString, Object value)
			throws PersistenceException {
		return find(queryString, new Object[] { value });
	}

	@Override
	public List find(String queryString, Object[] values)
			throws PersistenceException {
		try {
			Query queryObject = getHibernateSession().createQuery(queryString);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.list();
		} catch (Exception ex) {
			throw new PersistenceException("find query error", ex);
		}
	}

	@Override
	public Object unique(String queryString) throws PersistenceException {
		return unique(queryString, (Object[]) null);
	}

	@Override
	public Object unique(String queryString, Object value)
			throws PersistenceException {
		return unique(queryString, new Object[] { value });
	}

	@Override
	public Object unique(String queryString, Object[] values)
			throws PersistenceException {
		try {
			Query query = getHibernateSession().createQuery(queryString);
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					query.setParameter(i, values[i]);
			}
			return query.uniqueResult();
		} catch (Exception ex) {
			throw new PersistenceException("unique result query error", ex);
		}
	}

	@Override
	public List findByNamedQuery(String queryName) throws PersistenceException {
		return findByNamedQuery(queryName, (Object[]) null);
	}

	@Override
	public List findByNamedQuery(String queryName, Object value)
			throws PersistenceException {
		return findByNamedQuery(queryName, new Object[] { value });
	}

	@Override
	public List findByNamedQuery(String queryName, Object[] values)
			throws PersistenceException {
		try {
			Query query = getHibernateSession().getNamedQuery(queryName);
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					query.setParameter(i, values[i]);
			}
			return query.list();
		} catch (Exception ex) {
			throw new PersistenceException("execute query " + queryName
					+ " error", ex);
		}
	}

	@Override
	public Object uniqueWithNamedQuery(String queryName, Object[] values)
			throws PersistenceException {
		try {
			Query query = getHibernateSession().getNamedQuery(queryName);
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					query.setParameter(i, values[i]);
			}
			return query.uniqueResult();
		} catch (Exception ex) {
			throw new PersistenceException("unique result query " + queryName
					+ " error", ex);
		}
	}

	@Override
	public Object uniqueWithNamedQuery(String queryName, Object value)
			throws PersistenceException {
		return uniqueWithNamedQuery(queryName, new Object[] { value });
	}

	@Override
	public Object uniqueWithNamedQuery(String queryName)
			throws PersistenceException {
		return uniqueWithNamedQuery(queryName, (Object[]) null);
	}

	@Override
	public Session getHibernateSession() throws PersistenceException {
		try {
			return getSession();
		} catch (Exception e) {
			throw new PersistenceException(
					"Unable to create hibernate session", e);
		}
	}

	@Override
	public List findBySQLQuery(String queryString) throws PersistenceException {
		try {
			SQLQuery sqlQuery = getHibernateSession().createSQLQuery(
					queryString);
			return sqlQuery.list();
		} catch (Exception e) {
			throw new PersistenceException("execute sql query error", e);
		}
	}

	@Override
	public List find(String queryString, int from, int count)
			throws PersistenceException {
		return find(queryString, (Object[]) null, from, count);
	}

	@Override
	public List find(String queryString, Object value, int from, int count)
			throws PersistenceException {
		return find(queryString, new Object[] { value }, from, count);
	}

	@Override
	public List find(String queryString, Object[] values, int from, int count)
			throws PersistenceException {
		try {
			Query query = getHibernateSession().createQuery(queryString);
			query.setFirstResult(from);
			query.setMaxResults(count);
			if (values != null) {
				for (int i = 0; i < values.length; i++)
					query.setParameter(i, values[i]);
			}
			return query.list();
		} catch (Exception ex) {
			throw new PersistenceException("unique result query error", ex);
		}
	}

}
