
package edu.kit.jsoncore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@context",
    "id",
    "type",
    "motivation",
    "creator",
    "created",
    "generator",
    "generated",
    "stylesheet",
    "body",
    "target"
})
public class AnnotationJson {

    @JsonProperty("@context")
    private String context;
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("motivation")
    private String motivation;
    @JsonProperty("creator")
    private Creator creator;
    @JsonProperty("created")
    private String created;
    @JsonProperty("generator")
    private Generator generator;
    @JsonProperty("generated")
    private String generated;
    @JsonProperty("stylesheet")
    private Stylesheet stylesheet;
    @JsonProperty("body")
    private List<Body> body = null;
    @JsonProperty("target")
    private Target target;

    @JsonProperty("@context")
    public String getContext() {
        return context;
    }

    @JsonProperty("@context")
    public void setContext(String context) {
        this.context = context;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("motivation")
    public String getMotivation() {
        return motivation;
    }

    @JsonProperty("motivation")
    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    @JsonProperty("creator")
    public Creator getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("generator")
    public Generator getGenerator() {
        return generator;
    }

    @JsonProperty("generator")
    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    @JsonProperty("generated")
    public String getGenerated() {
        return generated;
    }

    @JsonProperty("generated")
    public void setGenerated(String generated) {
        this.generated = generated;
    }

    @JsonProperty("stylesheet")
    public Stylesheet getStylesheet() {
        return stylesheet;
    }

    @JsonProperty("stylesheet")
    public void setStylesheet(Stylesheet stylesheet) {
        this.stylesheet = stylesheet;
    }

    @JsonProperty("body")
    public List<Body> getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(List<Body> body) {
        this.body = body;
    }

    @JsonProperty("target")
    public Target getTarget() {
        return target;
    }

    @JsonProperty("target")
    public void setTarget(Target target) {
        this.target = target;
    }
    
}
