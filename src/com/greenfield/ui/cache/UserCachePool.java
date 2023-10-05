package com.greenfield.ui.cache;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.security.ParameterEncrypt;
import com.greenfield.common.util.EncryptUtil;
import com.greenfield.common.util.StringHelper;

public class UserCachePool {
	private static final Logger LOGGER = Logger.getLogger(UserCachePool.class);

	public static void resetPool() {
		try {
			// simple remove the cached data from the pool
			UserCacheStore pool = UserCacheStore.getInstance();

			// just call
			LOGGER.warn("UserCacheStore resetPool() is called.");
			pool.reset();
		} catch (Exception e) {
			LOGGER.warn("UserCacheStore: cannot reset pool: " + e.getMessage());
			e.printStackTrace();
		}

	}


	/**
	 * Put encrypted UserId to Cache - to reduce some repeat calculation.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getEncryptedUserId(String userId) throws Exception {
		UserCacheStore pool = UserCacheStore.getInstance();

		// in this call, we allow null for marketType and/or ticker
		String key = StringHelper.nvl(userId, "*") + ObjectPoolKeys.USER_ID_ENCRYPT_KEY_SUFFIX;
		String encrypt = (String) pool.getObject(key);

		if (encrypt == null) {
			// not in cache
			encrypt = ParameterEncrypt.decrypt(key);

			if (encrypt != null) {
				pool.loadObject(key, encrypt);
			}
		}

		return encrypt;
	}
}

/* end */
