package edu.kit.annotation;

import java.util.HashSet;
import java.util.Set;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.object.RDFObject;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.Body;
import com.github.anno4j.model.Motivation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.State;
import com.github.anno4j.model.Style;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Person;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.body.TextualBody;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.state.HttpRequestState;
import com.github.anno4j.model.impl.targets.SpecificResource;

/**
 * 
 * @author Vaibhav
 *
 */
public class Anno4JImpl {

	public static void main(String[] args) {
		
		try {
			Anno4j anno4j = new Anno4j();
			Annotation annotation = anno4j.createObject(Annotation.class);
			
			System.out.println("objets Created");


			// setting the motivations
//			Motivation motivation = MotivationFactory.getCommenting(anno4j);
			Motivation motivation = MotivationFactory.getTagging(anno4j);
			annotation.addMotivation(motivation);
			
			// setting creator it is a agent of type person and it can also be
			// software generated in that case use software inplace of person
			Person person = anno4j.createObject(Person.class);
			person.setOpenID("http://example.org/user1");
			person.setName("A. Person");
			person.setNickname("user1");
			annotation.setCreator(person);
			
//			set created the date and time stamp UTC
			annotation.setCreated("2015-10-13T13:00:00Z");
			
//			set generator it is denoted byt the system/person used to generate this annotations
			Software software = anno4j.createObject(Software.class);
			software.setName("Code v2.1");
			software.setHomepage("http://example.org/homepage1");
			annotation.setGenerator(software);

//			seting the generated date time stamp by the system/person
			annotation.setGenerated("2015-10-14T15:13:28Z");
			
//			adding the information of stylesheet
			Style style = anno4j.createObject(Style.class);
			annotation.setStyledBy(style);
			
//			adding multiples bodies with their properties and values and types ...
			Set<Body> multiBody = new HashSet<Body>();
			
			TextualBody txtBody1 = anno4j.createObject(TextualBody.class);
			Motivation taggingMotivation = MotivationFactory.getTagging(anno4j);
			txtBody1.addPurpose(taggingMotivation);
			txtBody1.setValue("love");
//			adding to body type choice having multiple body inside it
			Choice choice = anno4j.createObject(Choice.class);
//			1 of 2 multiple bodies
			TextualBody txtBody2 = anno4j.createObject(TextualBody.class);
			Motivation descMotiv = MotivationFactory.getDescribing(anno4j);
			txtBody2.addPurpose(descMotiv);
			txtBody2.setValue("I really love this particular bit of text in this XML. No really.");
			txtBody2.setProcessingLanguage("text/plain");
			txtBody2.addLanguage("en");
			txtBody2.setCreator(person);
			
			Set<RDFObject> multiplebodies = new HashSet<RDFObject>();
			multiplebodies.add(txtBody2);
			choice.addItem(txtBody2);
//			2 of 2 mulitple bodies		
			/*SpecificResource spicificRsr = anno4j.createObject(SpecificResource.class);
			Motivation describing = MotivationFactory.getDescribing(anno4j);
			*/
//			error while adding multiple 
			multiBody.add(txtBody1);
			multiBody.add(choice);
//			multiBody.add(txtBody2);
			annotation.setBodies(multiBody);
//			annotation.addBody(txtBody1);
//			annotation.addBody(choice);
			
//			Target target = anno4j.createObject(Target.class);
			SpecificResource specific = anno4j.createObject(SpecificResource.class);
			specific.addStyleClass("mystyle");
			ResourceObject source = anno4j.createObject(ResourceObject.class);
//			source.setResourceAsString("http://example.com/document1");
			specific.setSource(source);
			State state = anno4j.createObject(State.class);
			HttpRequestState reqState = anno4j.createObject(HttpRequestState.class);
			reqState.setValue("Accept: application/xml");
			specific.addState(state);
			
			annotation.addTarget(specific);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("JSONLD FORMAT>>>>>>>>>>>>>\n"+annotation.getTriples(RDFFormat.JSONLD));
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
}
