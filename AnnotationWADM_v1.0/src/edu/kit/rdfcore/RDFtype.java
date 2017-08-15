package edu.kit.rdfcore;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace=IRINS.rdf)
@XmlAccessorType(XmlAccessType.FIELD)
public class RDFtype {
	
	@XmlElement(name="Annotation", namespace=IRINS.j0)
	private AnnotationType annotation;
	
	@XmlAnyElement(lax = true)
	public List<Object> any;
	
	public List<Object> getAny() {
		return any;
	}

	public void setAny(List<Object> any) {
		this.any = any;
	}

	public AnnotationType getAnnotation() {
		return annotation;
	}

	public void setAnnotation(AnnotationType annotation) {
		this.annotation = annotation;
	}
	
}
