package warehousedb.model;

/**This class contains the information related to a client
 * @author Vlad-Cristian Buda
 *
 */
public class Client 
{
	private int id;
	private String name, address, email;
	
	public Client()
	{
		
	}
	
	public Client(int id, String name, String address, String email)
	{
		this.id = id;
		this.name = name;
		this.address = address;
		this.email = email;
	}
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String toString()
	{
		return String.format("%d %s %s %s", id,name,address,email);
	}
	
}
