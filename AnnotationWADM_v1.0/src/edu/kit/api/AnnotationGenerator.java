package edu.kit.api;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.jena.update.GraphStore;
import org.apache.jena.update.GraphStoreFactory;
import org.apache.jena.update.UpdateAction;
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
import edu.kit.util.PropertyHandler;
import edu.kit.util.QueryUtil;

/**
 * 
 * @author Vaibhav
 *
 */

public class AnnotationGenerator {

	private Anno4j anno4j;
	private QueryUtil qureyUtil;

	final String ServiceURI = PropertyHandler.instance().serviceURL;
	String annotationURL = PropertyHandler.instance().baseURL;

//	private List<Model> modelList = new ArrayList<Model>();
	private Map<String, Model> modelMap = new HashMap<String, Model>();

	public AnnotationGenerator() {
		qureyUtil = new QueryUtil();

	}

	public void parseAnnotations(String digitalObjID, String annotationString)
			throws RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException,
			MalformedQueryException, UpdateExecutionException {
		anno4j = new Anno4j();

		// converting normal String to xmlObject
		PcGtsType pcgtsTypeObj = parseXML(annotationString);
		// processing the xml file and converting the xml to WADM
		processXML(pcgtsTypeObj,digitalObjID);
		// store to apache Jena
		postToJenaStore();
	}

	private void postToJenaStore() {
		System.out.println("insideJena Store");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(ServiceURI);
//		for (Model model : modelList) {
//			accessor.add(storeURL, model);
//		}
		String pageXmlID = ""+UUID.randomUUID();
		UpdateRequest request = UpdateFactory.create();
		request.add("CREATE GRAPH <"+ServiceURI+"/registry>");
		
		for (String resourceID : modelMap.keySet()) {
			accessor.add(annotationURL+resourceID, modelMap.get(resourceID));
			request.add(qureyUtil.getAnnotationRegistryQuery(pageXmlID, annotationURL + resourceID));
		}
		 UpdateProcessor createRemote = UpdateExecutionFactory.createRemote(request, ServiceURI);
	        createRemote.execute();
		

	}

	/**
	 * This Method is used to create all the annotations based on the Regions
	 * and Page Tag.
	 * 
	 * @param pcgtsTypeObj
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws RepositoryException
	 * @throws UpdateExecutionException
	 * @throws MalformedQueryException
	 */
	private void processXML(PcGtsType pcgtsTypeObj,String digitalObjID) throws RepositoryException, IllegalAccessException,
			InstantiationException, MalformedQueryException, UpdateExecutionException {

		String softAgentResourceID = checkAgentInRegistry(pcgtsTypeObj.getMetadata());
		createPageAnnotation(pcgtsTypeObj, softAgentResourceID,digitalObjID);
		createOtherAnnotations(pcgtsTypeObj, softAgentResourceID,digitalObjID);
//		modelList.add(pageModel);
//		modelList.addAll(regionModel);
	}

	private List<Model> createOtherAnnotations(PcGtsType pcgtsTypeObj, String softAgentResourceID,String digitalObjID) throws RepositoryException,
			IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException {
//		List<Model> modelList = new ArrayList<Model>();
		for (RegionType regions : pcgtsTypeObj.getPage().getTextRegionOrImageRegionOrLineDrawingRegion()) {
			Annotation annoations = createAnnoataionPart(pcgtsTypeObj.getMetadata(), softAgentResourceID);
			Model otherModel = createOtherBodyTarget(annoations, regions,digitalObjID);
//			modelList.add(otherModel);
			modelMap.put(annoations.getResourceAsString(), otherModel);
		}
//		return modelList;
		return null;

	}

