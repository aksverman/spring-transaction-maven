package com.zetainteractive.security.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zetainteractive.security.bo.UserBO;
import com.zetainteractive.security.controller.SecurityController;
import com.zetainteractive.security.dao.SecurityDAO;


@Service
//@Scope(value="prototype",proxyMode=ScopedProxyMode.TARGET_CLASS) 

//@Transactional (propagation = Propagation.REQUIRED, readOnly = false)
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	SecurityDAO	securityDao;
	
	@Autowired
	PlatformTransactionManager txManager;
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

	/* 1
	 * Transaction method as Propagation behavior as REQUIRED 
	 * 
	 * REQUIRED :--Indicates that the target method can not run without an active tx. 
	 * If a tx has already been started before the invocation of this method, 
	 * then it will continue in the same tx or a new tx would begin soon as this method is called.    
	 * 
	 * @see http://www.byteslounge.com/tutorials/spring-transaction-propagation-tutorial
	 */
	@Transactional (propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = false, rollbackFor = Exception.class)
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		long userId = 0;
		
		/*DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		TransactionStatus	txStatus = txManager.getTransaction(definition);*/
		try {
			userId = securityDao.saveUser(userBO);
			logger.info("User Created Successfully..." + userId);
		} catch( Exception e) {
			logger.error("Exception occurred : "+e);
		}
		//txManager.commit(txStatus);
		return userId;	
	}

	
	/* 2
	 * Transaction implemented with Propagation as NEVER which makes this transaction failed
	 * ********** User Not Created ********* 
	 * NEVER : - Indicates that the target method will raise an exception if executed in a transactional process.
	 * This option is mostly not used in projects.
	 * @see 	https://dzone.com/articles/spring-transaction-management
	 */
	
	/*@Transactional (propagation = Propagation.NEVER, isolation = Isolation.READ_COMMITTED, readOnly = false, rollbackFor = Exception.class)
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		Long userId = null;
		
		DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		TransactionStatus	txStatus = txManager.getTransaction(definition);
		userId = securityDao.saveUser(userBO);
		logger.info("User Created Successfully..." + userId);
		return userId;	
	}*/

	/* 3
	 * REQUIRES_NEW : - Indicates that a new tx has to start every time the target method is called. 
	 * If already a tx is going on, it will be suspended before starting a new one.
	 * 
	 * inner transaction may commit or rollback independently of the outer transaction,
	 *  i.e. the outer transaction will not be affected by the inner transaction result
	 * @see 	http://www.byteslounge.com/tutorials/spring-transaction-propagation-tutorial
	 */
	/*@Transactional (propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = false)
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		Long userId = null;
		
		DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		TransactionStatus	txStatus = txManager.getTransaction(definition);
		userId = securityDao.saveUser(userBO);
		
		logger.info("User Created Successfully..." + userId);
		return userId;	
		
	}
	*/
	
	/* 4
	 * 
	 * Transaction method implemented as Propagation behavior as SUPPORTS
	 * SUPPORTS
	 * Indicates that the target method can execute irrespective of a tx. 
	 * If a tx is running, it will participate in the same tx. 
	 * If executed without a tx it will still execute if no errors.
	 * 
	 * Methods which fetch data are the best candidates for this option.
	 * 
	 * @see 	https://dzone.com/articles/spring-transaction-management
	 */
	/*@Transactional (propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = false, rollbackFor = Exception.class)
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		Long userId = null;
		
		DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		TransactionStatus	txStatus = txManager.getTransaction(definition);
		userId = securityDao.saveUser(userBO);
		logger.info("User Created Successfully..." + userId);
		return userId;	
	}*/
	
	
	/* 5
	 * Propagation behavior as MANDATORY with isolation as READ_COMMITTED
	 * MANDATORY : -
	 * 		Indicates that the target method requires an active tx to be running.
	 * 		If a tx is not going on, it will fail by throwing an exception.
	 * 		It requires an active tx otherwise tx fails
	 * ********will fail to create new User as no active transaction found
	 * ******** User Not Created *********
	 * @see 	https://dzone.com/articles/spring-transaction-management
	 */
	/*@Transactional (propagation = Propagation.MANDATORY, isolation = Isolation.READ_COMMITTED, readOnly = false, rollbackFor = Exception.class)
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		Long userId = null;
		
		DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
		TransactionStatus	txStatus = txManager.getTransaction(definition);
		
		userId = securityDao.saveUser(userBO);
		logger.info("User Created Successfully..." + userId);
		return userId;
	}
	*/
	
	/* 6
	 * Tx with Propagation as REQUIRED & isolation as READ_UNCOMMITTED
	 * it reads uncommitted records of another parallel transaction
	 * including “dirty” or uncommitted data. This is the lowest isolation level
	 * resulting in inconsistent records 
	 * 
	 * **  Dirty Reads: ** Transaction 'A' writes a record. 
	 * Meanwhile Transaction 'B' reads that same record before Transaction A commits. 
	 * Later Transaction A decides to rollback and now we have changes in Transaction B that are inconsistent. 
	 * This is a dirty read.  
	 * 
	 * @see 	http://www.byteslounge.com/tutorials/spring-transaction-isolation-tutorial
	 * @see		https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html#idm140523540100800
	 */
	/*@Transactional (propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED, readOnly = false, rollbackFor = Exception.class)
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		Long userId = null;
		
		DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		TransactionStatus	txStatus = txManager.getTransaction(definition);
		userId = securityDao.saveUser(userBO);
		logger.info("User Created Successfully..." + userId);
		return userId;
	}*/
	
	
	/*
	 * Propagation as REQUIRED & Isolation as READ_COMMITTED
	 * 
	 * READ_COMMITTED  : - A constant indicating that dirty reads are prevented; 
	 * non-repeatable reads and phantom reads can occur.
	 * 
	 * @see 	https://dzone.com/articles/spring-transaction-management
	 * @see		https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html#idm140523540167984
	 * 
	 */
	/*@Transactional (propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public UserBO getUserById(long userId) {
		logger.info("Begin : " + getClass().getName() + " :getUserById()");
		DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		TransactionStatus	txStatus = txManager.getTransaction(definition);
		logger.info("Begin : " + getClass().getName() + " :getUserById()");
		return securityDao.getUserById(userId);
	}*/

	/*
	 * Propagation as REQUIES_NEW   & Isolation as REPEATABLE_READ
	 * REPEATABLE_READ : - This is the default level used in MySQL. 
	 * A constant indicating that dirty reads and non-repeatable reads are prevented; 
	 * 
	 * Non-Repeatable Reads: 
	 * Transaction 'A' reads some record. Then Transaction 'B' writes that same  record and commits. Later Transaction A reads that same record again and may get different values because Transaction B made changes to that record and committed. This is a non-repeatable read.
	 * phantom reads can occur.
	 * @see com.zetainteractive.security.service.SecurityService#getUserById(long)
	 */
	@Transactional (propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = false, rollbackFor = Exception.class)
	@Override
	public UserBO getUserById(long userId) {
		logger.info("Begin : " + getClass().getName() + " :getUserById()");
		/*DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		definition.setName("getTx");
		//definition.setIsolationLevel(2);
		TransactionStatus	txStatus = txManager.getTransaction(definition);*/
		UserBO userBO = null;
		userBO = securityDao.getUserById(userId);
		logger.info("First read occurred " + userBO);
		
		userBO = securityDao.getUserById(userId);
		logger.info("Second read " + userBO);
		logger.info("Begin : " + getClass().getName() + " :getUserById()");
		return userBO;
		
	}
	
	/*
	 * 
	 * @see com.zetainteractive.security.service.SecurityService#updateUser(com.zetainteractive.security.bo.UserBO)
	 */
	@Transactional (propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class) 
	@Override
	public long updateUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
				
		/*DefaultTransactionDefinition	definition = new DefaultTransactionDefinition();
		definition.setName("updatTX");
		TransactionStatus	txStatus = txManager.getTransaction(definition);*/
		long result = securityDao.updateUser(userBO);
		logger.info("User Updated Successfully..." + userBO.getUserID());
		return result;
	}

	
	/*
	 * Propagation as REQUIRES_NEW with Isolation as SERIALIZABLE
	 * 
	 * SERIALIZABLE : - A constant indicating that dirty reads, non-repeatable reads, and phantom reads are prevented.
	 * This is the highest isolation level and
	 *  usually requires the use of shared read locks and exclusive write locks (as in the case of MySQL).
	 *  
	 * **Phantom Reads:**- Transaction 'A' reads a range of records. 
	 * Meanwhile Transaction 'B' inserts a new record in the same range that Transaction A initially fetched and commits. 
	 * Later Transaction A reads the same range again and will also get the record that Transaction B just inserted. 
	 * 	This is a phantom read: a transaction fetched a range of records multiple times from the database 
	 * 	and obtained result sets (containing phantom records).
	 * 
	 * @see		https://dzone.com/articles/spring-transaction-management
	 * @see 	https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html#idm140523540092016
	 */
	@Transactional (propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	@Override
	public List<UserBO> listUser(String criteria) {
		logger.info("Begin : " + getClass().getName() + " : listUser()");
		List<UserBO>  userBOList = securityDao.listUser(criteria);
		logger.info("User list 1st fetched : " + userBOList.size());
		
		userBOList = securityDao.listUser(criteria);
		logger.info("User list 2nd fetched : " + userBOList.size());
		return userBOList;
	}

	
	/*
	 * Spring Framework provides two ways to manage programmatic transaction list shown below:
	 * 	---	By using TransactionTemplate
	 * 	---	By using PlatformTransactionManager implementation directly to the code
	 * 
	 * Typical usage: Allows for writing low-level data access objects that use resources 
	 * such as JDBC DataSources but are not transaction-aware themselves. 
	 * Instead, they can implicitly participate in transactions handled by higher-level application services utilizing this class, 
	 * making calls to the low-level services via an inner-class callback object.
	 * 
	 * @see 	http://javahonk.com/spring-programmatic-transaction-using-transactiontemplate/
	 */
	//@Transactional (propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public long saveUserWithTemplate(UserBO user) {
		logger.info("Begin : " + getClass().getName() + " :saveUser()");
		long userId = 0;
		
		try {
			userId = securityDao.saveUserWithTemplate(user);
			logger.info("User Created Successfully  with template..." + userId);
		} catch( Exception e) {
			logger.error("Exception occurred while saving with template: "+e);
		}
		return userId;
	}
	
}
