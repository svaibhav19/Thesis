package edu.kit.api;

import java.util.Set;

import org.openrdf.idGenerator.IDGenerator;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;

public class CustomIDGenerator implements IDGenerator {

	@Override
	public Resource generateID(Set<URI> types) {
		return null;
	}

}
