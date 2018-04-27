package warehousedb.gui;

/**This class contains the graphical user interfaces for each table in the MYSQL database.
 * @author Vlad-Cristian Buda
 *
 */
public class GUI 
{
	private ClientsGUI clients;
	private ProductsGUI products;
	private OrdersGUI orders;
	
	public GUI()
	{
		clients = new ClientsGUI();
		products = new ProductsGUI();
		orders = new OrdersGUI();
	}
}
