# Annotation Store
Annotation Store is used to store the Annotation in the form of Web Annotation Data Model (WADM).
The Annotation Store is capable of storing the application/ld+json;profile="http://www.w3.org/ns/anno.jsonld" as well other format such as "RDF/XML", "N-TRIPLE", "JSON-LD" & "TURTLE". Along with this format support Annotation Store also support PAGE XML which is used to convert the PAGE XML to WADM and store & retrieve to display on UI.
Annotation Store uses Apache JENA to store the Annotation.

For this operation different REST services are provided,

#### 1. http://<ip-address:portNo>/AnnotationWADM/rest/postToAnnoStore : 

TYPE         : POST


CONTENT-TYPE : application/xml,application/json


QUERY-PARAM  : digitalObjID

For "application/xml" it accepts page xml generated by SWATI workflow and Annotation Store converts & Store it the form of WADM.
For "application/json" it accepts the JSONObject as per the WADM and further it map to Annotation and store into Annotation Store.

#### 2. http://<ip-address:portNo>/AnnotationWADM/rest/postRDFToAnnoStore

TYPE         : POST


CONTENT-TYPE : application/xml


QUERY-PARAM  : format


This service accept the Annotation in "RDF/XML", "N-TRIPLE", "JSON-LD" & "TURTLE" format, further it is used to validate to WADM and store into Annotation Store. This service accept the "QUERY-PARAM" format specified above as an input.

#### 3. http://<ip-address:portNo>/AnnotationWADM/rest/byid  : 

TYPE         : POST


CONTENT-TYPE : application/xml


QUERY-PARAM  : format,ID



This service accept the annotation ID and retrieve the complete annotation for the specified ID  in the specified format by "QUERY-PARAM".

#### 4. http://<ip-address:portNo>/AnnotationWADM/rest/query : 

TYPE         : POST

CONTENT-TYPE : application/xml

QUERY-PARAM  : format



This service is used to execute the custom (SPARQ) queries and fetch the result in specified format in "QUERY-PARAM".

### PRE-REQUISITES
1. Apache Tomcat (any web server)
2. Apache JENA

### Installation
#### 1. Setup APACHE Jena
Download Apache Jena https://jena.apache.org/ follow the instruction provided on the site to install and configure.
Run JENA and create dataset (by default the annotation store has been configure to kit  dataset )
#### 2. Deploy Annotation Store
Buil the project to generate the ANNOTATION.war file. Deploy into tomcat (web server) and call the REST services.
Even the Annotation Store can be access via web browser by visiting following link http://<ip-address:portNo>/AnnotationWADM
e.g. http://localhost:8080/AnnotationWADM

#### Note :
In case if new dataset has been created in JENA with different name or the Annotation Store has been deployed to different system, 
then you have to set this information in properties file. This location can be found in the web server /AnnotationWADM/WEB-INF/classes/jenaService.properties
