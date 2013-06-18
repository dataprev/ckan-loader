package br.gov.dados.script.config;

import java.io.InputStream;
import java.net.URL;

/**
 * Classe utilit�ria para obten��o de recursos do sistema
 * 
 * @author Michel Antunes 
 */
public final class ResourceLoader {
	/**
	 * Construtor padr�o
	 * */
	private ResourceLoader() {
	}

	/**
	 * Retorna um inputStream de atrav�s do nome do arquivo
	 * 
	 * @param fileName
	 *            Nome do arquivo
	 * @return InputStream do arquivo especificado
	 * 
	 * */
	public static InputStream getResourceStream(String fileName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResourceAsStream(fileName);
	}

	/**
	 * Retorna a URL de um recurso dispon�vel no classpath
	 * 
	 * @param resourceName
	 *            o nome do recurso
	 * @return a URL de acesso ao recurso
	 */
	public static URL getResourceURL(String resourceName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResource(resourceName);
	}
}
