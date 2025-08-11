package utils;

import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.builder.*;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SpecificationFactory {
	private static RequestSpecification reqSpec;
	private static ResponseSpecification responseSpec;
    public static RequestSpecification getRequestSpecification() {
        if (reqSpec == null) {
            synchronized (SpecificationFactory.class) {
                if (reqSpec == null) {
                    String baseURI = null;
					try {
						baseURI = PropertyFileReader.getProperty("baseURI");
						System.out.println("baseURI - "+baseURI);
					} catch (IOException e) {
						System.out.println("Unable to read baseURI from the global properties file.");
						e.printStackTrace();
					}
                    String authToken = null;
					try {
						authToken = PropertyFileReader.getProperty("AuthorizationToken");
						System.out.println("authToken - "+authToken);
					} catch (IOException e) {
						System.out.println("Unable to read AuthorizationToken from the global properties file.");
						e.printStackTrace();
					}
                    reqSpec = new RequestSpecBuilder()
                        .setBaseUri(baseURI)
                        .addHeader("Content-Type", "application/json") 
                        .addHeader("Authorization", authToken)
                        .log(LogDetail.ALL) 
                        .build();
                }
            }
        }
        return reqSpec;
    }
    
    public static ResponseSpecification getResponseSpecification(String APIMethod)
    {
    	if(APIMethod.equalsIgnoreCase("POST"))
    	{
    		responseSpec = new ResponseSpecBuilder()
    						.expectStatusCode(201)
    						.expectContentType(ContentType.JSON)
    						.expectBody(matchesJsonSchemaInClasspath("TestResources/expectedjsonschema.json"))
    						.build();
    	}
    	return responseSpec;
    }
    
    
    public static String getEndPoint(String APIMethod)
    {
    	if(APIMethod.equalsIgnoreCase("POST"))
		{
			try {
				return PropertyFileReader.getProperty("AddUserEndPoint");
			} catch (IOException e) {
				System.out.println("Unable to fetch Add User End Point from the global properties file.");
				e.printStackTrace();
			}
		}
		return null;
    }

}
