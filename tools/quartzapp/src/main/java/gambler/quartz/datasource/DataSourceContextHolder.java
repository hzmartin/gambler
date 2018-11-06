package gambler.quartz.datasource;

public class DataSourceContextHolder {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void selectDataSource(String name) {
		contextHolder.set(name);
	}

	public static String getDataSourceName() {
		return ((String) contextHolder.get());
	}

	public static void clearDataSourceSelection() {
		contextHolder.remove();
	}
}