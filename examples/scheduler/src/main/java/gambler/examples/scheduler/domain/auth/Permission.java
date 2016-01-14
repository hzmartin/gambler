package gambler.examples.scheduler.domain.auth;

import java.io.Serializable;

/**
 * permission definition : (name, content)
 *
 * @auther Martin
 */

public class Permission implements Serializable
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3005970060595537942L;

	private long pid;

	private String name;
	
	private int ptype;

	private String remark;

	@Override
	public String toString() {
		return "Permission [pid=" + pid + ", name=" + name + "]";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getPid()
	{
		return pid;
	}

	public void setPid(long pid)
	{
		this.pid = pid;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Permission other = (Permission) obj;
		return pid == other.pid;
	}

	public int getPtype() {
		return ptype;
	}

	public void setPtype(int ptype) {
		this.ptype = ptype;
	}

}
