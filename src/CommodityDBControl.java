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
    // URL指向要访问的数据库名Shop_Stock
    private String url = "jdbc:mysql://127.0.0.1:3306/shop";
    // MySQL配置时的用户名
    private String user = "root"; 

    // MySQL配置时的密码
    private String password = "123456";
    
    private Connection conn = null;		//与商品的连接
    
    public CommodityDBControl() throws Exception
    {
    	try{
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            conn = DriverManager.getConnection(url, user, password);
            // statement用来执行SQL语句
    	} catch (Exception e) {
    		throw e;
    	}
    }
    //断开数据库连接
    public void disconnect() throws SQLException
    {
    	conn.close();
    }
    
    //按商品码查找商品
	public Vector<Commodity> searchCommodityByBarcode(final String barcode) throws Exception
    {
		String complete_barcode = "";	//完整的商品码
    	String name = "";	//名称
    	String unit = "";	//单元
    	float price = 0;	//价格
    	float discount = 1; 	//折扣
    	boolean promotion = false;	//是否买二赠一
    	Vector<Commodity> result = new Vector<Commodity>();	//搜索结果
    	
    	String sql = "SELECT barcode,name,unit,price FROM stock WHERE barcode LIKE \'%"+barcode+"%\'";
    	
    	ResultSet rs = null;
    	Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);	//查询商品
			
			//在库存查找
			while(rs.next())
			{
				//根据搜索结果创建商品
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
    
    //按名字查找商品
	public Vector<Commodity> searchCommodityByName(String name) throws Exception
    {
		String barcode = "";	//商品码
    	String complete_name = "";	//名称
    	String unit = "";	//单元
    	float price = 0;	//价格
    	float discount = 1; 	//折扣
    	boolean promotion = false;	//是否买二赠一
    	Vector<Commodity> result = new Vector<Commodity>();	//查询结果
    	
    	String sql = "SELECT barcode,name,unit,price FROM stock WHERE name LIKE \'%"+name+"%\'";
    	
    	ResultSet rs = null;
    	Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);	//查询商品
			
			//在库存查找
			while(rs.next())
			{
				//根据搜索结果创建商品
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
	
	//账单商品数量改变后，改变库存中的商品数量，num为改变的数量，正为增加，负为减少
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
				throw new Exception("商品码错误！");
			if(stock_num < 0)
				throw new Exception("库存数量不合法！");
			else if(stock_num+num < 0)
				throw new Exception("库存不足！");
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
	
	//设置商品数量
    public void setNum(String barcode,int num) throws Exception		
    {
    	if(num < 0)
    		throw new Exception("库存数值不合法！请联系相关技术负责人。"); 
    	Statement statement = null;
		try {
			String sql = "UPDATE stock SET num="+Integer.toString(num)+" WHERE barcode=\'"+barcode+"\'";
			statement = conn.createStatement();
			if(statement.executeUpdate(sql) == 0)
				throw new Exception("商品码错误！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			statement.close();
		}
    }
    
    //获取打折信息
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
    
    //获取买二赠一信息
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
