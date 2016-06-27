package test;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Domain.Commodity;
import Domain.Bill;
import control.CommodityDBControl;

public class CommodityDBControlTest {

	CommodityDBControl cdbc=null;
	String[] CommodityBarcode={1,2,3,4};	//1,2,3 is exist commodity 4 is not exist
	
	String[] CommodityName={1,2,3,4};		//1,2 is exist name, 3 is relative name,4 is not exist name
	
	@Before
	public void setUp() throws Exception{
		try {
			cdbc = new CommodityDBControl();
			assertNotEquals(cdbc,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearchCommodityByBarcode(){
		Vector<Commodity> result=null;
		String testStatus;
		//when
		testStatus=" when searching 1 normal Commodity by Barcode";
		result=cdbc.searchCommodityByBarcode(CommodityBarcode[0]);
		//then
		if(result==null)fail("cannot finish search"+testStatus);
		else if(result.size()==0)fail("cannot search this Commodity"+testStatus);
		else if(result.size()!=1)fail("search several Commodity error"+testStatus);
		else if(result.get(0).barcode!=CommodityBarcode[0])fail("search wrong Commodity"+testStatus);
		
		result.clear();
		
		//when
		testStatus=" when searching 1 not exist Commodity by Barcode";
		result=cdbc.searchCommodityByBarcode(CommodityBarcode[3]);
		//then
		if(result==null)fail("cannot finish search"+testStatus);
		else if(result.size()!=0)fail("search Commodity"+testStatus);	
		result.clear();
		
		
	}
	
	@Test
	public void testSearchCommodityByName(){
		Vector<Commodity> result=null;
		String testStatus;
		
		//when
		testStatus=" when searching 1 normal Commodity 1 by name";
		result=cdbc.searchCommodityByName(CommodityName[0]);
		//then
		if(result==null)fail("cannot finish search"+testStatus);
		else if(result.size()==0)fail(" cannot search Commodity"+testStatus);
		result.clear();
		
		//when
		testStatus=" when searching 1 normal Commodity 2 by name";
		result=cdbc.searchCommodityByName(CommodityName[1]);
		//then
		if(result==null)fail("cannot finish search"+testStatus);
		else if(result.size()==0)fail(" cannot search Commodity"+testStatus);	
		result.clear();
		
		//when
		testStatus=" when searching relative Commodity by name";
		result=cdbc.searchCommodityByName(CommodityName[2]);
		//then
		if(result==null)fail("cannot finish search"+testStatus);
		else if(result.size()==0)fail(" cannot search Commodity"+testStatus);	
		result.clear();
		
		//when 
		testStatus=" when searching not exist Commodity by name";
		result=cdbc.searchCommodityByName(CommodityName[3]);
		//then
		if(result==null)fail("cannot finish search"+testStatus);
		else if(result.size()!=0)fail("searched Commodity"+testStatus);
		result.clear();
	}
	
	@Test
	public void testSetNum(){
		ResultSet rs = null;
		Statement statement = null;
		int stock_num;
		
		//when
		
		//then
	}
	
	@Tset
	public void testChangeCommodityNum(){
		
	}
	
	@Test
	public void testGetPromotion(){
		
	}
	
	
	
}
