package edu.kit.api.page;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.selector.SvgSelector;
import com.github.anno4j.model.impl.targets.SpecificResource;

import edu.kit.api.TextAnnotationBody;
import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.exceptions.StatusCode;
import edu.kit.pagexml.AdvertRegionType;
import edu.kit.pagexml.ChartRegionType;
import edu.kit.pagexml.ChemRegionType;
import edu.kit.pagexml.GraphicRegionType;
import edu.kit.pagexml.ImageRegionType;
import edu.kit.pagexml.LineDrawingRegionType;
import edu.kit.pagexml.MathsRegionType;
import edu.kit.pagexml.MetadataType;
import edu.kit.pagexml.MusicRegionType;
import edu.kit.pagexml.NoiseRegionType;
import edu.kit.pagexml.PageType;
import edu.kit.pagexml.PcGtsType;
import edu.kit.pagexml.RegionType;
import edu.kit.pagexml.SeparatorRegionType;
import edu.kit.pagexml.TableRegionType;
import edu.kit.pagexml.TextRegionType;
import edu.kit.pagexml.UnknownRegionType;
import edu.kit.pagexml.UserAttributeType;
import edu.kit.util.QueryUtil;

/**
 * 
 * @author Vaibhav
 *	
 *	This method is used to parse and transform the PAGE to annotation model.
 *	The corresponding classes are specific to the PAGE 2017 specification. 
 *
 */

public class PageAnnotationGeneratorImpl implements PageAnnotationGenerator {

	private Anno4j anno4j;
	private QueryUtil qureyUtil;

	DatasetAccessor accessor;
	private List<String> resourceIDList = new ArrayList<String>();

	public PageAnnotationGeneratorImpl() throws RepositoryException, RepositoryConfigException {
		qureyUtil = new QueryUtil();
		anno4j = new Anno4j();
	}

	public PageAnnotationGeneratorImpl(Anno4j anno4j) {
		this.anno4j = anno4j;
		qureyUtil = new QueryUtil();
	}

	/**
	 * This method performs operation like transforming the string to JAXBObject, convert the annotation and store into WADM.
	 * 
	 * @param	String DigitalObject id
	 * @param	String PAGE-XML
	 * @return	String Success message 
	 */
	@Override
	public String parseAnnotations(String digitalObjID, String annotationString) throws AnnotationExceptions {

		createDataSet();
		
		// converting normal String to xmlObject
		PcGtsType pcgtsTypeObj = parseXML(annotationString);
		String softAgentResourceID = checkAgentInRegistry(pcgtsTypeObj.getMetadata());
		// processing the xml file and converting the xml to WADM
		processXML(pcgtsTypeObj, digitalObjID, softAgentResourceID);
		// store to apache Jena

		return postToJenaStore();

	}

	private void createDataSet() {
		accessor = DatasetAccessorFactory.createHTTP(ServiceURI);
	}

	/**
	 * This Method is used to create all the annotations based on the Regions
	 * and Page Tag.
	 * 
	 * @param pcgtsTypeObj
	 * @param softAgentResourceID
	 * @return
	 * @throws AnnotationExceptions
	 */
	@Override
	public void processXML(PcGtsType pcgtsTypeObj, String digitalObjID, String softAgentResourceID)
			throws AnnotationExceptions {
		createPageAnnotation(pcgtsTypeObj, softAgentResourceID, digitalObjID);
		createOtherAnnotations(pcgtsTypeObj, softAgentResourceID, digitalObjID);
	}

