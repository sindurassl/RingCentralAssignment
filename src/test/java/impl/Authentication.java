package impl;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author Sindura Sistla
 * This class is to execute Ringout API and CallLogAPI 
 * Config data is received from LoadConfig.java. 
 * Authentication class is extending LoadConfig class
 */

public class Authentication extends LoadConfig{
	
	
	//@Test
	public void getAuthorizationCode(){	
		RequestSpecification request = given();        
		request.header("Content-Type","application/x-www-form-urlencoded");  
		request.param("client_id", client_id);
		request.param("redirect_uri",redirect_uri);
		request.param("response_type", "code");
		request.param("prompt", "login");
		request.param("state", "ABC123");
		
		Response response = request.get(baseURI +"/restapi/oauth/authorize");
		
		int respcode = response.getStatusCode();
		String responseBody = response.asString();
		
		if(redirect_uri.equalsIgnoreCase("https://localhost:8443/")){
			Assert.assertEquals(respcode, 200);  
			System.out.println(responseBody);
		}
		else{
			Assert.assertEquals(respcode, 400);  
			boolean errormessage = responseBody.contains("Redirect URIs do not match");
			Assert.assertTrue(errormessage, "Redirect URIs do not match");
			
		}		
	}
	
}
