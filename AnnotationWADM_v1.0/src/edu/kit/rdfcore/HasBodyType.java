package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class HasBodyType {

	@XmlElement(name="Choice", namespace=IRINS.j0)
	private ChoiceType choice;
	
	@XmlElement(name="CreationProvenance", namespace=IRINS.j4)
	private CreationProvenanceType creationProvenance;
	
	@XmlElement(name="ExternalWebResource", namespace= IRINS.j4)
	private ExternalWebResourceType externalWebResource;
	
	@XmlElement(name="Resource", namespace=IRINS.j4)
	private ResourceBodyType resource;

	public CreationProvenanceType getCreationProvenance() {
		return creationProvenance;
	}

	public void setCreationProvenance(CreationProvenanceType creationProvenance) {
		this.creationProvenance = creationProvenance;
	}

	public ChoiceType getChoice() {
		return choice;
	}

	public void setChoice(ChoiceType choice) {
		this.choice = choice;
	}

	public ExternalWebResourceType getExternalWebResource() {
		return externalWebResource;
	}

	public void setExternalWebResource(ExternalWebResourceType externalWebResource) {
		this.externalWebResource = externalWebResource;
	}

	public ResourceBodyType getResource() {
		return resource;
	}

	public void setResource(ResourceBodyType resource) {
		this.resource = resource;
	}
	
}
