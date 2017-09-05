package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SelectorType {
	
	@XmlElement(name="conformsTo", namespace = IRINS.j1)
	private String conformsTo;
	
	@XmlElement(name="value", namespace = IRINS.rdf)
	private String value;

	public String getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
