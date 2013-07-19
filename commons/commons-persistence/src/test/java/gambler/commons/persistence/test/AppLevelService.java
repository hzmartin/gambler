package gambler.commons.persistence.test;

import gambler.commons.persistence.PersistenceException;
import gambler.commons.persistence.hibernate.IHibernatePersistence;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AppLevelService {

	private IHibernatePersistence persistence;

	@Transactional
	public void save(SysAppLevel level) throws PersistenceException {
		persistence.save(level);
	}

	public SysAppLevel findById(long id) throws PersistenceException {
		return (SysAppLevel) persistence.load(SysAppLevel.class, id);
	}

	@Transactional
	public void delete(SysAppLevel level) throws PersistenceException {
		persistence.delete(level);
	}

	public IHibernatePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(IHibernatePersistence persistence) {
		this.persistence = persistence;
	}

	public void saveOrUpdate(SysAppLevel level) throws PersistenceException {
		persistence.saveOrUpdate(level);

	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void moreTasks(SysAppLevel level) throws PersistenceException{
		persistence.save(level);
		level.setParent(level.getParent() + 1);
		persistence.saveOrUpdate(level);
	}
}
