package edu.kit.api;


import org.openrdf.annotations.Iri;

import com.github.anno4j.model.Agent;
import com.github.anno4j.model.Body;
import com.github.anno4j.model.impl.body.TextualBody;
import com.github.anno4j.model.namespaces.DC;
import com.github.anno4j.model.namespaces.DCTERMS;
import com.github.anno4j.model.namespaces.OADM;
import com.github.anno4j.model.namespaces.RDF;

@Iri("http://www.w3.org/ns/oa#EmbeddedContent")
public interface TextAnnotationBody extends Body, TextualBody {

    @Iri(DC.FORMAT)  
    String getFormat();
    
    @Iri(DC.FORMAT)
    void setFormat(String format);

    @Iri(RDF.VALUE)
    String getValue();

    @Iri(RDF.VALUE)
    void setValue(String value);
    
    @Iri(DC.LANGUAGE)
    String getLanguage();

    @Iri(DC.LANGUAGE)
    void setLanguage(String language);
    
    @Iri(DC.NS+"title")  
    String getName();
    
    @Iri(DC.NS+"title")  
    void setName(String Name);
    
    @Iri(DC.NS+"subject")  
    String getSubject();
    
    @Iri(DC.NS+"subject")  
    void setSubject(String subject);
    
    @Iri(DC.NS+"identifier")  
    String getIdentifier();
    
    @Iri(DC.NS+"identifier")  
    void setIdentifier(String identifier);
    
    @Iri(DC.NS+"contributor")  
    String getContributor();
    
    @Iri(DC.NS+"contributor")  
    void setContributor(String contributor);
    
    @Iri(DC.NS+"description ")  
    String getDescription ();
    
    @Iri(DC.NS+"description ")  
    void setDescription (String description );
    
    @Iri("http://qudt.org/vocab/unit")
    String getUnit();
    
    @Iri("http://qudt.org/vocab/unit")
    void setUnit(String unit);
    
    @Iri(DCTERMS.CREATOR)
    Agent getCreator();

    @Iri(DCTERMS.CREATOR)
    void setCreator(Agent agent);
    
}