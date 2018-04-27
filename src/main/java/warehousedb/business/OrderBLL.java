package warehousedb.business;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import warehousedb.dao.ClientDAO;
import warehousedb.dao.OrderDAO;
import warehousedb.dao.ProductDAO;
import warehousedb.gui.ProductsGUI;
import warehousedb.model.Client;
import warehousedb.model.Order;
import warehousedb.model.Product;

public class OrderBLL
{
	static OrderDAO orderDAO = new OrderDAO();
	static ClientDAO clientDAO = new ClientDAO();
	static ProductDAO productDAO = new ProductDAO();
	private static int orderNumber = OrderDAO.getOrderId() + 1;
	 
	 public int insert(Order order)
	 {
			Product p = null;
			Client c = null;
			if((c = clientDAO.findById(order.getClientId())) == null) return -1; //client not found
			else
			{
				if((p = productDAO.findById(order.getProductId())) == null) return -2; //product not found
				else
				{
					if(p.getStock() >= order.getQuantity())
					{
						p.setStock(p.getStock()-order.getQuantity());
						productDAO.update(p);
					//	ProductsGUI.showItems();
						order.setId(orderNumber);
						order.setTotal(p.getPrice()*order.getQuantity());
						orderNumber++;
						orderDAO.insert(order);
						emitReceipt(c,p,order);
						return 1;
					}
					else return -3; //insufficient stock
				}
			}
	 }
	 
	 public List<Order> findAll()
	 {
		 return orderDAO.findAll();
	 }
	 
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
