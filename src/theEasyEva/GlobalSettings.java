package theEasyEva;

import java.io.InputStream;
import java.util.Properties;

public class GlobalSettings {
	// ≈‰÷√JDBC driver
	public static Properties prop = getProperties();
	public static String odriver = prop.getProperty("odriver",
			"oracle.jdbc.driver.OracleDriver");
	public static String sdriver = prop.getProperty("sdriver",
			"com.microsoft.sqlserver.jdbc.SQLServerDriver");
	public static String pdriver = prop.getProperty("pdriver",
			"org.postgresql.Driver");

	public static String getProperty(String property) {
		return prop.getProperty(property);
	}

	public static Properties getProperties() {
		Properties prop = new Properties();
		try {
			InputStream file = GlobalSettings.class.getClass()
					.getResourceAsStream("/Globalsetting.properties");
			prop.load(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
}
