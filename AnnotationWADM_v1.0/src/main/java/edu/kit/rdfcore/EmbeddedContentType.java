package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class EmbeddedContentType {
	
	@XmlElement(name="title",namespace=IRINS.dc)
	private String title;
	
	@XmlElement(name="subject", namespace=IRINS.dc)
	private String subject;
	
	@XmlElement(name="value", namespace=IRINS.rdf)
	private String value;
	
	@XmlElement(name="identifier",namespace=IRINS.dc)
	private String identifier;
	
	@XmlElement(name = "unit", namespace=IRINS.j6)
	private String unit;
	
	@XmlElement(name="format", namespace=IRINS.dc)
	private String format;
	
	@XmlElement(name="contributor", namespace=IRINS.dc)
	private String contributor;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

}
