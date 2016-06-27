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
			fail("�Ͽ����ݿ�ʧ��");
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
			fail("��Ʒ���ҳ���");
		}
	}

	@Test
	public void testSearchCommodityNotExist() {
		//testDBControl();
		Commodity comm = null;
		try {
			comm = c.searchCommodity("������");
		} catch (Exception e) {
			// TODO Auto-generated catch block	
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("��Ʒ������");
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
			fail("��Ʒ��Ϊ��");
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
			fail("�����ݿ�����ʧ��");
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
			fail("�����ݿ�����ʧ��");
		}
		if(Math.abs(discount - 0.8) < 0.01)
			fail("�ۿ۲���ȷ");
	}

	@Test
	public void testGetDiscountNotExist() {
		float discount = -1;
		try {
			discount = c.getDiscount("������");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			fail("���ݿ�����ʧ��");
		}
		if(Math.abs(discount - 1) < 0.01)
			fail("����Ʒ�ڱ��̵겻���ڴ��ۻ򱾵��޴���Ʒ");
	}
}
