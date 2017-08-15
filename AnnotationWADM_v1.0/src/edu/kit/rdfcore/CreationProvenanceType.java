package edu.kit.rdfcore;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CreationProvenanceType {

	@XmlElement(name = "hasSource", namespace = IRINS.j0)
	private HasSourceType hasSource;

	@XmlElement(name = "hasSelector", namespace = IRINS.j0)
	private HasSelector hasSelector;
	
	@XmlElement(name="items", namespace = IRINS.j3)
	private List<ItemsType> items;
	
	@XmlElement(name="title",namespace=IRINS.dc)
	private String title;
	
	@XmlElement(name="subject", namespace=IRINS.dc)
	private String subject;
	
	@XmlElement(name="value", namespace=IRINS.rdf)
	private String value;
	
	@XmlElement(name="identifier",namespace=IRINS.dc)
	private String identifier;

	public HasSourceType getHasSource() {
		return hasSource;
	}

	public void setHasSource(HasSourceType hasSource) {
		this.hasSource = hasSource;
	}

	public HasSelector getHasSelector() {
		return hasSelector;
	}

	public void setHasSelector(HasSelector hasSelector) {
		this.hasSelector = hasSelector;
	}

	public List<ItemsType> getItems() {
		return items;
	}

	public void setItems(List<ItemsType> items) {
		this.items = items;
	}

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

}
