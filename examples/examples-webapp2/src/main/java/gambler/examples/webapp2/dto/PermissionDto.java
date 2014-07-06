package gambler.examples.webapp2.dto;

public class PermissionDto {

    private Long pid;

    private String name;
    
    private int ptype;

    private String remark;

    private boolean userHave;

    private boolean roleHave;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isUserHave() {
        return userHave;
    }

    public void setUserHave(boolean userHave) {
        this.userHave = userHave;
    }

    public boolean isRoleHave() {
        return roleHave;
    }

    public void setRoleHave(boolean roleHave) {
        this.roleHave = roleHave;
    }

	public int getPtype() {
		return ptype;
	}

	public void setPtype(int ptype) {
		this.ptype = ptype;
	}
}
