package test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import Domain.Commodity;
import control.DBControl;

public class DBControlTest {
	DBControl c = null;

	public DBControlTest()
	{
		try {
			c = new DBControl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void setUp() throws Exception {
		try {
			c = new DBControl();
			assertNotEquals(c,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDisconnect() {
		
		try {
			c.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("断开数据库失败");
		}
	}

	@Test
	public void testSearchCommodityNormal() {
		//testDBControl();
		Commodity comm = null;
		try {
			comm = c.searchCommodity("ITEM000000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("商品查找出错");
		}
	}

	@Test
	public void testSearchCommodityNotExist() {
		//testDBControl();
		Commodity comm = null;
		try {
			comm = c.searchCommodity("烫烫烫");
		} catch (Exception e) {
			// TODO Auto-generated catch block	
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("商品不存在");
		}
	}
	
	@Test
	public void testSearchCommodityEmpty() {
		//testDBControl();
		Commodity comm = null;
		try {
			comm = c.searchCommodity("");
		} catch (Exception e) {
			// TODO Auto-generated catch block	
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("商品码为空");
		}
	}
	
	@Test
	public void testSearchCommodityErrorParameter() {
		//testDBControl();
		Commodity comm = null;
		try {
			comm = c.searchCommodity(123);
		} catch (Exception e) {
			// TODO Auto-generated catch block	
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("与数据库连接失败");
		}
	}
	
	@Test
	public void testSetNum() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanGetDiscount() {
		float discount = 0;
		try {
			c.getDiscount("ITEM000000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("与数据库连接失败");
		}
		if(Math.abs(discount - 0.8) < 0.01)
			fail("折扣不正确");
	}

	@Test
	public void testGetDiscountNotExist() {
		float discount = -1;
		try {
			discount = c.getDiscount("烫烫烫");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("数据库连接失败");
		}
		if(Math.abs(discount - 1) < 0.01)
			fail("该商品在本商店不存在打折或本店无此商品");
	}
}
