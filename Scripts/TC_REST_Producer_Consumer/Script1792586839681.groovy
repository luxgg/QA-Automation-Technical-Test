import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.llm.keyword.LlmKeywords as LLM
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys


import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent

// REST API endpoint
String url = 'https://jsonplaceholder.typicode.com/posts'

// Producer: POST request
RequestObject postRequest = new RequestObject()
postRequest.setRestUrl(url)
postRequest.setRestRequestMethod('POST')
postRequest.setHttpHeaderProperties([
	new com.kms.katalon.core.testobject.TestObjectProperty(
		'Content-Type',
		com.kms.katalon.core.testobject.ConditionType.EQUALS,
		'application/json'
	)
])

postRequest.setBodyContent(new HttpTextBodyContent(
	'{"title":"QA Testing","body":"Katalon API Test","userId":1}',
	'UTF-8',
	'application/json'
))

ResponseObject postResponse = WS.sendRequest(postRequest)
WS.verifyResponseStatusCode(postResponse, 201)

// Consumer: GET request
RequestObject getRequest = new RequestObject()
getRequest.setRestUrl(url + '/1')
getRequest.setRestRequestMethod('GET')

ResponseObject getResponse = WS.sendRequest(getRequest)
WS.verifyResponseStatusCode(getResponse, 200)

println('REST API Testing PASSED')
