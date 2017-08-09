package edu.kit.annotation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.object.RDFObject;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.ResourceObjectSupport;
import com.github.anno4j.model.impl.collection.AnnotationPage;
import com.github.anno4j.model.impl.collection.AnnotationPageSupport;

public abstract class AnnotationPageSupport2  extends AnnotationPageSupport {

	@Override
    public String getTriples(RDFFormat format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RDFParser parser = Rio.createParser(RDFFormat.NTRIPLES);
        RDFWriter writer = Rio.createWriter(format, out);
        parser.setRDFHandler(writer);

        try {
            this.getObjectConnection().exportStatements(this.getResource(), null, null, true, writer);

            if(this.getItems() != null) {
                for(RDFObject item : this.getItems()) {
                    if(item instanceof ResourceObject) {
                        parser.parse(IOUtils.toInputStream( ((ResourceObject)item).getTriples(format)), "");
                    }
                }
            }
        } catch (IOException | RDFHandlerException | RDFParseException | RepositoryException e) {
            e.printStackTrace();
        }

        return out.toString();
    }
}
