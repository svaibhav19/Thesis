package edu.kit.annotation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.object.RDFObject;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.io.ObjectParser;
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
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

/**
 * 
 * @author Vaibhav
 *
 */
public class Anno4JImpl {

	public static void main(String[] args) throws IOException, JsonLdError {
		
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
			System.out.println("JSONLD FORMAT>>>>>>>>>>>>>\n"+annotation.getTriples(RDFFormat.RDFXML));
			
			System.out.println("+++++++++++++++++++++++++------------+++++++++++++++++++++++++++++++++++++++++");
//			ObjectParser objectParser = new ObjectParser();
//			List<Annotation> annotations = objectParser.parse(annotation.getTriples(RDFFormat.JSONLD), new URL("http://example.com/"), RDFFormat.JSONLD);
//			List<Annotation> annotations = objectParser.parse(annotation.getTriples(RDFFormat.NTRIPLES).toString(), new URL("http://www.example.com/"), RDFFormat.NTRIPLES);
//			System.out.println("---------------------"+annotations.size());
//			System.out.println(annotations.get(0).getTriples(RDFFormat.JSONLD));
			
//			DatasetAccessor da = DatasetAccessorFactory.createHTTP("http://localhost:3030/kit/abc");
//			Model model = ModelFactory.createDefaultModel();
//			 model.read(new FileInputStream("E://rdfttl.ttl"),null,"TTL");
//			InputStream stream = new ByteArrayInputStream(annotation.getTriples(RDFFormat.TURTLE).toString().getBytes(StandardCharsets.UTF_8));
//			InputStream stream = new ByteArrayInputStream(annotation.getTriples(RDFFormat.RDFXML).toString().getBytes(StandardCharsets.UTF_8));
//			 model.read(stream,null,"RDFXML");
//			da.putModel("abcde", model);
//			AnnotationCollection annoCollection = anno4j.createObject(AnnotationCollection.class);
			
			
			
			
			
			// Open a valid json(-ld) input file
//			InputStream inputStream = new FileInputStream("input.json");
//			String jsonLdStr = annotation.getTriples(RDFFormat.JSONLD).substring(1);
//			jsonLdStr = jsonLdStr+"[{ \"Content-Location\": \"http://nonexisting.example.com/context\",\"X-Classpath\": \"custom/contexttest-0001.jsonld\","+
//					"\"Content-Type\": \"application/ld+json\" },";
//			InputStream stream = new ByteArrayInputStream(jsonLdStr.getBytes(StandardCharsets.UTF_8));
//			// Read the file into an Object (The type of this object will be a List, Map, String, Boolean,
//			// Number or null depending on the root object in the file).
//			Object jsonObject = JsonUtils.fromInputStream(stream);
//			// Create a context JSON map containing prefixes and definitions
//			Map context = new HashMap();
//			// Customise context...
//			// Create an instance of JsonLdOptions with the standard JSON-LD options
//			JsonLdOptions options = new JsonLdOptions();
//			// Customise options...
//			// Call whichever JSONLD function you want! (e.g. compact)
//			Object compact = JsonLdProcessor.compact(jsonObject, context, options);
//			// Print out the result (or don't, it's your call!)
//			System.out.println("final callsss");
//			System.out.println(JsonUtils.toPrettyString(compact));
			
			
			
			
			// Inject a context document into the options as a literal string
//			DocumentLoader dl = new DocumentLoader();
//			JsonLdOptions options = new JsonLdOptions();
//			// ... the contents of "contexts/example.jsonld"
//			String jsonContext = "{ \"@contxt\": { ... } }";
//			dl.addInjectedDoc("http://www.example.com/context",  jsonContext);
//			options.setDocumentLoader(dl);
//
//			InputStream inputStream = new ByteArrayInputStream(annotation.getTriples(RDFFormat.JSONLD).getBytes(StandardCharsets.UTF_8));
//			Object jsonObject = JsonUtils.fromInputStream(inputStream);
//			Map context = new HashMap();
//			Object compact = JsonLdProcessor.compact(jsonObject, context, options);
//			System.out.println(JsonUtils.toPrettyString(compact));
			
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} 	}
}
