package theEasyEva;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EasyDB {
	private Connection conn = null; // ���ݿ����Ӷ���
	private Statement ps = null; // ���ݿ�Ԥ�������
	private ResultSet rs = null; // ��ѯ�����
	String url;
	String instance;
	String username;
	String password;
	String connurl;

	public EasyDB(String url, String instance, String username,
			String password) {
		this.url = url;
		this.instance = instance;
		this.username = username;
		this.password = password;
	}

	// ��ȡ���ݿ�����
	public Connection getConn(String databasetype) {
		try {
			if (databasetype.equals("sqlserver")) {
				Class.forName(GlobalSettings.sdriver);
				connurl = "jdbc:sqlserver://" + url + ";databaseName="
						+ instance;
			} else if (databasetype.equals("postgres")) {
				Class.forName(GlobalSettings.pdriver);
				connurl = "jdbc:postgresql://" + url + ":5432/" + instance;
			} else {
				// �������ݿ�����
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(connurl, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// �ر���������
	public void shutConn() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		if (ps != null)
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	// ��ȡ������ѯ���
	public String[] Query(String sql,String column) {
		List<String> list = new ArrayList<String>();
        String[] farms = null;
		try {
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			while (rs.next()) {
				String farm = (rs.getString(column));
				list.add(farm);
			}
			farms = (String[]) list.toArray(new String[list.size()]);
			System.out.println("��ѯ�ɹ���");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return farms;
	}

}
