import httplib2 as http
import json



try:
    from urlparse import urlparse
except ImportError:
    from urllib.parse import urlparse

headers = {
    'Accept': 'application/json',
    'fiware-service': 'openiot',
    'fiware-servicepath': '/',
    'X-Requested-With': 'XMLHttpRequest'
}

uri = 'https://cors-anywhere.herokuapp.com/http://177.104.61.52:8668/v2/urn:ngsd-ld:SoilProbeMP:001&&limit=99999'


target = urlparse(uri)
method = 'GET'
body = ''

h = http.Http()

# If you need authentication some example:


response, content = h.request(
        target.geturl(),
        method,
        body,
        headers)

# assume that content is a json reply
# parse content with the json module
print(content)

