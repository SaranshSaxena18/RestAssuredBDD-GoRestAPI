package utils;

import pojo.createUserPojo;

public class testDataBuild {
	public static createUserPojo createUserPayload(String name, String gender, String email, String status)
	{
		createUserPojo createUser = new createUserPojo();
		createUser.setName(name);
		createUser.setGender(name);
		createUser.setEmail(name);
		createUser.setStatus(name);
		return createUser;
	}
}
