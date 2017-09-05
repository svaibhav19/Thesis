package edu.kit.rdfcore;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationType {
	
	@XmlAttribute(name="about", namespace=IRINS.rdf)
	private String about;
	
	@XmlElement(name="motivatedBy", namespace=IRINS.j0)
	private MotivatedBy motivatedBy;
	
	@XmlElement(name="hasTarget", namespace = IRINS.j0)
	private HasTarget hasTarget;
	
	@XmlElement(name="hasBody",namespace=IRINS.j0)
	private List<HasBodyType> hasBody;
	
	@XmlElement(name="modified", namespace=IRINS.j1)
	private String modified;
	
	@XmlElement(name="created", namespace=IRINS.j1)
	private String created;
	
	@XmlElement(name="creator", namespace=IRINS.j1)
	private CreatorType creator;
	
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public MotivatedBy getMotivatedBy() {
		return motivatedBy;
	}

	public void setMotivatedBy(MotivatedBy motivatedBy) {
		this.motivatedBy = motivatedBy;
	}

	public HasTarget getHasTarget() {
		return hasTarget;
	}

	public void setHasTarget(HasTarget hasTarget) {
		this.hasTarget = hasTarget;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public CreatorType getCreator() {
		return creator;
	}

	public void setCreator(CreatorType creator) {
		this.creator = creator;
	}

	public List<HasBodyType> getHasBody() {
		return hasBody;
	}

	public void setHasBody(List<HasBodyType> hasBody) {
		this.hasBody = hasBody;
	}
	
}
