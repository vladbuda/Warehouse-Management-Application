package warehousedb.model;

/**This class contains the information related to an order
 * @author Vlad-Cristian Buda
 *
 */
public class Order 
{
	private int id, clientId, productId, quantity, total;
	
	public Order()
	{
		
	}
	public Order(int clientId, int productId, int quantity)
	{
		this.clientId = clientId;
		this.productId = productId;
		this.quantity = quantity;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getClientId()
	{
		return clientId;
	}
	
	public int getProductId()
	{
		return productId;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	public int getTotal()
	{
		return this.total;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setClientId(int clientid)
	{
		this.clientId = clientid;
	}
	
	public void setProductId(int productid)
	{
		this.productId = productid;
	}
	
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	public void setTotal(int total)
	{
		this.total = total;
	}
}
