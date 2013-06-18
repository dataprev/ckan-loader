package br.gov.dados.script.config;

import java.io.InputStream;
import java.net.URL;

/**
 * Classe utilitária para obtenção de recursos do sistema
 * 
 * @author Michel Antunes 
 */
public final class ResourceLoader {
	/**
	 * Construtor padrão
	 * */
	private ResourceLoader() {
	}

	/**
	 * Retorna um inputStream de através do nome do arquivo
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
	 * Retorna a URL de um recurso disponí­vel no classpath
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
