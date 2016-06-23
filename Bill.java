package Domain;

import java.util.Vector;

import control.DBControl;

public class Bill{	
	//shopping list store the commodity and number
	private Vector<Commodity> shoppoingList;
	//num list is xiao3 jie2 list 
	private Vector<Integer> priNumList;
	//price list is xiao3 jie2 list 
	private Vector<Float> priPriceList;
	//sum price
	public float sumPrice;
	
	public Bill() throws Exception
	{
		shoppoingList=new Vector<Commodity>();
		priNumList=new Vector<Integer>();
		priPriceList=new Vector<Float>();
		sumPrice = 0;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		s += "***�̵깺���嵥***\n";
		int index = 0;
		for(Commodity c: shoppoingList)
		{
			s += "���ƣ�"+c.getName()+"��������"+priNumList.get(index)+c.getUnit()+
				"�����ۣ�"+Float.toString(c.getPrice())+"(Ԫ)��С�ƣ�"+priPriceList.get(index)+"(Ԫ)\n";
			index++;
		}
		s += "----------------------\n";
		s += "�ܼƣ�"+Float.toString(sumPrice)+"(Ԫ)\n";
		s += "**********************\n";
		return s;
	}
	
	public Vector<Commodity> getShoppoingList()
	{
		return shoppoingList;
	}
	public Vector<Integer> getPriNumList()
	{
		return priNumList;
	}
	public Vector<Float> getPriPriceList()
	{
		return priPriceList;
	}
	public void setSumPrice(float sum)
	{
		sumPrice = sum;
	}
	public float getSumPrice()
	{
		return sumPrice;
	}
	public void clear()
	{
		shoppoingList.clear();
		priNumList.clear();
		priPriceList.clear();
		sumPrice = 0;
	}
};

