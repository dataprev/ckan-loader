package br.gov.dados.script.config;

/**
 * The Class ConfigException.
 * 
 * @author Michel Antunes 
 */
public class ConfigException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2500840134976575142L;

	/**
	 * Construtor padr�o.
	 */
	public ConfigException() {
		super();
	}

	/**
	 * Construtor com mensagem de erro e exce��o ocorrida.
	 * 
	 * @param message 	mensagem de erro
	 * @param exception exce��o ocorrida
	 */
	public ConfigException(String message, Exception exception) {
		super(message, exception);
	}

	/**
	 * Construtor com mensagem de erro.
	 * 
	 * @param message mensagem de erro
	 */
	public ConfigException(String message) {
		super(message);
	}

	/**
	 * Construtor com exce��o ocorrida.
	 * 
	 * @param exception exce��o ocorrida
	 */
	public ConfigException(Exception exception) {
		super(exception);
	}
}
