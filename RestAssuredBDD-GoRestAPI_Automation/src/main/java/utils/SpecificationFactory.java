package utils;

import java.io.IOException;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public class SpecificationFactory {
	private static RequestSpecification reqSpec;
    public static RequestSpecification getRequestSpecification() {
        if (reqSpec == null) {
            synchronized (SpecificationFactory.class) {
                if (reqSpec == null) {
                    String baseURI = null;
					try {
						baseURI = PropertyFileReader.getProperty("baseURI");
					} catch (IOException e) {
						System.out.println("Unable to read baseURI from the global properties file.");
						e.printStackTrace();
					}
                    String authToken = null;
					try {
						authToken = PropertyFileReader.getProperty("AuthorizationToken");
					} catch (IOException e) {
						System.out.println("Unable to read AuthorizationToken from the global properties file.");
						e.printStackTrace();
					}
					
                    reqSpec = new RequestSpecBuilder()
                        .setBaseUri(baseURI)
                        .addHeader("Content-Type", "application/json")  // corrected the value
                        .addHeader("Authorization", authToken)
                        .log(LogDetail.ALL) // optional, for logging requests for debugging
                        .build();
                }
            }
        }
        return reqSpec;
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
