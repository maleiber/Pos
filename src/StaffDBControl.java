package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StaffDBControl 
{
	private String driver = "com.mysql.jdbc.Driver";
    // URLָ��Ҫ���ʵ����ݿ���Shop_Stock
    private String url = "jdbc:mysql://127.0.0.1:3306/shop";
    // MySQL����ʱ���û���
    private String user = "root"; 

    // MySQL����ʱ������
    private String password = "123456";
    
    private Connection conn = null;		//����Ʒ������
    private Statement statement = null;	//
    
    public StaffDBControl() throws Exception
    {
    	try{
            // ������������
            Class.forName(driver);
            // �������ݿ�
            conn = DriverManager.getConnection(url, user, password);

            // statement����ִ��SQL���
            statement = conn.createStatement();
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    public int login(String name,String password) throws Exception
    {
    	String sql = "SELECT staff_id,is_login FROM staff WHERE staff_name=\'"+name+"\' AND password=\'"+password+"\'";
    	ResultSet rs = null;
    	try {
			rs = statement.executeQuery(sql);
	    	if(rs.next())
	    	{
	    		int staff_id = rs.getInt("staff_id");
	    		int is_login = rs.getInt("is_login");
	    		if(rs.next())
	    			throw new Exception("���ҵ������ͬ�û�������ϵ��ؼ�����Ա����");
	    		else
	    		{
	    			if(is_login == 1)
	    				throw new Exception("�������������ط��ѵ�¼������������ϵ��ؼ�����Ա����");
	    			else
	    			{
	    				sql = "UPDATE staff SET is_login = 1 WHERE staff_id="+staff_id;
	    				statement.executeUpdate(sql);
	    				return staff_id;
	    			}
	    		}
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			rs.close();
		}
    	return -1;
    }
    
    public void logout(int id) throws Exception
    {
    	String sql = "SELECT is_login FROM staff WHERE staff_id="+Integer.toString(id);
    	ResultSet rs = null;
    	try {
			rs = statement.executeQuery(sql);
	    	if(rs.next())
	    	{
	    		int staff_id = rs.getInt("staff_id");
	    		int is_login = rs.getInt("is_login");

	    		if(is_login == 0)
	    			throw new Exception("����ע��������������ϵ��ؼ�����Ա����");
	    		else
	   			{
	   				sql = "UPDATE staff SET is_login = 0 WHERE staff_id="+staff_id;
	   				statement.executeUpdate(sql);
	   			}
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			rs.close();
		}
    }
    
    public void disconnect() throws SQLException
    {
    	conn.close();
    	statement.close();
    }
}
