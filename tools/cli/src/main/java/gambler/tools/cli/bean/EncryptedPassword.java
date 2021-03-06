package gambler.tools.cli.bean;

/**
 *
 * @author wangqihui
 */
public class EncryptedPassword {
	
	public static final String TYPE_DEFAULT = "-";
	public static final String TYPE_LOGIN = "login";
	public static final String TYPE_QUERY= "query";
	public static final String TYPE_PAY = "pay";
    
    private Integer id;
    
    private String uid;
    
    private String site;
    
    private String type;
    
    private String passwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
}
