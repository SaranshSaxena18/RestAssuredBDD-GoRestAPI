package utils;

import pojo.CreateUserRequestPojo;

public class testDataBuild {
	public static CreateUserRequestPojo createUserPayload(String name, String gender, String email, String status)
	{
		CreateUserRequestPojo createUser = new CreateUserRequestPojo();
		createUser.setName(name);
		createUser.setGender(gender);
		createUser.setEmail(email);
		createUser.setStatus(status);
		return createUser;
	}
}
