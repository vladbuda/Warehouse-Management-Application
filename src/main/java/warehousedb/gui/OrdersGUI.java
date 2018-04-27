package warehousedb.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import warehousedb.business.Manager;
import warehousedb.business.OrderBLL;
import warehousedb.model.Client;
import warehousedb.model.Order;

/**Class responsible for creating the graphical user interface for the products table
 * @author Vlad-Cristian Buda
 * @version 1.0
 */
public class OrdersGUI 
{
	private JFrame orders;
	private JTable ordersTable;
	private Object[][] data;
	private String[] header;
	private DefaultTableModel model;
	private JScrollPane ordersScroll;
	private JTextField clientIdField, productIdField, quantityField;
	private JButton insert;
	private int clientid, productid, quantity;
	private ArrayList<String[]> items;

	
	OrderBLL orderBLL;
	/**Instantiates all the graphic components related to the products table
	 * 
	 */
	public OrdersGUI()
	{
		orderBLL = new OrderBLL();
		
		orders = new JFrame("Orders");
		
		clientIdField = new JTextField("Client ID");
		productIdField = new JTextField("Product ID");
		quantityField = new JTextField("Quantity");
		
		insert = new JButton("Place Order");
		
		header = new String[5];
		data = new Object[0][0];
		initializeHeader();
		model = new DefaultTableModel(data,header);
		
		orders.setSize(600, 500);
		orders.setResizable(false);
	
		ordersTable = new JTable(model);
		
		ordersTable.setCellSelectionEnabled(false);
		ordersTable.setColumnSelectionAllowed(false);
		
		ordersScroll = new JScrollPane();		
		ordersScroll.setBounds(50, 150, 500, 275);
		ordersScroll.setViewportView(ordersTable);
		
		clientIdField.setBounds(50, 30, 133, 30);
		productIdField.setBounds(233,30,133,30);
		quantityField.setBounds(416,30,134,30);
		
		clientIdField.addMouseListener(new MouseHandler());
		productIdField.addMouseListener(new MouseHandler());
		quantityField.addMouseListener(new MouseHandler());
		
		insert.setBounds(200, 90, 200, 30);
		
		insert.setFocusable(false);
		
		insert.addActionListener(new ActionHandler());
		
		orders.add(clientIdField);
		orders.add(productIdField);
		orders.add(quantityField);
		
		orders.add(insert);
		
		orders.add(ordersScroll);
		orders.setLayout(null);
		orders.setVisible(true);
		orders.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		showItems();
	}
	
	/**
	 * This method displays all the entries in an SQL table in its corresponding JTable
	 */
	private void showItems()
	{
		model.setRowCount(0);
		List<Order> list = orderBLL.findAll();
		items = Manager.getData(list);
		for(String[] s : items)
			model.addRow(s);
	}
	
	/**This method creates the header of the JTable using the reflection technique
	 * 
	 */
	private void initializeHeader()
	{
		Field[] fields = Order.class.getDeclaredFields();
		int i = 0;
		for(Field f : fields)
		{
			header[i] = f.getName();
			i++;
		}	
	}
	
	/**This class implements the actionListener interface for handling action events
	 * @author Vlad-Cristian Buda
	 *
	 */
	private class ActionHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == insert)
			{
				if(clientIdField.getText().length() == 0 || productIdField.getText().length() == 0  || quantityField.getText().length() == 0 )
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						clientid = Integer.parseInt(clientIdField.getText());
						productid = Integer.parseInt(productIdField.getText());
						quantity = Integer.parseInt(quantityField.getText());
						if(quantity < 0) throw new IllegalArgumentException();
						
						int report = orderBLL.insert(new Order(clientid,productid,quantity));
						
						if(report == - 1) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The client does not exist in the database" ), "Error", JOptionPane.ERROR_MESSAGE, null);
						else if (report == -2) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The product does not exist in the database" ), "Error", JOptionPane.ERROR_MESSAGE, null);
						else if (report == -3)
							JOptionPane.showMessageDialog(null, String.format("%s", "Insufficient stock" ), "Error", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							JOptionPane.showMessageDialog(null, String.format("%s", "Order successfully placed!" ), "Success", JOptionPane.INFORMATION_MESSAGE, null);
							showItems();
							clientIdField.setText("Client ID");
							productIdField.setText("Product ID");
							quantityField.setText("Quantity");
						}
						
					}
					catch(Exception exception)
					{
						exception.printStackTrace();
						//if(exception.getClass().getSimpleName().equals("IllegalArgumentException")) JOptionPane.showMessageDialog(null, String.format("%s","Quantity cannot be negative" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
						//else JOptionPane.showMessageDialog(null, String.format("%s","Client ID, Product ID and quantity should be integers" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
					}
				}
			}
		}
		
	}
	/**This class implements the mouseListener interface for handling mouse events
	 * @author Vlad-Cristian Buda
	 *
	 */
	private class MouseHandler implements MouseListener
	{

		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == clientIdField) clientIdField.setText(null);
			else if(e.getSource() == productIdField) productIdField.setText(null);
			else if(e.getSource() == quantityField) quantityField.setText(null);
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
