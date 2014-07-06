package gambler.examples.webapp2.dto;

public class NaviItemDto {

    private String name;

    private String url;

    private String target;

    /**
     * @param name
     * @param url
     */
    public NaviItemDto(String name, String url) {
        super();
        this.name = name;
        this.url = url;
    }

    public NaviItemDto(String name, String url, String target) {
        super();
        this.name = name;
        this.url = url;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
