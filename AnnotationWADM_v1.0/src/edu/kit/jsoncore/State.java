
package edu.kit.jsoncore;

public class State {

    private String type;
    private String value;
    private RefinedBy refinedBy;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RefinedBy getRefinedBy() {
        return refinedBy;
    }

    public void setRefinedBy(RefinedBy refinedBy) {
        this.refinedBy = refinedBy;
    }

}
