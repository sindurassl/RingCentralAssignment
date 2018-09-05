package impl;

import static io.restassured.RestAssured.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


/**
 * @author Sindura Sistla
 * This class is to execute Ringout API and CallLogAPI 
 * Config data is received from LoadConfig.java
 * Authentication class is extending LoadConfig class
 */

public class RingOut_CallLog extends LoadConfig {
	LoadConfig config = new LoadConfig();
	public String generateJsonBody(String path) throws IOException {
	    return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	
	
	/**
	 * This method is to execute Ring-Out APIs (positive and negative). Input is from a file.
	 * This TC will be executed first
	 */
	@Test (priority = 1)
	public void ringOut() throws IOException{
		CSVParser fileParser = null;
		SoftAssert softAssert = new SoftAssert();
		
		try {
				File file = new File("./config/inputs/csv/RingOutData.csv");
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				String[] headers = bufferedReader.readLine().split(",");

				fileParser = new CSVParser(bufferedReader, CSVFormat.RFC4180.withHeader(headers));

				List<CSVRecord> csvRecords = fileParser.getRecords();
				
				for (int counter=0;counter<csvRecords.size();counter++){
					Map<String, String> record = csvRecords.get(counter).toMap();
					String testCaseHeader = LoadConfig.getJsonBody(counter);
					
					RequestSpecification request = RestAssured.given();
					request.header("Authorization", auth_code);
					request.contentType(contentType);	
					request.header("Accept", ContentType.JSON);
					request.body(testCaseHeader);					
					request.log().all();

					Response response = request.post((baseURI + record.get("EndPoint")));
		
					int respcode = response.statusCode();
					String respbody = response.getBody().asString();
					System.out.println("respcode: "+respcode);
					
					System.out.println("Request Body::: "+record.get("TestCaseDescription"));
					System.out.println("Request Body::: "+record.get("JSONbody"));
					
					if(record.get("TestCaseType").equalsIgnoreCase("Positive")){
						softAssert.assertEquals(respcode, record.get("HttpStatusCode"));
						System.out.println(respbody);
						softAssert.assertTrue(respbody.contains("InProgress"), "Call in progress");
					}
					else if(record.get("TestCaseType").equalsIgnoreCase("Negative")){
						System.out.println(respbody);
						softAssert.assertTrue((respcode == 400) || (respcode == 403));
						//Assert.assertTrue(respbody.contains(record.get("ErrorMessage")), "Error obtained as expected. Error message is: "+record.get("ErrorMessage"));
					}
					else if(respcode == 401){
						System.out.println("Authorization failed. Please pass valid authorization code");
					}
		} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (fileParser != null) {
				fileParser.close();
			}
		}
	}


	/**
	 * This method is to execute CallLog APIs (positive and negative). Input is from a file.
	 * This TC will be executed after TC ringOut()
	 */
	//@Test (priority = 2)
	public void callLog() throws IOException{
		CSVParser fileParser = null;
 
		try {
				File file = new File("./config/inputs/callLogData.numbers");
				BufferedReader in = new BufferedReader(new FileReader(file));
				String[] headers = in.readLine().split(",");

				fileParser = new CSVParser(in, CSVFormat.RFC4180.withHeader(headers));

				List<CSVRecord> csvRecords = fileParser.getRecords();
				
				for (int i=0;i<csvRecords.size();i++){
					Map<String, String> record = csvRecords.get(i).toMap();
					System.out.println(record.get("TestCaseDescription"));
					
					RequestSpecification request = RestAssured.given();
					request.header("Authorization", auth_code);
					request.contentType(contentType);
			
					request.param("extensionNumber", record.get("extensionNumber"));
					request.param("showBlocked", record.get("showBlocked"));
					request.param("phoneNumber", record.get("phoneNumber"));
					request.param("page", record.get("page"));
					request.param("perPage", record.get("perPage"));

					Response response = request.get((baseURI + record.get("EndPoint")));
		
					int respcode = response.statusCode();
					String respbody = response.getBody().asString();
			
					if(respcode == 200) {
						System.out.println(respbody);
						Assert.assertFalse(respbody.isEmpty());
					}else if(respcode == 400){
						System.out.println(respbody);
					}
		} 
		} catch (IOException exception) {
			exception.printStackTrace();
		} finally{
			if (fileParser != null) {
				fileParser.close();
			}
		}
	}
	
	
}
