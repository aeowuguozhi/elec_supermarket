package com.bnuz.electronic_supermarker.common.db;

/**
 * 数据源操作
 */
public class DataSourceHolder {

	// 线程本地环境
	private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

	// 设置数据源
	public static void setDataSource(String customerType) {
		dataSources.set(customerType);
	}

	// 设置数据源为写库
	public static void setWrite() {
		dataSources.remove();
	}

	// 设置数据库为读库
	public static void setRead() {
		dataSources.set("read");
	}

	// 获取数据源
	public static String getDataSource() {
		return (String) dataSources.get();
	}

	// 清除数据源
	public static void clearDataSource() {
		dataSources.remove();
	}

}