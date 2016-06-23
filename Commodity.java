package Domain;

public class Commodity {
	private String name;	//name
	private String barcode;	//barcode shi tiao xing ma
	private String unit;	//is liang ci
	private float price;	// shou jia
	private float discount=1;	//zhe kou
	
	public void setName(String name){this.name=name;};
	public String getName(){return name;};
	public void setBarcode(String barcode){this.barcode=barcode;};
	public String getBarcode(){return barcode;};
	public void setUnit(String unit){this.unit=unit;};
	public String getUnit(){return unit;};
	public void setPrice(Float price){this.price=price;};
	public float getPrice(){return price;};
	public void setDiscount(Float discount){this.discount=discount;};
	public float getDiscount(){return discount;};
	
	@Override
	public String toString()
	{
		String s = "";
		s += "barcode: \'"+barcode+"\',";
		s += "name: \'"+name+"\',";
		s += "unit: \'"+unit+"\',";
		s += "price:\'"+Float.toString(price)+"\'";
        if(discount != 1)
        	s += "discount:\'"+Float.toString(discount)+"\'";
        return s;
	}
};
