package com.zetainteractive.security.service;

import java.util.List;

import com.zetainteractive.security.bo.UserBO;

public interface SecurityService {
	long	saveUser(UserBO userBO);
	UserBO getUserById(long userId);
	long 	updateUser(UserBO userBO);
	List<UserBO>	listUser(String criteria);
	long saveUserWithTemplate(UserBO user);
}