	/**
	 * 	This Method is required to create the annotation part. 
	 * 
	 */
	@Override
	public List<Model> createOtherAnnotations(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws AnnotationExceptions {
		for (RegionType regions : pcgtsTypeObj.getPage().getTextRegionOrImageRegionOrLineDrawingRegion()) {
			Annotation annoations = createAnnoataionPart(pcgtsTypeObj.getMetadata(), softAgentResourceID);
			Model otherModel = createOtherBodyTarget(annoations, regions, digitalObjID);
			accessor.add(annotationURL + annoations.getResourceAsString(), otherModel);
			resourceIDList.add(annoations.getResourceAsString());
		}
		return null;

	}

	/**
	 * This method is used create the annotation of body and target of the body.
	 * 
	 * @param	Annotation
	 * @param	RegionType
	 * @param	String
	 * @return	Model
	 * 
	 */
	@Override
	public Model createOtherBodyTarget(Annotation annoations, RegionType regions, String digitalObjID) throws AnnotationExceptions {

		Choice choice;
		try {
			choice = anno4j.createObject(Choice.class);

			for (UserAttributeType userAttribuets : regions.getUserDefined().getUserAttribute()) {
				TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
				txtBody2.setValue(userAttribuets.getValue());
				txtBody2.setName(userAttribuets.getName());
				txtBody2.setSubject(getRegionType(regions));
				txtBody2.setIdentifier(regions.getId());
				txtBody2.setFormat(userAttribuets.getType());
				if (null != userAttribuets.getDescription())
					txtBody2.setUnit(userAttribuets.getDescription().split(":")[1]);

				if (null != regions.getCustom())
					txtBody2.setContributor(regions.getCustom());

				choice.addItem(txtBody2);
				
			}

			SpecificResource specific = getTarget(regions.getCoords().getPoints(), digitalObjID);

			
			annoations.addBody(choice);
			annoations.addTarget(specific);
			

			Model model = getJenaModel(annoations.getTriples(RDFFormat.NTRIPLES));
			return model;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
	}

	/**
	 * This method is used to get the Specific Region Type.
	 * Different region types.
	 * 
	 * @param	RegionType
	 * @return	String
	 */
	@Override
	public String getRegionType(RegionType regionType) {
		if (regionType.getClass().equals(TextRegionType.class))
			return "TextRegion";
		if (regionType.getClass().equals(ImageRegionType.class))
			return "ImageRegion";
		if (regionType.getClass().equals(LineDrawingRegionType.class))
			return "LineDrawingRegion";
		if (regionType.getClass().equals(GraphicRegionType.class))
			return "GraphicRegion";
		if (regionType.getClass().equals(TableRegionType.class))
			return "TableRegion";
		if (regionType.getClass().equals(ChartRegionType.class))
			return "ChartRegion";
		if (regionType.getClass().equals(SeparatorRegionType.class))
			return "SeparatorRegion";
		if (regionType.getClass().equals(MathsRegionType.class))
			return "MathsRegion";
		if (regionType.getClass().equals(ChemRegionType.class))
			return "ChemRegion";
		if (regionType.getClass().equals(MusicRegionType.class))
			return "MusicRegion";
		if (regionType.getClass().equals(AdvertRegionType.class))
			return "AdvertRegion";
		if (regionType.getClass().equals(NoiseRegionType.class))
			return "NoiseRegion";
		if (regionType.getClass().equals(UnknownRegionType.class))
			return "UnknownRegion";

		return null;
	}

	/**
	 * This method is used to create the Annotation part using the PAGE XML.
	 *  
	 *  @param	PcGtsType 	PAGE XML object
	 *  @param	String	Agent id
	 *  @param	String	Digital Object ID DOI
	 *	@return	Model	RDF model
	 */
	@Override
	public Model createPageAnnotation(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws AnnotationExceptions {
		Annotation annoations;
		Model model;
		annoations = createAnnoataionPart(pcgtsTypeObj.getMetadata(), softAgentResourceID);
		model = createBodyTarget(annoations, pcgtsTypeObj.getPage(), digitalObjID);
		accessor.add(annotationURL + annoations.getResourceAsString(), model);
		resourceIDList.add(annoations.getResourceAsString());
		return model;

	}

	/**
	 * This method is only used to create the body and target part of the Annotations.
	 * @param	Annotation this object will be keep on using to create the annotations.
	 * @param	PageType	This object from PAGE XML
	 * @param	String Digital Object id
	 * @return	Model	RDF MODEL	
	 * 
	 */
	@Override
	public Model createBodyTarget(Annotation annoations, PageType page, String digitalObjID)
			throws AnnotationExceptions {

		TextAnnotationBody fileNameBody;
		try {
			fileNameBody = anno4j.createObject(TextAnnotationBody.class);
			fileNameBody.setUnit(page.getCustom().split(":")[1]);
			fileNameBody.setName("imageFileName");
			fileNameBody.setValue(page.getImageFilename());

			TextAnnotationBody imageHeightBody = anno4j.createObject(TextAnnotationBody.class);
			imageHeightBody.setUnit(page.getCustom().split(":")[1]);
			imageHeightBody.setName("imageHeight");
			imageHeightBody.setValue("" + page.getImageHeight());

			TextAnnotationBody imageWidthBody = anno4j.createObject(TextAnnotationBody.class);
			imageWidthBody.setUnit(page.getCustom().split(":")[1]);
			imageWidthBody.setName("imageWidth");
			imageWidthBody.setValue("" + page.getImageWidth());

			Choice choice = anno4j.createObject(Choice.class);
			for (UserAttributeType userAttribuets : page.getUserDefined().getUserAttribute()) {
				TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
				txtBody2.setValue(userAttribuets.getValue());
				txtBody2.setName(userAttribuets.getName());
				txtBody2.setFormat(userAttribuets.getType());
				if (null != userAttribuets.getDescription()) {
					txtBody2.setUnit(userAttribuets.getDescription().split(":")[1]);
				}
				choice.addItem(txtBody2);
			}

			SpecificResource specific = getTarget(
					(null != page.getBorder() ? page.getBorder().getCoords().getPoints() : ""), digitalObjID);
			annoations.addBody(fileNameBody);
			annoations.addBody(imageHeightBody);
			annoations.addBody(imageWidthBody);
			annoations.addBody(choice);
			annoations.addTarget(specific);
			Model model = getJenaModel(annoations.getTriples(RDFFormat.NTRIPLES));

			return model;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}


	/**
	 * This method is responsible to get the Specific resource of the given type.
	 * 
	 * @param	String	cordinates points
	 * @param	String	Digital object id or the target URL
	 * @return	SpecificResource Object.
	 */
	@Override
	public SpecificResource getTarget(String points, String digitalObjID) throws AnnotationExceptions {
		SpecificResource specific;
		try {
			specific = anno4j.createObject(SpecificResource.class);
			SvgSelector svg = anno4j.createObject(SvgSelector.class);
			if (!points.isEmpty()) {
				svg.setValue(points);
				specific.setSelector(svg);
			}
			ResourceObject source = anno4j.createObject(ResourceObject.class);
			source.setResourceAsString(digitalObjID);
			specific.setSource(source);

			return specific;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (MalformedQueryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (UpdateExecutionException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		
	}

	/**
	 * This method is used to create Annotation part using the metadata present inside the PAGE XML.
	 * 
	 * @param	MetadataType
	 * @param	String
	 * @return	Annotation
	 * 
	 */
	@Override
	public Annotation createAnnoataionPart(MetadataType metadata, String softAgentResourceID)
			throws AnnotationExceptions {
		Annotation annotation;
		try {
			annotation = anno4j.createObject(Annotation.class);
			Software agent = anno4j.createObject(Software.class);
			agent.setResourceAsString(softAgentResourceID);
			annotation.setCreator(agent);
			annotation.setCreated(metadata.getCreated().toString() + "z");
			annotation.setModified(metadata.getCreated().toString() + "z");
			annotation.addMotivation(MotivationFactory.getDescribing(anno4j));
			
			return annotation;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (MalformedQueryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (UpdateExecutionException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}

	/**
	 * This Method is used to store new creator information and retrieve the
	 * stored user from JENA.
	 * 
	 * @param softwareAgent
	 * @return
	 * @throws AnnotationExceptions
	 */
	@Override
	public String checkAgentInRegistry(MetadataType metadata) throws AnnotationExceptions {

		QueryExecution query = QueryExecutionFactory.sparqlService(ServiceURI,
				qureyUtil.checkAgentQuery(metadata.getCreator()));
		ResultSet results = query.execSelect();
		// below method can be used for printing RDF Jena data
		// ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("o");
			if (x.toString().equals(metadata.getCreator())) {
				return soln.get("s").toString();
			}
		}

		Software softwareAgent;
		try {
			softwareAgent = anno4j.createObject(Software.class);
			softwareAgent.setName(metadata.getCreator());
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

		return softwareAgent.getResourceAsString();
	}

	/**
	 * 
	 * This method is used to convert the formed annotation string to RDF representations using JENA Model.
	 * 
	 * @param	String annotation string using anno4j in TURTLE FORMAT
	 * @return	Model	
	 */
	@Override
	public Model getJenaModel(String triples) {
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(triples.getBytes("UTF-8"))) {
			model.read(in, null, "TTL");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return model;
	}

	/**
	 * This method is used to convert the xmlString into PcGtsType Object using
	 * JAXb.
	 * 
	 * @param annotationString
	 * @return Object PcGtsType
	 * @throws AnnotationExceptions
	 */
	private PcGtsType parseXML(String annotationString) throws AnnotationExceptions {
		Source source = new StreamSource(new StringReader(annotationString));
		PcGtsType pcgtsTypeObj = null;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(PcGtsType.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<PcGtsType> root = jaxbUnmarshaller.unmarshal(source, PcGtsType.class);
			pcgtsTypeObj = root.getValue();
			return pcgtsTypeObj;
		} catch (JAXBException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		}
	}

	/**
	 * This method is used to store the annotation id into Annotation Registry
	 * 
	 *  @return	String	conforming message.	
	 */
	@Override
	public String postToJenaStore() {
		int counter = 0;
		String pageXmlID = "" + UUID.randomUUID();
		UpdateRequest request = UpdateFactory.create();
		request.add("CREATE GRAPH <" + ServiceURI + "/registry>");

		for (String resourceID : resourceIDList) {
			request.add(qureyUtil.getAnnotationRegistryQuery(pageXmlID, annotationURL + resourceID));
			counter++;
		}
		UpdateProcessor createRemote = UpdateExecutionFactory.createRemote(request, ServiceURI);
		createRemote.execute();

		return "Total No Of Annotation Create : " + counter;

	}

}
