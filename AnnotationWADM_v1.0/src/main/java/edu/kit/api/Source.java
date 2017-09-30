package edu.kit.api;

import org.openrdf.annotations.Iri;

import com.github.anno4j.model.Agent;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.namespaces.Anno4jNS;
import com.github.anno4j.model.namespaces.DC;
import com.github.anno4j.model.namespaces.OADM;

import edu.kit.jsoncore.Creator;

@Iri(OADM.HAS_SOURCE)
public interface Source extends ResourceObject {

	@Iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
	String getId();
	
	@Iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
	void setId(String id);

	@Iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
	String getType();
	
	@Iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
	void setType(String type);

	@Iri(DC.FORMAT)
	String getFormat();

	@Iri(DC.FORMAT)
	void setFormat(String format);

	@Iri(DC.LANGUAGE)
	String getLanguage();

	@Iri(DC.LANGUAGE)
	void setLanguage(String language);
	
	@Iri(Anno4jNS.AGENT)
	void setCreator(Agent agent);
	
	@Iri(Anno4jNS.AGENT)
	Agent getAgent();

}
