import ckanclient

base_location = 'http://localhost:5000/api'
api_key = 'change_key'
client = ckanclient.CkanClient(base_location, api_key)
pkg = dict(
    name='test-ckanext-datapreview',
    title='Test CKANext Data Preview',
    resources=[
        dict(
            url='http://webstore.thedatahub.org/rufuspollock/gold_prices/data.csv',
            description='Gold Prices (csv)',
            format='csv',
            webstore_url='http://webstore.thedatahub.org/rufuspollock/gold_prices/data'
            ),
        dict(
            url='http://afghanistanelectiondata.org/sites/default/files/district_centerpoints.csv',
            description='Afghanistan (csv)'
            ),
        dict(
            url='http://old.openeconomics.net/store/thatcher_wages_middle_ages/data',
            format='csv',
            description='Thatcher wages (csv, no extension but format)'

            ),
        dict(
            url='http://old.openeconomics.net/store/thatcher_wages_middle_ages/data',
            format='CSV',
            description='Thatcher wages (CSV, no extension but format)'

            ),
        dict(
            url='http://old.openeconomics.net/store/thatcher_wages_middle_ages/data',
            format='text/csv',
            description='Thatcher wages (text/csv)'
            ),
        # now n3 / rdf (what about sparql?)
        dict(
            url='http://lod.taxonconcept.org/ses/iuCXz.rdf',
            name='taxonconcept-example',
            format='example/rdf+xml',
            description='Link to an example insect'
            ),
        dict(
            url='http://www.ggdc.net/MADDISON/Historical_Statistics/vertical-file_02-2010.xls',
            description='Maddison World Pop (xls)'
            ),
        dict(
            url='http://ckan.net/',
            description='An html page',
            format='text/html'
            ),
        dict(
            url='http://data.gov.uk/sparql',
            description='A SPARQL API',
            format='api/sparql'
            ),
        dict(
            url='http://ckan.net/mydata.zip',
            description='Zip file',
            format='zip'
            ),
        dict(
            url='http://ckan.net/mydata.csv.zip',
            description='Zipped csv file',
            format='zip:csv'
            ),
        ]
    )

try:
    client.package_register_post(pkg)
except ckanclient.CkanApiError:
    client.package_entity_put(pkg)
