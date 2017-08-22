package db;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.mysql.jdbc.Connection;


public final class jdbcUtils {

	public static String connstr = "jdbc:mysql://172.16.32.28:3306/scan?user=root&password=&useUnicode=true&characterEncoding=UTF-8";
	public static String driver = "com.mysql.jdbc.Driver";
	public static Connection conn = null;
	private static jdbcUtils jdbcUtilSingle = null;
	
	public static jdbcUtils getInitJDBCUtil() {
        if (jdbcUtilSingle == null) {
            // ������� ��ֹ�̲߳���
            synchronized (jdbcUtils.class) {
                if (jdbcUtilSingle == null) {
                    jdbcUtilSingle = new jdbcUtils();
                }
            }
        }
        return jdbcUtilSingle;
    }
	
	static {
        try {
            // ע�����������·�ʽ��
            // 1.ͨ������������ע������������ע�����Σ����һ�������������������಻�����Ǿͱ����ˡ�
            // DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            // 2.��3����
            // System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");// �Ƽ�ʹ�÷�ʽ
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	 public Connection getConnection(String connstr) {
	        try {
	            conn = (Connection) DriverManager.getConnection(connstr);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return conn;

	 }
	 
	 
	 public int insert(Map<String,String> sql,Connection conn,PrintWriter stdout) {
		 String sql_exec = "INSERT INTO httplog(url,method,header,body) VALUE(?,?,?,?)";
		 try {
			PreparedStatement ps = conn.prepareStatement(sql_exec);
			ps.setString(1, sql.get("url"));
			ps.setString(2, sql.get("method"));
			ps.setString(3, sql.get("headers"));
			ps.setString(4, sql.get("body"));
			int i = ps.executeUpdate();
			stdout.println("[+] insert ["+i+"] row ");
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		 return 0;
	 }
	 

	 public int queryrepeat(String url,String body,Connection conn) {
		 String sql_exec = "SELECT COUNT(*) as count FROM httplog WHERE url=? AND body = ?";
		 int flag = 0;
		 try {
			PreparedStatement ps = conn.prepareStatement(sql_exec);
			ps.setString(1, url);
			ps.setString(2, body);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				flag = rs.getInt("count");
			}
			return flag;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return 0;
	 }
	 
}
