package com.bnuz.electronic_supermarker.common.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DS {

	private static Logger logger = LoggerFactory.getLogger(com.bnuz.electronic_supermarker.common.db.DS.class);

	@Autowired
	private static DataSourceHolder dsh;

	public static void write() {
		logger.debug("DataSource : write");
		dsh.setWrite();
	}

	public static void read() {
		logger.debug("DataSource : read");
		dsh.setRead();
	}

}
