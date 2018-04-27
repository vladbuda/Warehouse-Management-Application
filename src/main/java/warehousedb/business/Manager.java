package warehousedb.business;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import warehousedb.dao.ClientDAO;
import warehousedb.dao.OrderDAO;
import warehousedb.dao.ProductDAO;
import warehousedb.gui.ProductsGUI;
import warehousedb.model.Client;
import warehousedb.model.Order;
import warehousedb.model.Product;

/**This class implements the application logic for performing SQL operations on a database
 * @author Vlad-Cristian Buda
 * @version 1.0
 */
public class Manager 
{
	
	/**
	 * object of type ClientDAO used for performing operations on Client table 
	 */
	private static ClientDAO clientDAO = new ClientDAO();
	/**
	 * object of type ProductDAO used for performing operations on Product table 
	 */
	private static ProductDAO productDAO = new ProductDAO();
	/**
	 * object of type ProductDAO used for performing operations on Order table 
	 */
	private static OrderDAO orderDAO = new OrderDAO();
	/**
	 * integer value used for generating order id
	 */
	private static int orderNumber = OrderDAO.getOrderId() + 1;
	
	
	/**This method insert an object in the corresponding database table
	 * @param object the object to be inserted
	 * @return returns an integer value specifying if the operation was successful (1) or not (equal or less than 0)
	 */
	public static int insert(Object object)
	{
		if (object.getClass().getSimpleName().equals("Client"))
		{
			Client c = (Client) object;
			if (clientDAO.insert(c) == true) return 1;
		}
		else if (object.getClass().getSimpleName().equals("Product"))
		{
			Product p = (Product) object;
			if (productDAO.insert(p) == true) return 1;
		}
		else if (object.getClass().getSimpleName().equals("Order"))
		{
			Order o = (Order) object;
			Product p = null;
			Client c = null;
			if((c = clientDAO.findById(o.getClientId())) == null) return -1; //client not found
			else
			{
				if((p = productDAO.findById(o.getProductId())) == null) return -2; //product not found
				else
				{
					if(p.getStock() >= o.getQuantity())
					{
						p.setStock(p.getStock()-o.getQuantity());
						productDAO.update(p);
						ProductsGUI.showItems();
						o.setId(orderNumber);
						o.setTotal(p.getPrice()*o.getQuantity());
						orderNumber++;
						orderDAO.insert(o);
						emitReceipt(c,p,o);
						return 1;
					}
					else return -3; //insufficient stock
				}
			}
		}
		return 0;
	}
	
	/**This method updates an object in the corresponding database table
	 * @param object the object to be updated
	 * @return returns true if the operation was successful or false otherwise
	 */
	public static boolean edit(Object object)
	{
		if (object.getClass().getSimpleName().equals("Client"))
		{
			Client c = (Client) object;
			return clientDAO.update(c);
		}
		else if (object.getClass().getSimpleName().equals("Product"))
		{
			Product p = (Product) object;
			return productDAO.update(p);
		}
		return false;
	}
	
	/**This method deletes the specified object in the corresponding database table
	 * @param object the object to be deleted
	 * @return returns true if the operation was successful or false otherwise
	 */
	public static boolean delete(Object object)
	{
		if (object.getClass().getSimpleName().equals("Client"))
		{
			Client c = (Client) object;
			return clientDAO.delete(c);
		}
		else if (object.getClass().getSimpleName().equals("Product"))
		{
			Product p = (Product) object;
			return productDAO.delete(p);
		}
		return false;
	}
	
	/**This method obtains a string representation of all the entries in the corresponding table
	 * @param c the class corresponding to the table in the database
 	 * @return returns an ArrayList of strings containing all the entries in the table
	 */
	public static ArrayList<String[]> getData(Class c) 
	{
		ArrayList<String[]> data = new ArrayList<String[]>();
		if (c.getSimpleName().equals("Client")) 
		{
			List<Client> list = clientDAO.findAll();
			if(list != null)
			{
				for(Client client : list)
				{
					data.add(new String[] {""+client.getId(), ""+client.getName(), ""+client.getAddress(),""+client.getEmail()});
				}
			}
		}
		else if (c.getSimpleName().equals("Product"))
		{
			List<Product> list = productDAO.findAll();
			if(list != null)
			{
				for(Product product : list)
				{
					data.add(new String[] {""+product.getId(), ""+product.getName(), ""+product.getPrice(),""+product.getStock()});
				}
			}
		}
		else if (c.getSimpleName().equals("Order")) 
		{
			List<Order> list = orderDAO.findAll();
			if(list != null)
			{
				for(Order order : list)
				{
					data.add(new String[] {""+order.getId(), ""+order.getClientId(), ""+order.getProductId(),""+order.getQuantity(),""+order.getTotal()});
				}
			}
		}
		return data;
	}
	
	/**This method creates a .txt file representing the receipt for the current placed order
	 * @param c the client who placed the order
	 * @param p the ordered product
	 * @param o the order object
	 */
	private static void emitReceipt(Client c, Product p, Order o)
	{
		String name = "Order "+orderNumber+".txt";
		PrintWriter writer = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		try 
		{
			writer = new PrintWriter(name, "UTF-8");
			writer.println("Order number: "+orderNumber);
			writer.println("Data: " +dateFormat.format(date));
			writer.println("Client: "+c.getName());
			writer.println("Address: "+c.getAddress());
			writer.println("Contact: "+c.getEmail());
			writer.println("Product: "+p.getName());
			writer.println("Price: $"+p.getPrice());
			writer.println("Quantity: "+o.getQuantity());
			writer.println("Subtotal: $"+o.getTotal());
			writer.println("Sales Tax (19%): $"+(float)(o.getTotal()*19)/100);
			writer.println("Total: $"+(float)(o.getTotal() + (float)(o.getTotal()*19)/100));
			writer.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	}
}
