import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.HttpTextBodyContent
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

// Katalon sends a REST request (producer), then reads it back (consumer).
String id = UUID.randomUUID().toString()
String message = 'Katalon API integration test'
String base = 'http://127.0.0.1:8088/messages'
RequestObject post = new RequestObject('POST_Message')
post.setRestUrl(base)
post.setRestRequestMethod('POST')
post.setHttpHeaderProperties([new com.kms.katalon.core.testobject.TestObjectProperty('Content-Type', com.kms.katalon.core.testobject.ConditionType.EQUALS, 'application/json')])
post.setBodyContent(new HttpTextBodyContent(JsonOutput.toJson([id:id, message:message]), 'UTF-8', 'application/json'))
def created = WS.sendRequest(post)
WS.verifyResponseStatusCode(created, 201)
assert new JsonSlurper().parseText(created.getResponseText()).id == id

RequestObject get = new RequestObject('GET_Message')
get.setRestUrl(base + '/' + id)
get.setRestRequestMethod('GET')
def fetched = WS.sendRequest(get)
WS.verifyResponseStatusCode(fetched, 200)
def data = new JsonSlurper().parseText(fetched.getResponseText())
assert data.id == id
assert data.message == message
println 'PASS: REST producer/consumer for ' + id
