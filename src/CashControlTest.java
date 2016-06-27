package test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import Domain.Commodity;
import Domain.Bill;
import control.DBControl;

/**
 * ��һ�ε���������
 * cash control �������Ʋ�����
 * 2016-6-24 16:12:49
 *	@author maleiber_passL
 *	@see CashControl
 *	@param cc �������ƶ���
 *	@param testCommodity ���ڲ��ԵĲ�����Ʒ��������
 * 
 *	��Ҫ����д
 *	���˵�������Ʒʱ��ʹ�õĲ�������Ʒ����
 *	����Ҫ�Ĳ�����testGetSumList
 *	����CashControl���˵�������˽�еģ�������Ҫ��һ������hasBill���ص�ǰ�Ƿ����˵�
 *
 *	��Ҫ���ƣ�
 *	testCommodity������
 *	testPriSumUp������except
 *	testSumUp��sumExcept��saveExcept
 *	testGetSumList��exceptSumList
 *
 *	CashControl����hasBill��������boolean��ʾ�Ƿ����˵�������
 *	����CashControl����֧��Commodity��Ϊ�������ݵ�addCommodity����
 */
public class CashControlTest {

	
	CashControl cc=null;	//����������
	
	
	Commodity[] testCommodity={1,2,3,4};	
	//1,2 �ǲ����۵���Ʒ
	//3,4 �Ǵ�����Ʒ
	
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
	public void testPriSumUp(){	//��ƷС�����
		
		//given
		//Commodity[] testCommodity={1,2};	//1 is nodiscount commodity ��ɳ�Ա����
											//2 is discount commodity
		float[] except={1,2,3,4};	//������С���������
								//1 is no discount sum up,
								//2 is dis count sum up
								//3 is several no discount sum up
								//4 is several discount sum up
		
		
		//when
		cc.newBill();	//�Ƚ�����
		
		cc.addCommodity(testCommodity[0]);	//�����ֳ���Ʒ��
	
		
		//then �ж���ƷС����ֵ
		if(Math.abs(cc.b.priPriceList.get(0)-except[0]) >0.01)
			fails("��������ƷС��������");
		
	
		
		//when
		cc.addCommodity(testCommodity[2]);
		
				
		//then
		if(Math.abs(cc.b.priPriceList.get(1)-except[1]) >0.01)
			fails("������ƷС��������");
		
		//after 2 test del the bill
		cc.delBill();
				
		//when
		cc.newBill();	//new bill test the case several
		
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		
		
		//then
		if(Math.abs(cc.b.priPriceList.get(0)-except[2]) >0.01)
			fails("�����������ƷС��������");

		//when
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
		cc.addCommodity(testCommodity[2]);
	
				
		//then
		if(Math.abs(cc.b.priPriceList.get(1)-except[3]) >0.01)
			fails("���������ƷС��������");
			
		cc.delBill();
		

	}
	
	@Test
	public void testSumUp(){	//������ƷС����Թ���
		//given
		float[] sumExcept={1,2,3,4,5};	//1  �ǲ�����һ����Ʒ������ܼ۸�,
									//2  �Ǵ���һ����Ʒ����ļ۸�
									//3 �Ǽ���,���� ��������Ʒ����ļ۸�
									//4 �Ǽ��֣�����������Ʒ����ļ۸�
									//5 �Ǽ��֣��������ۺͲ����۶��е���Ʒ�Ľ���۸�
		float[] saveExcept={1,2,3,4,5};	//�Ż��ܼ�������ܼ�����
		
		//when 
		cc.newBill();	//new a bill first
		cc.addCommodity(testCommodity[0]);
		
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[0]) > 0.01)
			fails("������һ����Ʒ������ܼ۸����");
		if(Math.abs(cc.b.discountPrice-saveExcept[0] > 0.01))
			fails("������һ����Ʒ��������Żݼ۸����");
		
		cc.delBill();
		
		//when 
		cc.newBill();	
		cc.addCommodity(testCommodity[2]);
	
		//then
		if(Math.abs(cc.b.sumPrice-sumExcept[1]) > 0.01)
			fails("����һ����Ʒ������ܼ۸����");
		if(Math.abs(cc.b.discountPrice-saveExcept[1] > 0.01))
			fails("����һ����Ʒ��������Żݼ۸����");
		
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
			fails("����,���� ��������Ʒ����ļ۸����");
		if(Math.abs(cc.b.discountPrice-saveExcept[2] > 0.01))
			fails("����,���� ��������Ʒ�Żݵļ۸����");
			
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
			fails("����,���� ������Ʒ����ļ۸����");
		if(Math.abs(cc.b.discountPrice-saveExcept[3] > 0.01))
			fails("����,���� ������Ʒ�Żݵļ۸����");
		
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
			fails("���֣��������ۺͲ����۶��е���Ʒ�Ľ���۸����");
		if(Math.abs(cc.b.discountPrice-saveExcept[4] > 0.01))
			fails("���֣��������ۺͲ����۶��е���Ʒ���Żݼ۸����");
		
		cc.delBill();
		
		
	}
	
	@Test
	public void testAddCommodity(){	//������Ʒ�Ĳ���
		
		
		//when
		cc.newBill();
		cc.addCommodity(testCommodity[0].barcode);
		cc.addCommodity(testCommodity[1].barcode);
		cc.addCommodity(testCommodity[1].barcode);

		//then
		if(cc.b.shoppingList.size()==0)
			fails("һ����ƷҲ�Ӳ��ϴ���");
		else if(cc.b.shoppingList.size()!=2)
			fails("�ӵ���Ʒ����������");
		
		if(cc.b.priNumList[0]!=1)
			fails("һ����Ʒ�м�һ������");
		
		if(cc.b.priNumList[1]!=2)
			fails("һ����Ʒ�Ӽ�������");
		
	}
	
	@Test
	public void testGetSumList(){	//�����Ʒ�嵥����	���ǵ�һ�ε�������Ҫ�Ĺ��ܲ���	�����������������Ĺ���
		//given
		
		String[] exceptSumList={1,2,3};	//1 is һ�ֲ�������Ʒ�˵�
										//2 is һ�ִ�����Ʒ�˵�
										//3 is ���ִ��ۺͲ�������Ʒ�˵�
		
		String actual;
		//when
		
		cc.newBill();
		cc.addCommodity(testCommodity[0]);
		cc.addCommodity(testCommodity[0]);
		actual=cc.getSumList();
		//then
		if(!actual.equals(exceptSumList[0]))
			fails("һ�ֲ�������Ʒ�˵�����");
		cc.delBill();
		
		//when
		cc.newBill();
		cc.addCommodity(testCommodity[3]);
		cc.addCommodity(testCommodity[3]);
		cc.addCommodity(testCommodity[3]);
		actual=cc.getSumList();
		//then
		if(!actual.equals(exceptSumList[1]))
			fails("һ�ִ�����Ʒ�˵�����");
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
			fails("���ִ��ۺͲ�������Ʒ�˵�");
		cc.delBill();
		
	}
	
	
}
