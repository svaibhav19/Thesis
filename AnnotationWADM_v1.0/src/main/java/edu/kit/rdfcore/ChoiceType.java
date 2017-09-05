package edu.kit.rdfcore;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChoiceType {

	@XmlAttribute(name="about",namespace=IRINS.rdf)
	private String about;
	
	@XmlElement(name="items", namespace = IRINS.j3)
	private List<ItemsType> items;

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<ItemsType> getItems() {
		return items;
	}

	public void setItems(List<ItemsType> items) {
		this.items = items;
	}
	
}
