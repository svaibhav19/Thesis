package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;

@XmlAccessorType(XmlAccessType.FIELD)
public class ItemsType {
	
	@XmlElement(name="CreationProvenance", namespace = IRINS.j4)
	private CreationProvenanceType creationProvenance;
	
	@XmlElement(name="EmbeddedContent", namespace=IRINS.j0)
	private EmbeddedContentType embeddedContent;
	
	@XmlElement(name="ExternalWebResource", namespace=IRINS.j4)
	private ExternalWebResourceType externalWebResource;
	
	@XmlElement(name="Resource", namespace=IRINS.j4)
	private ResourceBodyType resource;

	
	
	public CreationProvenanceType getCreationProvenance() {
		return creationProvenance;
	}

	public void setCreationProvenance(CreationProvenanceType creationProvenance) {
		this.creationProvenance = creationProvenance;
	}

	public EmbeddedContentType getEmbeddedContent() {
		return embeddedContent;
	}

	public void setEmbeddedContent(EmbeddedContentType embeddedContent) {
		this.embeddedContent = embeddedContent;
	}

	public ResourceBodyType getResource() {
		return resource;
	}

	public void setResource(ResourceBodyType resource) {
		this.resource = resource;
	}

	public ExternalWebResourceType getExternalWebResource() {
		return externalWebResource;
	}

	public void setExternalWebResource(ExternalWebResourceType externalWebResource) {
		this.externalWebResource = externalWebResource;
	}
	
}
