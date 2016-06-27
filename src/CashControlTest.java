package test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import Domain.Commodity;
import Domain.Bill;
import control.DBControl;

/**
 * 第一次迭代：测试
 * cash control 收银控制测试类
 * 2016-6-24 16:12:49
 *	@author maleiber_passL
 *	@see CashControl
 *	@param cc 收银控制对象
 *	@param testCommodity 用于测试的测试商品对象数组
 * 
 *	需要继续写
 *	向账单加入商品时，使用的参数是商品对象
 *	最主要的测试是testGetSumList
 *	由于CashControl的账单对象是私有的，所以需要加一个函数hasBill返回当前是否有账单
 *
 *	需要完善：
 *	testCommodity的内容
 *	testPriSumUp方法的except
 *	testSumUp的sumExcept和saveExcept
 *	testGetSumList的exceptSumList
 *
 *	CashControl增加hasBill方法返回boolean标示是否有账单正处理
 *	可以CashControl增加支持Commodity作为参数传递的addCommodity方法
 */
public class CashControlTest {

	
	CashControl cc=null;	//收银控制类
	
	
	Commodity[] testCommodity={1,2,3,4};	
	//1,2 是不打折的商品
	//3,4 是打折商品
	
	@Before
	public void setUp() throws Exception {
		try {
			cc = new CashControl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNewCashControl()
	{
		try {
			cc = new CashControl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotEquals(cc,null);
	}
	
	@Test
	public void testNewBill() {
		
		boolean actualStatus=false;
		try {
			actualStatus=cc.newBill();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("new bill error");
		}
		assertNotEquals(cc,false);
	}
	
	@Test
	public void testDelBill(){
		
		if(!cc.hasBill()){cc.newBill();}
		
		try {
			cc.delBill();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("cancel bill error");
		}
		assertNotEquals(cc.hasBill(),true);
		
	}
	
	@Test
	public void testPriSumUp(){	//商品小结测试
		
		//given
		//Commodity[] testCommodity={1,2};	//1 is nodiscount commodity 变成成员变量
											//2 is discount commodity
		float[] except={1,2,3,4};	//期望的小结测试数据
								//1 is no discount sum up,
								//2 is dis count sum up
								//3 is several no discount sum up
								//4 is several discount sum up
		
		
		//when
		cc.newBill();	//先建订单
		
		cc.addCommodity(testCommodity[0]);	//加入现成商品类
	
		
		//then 判断商品小结数值
		if(Math.abs(cc.b.priPriceList.get(0)-except[0]) >0.01)
			fails("不打折商品小结计算错误");
		
	
		
		//when
		cc.addCommodity(testCommodity[2]);
		
				
		//then
		if(Math.abs(cc.b.priPriceList.get(1)-except[1]) >0.01)
			fails("打扎商品小结计算错误");
		
		//after 2 test del the bill
		cc.delBill();
				
		//when
		cc.newBill();	//new bill test the case several
		
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		
		
		//then
		if(Math.abs(cc.b.priPriceList.get(0)-except[2]) >0.01)
			fails("多个不打折商品小结计算错误");

		//when
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
	
				
		//then
		if(Math.abs(cc.b.priPriceList.get(1)-except[3]) >0.01)
			fails("多个打折商品小结计算错误");
			
		cc.delBill();
		

	}
	
	@Test
	public void testSumUp(){	//类似商品小结测试过程
		//given
		float[] sumExcept={1,2,3,4,5};	//1  是不打折一个商品结算的总价格,
									//2  是打折一个商品结算的价格
									//3 是几种,几个 不打折商品结算的价格
									//4 是几种，几个打折商品结算的价格
									//5 是几种，几个打折和不打折都有的商品的结算价格
		float[] saveExcept={1,2,3,4,5};	//优惠总价与结算总价类似
		
		//when 
		cc.newBill();	//new a bill first
		cc.addCommodity(testCommodity[0]);
		
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[0]) > 0.01)
			fails("不打折一个商品结算的总价格错误");
		if(Math.abs(cc.b.discountPrice-saveExcept[0] > 0.01))
			fails("不打折一个商品结算的总优惠价格错误");
		
		cc.delBill();
		
		//when 
		cc.newBill();	
		cc.addCommodity(testCommodity[2]);
	
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[1]) > 0.01)
			fails("打折一个商品结算的总价格错误");
		if(Math.abs(cc.b.discountPrice-saveExcept[1] > 0.01))
			fails("打折一个商品结算的总优惠价格错误");
		
		cc.delBill();
		
		//when 
		cc.newBill();	
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[1]);
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[1]);
		
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[2]) > 0.01)
			fails("几种,几个 不打折商品结算的价格错误");
		if(Math.abs(cc.b.discountPrice-saveExcept[2] > 0.01))
			fails("几种,几个 不打折商品优惠的价格错误");
			
		cc.delBill();
		
		//when 
		cc.newBill();	
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[3]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[3]);
		
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[3]) > 0.01)
			fails("几种,几个 打折商品结算的价格错误");
		if(Math.abs(cc.b.discountPrice-saveExcept[3] > 0.01))
			fails("几种,几个 打折商品优惠的价格错误");
		
		cc.delBill();
		
		//when 
		cc.newBill();	
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[1]);
		cc.addCommodity(testCommodity[1]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[3]);
		cc.addCommodity(testCommodity[3]);
		
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[4]) > 0.01)
			fails("几种，几个打折和不打折都有的商品的结算价格错误");
		if(Math.abs(cc.b.discountPrice-saveExcept[4] > 0.01))
			fails("几种，几个打折和不打折都有的商品的优惠价格错误");
		
		cc.delBill();
		
		
	}
	
	@Test
	public void testAddCommodity(){	//增加商品的测试
		
		
		//when
		cc.newBill();
		cc.addCommodity(testCommodity[0].barcode);
		cc.addCommodity(testCommodity[1].barcode);
		cc.addCommodity(testCommodity[1].barcode);

		//then
		if(cc.b.shoppingList.size()==0)
			fails("一个商品也加不上错误");
		else if(cc.b.shoppingList.size()!=2)
			fails("加的商品种类数错误");
		
		if(cc.b.priNumList[0]!=1)
			fails("一种商品中加一个错误");
		
		if(cc.b.priNumList[1]!=2)
			fails("一种商品加几个错误");
		
	}
	
	@Test
	public void testGetSumList(){	//输出商品清单测试	这是第一次迭代最主要的功能测试	这是收银控制期望的功能
		//given
		
		String[] exceptSumList={1,2,3};	//1 is 一种不打折商品账单
										//2 is 一种打折商品账单
										//3 is 几种打折和不打折商品账单
		
		String actual;
		//when
		
		cc.newBill();
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		actual=cc.getSumList();
		//then
		if(!actual.equals(exceptSumList[0]))
			fails("一种不打折商品账单错误");
		cc.delBill();
		
		//when
		cc.newBill();
		cc.addCommodity(testCommodity[3]);
		cc.addCommodity(testCommodity[3]);
		cc.addCommodity(testCommodity[3]);
		actual=cc.getSumList();
		//then
		if(!actual.equals(exceptSumList[1]))
			fails("一种打折商品账单错误");
		cc.delBill();
		
		//when
		cc.newBill();
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[1]);
		cc.addCommodity(testCommodity[1]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
		actual=cc.getSumList();
		//then
		if(!actual.equals(exceptSumList[2]))
			fails("几种打折和不打折商品账单");
		cc.delBill();
		
	}
	
	
}
