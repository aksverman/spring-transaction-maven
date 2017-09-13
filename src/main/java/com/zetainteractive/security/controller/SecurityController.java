package com.zetainteractive.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zetainteractive.security.bo.UserBO;
import com.zetainteractive.security.service.SecurityService;


@RestController
@RequestMapping("/rest")
public class SecurityController {
	
	@Autowired
	SecurityService	securityService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public	ResponseEntity<?>	saveUser(@RequestBody UserBO userBO, @RequestHeader HttpHeaders headers, BindingResult bindingResult) {
		//logger.info("Begin : " + getClass().getName() + ": saveUser()");

		long userId = securityService.saveUser(userBO);
		//logger.info("Begin : " + getClass().getName() + ": saveUser()");
		return new ResponseEntity<Long>(userId,HttpStatus.OK);
	}
	
	@RequestMapping(value="/savetemplate", method=RequestMethod.POST)
	public	ResponseEntity<?>	saveUserWithTemplate(@RequestBody UserBO userBO, @RequestHeader HttpHeaders headers, BindingResult bindingResult) {
		//logger.info("Begin : " + getClass().getName() + ": saveUser()");

		long userId = securityService.saveUserWithTemplate(userBO);
		//logger.info("Begin : " + getClass().getName() + ": saveUser()");
		return new ResponseEntity<Long>(userId,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get/{userId}", method=RequestMethod.GET)
	public	ResponseEntity<?>	getUser(@PathVariable long userId,@RequestHeader HttpHeaders headers) {
		logger.info("Begin : " + getClass().getName() + ": getUser()");
		UserBO userBO = null;
		try {
			userBO  = securityService.getUserById(userId);
		} catch (Exception e) {
			logger.error("Exception : " + e);
			return new ResponseEntity<String>("getUser method faild",HttpStatus.BAD_REQUEST);
		}
		logger.info("End : " + getClass().getName() + ": getUser()");
		return new ResponseEntity<UserBO>(userBO,HttpStatus.OK);
	}

	@RequestMapping (value="/update", method = RequestMethod.POST)
	public ResponseEntity<?>	updatUser(@RequestBody UserBO userBO, @RequestHeader HttpHeaders header, BindingResult bindingResult) {
		try {
			long result = securityService.updateUser(userBO);
			if(result > 0)
				return new ResponseEntity<String>("User updated ",HttpStatus.OK);
			else
				return new ResponseEntity<String>("Updation failed",HttpStatus.NOT_MODIFIED);
		} catch (Exception e) {
			return new ResponseEntity<String>("UserBO updation failed",HttpStatus.BAD_REQUEST);
			
		}
	}
	
	@RequestMapping (value="/list/{criteria}", method = RequestMethod.GET)
	public ResponseEntity<?>	listUser(@PathVariable String criteria, @RequestHeader HttpHeaders header) {
		try {
			int noOfUser = securityService.listUser(criteria).size();
			return new ResponseEntity<Integer>(noOfUser, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Exception while list user: ");
			return new ResponseEntity<String>("list user failed", HttpStatus.BAD_REQUEST);
		}
	}
}
