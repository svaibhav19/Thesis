package edu.kit.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class HasSelector {
	
	@XmlElement(name = "Selector", namespace = IRINS.j4)
	private SelectorType selector;

	public SelectorType getSelector() {
		return selector;
	}

	public void setSelector(SelectorType selector) {
		this.selector = selector;
	}

}
