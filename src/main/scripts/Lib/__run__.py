# This Python file uses the following encoding: utf-8
'''
Created on 18/12/2012

@author: michel.silva

Este modulo responsavel por carregar um arquivo CSV no ambiente de dados abertos CKAN.
'''
#iso-8859-1
import sys, os;
sys.path.insert(0, os.getcwd());
sys.path.insert(1, os.getcwd()+"/ckan-loader-full.jar");
print sys.path;
from br.gov.dados.script.config import CkanConfigurationManager;
from br.gov.dados.script.config.mapping.ckanconfig import CkanConfig, CkanConfigParamsType;

class ConfigurationHandle:
    '''
    Classe responsavel por efetuar a conversao da estrutura java para python
    @author: Michel Antunes
    '''
    
    def __init__(self, name):
        self._ckanXmlConfig = CkanConfigurationManager.getInstance().getCkanConfiguration(name);
    
    def getCkanEntity(self):
        conn = self._ckanXmlConfig.getCkanConnection();
        #print "Url-api: " + conn.getUrlApi() + "    Api-key: " + conn.getApiKey();
        
        datasetList = [];
        xmlDatasetList = self._ckanXmlConfig.getCkanDataset();
        for xmlDataset in xmlDatasetList:
            #print "##########################"
            #print "%% Summary %%"
            #print  "Name: "+xmlDataset.getName();
            #print  "Title: "+xmlDataset.getTitle();
            #print  "Url: "+xmlDataset.getUrl();
            #print  "Author: "+xmlDataset.getAuthor();
            #print  "Author Email: "+xmlDataset.getAuthorEmail();
            #print  "Maintainer: "+xmlDataset.getMaintainer();
            #print  "Maintainer Email: "+xmlDataset.getMaintainerEmail();
            #print  "License Id: "+xmlDataset.getLicenseId();
            #print  "Version: "+xmlDataset.getVersion();
            #print  "Notes: "+xmlDataset.getNotes();
            #print  "Tags: "+xmlDataset.getTags();
            #print  "State: "+xmlDataset.getState();
            #print  "Csv Resources Path: "+xmlDataset.getResourcesCsvPath();
            #print  "Csv Groups Path: "+xmlDataset.getGroupsCsvPath();
            #print "##########################"
            
            #Read csv resources file
            csvFile = xmlDataset.getResourcesCsvPath();
            cvsHandle = CsvHandle();
            csvData = cvsHandle.captureCsvData(csvFile);
            
            resourceList=[];
            #print "##########################"
            #print "CSV Header:";
            header = csvData.getHeader();
            #print header;
            
            #print "##########################"
            #print "CSV Data:";
            data = csvData.getData(); 
            #print data;
            
            for dataList in data:
                resource = dict();
                #print "##########################"
                #print "Data List:";
                #print dataList;
                #print "##########################"
                for ix in range(len(dataList)):
                    resource[header[ix]]=dataList[ix];
                    #print "@@@@@@@@@@@@@@@@@@@@@@@@@@"
                    #print "Partial Resource:"
                    #print resource;
                    #print "@@@@@@@@@@@@@@@@@@@@@@@@@@"
                
                resourceList.append(resource);
                
                #print "--------------------------"
                #print "Partial Resource List: ";
                #print resourceList;
                #print "--------------------------"
            
            dataset = dict(name=xmlDataset.getName(),
                           title=xmlDataset.getTitle(),
                           url=xmlDataset.getUrl(),
                           author=xmlDataset.getAuthor(),
                           author_email=xmlDataset.getAuthorEmail(),
                           maintainer=xmlDataset.getMaintainer(),
                           maintainer_email=xmlDataset.getMaintainerEmail(),
                           license_id=xmlDataset.getLicenseId(),
                           version=xmlDataset.getVersion(),
                           notes=xmlDataset.getNotes(),
                           tags=xmlDataset.getTags(),
                           state=xmlDataset.getState(),
                           resources=resourceList);
            
            datasetList.append(dataset);
        
        ckanEntity = CkanEntity(conn.getUrlApi(), conn.getApiKey(), datasetList);
        return ckanEntity;

class CkanEntity:
    
    def __init__(self, pUrlApi, pApiKey, pDatasetList):
        self.urlApi = pUrlApi;
        self.apiKey = pApiKey;
        self.datasetList = pDatasetList;
    def getUrlApi(self):
        return self.urlApi;
    def getApiKey(self):
        return self.apiKey;
    def getDatasetList(self):
        return self.datasetList;
    
