package gambler.examples.scheduler.domain.auth;

import java.io.Serializable;

/**
 * the relationship between role and permission
 * 
 * @auther Martin
 */

public class RolePermission implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2603872394551840349L;

    private Long id;
    
    private Long rid;
    
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

}
