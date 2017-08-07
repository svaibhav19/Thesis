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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.Motivation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.State;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.body.TextualBody;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.selector.SvgSelector;
import com.github.anno4j.model.impl.state.HttpRequestState;
import com.github.anno4j.model.impl.targets.SpecificResource;

import edu.kit.pagexml.AdvertRegionType;
import edu.kit.pagexml.ChartRegionType;
import edu.kit.pagexml.ChemRegionType;
import edu.kit.pagexml.GraphicRegionType;
import edu.kit.pagexml.ImageRegionType;
import edu.kit.pagexml.LineDrawingRegionType;
import edu.kit.pagexml.MathsRegionType;
import edu.kit.pagexml.MusicRegionType;
import edu.kit.pagexml.NoiseRegionType;
import edu.kit.pagexml.PcGtsType;
import edu.kit.pagexml.RegionType;
import edu.kit.pagexml.SeparatorRegionType;
import edu.kit.pagexml.TableRegionType;
import edu.kit.pagexml.TextRegionType;
import edu.kit.pagexml.UnknownRegionType;
import edu.kit.pagexml.UserAttributeType;

/**
 * 
 * @author Vaibhav
 *
 *         This class is used for processing the page.xml 2017 This has 3
 *         methods for different operations 1st Method is for converting
 *         xmlString to main Object 2nd Method is for processing the Object and
 *         converting the XML to WADM 3rd Method is for storing the WADM to
 *         Apache Jena.
 * 
 */
public class PageXMLProcessing {

	private String digitalObjId;
	private String xmlString;
	final String ServiceURI = "http://localhost:3030/kit/";
	String annoID="http://kit.edu/anno/12345";
	Model model = ModelFactory.createDefaultModel();
	
	Anno4j anno4j;

	public PageXMLProcessing(String digitalObjId, String xmlString) {
		this.digitalObjId = digitalObjId;
		this.xmlString = xmlString;
	}

	/**
	 * Processing starts from this class It requires to parameters.
	 * 
	 * @param digitalObjId
	 *            unique id
	 * @param xmlString
	 *            page.xml as InputStream/String
	 */
	public void parseAndStoreXML() {

		try {
			anno4j = new Anno4j();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		}

		// converting normal String to xmlObject
		PcGtsType pcgtsTypeObj = parseXML();
		// processing the xml file and converting the xml to WADM
		Annotation processedAnnotations = processXML(pcgtsTypeObj);
		// Storing the created Annotaions into apache Jena
		String message = storeAnnoataions(processedAnnotations);

	}

	/**
	 * This is the final phase for storing the created Annoations to apache
	 * JENA.
	 * 
	 * @param processedAnnotations
	 * @return success/error Message.
	 */
	private String storeAnnoataions(Annotation processedAnnotations) {
		
		
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(ServiceURI);
//		if(!accessor.containsModel(annoID)){
			accessor.putModel(annoID,model);
//			accessor.add(model);
//		}else
//		{
//			throw new Exception("Prospective graph for the givern workflow id already exists in store");
//		}

		return null;
	}

	/**
	 * This Method is used for converting the XML Object to WADM using anno4j
	 * Lib
	 * 
	 * @param pcgtsTypeObj
	 * @return annotations object
	 */
	private Annotation processXML(PcGtsType pcgtsTypeObj) {
		
		createPageAnnoatations(pcgtsTypeObj);
		createAllRegion(pcgtsTypeObj);
		return null;
	}

