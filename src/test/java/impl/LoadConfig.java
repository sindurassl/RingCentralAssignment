package impl;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


/**
 * @author Sindura Sistla
 * This class is to load the pre-config data from a file before test cases execution
 * Config data is sent to LoadConfig.java
 * LoadConfig class is the parent class
 */

public class LoadConfig {
	
	protected String redirect_uri;
	protected String baseURI;	
	protected String contentType;	
	protected String auth_code;
	protected String client_id;
	protected static String testcaseID1, testcaseID2, testcaseID3, testcaseID4, testcaseID5, testcaseID6, testcaseID7, testcaseID8, testcaseID9;
	protected static String testcaseID10, testcaseID11, testcaseID12,testcaseID13;
	
		@BeforeClass (groups = { "abstract" } )
		@Parameters (value = { "propFile" })
		public void setKeys(@Optional("./config/inputs/properties/loadConfig.properties") String configfile) throws Exception 
		{
		    Reporter.log("**** Reading the property values from the config..", true);
		    Properties p = new Properties();
		    FileInputStream  conf = new FileInputStream(configfile);
		    p.load(conf);

		    redirect_uri = p.getProperty("redirect_uri");
		    baseURI = p.getProperty("BaseURI");
		    contentType = p.getProperty("Content_type");
		    auth_code = p.getProperty("authorization_code");
		    client_id = p.getProperty("client_id");
		    testcaseID1 = p.getProperty("TestcaseID1");
		    testcaseID2 = p.getProperty("TestcaseID2");
		    testcaseID3 = p.getProperty("TestcaseID3");
		    testcaseID4 = p.getProperty("TestcaseID4");
		    testcaseID5 = p.getProperty("TestcaseID5");
		    testcaseID6 = p.getProperty("TestcaseID6");
		    testcaseID7 = p.getProperty("TestcaseID7");
		    testcaseID8 = p.getProperty("TestcaseID8");
		    testcaseID9 = p.getProperty("TestcaseID9");
		    testcaseID10 = p.getProperty("TestcaseID10");
		    testcaseID11 = p.getProperty("TestcaseID11");
		    testcaseID12 = p.getProperty("TestcaseID12");
		    testcaseID13 = p.getProperty("TestcaseID13");	    
		}
		
		//Helper method
		protected static String getJsonBody(int id) {
			String getJsonString = null;
			switch(id){
			case 0:
				getJsonString = testcaseID1;
			case 1:
				getJsonString = testcaseID2;
			case 2:
				getJsonString = testcaseID3;
			case 3:
				getJsonString = testcaseID4;
			case 4:
				getJsonString = testcaseID5;
			case 6:
				getJsonString = testcaseID7;
			case 7:
				getJsonString = testcaseID8;
			case 8:
				getJsonString = testcaseID9;
			case 9:
				getJsonString = testcaseID10;
			case 10:
				getJsonString = testcaseID11;
			case 11:
				getJsonString = testcaseID12;
			case 12:
				getJsonString = testcaseID13;
			}
			return getJsonString;
		}
}
