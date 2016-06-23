package control;

import java.util.Vector;

import Domain.Bill;
import Domain.Commodity;

public class CashControl extends DBControl
{
	private Bill b;	//账单
	//DBControl control;	
	//数据库控制类
	public CashControl() throws Exception
	{
		super();
		b = new Bill();
		//control = new DBControl();
	}
	
	public boolean newBill(){
		b=new Bill();
		return b==null?false:true;
	};
	
	public void cancelBill(){
		//cancel the bill 
		for(Commodity c:nowBill.shoppingList)
		{
			//a function can cancel by commodity
			cancelCommodity(c);
		}
		delBill();
	};
	
	public void delBill(){
		b.clear();
		del b;
		b=null;
	}
	
	private void priSumUp(){	//sum up all the pri sum up list
		nowBill.priSumList.clear();
		int i=0,j=nowBill.shoppingList.size();
		for(i=0;i<j;i++)
		{
			nowBill.priSumList.add(	  nowBill.shoppingList.index(i).getPrice() 
									* nowBill.shoppingList.index(i).getDiscount()
									* nowBill.QuantityList.index(i));
		}
	//	for(Commodity c:nowBill.shoppingList)
	//		nowBill.priSumList.add(c.getPrice() *c.getDiscount());
		
		
	};
	private void priSumUp(Commodity c,Vector<Float> priPriceList,boolean isAdd,int index){	//sum up the pri sum up list as new in bill
		if(isAdd){
			float price = priPriceList.get(index).floatValue();
			priPriceList.remove(index);
			priPriceList.insertElementAt(new Float(price+c.getPrice()*c.getDiscount()), index);
			//sumPrice += c.getPrice()*c.getDiscount();
			//discountPrice += (1-c.getDiscount())*c.getPrice();
			
			
			nowBill.priSumList.add(	  tempCommodity.getPrice() 
								* tempCommodity.getDiscount()
								* tempNum);
		}
		else{
			priPriceList.addElement(new Float(temp.getPrice()*temp.getDiscount()));
			
			//sumPrice += temp.getPrice()*temp.getDiscount();
			//discountPrice += (1-temp.getDiscount())*temp.getPrice();
			//System.out.println(discountPrice);
		}
		
	};
	public void sumUp(	Vector<Commodity> shoppoingList,
						Vector<Integer> priNumList,
						Vector<Float> priPriceList){
		//sum up all money and discount in bill
		float sumPrice=0;
		for(float priSum:priNumList)
		{
			sumPrice+=priSum;
		}
		System.out.println(sumPrice);
		
		float discountPrice=0;
		int i=0;j=nowBill.shoppingList.size();
		for(i=0;i<j;i++)
		{
			discountPrice+=	  shoppingList.index(i).getPrice() 
							* (1- shoppingList.index(i).getDiscount())
							* priNumList.index(i);
		}
		System.out.println(discountPrice);
		b.setSumPrice(sumPrice);
		b.setDiscountPrice(discountPrice);
	};
		
	
	
	//添加商品，返回商品信息
	public String addCommodity(String barcode) throws Exception
	{
		//获取账单信息
		Vector<Commodity>	shoppoingList	= b.getShoppoingList();
		Vector<Integer> 	priNumList		= b.getPriNumList();
		Vector<Float> 		priPriceList	= b.getPriPriceList();
		float 				sumPrice 		= b.getSumPrice();
		float				discountPrice	= b.getDiscountPrice();
		
		Commodity temp = control.searchCommodity(barcode);	//数据库中查找相应商品
		
		//if success
		boolean isAdd = false;
		int index = 0;
		int num=1;
		for(Commodity c: shoppoingList)
		{
			if(c.getBarcode().equals(temp.getBarcode()))
			{
				//exist
				isAdd = true;
				num = priNumList.get(index).intValue();
				priNumList.remove(index);
				priNumList.insertElementAt(new Integer(++num), index);
				
				
				
				
				
				break;
			}
			index++;
		}
		if(!isAdd)
		{
			//not exist
			shoppoingList.addElement(temp);
			priNumList.addElement(new Integer(1));
			
			//priSumUp(tempCommodity);

		}
		
		priSumUp(temp,priPriceList,num,isAdd,index);
		sumUp(shoppoingList,priNumList,priPriceList);
		
		
		
		return temp.toString();
	}
	
	
	public void modifyBillInf(){
		//modify commodity number in bill
		
		//DB Stock changed
		
	};
	public String getSumList()
	{
		return b.toString();
	}
	
	public void end()
	{
		b.clear();
	}
}
