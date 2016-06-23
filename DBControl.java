package control;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import Domain.Commodity;

public class DBControl 
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
    
    public DBControl() throws Exception
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
    
    public void disconnect() throws SQLException
    {
    	conn.close();
    	statement.close();
    }
    
    //������Ʒ
    public Commodity searchCommodity(String barcode) throws Exception
    {
    	String name = "";	//����
    	String unit = "";	//��Ԫ
    	float price = 0;	//�۸�
    	float discount = 1; 	//�ۿ�
    	int num = 0;
    	
    	String sql = "SELECT * FROM stock WHERE barcode=\'"+barcode+"\'";
    	
    	ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);	//��ѯ��Ʒ
			
			//�ڿ�����
			if(rs.next())
			{
				num = rs.getInt("num");
				if(num == 0)
					throw new Exception("��治��");
				name = rs.getString("name");
				unit = rs.getString("unit");
				price = rs.getFloat("price");
			}
			else
				throw new Exception("δ�ҵ�");	//δ�ҵ���Ʒ���׳��쳣
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw e;
		} finally {
			rs.close();
		}
		
		setNum(barcode,num-1);
		
		discount = getDiscount(barcode);
		
		Commodity c = new Commodity();
		c.setBarcode(barcode);
		c.setName(name);
		c.setPrice(price);
		c.setUnit(unit);
		c.setDiscount(discount);
		
		return c;
    }
    
    public void setNum(String barcode,int num) throws Exception	//�������	
    {
    	if(num < 0)
    		throw new Exception("�����ֵ���Ϸ�"); 
    	
		String sql = "UPDATE stock SET num="+Integer.toString(num)+" WHERE barcode=\'"+barcode+"\'";
		statement.executeUpdate(sql);
    }
    
    public float getDiscount(String barcode) throws Exception
    {
    	float discount = 1;
    	String sql = "SELECT discount FROM sale_commodity WHERE barcode=\'" + barcode + "\'";
    	ResultSet rs = null;
    	try {
			rs=statement.executeQuery(sql);
			if(rs.next())
				discount = rs.getFloat("discount");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			rs.close();
		}
    	return discount;
    }
    
    @Override
    public void finalize() throws SQLException{           
        //super.finalize();  
    	disconnect();
    }
}
