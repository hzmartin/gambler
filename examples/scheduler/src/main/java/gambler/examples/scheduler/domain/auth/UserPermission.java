package gambler.examples.scheduler.domain.auth;

import java.io.Serializable;

/**
 * the relationship between user and permission
 *
 * @auther Martin
 */

public class UserPermission implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2763947191459836226L;

    private Long id;
    
    private Long uid;
    
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

}
