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
    // URL指向要访问的数据库名Shop_Stock
    private String url = "jdbc:mysql://127.0.0.1:3306/shop";
    // MySQL配置时的用户名
    private String user = "root"; 

    // MySQL配置时的密码
    private String password = "123456";
    
    private Connection conn = null;		//与商品的连接
    private Statement statement = null;	//
    
    public DBControl() throws Exception
    {
    	try{
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            conn = DriverManager.getConnection(url, user, password);

            // statement用来执行SQL语句
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
    
    //查找商品
    public Commodity searchCommodity(String barcode) throws Exception
    {
    	String name = "";	//名称
    	String unit = "";	//单元
    	float price = 0;	//价格
    	float discount = 1; 	//折扣
    	int num = 0;
    	
    	String sql = "SELECT * FROM stock WHERE barcode=\'"+barcode+"\'";
    	
    	ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);	//查询商品
			
			//在库存查找
			if(rs.next())
			{
				num = rs.getInt("num");
				if(num == 0)
					throw new Exception("库存不足");
				name = rs.getString("name");
				unit = rs.getString("unit");
				price = rs.getFloat("price");
			}
			else
				throw new Exception("未找到");	//未找到商品，抛出异常
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
    
    public void setNum(String barcode,int num) throws Exception	//添加数量	
    {
    	if(num < 0)
    		throw new Exception("库存数值不合法"); 
    	
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
