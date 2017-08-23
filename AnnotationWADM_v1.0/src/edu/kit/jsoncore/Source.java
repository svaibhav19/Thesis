
package edu.kit.jsoncore;

public class Source {

    private String id;
    private String type;
    private String format;
    private String language;
    private Creator_ creator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Creator_ getCreator() {
        return creator;
    }

    public void setCreator(Creator_ creator) {
        this.creator = creator;
    }

}
