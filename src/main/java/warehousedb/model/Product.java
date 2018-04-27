package warehousedb.model;

/**This class contains the information related to a product
 * @author Vlad-Cristian Buda
 *
 */
public class Product 
{
	private int id;
	private String name;
	private int price, stock;
	
	public Product()
	{
		
	}
	public Product(int id, String name, int price, int stock)
	{
		this.id = id;
		this.price = price;
		this.name = name;
		this.stock = stock;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getStock()
	{
		return stock;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setPrice(int price)
	{
		this.price = price;
	}
	
	public void setStock(int stock)
	{
		this.stock = stock;
	}
}
