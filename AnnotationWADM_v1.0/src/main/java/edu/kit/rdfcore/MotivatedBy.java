package edu.kit.rdfcore;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MotivatedBy {

	@XmlElement(name = "Resource", namespace = IRINS.j4)
	private ResourceType resource;
	
	@XmlAnyElement(lax = true)
	public List<Object> any;
	
	public List<Object> getAny() {
		return any;
	}

	public void setAny(List<Object> any) {
		this.any = any;
	}

	public ResourceType getResource() {
		return resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}

}
