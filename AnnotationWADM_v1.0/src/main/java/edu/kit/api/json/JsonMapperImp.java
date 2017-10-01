package edu.kit.api.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.object.RDFObject;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Agent;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.Motivation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.Selector;
import com.github.anno4j.model.Style;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Person;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.selector.CSSSelector;
import com.github.anno4j.model.impl.selector.DataPositionSelector;
import com.github.anno4j.model.impl.selector.FragmentSelector;
import com.github.anno4j.model.impl.selector.RangeSelector;
import com.github.anno4j.model.impl.selector.SvgSelector;
import com.github.anno4j.model.impl.selector.TextPositionSelector;
import com.github.anno4j.model.impl.selector.TextQuoteSelector;
import com.github.anno4j.model.impl.selector.XPathSelector;
import com.github.anno4j.model.impl.targets.SpecificResource;
import com.google.gson.Gson;

import edu.kit.api.TextAnnotationBody;
import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.exceptions.StatusCode;
import edu.kit.jsoncore.AnnotationJson;
import edu.kit.jsoncore.Body;
import edu.kit.jsoncore.Creator;
import edu.kit.jsoncore.Creator_;
import edu.kit.jsoncore.EndSelector;
import edu.kit.jsoncore.Generator;
import edu.kit.jsoncore.Item;
import edu.kit.jsoncore.Source;
import edu.kit.jsoncore.StartSelector;
import edu.kit.jsoncore.Stylesheet;
import edu.kit.jsoncore.Target;

/**
 * 
 * @author Vaibhav 
 * 
 * This class is used to map JSON to JSON Pojo classes. Further
 *         this POJO classes are used to map to Anno4j classes.
 */
public class JsonMapperImp implements JsonMapper {

	private Anno4j anno4j;
	DatasetAccessor accessor;
	private Map<String, Software> softwareAgentList = new HashMap<String, Software>();
	private Map<String, Person> personAgentList = new HashMap<String, Person>();

	public JsonMapperImp() throws RepositoryException, RepositoryConfigException {
		anno4j = new Anno4j();
		accessor = DatasetAccessorFactory.createHTTP(ServiceURI);
	}

	/**
	 * this method perform 3 stage operation 1. parse AnnoJSON and map to
	 * respective classes using google gson lib. 2. transform JSON to Annotation
	 * using Anno4j lib. 3. Store the annotation into annotation Store.
	 * 
	 * return String "execution message success if annotation has been stored or
	 * thows exectition if any."
	 */
	@Override
	public String parseJson(String jsonString)
			throws RepositoryException, IllegalAccessException, InstantiationException, AnnotationExceptions {

		Gson gson = new Gson();
		AnnotationJson jsonObj = gson.fromJson(jsonString, AnnotationJson.class);
		Annotation annotation = convertJsonToAnnotation(jsonObj);

		return postToAnnotationStore(annotation);
	}

	/**
	 * This method is used to store all the created annotation into Apache Jena.
	 * The service and base URL are defined in properties file and accessed in implemented interface.
	 *   
	 * @param annotation
	 * @return "Success message"
	 */
	private String postToAnnotationStore(Annotation annotation) throws AnnotationExceptions {
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(
				annotation.getTriples(RDFFormat.TURTLE).getBytes("UTF-8"))) {
			model.read(in, null, "TTL");
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (IOException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		}
		accessor.add(annotationURL + annotation.getResourceAsString(), model);
		return "Annotation Stored Succesfully";
	}

	/**
	 * This is the method is used to create all the annotation from JSON.
	 * a. This mehtod first creates all the Annotation Part
	 * b. Secondly it creates Body part.
	 * c. Finally it creates Target part.
	 * All of the above parts are stored into annotation object.
	 * 
	 * @return annotation object 
	 */
	@Override
	public Annotation convertJsonToAnnotation(AnnotationJson jsonObj) throws AnnotationExceptions {

		Annotation annotation = null;
		annotation = createAnnotations(jsonObj);
		createAndAddBody(annotation, jsonObj);
		createAndAddTarget(annotation, jsonObj.getTarget());

		return annotation;
	}

