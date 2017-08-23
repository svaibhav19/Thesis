package edu.kit.api;

import java.util.List;

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
import com.github.anno4j.model.Style;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Person;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.targets.SpecificResource;
import com.google.gson.Gson;

import edu.kit.jsoncore.AnnotationJson;
import edu.kit.jsoncore.Body;
import edu.kit.jsoncore.Creator;
import edu.kit.jsoncore.Generator;
import edu.kit.jsoncore.Item;
import edu.kit.jsoncore.Source;
import edu.kit.jsoncore.Stylesheet;
import edu.kit.jsoncore.Target;

public class JsonMapperImp implements JsonMapper {

	private Anno4j anno4j;

	public JsonMapperImp() throws RepositoryException, RepositoryConfigException {
		anno4j = new Anno4j();
	}

	@Override
	public String parseJson(String jsonString)
			throws RepositoryException, IllegalAccessException, InstantiationException {
		Gson gson = new Gson();
		AnnotationJson jsonObj = gson.fromJson(jsonString, AnnotationJson.class);
		Annotation annotation = convertJsonToAnnotation(jsonObj);

		System.out.println(annotation.getTriples(RDFFormat.RDFXML));
		return null;
	}

	@Override
	public Annotation convertJsonToAnnotation(AnnotationJson jsonObj)
			throws RepositoryException, IllegalAccessException, InstantiationException {

		Annotation annotation = createAnnotations(jsonObj);
		createAndAddBody(annotation, jsonObj.getBody());
		createAndAddTarget(annotation, jsonObj.getTarget());
		return annotation;
	}

	private void createAndAddTarget(Annotation annotation, Target target) {
		try {
			SpecificResource specific = anno4j.createObject(SpecificResource.class);

			if (null != target.getStyleClass())
				specific.addStyleClass(target.getStyleClass());
			if (null != target.getSource()) {
				ResourceObject source = anno4j.createObject(ResourceObject.class);
				source.setResourceAsString(target.getSource());
				specific.setSource(source);
			}
			// if (null != target.getState()) {
			// no methods avail in anno4j
			// for (State state : target.getState()) {
			// com.github.anno4j.model.State stateObj =
			// anno4j.createObject(com.github.anno4j.model.State.class);
			// if(null != state.)
			// stateObj.setResourceAsString(state.get);
			// }
			// }

			// no full methods found for implementations
			// if(null != target.getSelector()){
			// Selector select = anno4j.createObject(Selector.class);
			// select.set
			// }
			annotation.addTarget(specific);

		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (UpdateExecutionException e) {
			e.printStackTrace();
		}

	}

	private void createAndAddBody(Annotation annotation, List<Body> body) {

		for (Body bodies : body) {
			if (bodies.getType().equalsIgnoreCase("choice")) {
				try {
					Choice choice = anno4j.createObject(Choice.class);
					for (Item item : bodies.getItems()) {
						if (item.getType().equalsIgnoreCase("TextualBody")) {
							TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);

							// if(null != item.getPurpose())
							// anno4j-no purpose
							if (null != item.getValue())
								textBody.setValue(item.getValue());
							if (null != item.getFormat())
								textBody.setFormat(item.getFormat());
							if (null != item.getLanguage())
								textBody.setLanguage(item.getLanguage());
							// if(null != item.getCreator())
							// textBody.setCreator(agent);
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
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}

			} else {
				try {
					TextAnnotationBody textBody = anno4j.createObject(TextAnnotationBody.class);

					if (null != bodies.getValue())
						textBody.setValue(bodies.getValue());

					annotation.addBody(textBody);
				} catch (RepositoryException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private RDFObject getSourceType(Source source) {
		try {

			ResourceObject sourceObj = anno4j.createObject(ResourceObject.class);
			if (null != source.getId())
				sourceObj.setResourceAsString(source.getId());

			// other implementation is not available

			return sourceObj;
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (UpdateExecutionException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Annotation createAnnotations(AnnotationJson jsonObj)
			throws RepositoryException, IllegalAccessException, InstantiationException {

		Annotation anno = anno4j.createObject(Annotation.class);

		try {
			if (null != jsonObj.getId())
				anno.setResourceAsString(jsonObj.getId());

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

		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (UpdateExecutionException e) {
			e.printStackTrace();
		}

		return anno;
	}

	private Style getStyleType(Stylesheet stylesheet) {
		try {

			Style style = anno4j.createObject(Style.class);
			if (null != stylesheet.getId())
				style.setResourceAsString(stylesheet.getId());

			return style;

		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (UpdateExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Agent getAgentType(Generator generator) {
		if (generator.getType().equalsIgnoreCase("person")) {
			Person personAgent;
			try {
				personAgent = anno4j.createObject(Person.class);
				if (null != generator.getId())
					personAgent.setOpenID(generator.getId());
				if (null != generator.getName())
					personAgent.setName(generator.getName());
				if (null != generator.getHomepage())
					personAgent.setHomepage(generator.getHomepage());

				return personAgent;

			} catch (RepositoryException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

		} else if (generator.getType().equalsIgnoreCase("software")) {
			try {

				Software softAgent = anno4j.createObject(Software.class);

				if (null != generator.getId())
					softAgent.setResourceAsString(generator.getId());
				if (null != generator.getName())
					softAgent.setName(generator.getName());
				if (null != generator.getHomepage())
					softAgent.setHomepage(generator.getHomepage());

				return softAgent;

			} catch (RepositoryException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (MalformedQueryException e) {
				e.printStackTrace();
			} catch (UpdateExecutionException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	private Agent getAgentType(Creator creator) {
		if (creator.getType().equalsIgnoreCase("person")) {
			Person personAgent;
			try {
				personAgent = anno4j.createObject(Person.class);
				if (null != creator.getId())
					personAgent.setOpenID(creator.getId());
				if (null != creator.getName())
					personAgent.setName(creator.getName());
				if (null != creator.getNickname())
					personAgent.setNickname(creator.getNickname());

				return personAgent;

			} catch (RepositoryException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

		} else if (creator.getType().equalsIgnoreCase("software")) {
			try {

				Software softAgent = anno4j.createObject(Software.class);

				if (null != creator.getId())
					softAgent.setResourceAsString(creator.getId());
				if (null != creator.getName())
					softAgent.setName(creator.getName());
				if (null != creator.getNickname())
					softAgent.setNickname(creator.getNickname());

				return softAgent;

			} catch (RepositoryException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (MalformedQueryException e) {
				e.printStackTrace();
			} catch (UpdateExecutionException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public Motivation getMotivationType(String motivation) {

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
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;

	}

}