	/**
	 * This method is used to convert all the sections of page xml to annotations.
	 * This method uses some of the new namespaces from
	 * http://dublincore.org/documents/2012/06/14/dcmi-terms/
	 * @param pcgtsTypeObj
	 */
	private void createAllRegion(PcGtsType pcgtsTypeObj) {

		for (RegionType regionType : pcgtsTypeObj.getPage().getTextRegionOrImageRegionOrLineDrawingRegion()) {

			Annotation regionAnnotations;
			try {
				regionAnnotations = anno4j.createObject(Annotation.class);
				addAgent(regionAnnotations, pcgtsTypeObj);
				regionAnnotations.addMotivation(addMotivation());

				TextualBody textBody = anno4j.createObject(TextualBody.class);
				textBody.setValue("Coords points=" + regionType.getCoords().getPoints());

				// pageAnnoataion.addBody(textBody);

				// adding multiples bodies with their properties and values and
				// types ...
				Choice choice = anno4j.createObject(Choice.class);

				for (UserAttributeType userAttribuets : regionType.getUserDefined().getUserAttribute()) {
					TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
					txtBody2.setValue(userAttribuets.getValue());
					txtBody2.setName(userAttribuets.getName());
					txtBody2.setSubject(getType(regionType));
					txtBody2.setIdentifier(regionType.getId());
					
					if(null != regionType.getCustom())
						txtBody2.setConformsTo(regionType.getCustom());
					
					choice.addItem(txtBody2);
				}

				regionAnnotations.addBody(textBody);
				regionAnnotations.addBody(choice);

				SpecificResource specific = getTarget(regionAnnotations, regionType.getCoords().getPoints());

				regionAnnotations.addTarget(specific);
				
				storeAnnotationToJena(regionAnnotations, regionType.getId());
				
			} catch (RepositoryException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * This method returns the type of Region has be present in the tag.
	 * @param regionType
	 * @return
	 */
	private String getType(RegionType regionType) {
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

	private void createPageAnnoatations(PcGtsType pcgtsTypeObj) {
		try {

			// creating page Annotations
			Annotation pageAnnoataion = anno4j.createObject(Annotation.class);

			addAgent(pageAnnoataion, pcgtsTypeObj);
			pageAnnoataion.addMotivation(addMotivation());

			TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);
			textBody.setValue("Coords points=" + pcgtsTypeObj.getPage().getBorder().getCoords().getPoints());
			if(null != pcgtsTypeObj.getPage().getCustom())
				textBody.setConformsTo(pcgtsTypeObj.getPage().getCustom());
			
			// adding multiples bodies with their properties and values and
			// types ...
			Choice choice = anno4j.createObject(Choice.class);

			for (UserAttributeType userAttribuets : pcgtsTypeObj.getPage().getUserDefined().getUserAttribute()) {
				TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
				txtBody2.setValue(userAttribuets.getValue());
				txtBody2.setName(userAttribuets.getName());
				choice.addItem(txtBody2);
			}

			pageAnnoataion.addBody(textBody);
			pageAnnoataion.addBody(choice);

			SpecificResource specific = getTarget(pageAnnoataion, pcgtsTypeObj.getPage().getBorder().getCoords().getPoints());

			pageAnnoataion.addTarget(specific);
			
			storeAnnotationToJena(pageAnnoataion,"pageAnnotations");

		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	private Motivation addMotivation() throws RepositoryException, IllegalAccessException, InstantiationException {
		return MotivationFactory.getDescribing(anno4j);
	}

	private void storeAnnotationToJena(Annotation pageAnnoataion, String id) {
		
		String turtleFile = pageAnnoataion.getTriples(RDFFormat.TURTLE);
		try( final InputStream in = new ByteArrayInputStream(turtleFile.getBytes("UTF-8")) ) {
		    /* Naturally, you'd substitute the syntax of your actual
		     * content here rather than use N-TRIPLE.
		     */
		    model.read(in, null, "TTL");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File f = new File("C:\\Users\\Vaibhav\\Desktop\\AnnotationOutPut\\"+id+".xml");
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(f), "utf-8"));
		    writer.write(pageAnnoataion.getTriples(RDFFormat.RDFXML));
		} catch (IOException ex) {
		  ex.printStackTrace();
		} finally {
		   try {writer.close();} catch (Exception ex) {/*ignore*/}
		}
		
	}

	private SpecificResource getTarget(Annotation pageAnnoataion, String points) {
		SpecificResource specific;
		try {
			specific = anno4j.createObject(SpecificResource.class);
			/*ResourceObject source = anno4j.createObject(ResourceObject.class);
			source.setResourceAsString("http://example.com/document1#points="
					+ points);
			specific.setSource(source);*/
			SvgSelector svg = anno4j.createObject(SvgSelector.class);
			svg.setValue(points);
			specific.setSelector(svg);
			
			
			State state = anno4j.createObject(State.class);
			HttpRequestState reqState = anno4j.createObject(HttpRequestState.class);
			reqState.setValue("Accept: application/xml");
			specific.addState(state);
			return specific;
		} catch (RepositoryException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} 

		return null;
	}

	private void addAgent(Annotation annoataion, PcGtsType pcgtsTypeObj) {
		Software softwareAgent;
		try {
			softwareAgent = anno4j.createObject(Software.class);
			softwareAgent.setName(pcgtsTypeObj.getMetadata().getCreator());

			annoataion.setCreator(softwareAgent);
			annoataion.setCreated(pcgtsTypeObj.getMetadata().getCreated().toString() + "z");
			annoataion.setModified(pcgtsTypeObj.getMetadata().getCreated().toString() + "z");
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This Method is used to convert the pageXML String to JAXb Object.
	 * 
	 * @return JAXB object for processing.
	 */
	private PcGtsType parseXML() {

		Source source = new StreamSource(new StringReader(xmlString));
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
