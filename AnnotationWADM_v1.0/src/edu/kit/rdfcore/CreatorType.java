package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CreatorType {
	
	@XmlElement(name="SoftwareAgent", namespace=IRINS.j2)
	private SoftwareAgentType softwareAgent;

	public SoftwareAgentType getSoftwareAgent() {
		return softwareAgent;
	}

	public void setSoftwareAgent(SoftwareAgentType softwareAgent) {
		this.softwareAgent = softwareAgent;
	}
	
}
