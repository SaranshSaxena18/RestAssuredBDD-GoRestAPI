package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
	public static String getProperty(String key) throws IOException
	{
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//Resources//global.properties");
		prop.load(fis);
		return prop.getProperty(key);
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
    	else if(APIMethod.equalsIgnoreCase("GET"))
		{
			try {
				return PropertyFileReader.getProperty("GetUserEndPoint");
			} catch (IOException e) {
				System.out.println("Unable to fetch Get User End Point from the global properties file.");
				e.printStackTrace();
			}
		}
    	else if(APIMethod.equalsIgnoreCase("PUT"))
		{
			try {
				return PropertyFileReader.getProperty("EditUserEndPoint");
			} catch (IOException e) {
				System.out.println("Unable to fetch Put End Point from the global properties file.");
				e.printStackTrace();
			}
		}
    	else if(APIMethod.equalsIgnoreCase("DELETE"))
		{
			try {
				System.out.println("DeleteUserEndPoint - "+PropertyFileReader.getProperty("DeleteUserEndPoint"));
				return PropertyFileReader.getProperty("DeleteUserEndPoint");
			} catch (IOException e) {
				System.out.println("Unable to fetch Delete User End Point from the global properties file.");
				e.printStackTrace();
			}
		}
		return null;
    }
}