class CkanResource:
    def __init__(self):
        pass;

class CsvHandle:
    
    def captureCsvData(self, pCsvFile):
        header = [];
        data = [];
        print "Reading file from: "+pCsvFile;
        import csv, sys;
        reader = csv.reader(open(pCsvFile, "r")); 
        try:
            count = 0;
            for row in reader:
                print row;
                print "\n";
                if (count == 0):
                    header = row;
                else:
                    data.append(row);
                count=count+1;
        except csv.Error, e:
            sys.exit('file %s, line %d: %s' % (pCsvFile, reader.line_num, e));
    
        #print "Header";
        #print header;
        #print "Data";
        #print data;
        
        csvData = CsvData(header,data);
        return csvData;

class CsvData:
    def __init__(self, header, data):
        self.header = header;
        self.data = data;
    def getHeader(self):
        return self.header;
    def getData(self):
        return self.data;



def sendDataToCkan(pCkanEntity):
    import ckanclient;
    #base_location = 'http://localhost:5000/api'
    #api_key = '3000c105-89fc-4293-8b45-2a3746d4c7fe'
    base_location = pCkanEntity.getUrlApi();
    api_key = pCkanEntity.getApiKey();
    client = ckanclient.CkanClient(base_location, api_key)
    #pkg = dict(
    #    name='test-ckanext-datapreview',
    #    title='Test CKANext Data Preview',
    #    resources=[
    #        dict(
    #            url='http://webstore.thedatahub.org/rufuspollock/gold_prices/data.csv',
    #            description='Gold Prices (csv)',
    #            format='csv',
    #            webstore_url='http://webstore.thedatahub.org/rufuspollock/gold_prices/data'
    #            ),
    #        dict(
    #            url='http://afghanistanelectiondata.org/sites/default/files/district_centerpoints.csv',
    #            description='Afghanistan (csv)'
    #            ),
    #        dict(
    #            url='http://old.openeconomics.net/store/thatcher_wages_middle_ages/data',
    #            format='csv',
    #            description='Thatcher wages (csv, no extension but format)'
    #            ),
    #        dict(
    #            url='http://old.openeconomics.net/store/thatcher_wages_middle_ages/data',
    #            format='CSV',
    #            description='Thatcher wages (CSV, no extension but format)'
    #            ),
    #        dict(
    #            url='http://old.openeconomics.net/store/thatcher_wages_middle_ages/data',
    #            format='text/csv',
    #            description='Thatcher wages (text/csv)'
    #            ),
    #        # now n3 / rdf (what about sparql?)
    #        dict(
    #            url='http://lod.taxonconcept.org/ses/iuCXz.rdf',
    #            name='taxonconcept-example',
    #            format='example/rdf+xml',
    #            description='Link to an example insect'
    #            ),
    #        dict(
    #            url='http://www.ggdc.net/MADDISON/Historical_Statistics/vertical-file_02-2010.xls',
    #            description='Maddison World Pop (xls)'
    #            ),
    #        dict(
    #            url='http://ckan.net/',
    #            description='An html page',
    #            format='text/html'
    #            ),
    #        dict(
    #            url='http://data.gov.uk/sparql',
    #            description='A SPARQL API',
    #            format='api/sparql'
    #            ),
    #        dict(
    #            url='http://ckan.net/mydata.zip',
    #            description='Zip file',
    #            format='zip'
    #            ),
    #        dict(
    #            url='http://ckan.net/mydata.csv.zip',
    #            description='Zipped csv file',
    #            format='zip:csv'
    #            ),
    #        ]
    #    )
    
    datasetList = pCkanEntity.getDatasetList();
    #print "\n"
    #print "##########################"
    #print "Dataset List:"
    #print datasetList
    #print "##########################"
    #print "\n"
    
    for dataset in datasetList:
        try:
            #print "##########################"
            #print "Dataset: "
            #print dataset
            #print "##########################"
            #print "\n"
            client.package_register_post(dataset);
        except ckanclient.CkanApiError:
            client.package_entity_put(dataset);
    
def main():
    print "Loading open data ... ";
    args = sys.argv[:];
    #print "1: " +args[0];
    #print "2: " +args[1];
    
    xmlKey = args[1];
    config = ConfigurationHandle(xmlKey);
    
    _ckanEntity = config.getCkanEntity();
    
    sendDataToCkan(_ckanEntity);
    print "Finished with success!"
    pass;
    
if __name__ == "__run__":
    main();
