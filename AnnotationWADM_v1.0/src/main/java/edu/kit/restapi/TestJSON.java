package edu.kit.restapi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.kit.api.JsonMapper;
import edu.kit.api.JsonMapperImp;

public class TestJSON {
	public static void main(String[] args) throws IOException, RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException {
		String content = readFile("resources/annojson.json", StandardCharsets.UTF_8);
		JsonMapper jsonMapper = new JsonMapperImp();
		jsonMapper.parseJson(content);
		
		
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
