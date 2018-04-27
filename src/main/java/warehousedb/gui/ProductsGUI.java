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
import warehousedb.business.ProductBLL;
import warehousedb.model.Order;
import warehousedb.model.Product;

/**Class responsible for creating the graphical user interface for the products table
 * @author Vlad-Cristian Buda
 * @version 1.0
 */
public class ProductsGUI 
{
	private JFrame products;
	private JTable productsTable;
	private Object[][] data;
	private String[] header;
	private static DefaultTableModel model;
	private JScrollPane productsScroll;
	private JTextField idField, nameField, priceField, stockField;
	private JButton insert, update, delete;
	private int id, price, stock;
	private String name;
	private static ArrayList<String[]> items;
	ProductBLL productBLL;
	/**Instantiates all the graphic components related to the products table
	 * 
	 */
	public ProductsGUI()
	{
		productBLL = new ProductBLL();
		products = new JFrame("Products");
		
		idField = new JTextField("ID");
		nameField = new JTextField("Name");
		priceField = new JTextField("Price");
		stockField = new JTextField("Stock");
		
		insert = new JButton("Insert");
		update = new JButton("Update");
		delete = new JButton("Delete");
		
		header = new String[4];
		data = new Object[0][0];
		initializeHeader();
		model = new DefaultTableModel(data,header);
		
		products.setSize(600, 500);
		products.setResizable(false);
	
		productsTable = new JTable(model);
		
		productsTable.setCellSelectionEnabled(false);
		productsTable.setColumnSelectionAllowed(false);
		
		productsScroll = new JScrollPane();		
		productsScroll.setBounds(50, 150, 500, 275);
		productsScroll.setViewportView(productsTable);
		
		idField.setBounds(50, 30, 40, 30);
		nameField.setBounds(110,30,133,30);
		priceField.setBounds(263, 30, 133, 30);
		stockField.setBounds(417, 30, 133, 30);
		
		idField.addMouseListener(new MouseHandler());
		nameField.addMouseListener(new MouseHandler());
		priceField.addMouseListener(new MouseHandler());
		stockField.addMouseListener(new MouseHandler());
		
		insert.setBounds(50, 90, 100, 30);
		update.setBounds(250, 90, 100, 30);
		delete.setBounds(450, 90, 100, 30);
		
		insert.setFocusable(false);
		update.setFocusable(false);
		delete.setFocusable(false);
		
		insert.addActionListener(new ActionHandler());
		update.addActionListener(new ActionHandler());
		delete.addActionListener(new ActionHandler());
		
		products.add(idField);
		products.add(nameField);
		products.add(priceField);
		products.add(stockField);
		
		products.add(insert);
		products.add(update);
		products.add(delete);
		
		products.add(productsScroll);
		products.setLayout(null);
		products.setVisible(true);
		products.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		showItems();
	}
	
	/**
	 * This method displays all the entries in an SQL table in its corresponding JTable
	 */
	public void showItems()
	{
		model.setRowCount(0);
		List<Product> list = productBLL.findAll();
		items = Manager.getData(list);
		for(String[] s : items)
			model.addRow(s);
	}
	
	/**This method creates the header of the JTable using the reflection technique
	 * 
	 */
	private void initializeHeader()
	{
		Field[] fields = Product.class.getDeclaredFields();
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
				if(idField.getText().length() == 0 || nameField.getText().length() == 0 || priceField.getText().length() == 0 )
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						id = Integer.parseInt(idField.getText());
						name = nameField.getText();
						price = Integer.parseInt(priceField.getText());
						stock = Integer.parseInt(stockField.getText());
						
						
						if(productBLL.insert(new Product(id,name,price,stock)) == 0) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The product already exists in the database" ), "ERROR", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							showItems();
							idField.setText("ID");
							nameField.setText("Name");
							priceField.setText("Price");
							stockField.setText("Stock");
						}
						
					}
					catch(Exception exception)
					{
						JOptionPane.showMessageDialog(null, String.format("%s","ID, price and stock must be an integers" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
					}
				}
			}
			else if (e.getSource() == update)
			{
				if(idField.getText().length() == 0 || nameField.getText().length() == 0 ||priceField.getText().length() == 0)
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						id = Integer.parseInt(idField.getText());
						name = nameField.getText();
						price = Integer.parseInt(priceField.getText());
						stock = Integer.parseInt(stockField.getText());
						
						if(productBLL.edit(new Product(id,name,price,stock)) == false) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The product does not exist in the database" ), "ERROR", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							showItems();
							idField.setText("ID");
							nameField.setText("Name");
							priceField.setText("Price");
							stockField.setText("Stock");
						}
						
					}
					catch(Exception exception)
					{
						JOptionPane.showMessageDialog(null, String.format("%s","ID, price and stock must be an integers" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
					}
				}
			}
			else
			{
				if(idField.getText().length() == 0 || nameField.getText().length() == 0 || priceField.getText().length() == 0)
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						id = Integer.parseInt(idField.getText());
						
						if(productBLL.delete(new Product(id,null,0,0)) == false) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The product does not exist in the database" ), "ERROR", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							showItems();
							idField.setText("ID");
							nameField.setText("Name");
							priceField.setText("Price");
							stockField.setText("Stock");
						}
						
					}
					catch(Exception exception)
					{
						JOptionPane.showMessageDialog(null, String.format("%s","ID must be an integer" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
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
			if(e.getSource() == idField) idField.setText(null);
			else if(e.getSource() == nameField) nameField.setText(null);
			else if(e.getSource() == priceField) priceField.setText(null);
			else if(e.getSource() == stockField) stockField.setText(null);
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