	/**
	 * This method is used to create the Body of Annotation.
	 * @param annotation
	 * @param jsonObj
	 * @throws AnnotationExceptions
	 */
	private void createAndAddBody(Annotation annotation, AnnotationJson jsonObj) throws AnnotationExceptions {

		for (Body bodies : jsonObj.getBody()) {
			if (bodies.getType().equalsIgnoreCase("choice")) {
				try {
					Choice choice = anno4j.createObject(Choice.class);
					for (Item item : bodies.getItems()) {
						if (item.getType().equalsIgnoreCase("TextualBody")) {
							TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);

							if (null != item.getPurpose())
								textBody.addPurpose(getMotivationType(item.getPurpose()));

							if (null != item.getValue())
								textBody.setValue(item.getValue());
							if (null != item.getFormat())
								textBody.setFormat(item.getFormat());
							if (null != item.getLanguage())
								textBody.setLanguage(item.getLanguage());
							if (null != item.getCreator())
								textBody.setCreator(getAgentType(item.getCreator()));
							if (null != item.getIdentifier())
								textBody.setIdentifier(item.getIdentifier());
							if (null != item.getSubject())
								textBody.setSubject(item.getSubject());
							if (null != item.getTitle())
								textBody.setName(item.getTitle());
							if (null != item.getUnit())
								textBody.setUnit(item.getUnit());

							choice.addItem(textBody);

						} else if (item.getType().equalsIgnoreCase("SpecificResource")) {

							SpecificResource specific = anno4j.createObject(SpecificResource.class);

							if (null != item.getSource())
								specific.setSource(getSourceType(item.getSource()));

							choice.addItem(specific);
						}

					}
					annotation.addBody(choice);

				} catch (RepositoryException e) {
					throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
				} catch (IllegalAccessException e) {
					throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
				} catch (InstantiationException e) {
					throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
				}

			} else {
				try {
					TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);

					if (null != bodies.getValue())
						textBody.setValue(bodies.getValue());
					if (null != bodies.getUnit())
						textBody.setUnit(bodies.getUnit());
					if (null != bodies.getTitle())
						textBody.setName(bodies.getTitle());
					if (null != bodies.getPurpose())
						textBody.addPurpose(getMotivationType(bodies.getPurpose()));
					if (null != bodies.getCreator())
						textBody.setCreator(getAgentType(bodies.getCreator()));

					annotation.addBody(textBody);
				} catch (RepositoryException e) {
					throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
				} catch (IllegalAccessException e) {
					throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
				} catch (InstantiationException e) {
					throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
				}
			}
		}
	}

	/**
	 * This method returns the object of the matching source object defined in WADM.
	 * 
	 * @param source
	 * @return source object
	 * @throws AnnotationExceptions
	 */
	private RDFObject getSourceType(Source source) throws AnnotationExceptions {
		try {
			edu.kit.api.Source sourceObj = anno4j.createObject(edu.kit.api.Source.class);
			if (null != source.getCreator())
				sourceObj.setCreator(getAgentType(source.getCreator()));
			if (null != source.getFormat())
				sourceObj.setFormat(source.getFormat());
			if (null != source.getId())
				sourceObj.setId(source.getId());
			if (null != source.getLanguage())
				sourceObj.setLanguage(source.getLanguage());
			if (null != source.getType())
				sourceObj.setType(source.getType());

			return sourceObj;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		}
	}

	/**
	 * This method returns the agent with the specified type and added the required informations to agent properties.
	 * @param creator
	 * @return agent type software/person
	 * @throws AnnotationExceptions
	 */
	private Agent getAgentType(Creator_ creator) throws AnnotationExceptions {

		if (creator.getType().equalsIgnoreCase("person")) {

			Person personAgent;
			try {
				personAgent = anno4j.createObject(Person.class);
				if (null != creator.getId())
					personAgent.setOpenID(creator.getId());

				return personAgent;

			} catch (RepositoryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			} catch (IllegalAccessException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (InstantiationException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			}

		} else if (creator.getType().equalsIgnoreCase("software")) {
			try {

				Software softAgent = anno4j.createObject(Software.class);

				if (null != creator.getId())
					softAgent.setResourceAsString(creator.getId());

				return softAgent;

			} catch (RepositoryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			} catch (IllegalAccessException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (InstantiationException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (MalformedQueryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
			} catch (UpdateExecutionException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			}

		}
		return null;
	}

	/**
	 * This method is used to create the annotation part and add all the required information to the annotation.
	 * The ID present inside the JSON is skipped and ID generated by the anno4j is added automatically.
	 * 
	 *  @return Annotation object
	 */
	@Override
	public Annotation createAnnotations(AnnotationJson jsonObj) throws AnnotationExceptions {
		Annotation anno = null;
		try {
			anno = anno4j.createObject(Annotation.class);
			if (null != jsonObj.getMotivation())
				anno.addMotivation(getMotivationType(jsonObj.getMotivation()));

			if (null != jsonObj.getCreator())
				anno.setCreator(getAgentType(jsonObj.getCreator()));

			if (null != jsonObj.getCreated())
				anno.setCreated(jsonObj.getCreated());

			if (null != jsonObj.getGenerator())
				anno.setGenerator(getAgentType(jsonObj.getGenerator()));

			if (null != jsonObj.getGenerated())
				anno.setGenerated(jsonObj.getGenerated());

			if (null != jsonObj.getStylesheet())
				anno.setStyledBy(getStyleType(jsonObj.getStylesheet()));

			if (null != jsonObj.getModified())
				anno.setModified(jsonObj.getModified());
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		}

		return anno;
	}

	/**
	 * This method is used to create the target part of the complete Annotations.
	 * 
	 * @param annotation
	 * @param target
	 * @throws AnnotationExceptions
	 */
	private void createAndAddTarget(Annotation annotation, Target target) throws AnnotationExceptions {
		try {
			SpecificResource specific = anno4j.createObject(SpecificResource.class);

			if (null != target.getStyleClass())
				specific.addStyleClass(target.getStyleClass());

			if (null != target.getSource()) {
				ResourceObject source = anno4j.createObject(ResourceObject.class);
				source.setResourceAsString(target.getSource());
				specific.setSource(source);
			}

			if (null != target.getSelector()) {
				if (null != target.getSelector().getType()) {
					Selector selector = getSelector(target.getSelector());
					specific.setSelector(selector);

				} else {
					// Selector selector = anno4j.createObject(Selector.class);
					// selector.setva
					// specific.setSelector(selector);
				}
			}

			annotation.addTarget(specific);

		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (MalformedQueryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (UpdateExecutionException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}

	/**
	 * This Method is used to return the object of the selector defined in the JSON.
	 * 
	 * @param selector
	 * @return Selector returns the defined selector
	 * @throws AnnotationExceptions
	 */
	private Selector getSelector(edu.kit.jsoncore.Selector selector) throws AnnotationExceptions {
		try {
			if (selector.getType().equalsIgnoreCase("FragmentSelector")) {
				FragmentSelector fragmentSelector = anno4j.createObject(FragmentSelector.class);
				if (null != selector.getConformsTo())
					fragmentSelector.setConformsTo(selector.getConformsTo());
				if (null != selector.getValue())
					fragmentSelector.setValue(selector.getValue());

				// if (null != selector.getRefinedBy()) {
				// throw new AnnotationExceptions("RefinedBy Not Implemented",
				// StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
				// }
				return fragmentSelector;
			}
			if (selector.getType().equalsIgnoreCase("CssSelector")) {
				CSSSelector cssSelector = anno4j.createObject(CSSSelector.class);
				if (null != selector.getValue())
					cssSelector.setValue(selector.getValue());

				return cssSelector;
			}
			if (selector.getType().equalsIgnoreCase("XPathSelector")) {
				XPathSelector xpathSelector = anno4j.createObject(XPathSelector.class);
				if (null != selector.getValue())
					xpathSelector.setValue(selector.getValue());

				return xpathSelector;
			}
			if (selector.getType().equalsIgnoreCase("TextQuoteSelector")) {
				TextQuoteSelector textQuoteSelector = anno4j.createObject(TextQuoteSelector.class);
				if (null != selector.getExact())
					textQuoteSelector.setExact(selector.getExact());
				if (null != selector.getPrefix())
					textQuoteSelector.setPrefix(selector.getPrefix());
				if (null != selector.getSuffix())
					textQuoteSelector.setSuffix(selector.getSuffix());

				return textQuoteSelector;
			}
			if (selector.getType().equalsIgnoreCase("TextPositionSelector")) {
				TextPositionSelector textPositionSelector = anno4j.createObject(TextPositionSelector.class);
				if (null != selector.getStart())
					textPositionSelector.setStart(selector.getStart());
				if (null != selector.getEnd())
					textPositionSelector.setEnd(selector.getEnd());

				return textPositionSelector;
			}
			if (selector.getType().equalsIgnoreCase("DataPositionSelector")) {
				DataPositionSelector dataPositionSelector = anno4j.createObject(DataPositionSelector.class);
				if (null != selector.getStart())
					dataPositionSelector.setStart(selector.getStart());
				if (null != selector.getEnd())
					dataPositionSelector.setEnd(selector.getEnd());

				return dataPositionSelector;
			}
			if (selector.getType().equalsIgnoreCase("SvgSelector")) {
				SvgSelector svg = anno4j.createObject(SvgSelector.class);
				if (null != selector.getValue())
					svg.setValue(selector.getValue());

				return svg;
			}
			if (selector.getType().equalsIgnoreCase("RangeSelector")) {
				RangeSelector rangeSelector = anno4j.createObject(RangeSelector.class);
				if (null != selector.getEndSelector()) {
					EndSelector endSelector = anno4j.createObject(EndSelector.class);
					endSelector.setType(selector.getStartSelector().getType());
					endSelector.setValue(selector.getStartSelector().getValue());
					rangeSelector.setEndSelector((Selector) endSelector);
				}
				if (null != selector.getStartSelector()) {
					StartSelector startSelector = anno4j.createObject(StartSelector.class);
					startSelector.setType(selector.getStartSelector().getType());
					startSelector.setValue(selector.getValue());
					rangeSelector.setStartSelector((Selector) startSelector);
				}
				return rangeSelector;
			}

		} catch (Exception e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		return null;
	}
	
	/**
	 * This Method is used to get the object of the style defined in JSON.
	 * 
	 * @param stylesheet
	 * @return Style Object used to defined the style
	 * @throws AnnotationExceptions
	 */

	private Style getStyleType(Stylesheet stylesheet) throws AnnotationExceptions {
		try {

			Style style = anno4j.createObject(Style.class);
			if (null != stylesheet.getId())
				style.setResourceAsString(stylesheet.getId());

			return style;

		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (MalformedQueryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (UpdateExecutionException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		}
	}

	/**
	 * This method returns the type of Agent (Software/Person). 
	 * Also map the required informations present in the JSON.
	 * 
	 * @param generator is a class from json object
	 * @return Agent	(Software/person)
	 * @throws AnnotationExceptions
	 */
	private Agent getAgentType(Generator generator) throws AnnotationExceptions {
		if (generator.getType().equalsIgnoreCase("person")) {

			Person personAgent;
			try {
				personAgent = anno4j.createObject(Person.class);
				if (null != generator.getId())
					personAgent.setOpenID(generator.getId());

				if (null != generator.getHomepage())
					personAgent.setHomepage(generator.getHomepage());

				if (null != generator.getName()) {
					if (personAgentList.containsKey(generator.getName()))
						return personAgentList.get(generator.getName());

					personAgent.setName(generator.getName());
					personAgentList.put(generator.getName(), personAgent);
				}
				return personAgent;

			} catch (RepositoryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			} catch (IllegalAccessException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (InstantiationException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			}

		} else if (generator.getType().equalsIgnoreCase("software")) {
			try {

				Software softAgent = anno4j.createObject(Software.class);

				if (null != generator.getId())
					softAgent.setResourceAsString(generator.getId());

				if (null != generator.getHomepage())
					softAgent.setHomepage(generator.getHomepage());

				if (null != generator.getName()) {
					if (softwareAgentList.containsKey(generator.getName()))
						return softwareAgentList.get(generator.getName());

					softAgent.setName(generator.getName());
					softwareAgentList.put(generator.getName(), softAgent);
				}
				return softAgent;

			} catch (RepositoryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			} catch (IllegalAccessException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (InstantiationException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (MalformedQueryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
			} catch (UpdateExecutionException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			}

		}
		return null;
	}

	/**
	 * This method returns the type of Agent (Software/Person). 
	 * Also map the required informations present in the JSON.
	 * 
	 * @param generator is a class from json object
	 * @return Agent	(Software/person)
	 * @throws AnnotationExceptions
	 */
	private Agent getAgentType(Creator creator) throws AnnotationExceptions {
		if (creator.getType().equalsIgnoreCase("person")) {
			Person personAgent;
			try {
				personAgent = anno4j.createObject(Person.class);
				if (null != creator.getId())
					personAgent.setOpenID(creator.getId());

				if (null != creator.getNickname())
					personAgent.setNickname(creator.getNickname());

				if (null != creator.getName()) {
					if (personAgentList.containsKey(creator.getName()))
						return personAgentList.get(creator.getName());

					personAgent.setName(creator.getName());
					personAgentList.put(creator.getName(), personAgent);
				}
				return personAgent;

			} catch (RepositoryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			} catch (IllegalAccessException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (InstantiationException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			}

		} else if (creator.getType().equalsIgnoreCase("software")) {
			try {

				Software softAgent = anno4j.createObject(Software.class);

				if (null != creator.getId())
					softAgent.setResourceAsString(creator.getId());

				if (null != creator.getNickname())
					softAgent.setNickname(creator.getNickname());

				if (null != creator.getName()) {
					if (softwareAgentList.containsKey(creator.getName()))
						return softwareAgentList.get(creator.getName());

					softAgent.setName(creator.getName());
					softwareAgentList.put(creator.getName(), softAgent);
				}
				return softAgent;

			} catch (RepositoryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			} catch (IllegalAccessException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (InstantiationException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
			} catch (MalformedQueryException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
			} catch (UpdateExecutionException e) {
				throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			}

		}
		return null;
	}

	/**
	 * This Method is used to get the type of motivation. This motivation is also used to add to the the purpose of the Annotation.
	 * 
	 * @param motivation
	 * @return
	 * @throws AnnotationExceptions
	 */
	public Motivation getMotivationType(String motivation) throws AnnotationExceptions {

		try {
			if (motivation.equalsIgnoreCase("assessing"))
				return MotivationFactory.getAssessing(anno4j);
			if (motivation.equalsIgnoreCase("bookmarking"))
				return MotivationFactory.getBookmarking(anno4j);
			if (motivation.equalsIgnoreCase("classifying"))
				return MotivationFactory.getClassifying(anno4j);
			if (motivation.equalsIgnoreCase("commenting"))
				return MotivationFactory.getCommenting(anno4j);
			if (motivation.equalsIgnoreCase("describing"))
				return MotivationFactory.getDescribing(anno4j);
			if (motivation.equalsIgnoreCase("editing"))
				return MotivationFactory.getEditing(anno4j);
			if (motivation.equalsIgnoreCase("highlighting"))
				return MotivationFactory.getHighlighting(anno4j);
			if (motivation.equalsIgnoreCase("linking"))
				return MotivationFactory.getLinking(anno4j);
			if (motivation.equalsIgnoreCase("moderating"))
				return MotivationFactory.getModerating(anno4j);
			if (motivation.equalsIgnoreCase("questioning"))
				return MotivationFactory.getQuestioning(anno4j);
			if (motivation.equalsIgnoreCase("replying"))
				return MotivationFactory.getReplying(anno4j);
			if (motivation.equalsIgnoreCase("tagging"))
				return MotivationFactory.getTagging(anno4j);

		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		}
		return null;

	}
}
