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

import warehousedb.business.ClientBLL;
import warehousedb.business.Manager;
import warehousedb.model.Client;
import warehousedb.validator.EmailValidator;

/**Class responsible for creating the graphical user interface for the clients table
 * @author Vlad-Cristian Buda
 * @version 1.0
 */
public class ClientsGUI 
{
	private JFrame clients;
	private JTable clientsTable;
	private Object[][] data;
	private String[] header;
	private DefaultTableModel model;
	private JScrollPane clientsScroll;
	private JTextField idField, nameField, addressField, emailField;
	private JButton insert, update, delete;
	private int id;
	private String name, address, email;
	private ArrayList<String[]> items;
	
	ClientBLL clientBLL;
	/**Instantiates all the graphic components related to the clients table
	 * 
	 */
	public ClientsGUI()
	{
		clientBLL = new ClientBLL();
		clients = new JFrame("Clients");
		
		idField = new JTextField("ID");
		nameField = new JTextField("Name");
		addressField = new JTextField("Address");
		emailField = new JTextField("Email");
		
		insert = new JButton("Insert");
		update = new JButton("Update");
		delete = new JButton("Delete");
		
		header = new String[4];
		data = new Object[0][0];
		initializeHeader();
		model = new DefaultTableModel(data,header);
		
		clients.setSize(600, 500);
		clients.setResizable(false);
	
		clientsTable = new JTable(model);
		
		clientsTable.setCellSelectionEnabled(false);
		clientsTable.setColumnSelectionAllowed(false);
		
		clientsScroll = new JScrollPane();		
		clientsScroll.setBounds(50, 150, 500, 275);
		clientsScroll.setViewportView(clientsTable);
		
		idField.setBounds(50, 30, 40, 30);
		nameField.setBounds(110,30,133,30);
		addressField.setBounds(263, 30, 133, 30);
		emailField.setBounds(417, 30, 133, 30);
		
		idField.addMouseListener(new MouseHandler());
		nameField.addMouseListener(new MouseHandler());
		addressField.addMouseListener(new MouseHandler());
		emailField.addMouseListener(new MouseHandler());
		
		insert.setBounds(50, 90, 100, 30);
		update.setBounds(250, 90, 100, 30);
		delete.setBounds(450, 90, 100, 30);
		
		insert.setFocusable(false);
		update.setFocusable(false);
		delete.setFocusable(false);
		
		insert.addActionListener(new ActionHandler());
		update.addActionListener(new ActionHandler());
		delete.addActionListener(new ActionHandler());
		
		clients.add(idField);
		clients.add(nameField);
		clients.add(addressField);
		clients.add(emailField);
		
		clients.add(insert);
		clients.add(update);
		clients.add(delete);
		
		clients.add(clientsScroll);
		clients.setLayout(null);
		clients.setVisible(true);
		clients.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		showItems();
	}
	
	/**
	 * This method displays all the entries in an SQL table in its corresponding JTable
	 */
	private void showItems()
	{
		model.setRowCount(0);
		List<Client> list = clientBLL.findAll();
		items = Manager.getData(list);
		for(String[] s : items)
			model.addRow(s);
	}
	
	/**This method creates the header of the JTable using the reflection technique
	 * 
	 */
	private void initializeHeader()
	{
		Field[] fields = Client.class.getDeclaredFields();
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
				if(idField.getText().length() == 0 || nameField.getText().length() == 0 || addressField.getText().length() == 0 || emailField.getText().length() == 0)
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						id = Integer.parseInt(idField.getText());
						name = nameField.getText();
						address = addressField.getText();
						email = emailField.getText();
						
						if(!EmailValidator.validate(email)) throw new IllegalArgumentException();
						
						if(clientBLL.insert(new Client(id,name,address,email)) == 0) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The client already exists in the database" ), "ERROR", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							idField.setText("ID");
							nameField.setText("Name");
							addressField.setText("Address");
							emailField.setText("Email");
							showItems();
						}
						
					}
					catch(Exception exception)
					{
						if(exception.getClass().getSimpleName().equals("IllegalArgumentException"))
							JOptionPane.showMessageDialog(null, String.format("%s","Invalid Email format" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
						else
							JOptionPane.showMessageDialog(null, String.format("%s","ID must be an integer" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
					}
				}
			}
			else if (e.getSource() == update)
			{
				if(idField.getText().length() == 0 || nameField.getText().length() == 0 || addressField.getText().length() == 0 || emailField.getText().length() == 0)
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						id = Integer.parseInt(idField.getText());
						name = nameField.getText();
						address = addressField.getText();
						email = emailField.getText();
						
						
						if(clientBLL.edit(new Client(id,name,address,email)) == false) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The client does not exist in the database" ), "ERROR", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							idField.setText("ID");
							nameField.setText("Name");
							addressField.setText("Address");
							emailField.setText("Email");
							showItems();
						}
						
					}
					catch(Exception exception)
					{
						JOptionPane.showMessageDialog(null, String.format("%s","ID must be an integer" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
					}
				}
			}
			else
			{
				if(idField.getText().length() == 0 || nameField.getText().length() == 0 || addressField.getText().length() == 0 || emailField.getText().length() == 0)
					JOptionPane.showMessageDialog(null, String.format("%s", "Input must not contain blank spaces" ), "Invalid format", JOptionPane.ERROR_MESSAGE, null);
				else
				{
					try
					{
						id = Integer.parseInt(idField.getText());
						name = nameField.getText();
						address = addressField.getText();
						email = emailField.getText();
						
						idField.setText("ID");
						nameField.setText("Name");
						addressField.setText("Address");
						emailField.setText("Email");
						
						if(clientBLL.delete(new Client(id,name,address,email)) == false) 
							JOptionPane.showMessageDialog(null, String.format("%s", "The client does not exist in the database" ), "ERROR", JOptionPane.ERROR_MESSAGE, null);
						else
						{
							showItems();
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
			else if(e.getSource() == addressField) addressField.setText(null);
			else if(e.getSource() == emailField) emailField.setText(null);
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
