package utils;

import pojo.CreateUserRequestPojo;
import pojo.EditUserRequestPojo;

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
	
	public static EditUserRequestPojo createEditUserPayload(int id, String name, String gender, String email, String status)
	{
		EditUserRequestPojo editUser = new EditUserRequestPojo();
		editUser.setId(id);
		editUser.setName(name);
		editUser.setGender(gender);
		editUser.setEmail(email);
		editUser.setStatus(status);
		return editUser;
	}
}
