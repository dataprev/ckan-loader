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
	 * Caminho para o arquivo de configuração principal. O valor atual é
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
	 * Método que retorna a instancia do gerenciador de configuração.
	 * 
	 * @return Instancia do gerenciador
	 */
	public static CkanConfigurationManager getInstance() {

		LOG.debug("entrou no método que retorna a instância");
		return ckanConfigManager;
	}

	/**
	 * Método que recupera a interface para a manipulação de configurações. <BR>
	 * A classe que estiver invocando este método deverá converter para o tipo
	 * apropriado de acordo com as classes geradas pelo JAXB
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return Objeto que contem as configurações XMLs mapeadas para objetos
	 * 
	 * @throws ServerConfigException
	 */
	public Object getCkanConfiguration(String name)
			throws ConfigException {

		LOG.debug("entrou no gerenciador");
		if (!name.trim().equals("")) {

			if (xmlObjects == null) {
				LOG.debug("preparando para carregar as configurações...");
				load();
			}
			LOG.debug("retornando " + name + "...");
			return xmlObjects.get(name);
		} else {
			throw new ConfigException("[ERRO] Parâmetro não informado!");
		}
	}

	/**
	 * Método que carrega todos os elementos de configuração a partir do
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
				LOG.debug("Objeto: " + name + " inserido na coleção!");

			}

		} catch (Exception e) {
			throw new ConfigException(
					"Ocorreu um erro ao tentar configurar a aplicação.", e);

		} finally {
			ckanDetail = null;
		}
	}

	/**
	 * Carrega a configuração do arquivo config.xml
	 * 
	 * @return configuração do arquivo config.xml
	 */
	private CkanConfig loadCkanConfiguration() {
		CkanConfig ckanConfiguration = null;
		InputStream fileStream = null;
		try {

			Unmarshaller unmarshaller = getUnmarshaller(CkanConfig.class
					.getPackage().getName());

			fileStream = ResourceLoader.getResourceStream(PATH_SERVER_XML);
			if (fileStream == null) {
				LOG.error("XML não encontrado: " + PATH_SERVER_XML);
				throw new ConfigException("XML não encontrado: "
						+ PATH_SERVER_XML);
			}
			ckanConfiguration = (CkanConfig) unmarshaller
					.unmarshal(fileStream);
		} catch (JAXBException je) {
			LOG.error("Erro ao fazer unmarshal "
					+ "do arquivo de configuração.", je);
			throw new ConfigException(
					"Erro ao fazer unmarshal do arquivo de configuração.", je);
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
			LOG.error("XSD não encontrado: " + pathToSchema);
			throw new ConfigException("XSD não encontrado: "
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
