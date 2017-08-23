package edu.kit.api;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.impl.targets.SpecificResource;

import edu.kit.pagexml.MetadataType;
import edu.kit.pagexml.PageType;
import edu.kit.pagexml.PcGtsType;
import edu.kit.pagexml.RegionType;
import edu.kit.util.PropertyHandler;

public interface AnnotationGenerator {
	
	String ServiceURI = PropertyHandler.instance().serviceURL;
	String annotationURL = PropertyHandler.instance().baseURL;

	public void parseAnnotations(String digitalObjID, String annotationString)
			throws RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException,
			MalformedQueryException, UpdateExecutionException;

	void postToJenaStore();

	void processXML(PcGtsType pcgtsTypeObj, String digitalObjID, String softAgentResourceID) throws RepositoryException,
			IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException;

	List<Model> createOtherAnnotations(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException,
			UpdateExecutionException;

	Model createOtherBodyTarget(Annotation annoations, RegionType regions, String digitalObjID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException,
			UpdateExecutionException;

	String getRegionType(RegionType regionType);

	Model createPageAnnotation(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException,
			UpdateExecutionException;

	Model createBodyTarget(Annotation annoations, PageType page, String digitalObjID) throws RepositoryException,
			IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException;

	SpecificResource getTarget(String points, String digitalObjID) throws RepositoryException, IllegalAccessException,
			InstantiationException, MalformedQueryException, UpdateExecutionException;

	Annotation createAnnoataionPart(MetadataType metadata, String softAgentResourceID) throws RepositoryException,
			IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException;

	String checkAgentInRegistry(MetadataType metadata)
			throws RepositoryException, IllegalAccessException, InstantiationException;

	Model getJenaModel(String triples);
}
