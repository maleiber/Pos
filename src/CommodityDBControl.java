package control;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Vector;

import Domain.Commodity;

public class CommodityDBControl 
{
	private String driver = "com.mysql.jdbc.Driver";
    // URLָ��Ҫ���ʵ����ݿ���Shop_Stock
    private String url = "jdbc:mysql://127.0.0.1:3306/shop";
    // MySQL����ʱ���û���
    private String user = "root"; 

    // MySQL����ʱ������
    private String password = "123456";
    
    private Connection conn = null;		//����Ʒ������
    
    public CommodityDBControl() throws Exception
    {
    	try{
            // ������������
            Class.forName(driver);
            // �������ݿ�
            conn = DriverManager.getConnection(url, user, password);
            // statement����ִ��SQL���
    	} catch (Exception e) {
    		throw e;
    	}
    }
    //�Ͽ����ݿ�����
    public void disconnect() throws SQLException
    {
    	conn.close();
    }
    
    //����Ʒ�������Ʒ
	public Vector<Commodity> searchCommodityByBarcode(final String barcode) throws Exception
    {
		String complete_barcode = "";	//��������Ʒ��
    	String name = "";	//����
    	String unit = "";	//��Ԫ
    	float price = 0;	//�۸�
    	float discount = 1; 	//�ۿ�
    	boolean promotion = false;	//�Ƿ������һ
    	Vector<Commodity> result = new Vector<Commodity>();	//�������
    	
    	String sql = "SELECT barcode,name,unit,price FROM stock WHERE barcode LIKE \'%"+barcode+"%\'";
    	
    	ResultSet rs = null;
    	Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);	//��ѯ��Ʒ
			
			//�ڿ�����
			while(rs.next())
			{
				//�����������������Ʒ
				complete_barcode = rs.getString("barcode");
				name = rs.getString("name");
				unit = rs.getString("unit");
				price = rs.getFloat("price");
				discount = getDiscount(complete_barcode);
				promotion = getPromotion(complete_barcode);
				
				result.addElement(new Commodity(name,complete_barcode,unit,price,discount,promotion));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			statement.close();
			rs.close();
		}
		return result;
    }
    
    //�����ֲ�����Ʒ
	public Vector<Commodity> searchCommodityByName(String name) throws Exception
    {
		String barcode = "";	//��Ʒ��
    	String complete_name = "";	//����
    	String unit = "";	//��Ԫ
    	float price = 0;	//�۸�
    	float discount = 1; 	//�ۿ�
    	boolean promotion = false;	//�Ƿ������һ
    	Vector<Commodity> result = new Vector<Commodity>();	//��ѯ���
    	
    	String sql = "SELECT barcode,name,unit,price FROM stock WHERE name LIKE \'%"+name+"%\'";
    	
    	ResultSet rs = null;
    	Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);	//��ѯ��Ʒ
			
			//�ڿ�����
			while(rs.next())
			{
				//�����������������Ʒ
				barcode = rs.getString("barcode");
				complete_name = rs.getString("name");
				unit = rs.getString("unit");
				price = rs.getFloat("price");
				discount = getDiscount(barcode);
				promotion = getPromotion(barcode);
				
				result.addElement(new Commodity(complete_name,barcode,unit,price,discount,promotion));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			statement.close();
			rs.close();
		}
		return result;
    }	
	
	//�˵���Ʒ�����ı�󣬸ı����е���Ʒ������numΪ�ı����������Ϊ���ӣ���Ϊ����
	public void changeCommodityNum(String barcode,int num) throws Exception
	{
		if(num == 0)
			return;
		String sql = "SELECT num FROM stock "+"WHERE barcode=\'"+barcode+"\'";
		ResultSet rs = null;
		Statement statement = null;
		try {
			int stock_num;
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			if(rs.next())
				stock_num = rs.getInt("num");
			else
				throw new Exception("��Ʒ�����");
			if(stock_num < 0)
				throw new Exception("����������Ϸ���");
			else if(stock_num+num < 0)
				throw new Exception("��治�㣡");
			else
				setNum(barcode,stock_num+num);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			statement.close();
			rs.close();
		}
	}
	
	//������Ʒ����
    public void setNum(String barcode,int num) throws Exception		
    {
    	if(num < 0)
    		throw new Exception("�����ֵ���Ϸ�������ϵ��ؼ��������ˡ�"); 
    	Statement statement = null;
		try {
			String sql = "UPDATE stock SET num="+Integer.toString(num)+" WHERE barcode=\'"+barcode+"\'";
			statement = conn.createStatement();
			if(statement.executeUpdate(sql) == 0)
				throw new Exception("��Ʒ�����");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			statement.close();
		}
    }
    
    //��ȡ������Ϣ
    public float getDiscount(String barcode) throws Exception
    {
    	float discount = 1;
    	String sql = "SELECT discount FROM discount_commodity WHERE barcode=\'" + barcode + "\'";
    	ResultSet rs = null;
    	Statement statement = null;
    	try {
    		statement = conn.createStatement();
			rs=statement.executeQuery(sql);
			if(rs.next())
				discount = rs.getFloat("discount");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			statement.close();
			rs.close();
		}
    	return discount;
    }
    
    //��ȡ�����һ��Ϣ
    public boolean getPromotion(String barcode) throws Exception
    {
    	boolean promotion = false;
    	String sql = "SELECT * FROM promotion_commodity WHERE barcode=\'" + barcode + "\'";
    	ResultSet rs = null;
    	Statement statement = null;
    	try {
    		statement = conn.createStatement();
			rs=statement.executeQuery(sql);
			if(rs.next())
				promotion = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			statement.close();
			rs.close();
		}
    	return promotion;
    }
    
    
    
    @Override
    public void finalize() throws SQLException{           
        //super.finalize();  
    	disconnect();
    }
}
