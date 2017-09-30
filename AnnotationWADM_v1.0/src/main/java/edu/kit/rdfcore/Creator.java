package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Creator {
	
	@XmlAttribute(name = "resource", namespace = IRINS.rdf)
	private String resource;
	
	@XmlElement(name ="about", namespace=IRINS.rdf)
	private String about;

	@XmlElement(name = "SoftwareAgent", namespace = IRINS.j2)
	private SoftwareAgentType softwareAgent;

	public SoftwareAgentType getSoftwareAgent() {
		return softwareAgent;
	}

	public void setSoftwareAgent(SoftwareAgentType softwareAgent) {
		this.softwareAgent = softwareAgent;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
}
