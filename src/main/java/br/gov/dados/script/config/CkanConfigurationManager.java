package br.gov.dados.script.config;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;

import br.gov.dados.script.config.mapping.ckanconfig.CkanConfig;
import br.gov.dados.script.config.mapping.ckanconfig.CkanConfigParamsType;

/**
 * The Class CkanConfigurationManager.
 * 
 * @author Michel Antunes
 */
public class CkanConfigurationManager {

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(CkanConfigurationManager.class);

	/** The Constant XML_NS_URI. */
	private static final String XML_NS_URI = "http://www.w3.org/2001/XMLSchema";

	/**
	 * Caminho para o arquivo de configura��o principal. O valor atual �
	 * <code>ckan-config.xml</code>.
	 */
	public static final String PATH_SERVER_XML = "ckan-config.xml";

	/** The manager. */
	private static CkanConfigurationManager ckanConfigManager = new CkanConfigurationManager();

	/** The objects. */
	private Map<String, Object> xmlObjects;

	/**
	 * Cria um novo objeto ConfigurationManager.
	 */
	private CkanConfigurationManager() {
	}

	/**
	 * M�todo que retorna a instancia do gerenciador de configura��o.
	 * 
	 * @return Instancia do gerenciador
	 */
	public static CkanConfigurationManager getInstance() {

		LOG.debug("entrou no m�todo que retorna a inst�ncia");
		return ckanConfigManager;
	}

	/**
	 * M�todo que recupera a interface para a manipula��o de configura��es. <BR>
	 * A classe que estiver invocando este m�todo dever� converter para o tipo
	 * apropriado de acordo com as classes geradas pelo JAXB
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return Objeto que contem as configura��es XMLs mapeadas para objetos
	 * 
	 * @throws ServerConfigException
	 */
	public Object getCkanConfiguration(String name)
			throws ConfigException {

		LOG.debug("entrou no gerenciador");
		if (!name.trim().equals("")) {

			if (xmlObjects == null) {
				LOG.debug("preparando para carregar as configura��es...");
				load();
			}
			LOG.debug("retornando " + name + "...");
			return xmlObjects.get(name);
		} else {
			throw new ConfigException("[ERRO] Par�metro n�o informado!");
		}
	}

	/**
	 * M�todo que carrega todos os elementos de configura��o a partir do
	 * server-config.xml
	 */
	private synchronized void load() {

		xmlObjects = Collections.synchronizedMap(new HashMap<String, Object>());
		CkanConfigParamsType ckanDetail = null;

		try {
			CkanConfig ckanConfig = loadCkanConfiguration();
			List<CkanConfigParamsType> ckanDetails = ckanConfig
					.getCkanConfigParams();
			LOG.debug("Existem " + ckanDetails.size()
					+ " elementos para processar!");

			for (CkanConfigParamsType xml : ckanDetails) {

				ckanDetail = xml;
				String name = ckanDetail.getName();
				xmlObjects.put(name, ckanDetail);
				LOG.debug("Objeto: " + name + " inserido na cole��o!");

			}

		} catch (Exception e) {
			throw new ConfigException(
					"Ocorreu um erro ao tentar configurar a aplica��o.", e);

		} finally {
			ckanDetail = null;
		}
	}

	/**
	 * Carrega a configura��o do arquivo config.xml
	 * 
	 * @return configura��o do arquivo config.xml
	 */
	private CkanConfig loadCkanConfiguration() {
		CkanConfig ckanConfiguration = null;
		InputStream fileStream = null;
		try {

			Unmarshaller unmarshaller = getUnmarshaller(CkanConfig.class
					.getPackage().getName());

			fileStream = ResourceLoader.getResourceStream(PATH_SERVER_XML);
			if (fileStream == null) {
				LOG.error("XML n�o encontrado: " + PATH_SERVER_XML);
				throw new ConfigException("XML n�o encontrado: "
						+ PATH_SERVER_XML);
			}
			ckanConfiguration = (CkanConfig) unmarshaller
					.unmarshal(fileStream);
		} catch (JAXBException je) {
			LOG.error("Erro ao fazer unmarshal "
					+ "do arquivo de configura��o.", je);
			throw new ConfigException(
					"Erro ao fazer unmarshal do arquivo de configura��o.", je);
		}
		return ckanConfiguration;
	}

	/**
	 * Gets the unmarshaller.
	 * 
	 * @param packageName
	 *            the package name
	 * 
	 * @return the unmarshaller
	 * 
	 * @throws JAXBException
	 *             the JAXB exception
	 */
	private Unmarshaller getUnmarshaller(String packageName)
			throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller;
	}

	/**
	 * Gets the unmarshaller with schema validation.
	 * 
	 * @param packageName
	 *            the package name
	 * @param pathToSchema
	 *            the path to schema
	 * 
	 * @return the unmarshaller
	 * 
	 * @throws JAXBException
	 *             the JAXB exception
	 */
	private Unmarshaller getUnmarshaller(String packageName, String pathToSchema)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
		Schema schema = null;

		URL schemaURL = ResourceLoader.getResourceURL(pathToSchema);
		if (schemaURL == null) {
			LOG.error("XSD n�o encontrado: " + pathToSchema);
			throw new ConfigException("XSD n�o encontrado: "
					+ pathToSchema);
		}

		try {
			schema = SchemaFactory.newInstance(XML_NS_URI).newSchema(schemaURL);
		} catch (Throwable e) {
			LOG.error("Erro ao obter Schema: " + schemaURL);
			throw new ConfigException("Erro ao obter Schema: "
					+ schemaURL);
		}

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		return unmarshaller;
	}

}