	private Model createOtherBodyTarget(Annotation annoations, RegionType regions, String digitalObjID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException {
		// useless code but it is requried as it is not printing other
		// discriptions without this initialisations.
		TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);
		textBody.setValue(regions.getCoords().toString());

		Choice choice = anno4j.createObject(Choice.class);
		for (UserAttributeType userAttribuets : regions.getUserDefined().getUserAttribute()) {
			TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
			txtBody2.setValue(userAttribuets.getValue());
			txtBody2.setName(userAttribuets.getName());
			txtBody2.setSubject(getRegionType(regions));
			txtBody2.setIdentifier(regions.getId());

			if (null != regions.getCustom())
				txtBody2.setConformsTo(regions.getCustom());

			choice.addItem(txtBody2);
		}

		SpecificResource specific = getTarget(regions.getCoords().getPoints(),digitalObjID);

		annoations.addBody(choice);
		annoations.addTarget(specific);

		Model model = getJenaModel(annoations.getTriples(RDFFormat.NTRIPLES));
		writeTofile(annoations, "New_" + regions.getId());
		return model;
	}

	private String getRegionType(RegionType regionType) {
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

	private Model createPageAnnotation(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID) throws RepositoryException,
			IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException {
		Annotation annoations = createAnnoataionPart(pcgtsTypeObj.getMetadata(), softAgentResourceID);
		Model model = createBodyTarget(annoations, pcgtsTypeObj.getPage(),digitalObjID);
		modelMap.put(annoations.getResourceAsString(), model);
		return model;
	}

	private Model createBodyTarget(Annotation annoations, PageType page,String digitalObjID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException {

		TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);
		textBody.setValue(page.getBorder().getCoords().toString());

		Choice choice = anno4j.createObject(Choice.class);
		for (UserAttributeType userAttribuets : page.getUserDefined().getUserAttribute()) {
			TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
			txtBody2.setValue(userAttribuets.getValue());
			txtBody2.setName(userAttribuets.getName());
			choice.addItem(txtBody2);
		}

		SpecificResource specific = getTarget(page.getBorder().getCoords().getPoints(),digitalObjID);

		annoations.addBody(choice);
		annoations.addTarget(specific);

		Model model = getJenaModel(annoations.getTriples(RDFFormat.NTRIPLES));
		
		writeTofile(annoations, "pageAnnotation");
		return model;

	}

	private void writeTofile(Annotation annoations, String fileName) {
		File f = new File("C:\\Users\\Vaibhav\\Desktop\\AnnotationOutPut\\" + fileName + ".xml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
			writer.write(annoations.getTriples(RDFFormat.RDFXML));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}

	}

	private SpecificResource getTarget(String points,String digitalObjID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException {
		SpecificResource specific = anno4j.createObject(SpecificResource.class);
		SvgSelector svg = anno4j.createObject(SvgSelector.class);
		svg.setValue(points);
		specific.setSelector(svg);

		ResourceObject source = anno4j.createObject(ResourceObject.class);
		source.setResourceAsString(digitalObjID);
		specific.setSource(source);

		return specific;
	}

	private Annotation createAnnoataionPart(MetadataType metadata, String softAgentResourceID)
			throws RepositoryException, IllegalAccessException, InstantiationException, MalformedQueryException,
			UpdateExecutionException {
		Annotation annotation = anno4j.createObject(Annotation.class);

		Software agent = anno4j.createObject(Software.class);
		agent.setResourceAsString(softAgentResourceID);
		annotation.setCreator(agent);
		annotation.setCreated(metadata.getCreated().toString() + "z");
		annotation.setModified(metadata.getCreated().toString() + "z");
		annotation.addMotivation(MotivationFactory.getDescribing(anno4j));
		return annotation;
	}

	/**
	 * This Method is used to store new creator information and retrieve the
	 * stored user from JENA.
	 * 
	 * @param softwareAgent
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws RepositoryException
	 */
	private String checkAgentInRegistry(MetadataType metadata)
			throws RepositoryException, IllegalAccessException, InstantiationException {

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

		Software softwareAgent = anno4j.createObject(Software.class);
		softwareAgent.setName(metadata.getCreator());

		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(ServiceURI);
		Model model = getJenaModel(softwareAgent.getTriples(RDFFormat.NTRIPLES));
		accessor.add(annotationURL + "agents", model);
		return softwareAgent.getResourceAsString();
	}

	private Model getJenaModel(String triples) {
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
	 */
	private PcGtsType parseXML(String annotationString) {
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
			e.printStackTrace();
		}
		return null;
	}

}
