package warehousedb.dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import warehousedb.connection.ConnectionFactory;

/**
 * @author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @since: Apr 03, 2017
 * @source http://www.java-blog.com/mapping-javaobjects-database-reflection-generics
 */
public class AbstractDAO<T> 
{
	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

	private final Class<T> type;

	@SuppressWarnings("unchecked")
	public AbstractDAO() 
	{
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**This method creates an SQL string for the SELECT operation
	 * @param field the field where SELECT in applied
	 * @return returns the string representation of the SELECT operation applied on the specified field
	 */
	private String createSelectQuery(String field) 
	{
		if(field == null) return String.format("%s", "SELECT * FROM warehousedb."+type.getSimpleName());
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM warehousedb.");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " = ?");
		return sb.toString();
	}
	
	/**This method creates an SQL string for the INSERT operation
	 * @param t the object to be inserted in the database
	 * @return returns a string representation of the INSERT operation for the specified object
	 */
	private String createInsertQuery(T t)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append("warehousedb.");
		sb.append(type.getSimpleName());
		sb.append(" (");
		Field[] fields = type.getDeclaredFields();
		int i = 1;
		for(Field f : fields)
			{
			if(i == fields.length) sb.append(f.getName() + ") ");
			else
				{
					sb.append(f.getName() + ", ");
					i++;
				}
			}
		sb.append("VALUES ('");
		i = 1;
		for(Field f : fields)
		{
			f.setAccessible(true);
			if(i == fields.length)
			{
				try {
					sb.append(f.get(t)+"')");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				f.setAccessible(false);
				break;
			}
			try {
				sb.append(f.get(t)+"','");
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			f.setAccessible(false);
			i++;
		}
		return sb.toString();
	}

	/**This method creates an SQL string for the UPDATE operation
	 * @param t the object to be updated in the database
	 * @return returns a string representation of the UPDATE operation for the specified object
	 */
	private String createUpdateQuery(T t)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(type.getSimpleName());
		sb.append(" SET ");
		Field[] fields = type.getDeclaredFields();
		int i = 1;
		for(Field f : fields)
		{
			if(i == 1) 
			{
				i++;
				continue;
			}
			 sb.append(f.getName() + " = ");
			 if(i == fields.length)
			 {
				 f.setAccessible(true);
				 try {
					sb.append("'"+f.get(t) + "' WHERE id = ?");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 f.setAccessible(false);
				 break;
			 }
			 f.setAccessible(true);
			 try {
				sb.append("'"+f.get(t) +"', ");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 f.setAccessible(false);
			 i++;
		}
		return sb.toString();
	}
	
	/**This method creates an SQL string for the DELETE operation
	 * @return returns a string representation of the DELETE operation for the specified object
	 */
	public String createDeleteQuery()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE from ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE id = ?");
		return sb.toString();
	}
	
	/**This method finds all the entries in a table
	 * @return returns a List of objects of type corresponding to the searched table
	 */
	public List<T> findAll() 
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery(null);
		try 
		{
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			List<T> list = null;
			list = createObjects(resultSet);
			if(list == null) return null;
			else if (list.isEmpty()) return null;
			else return list;
		} 
		
		catch (SQLException e) 
		{
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
			//e.printStackTrace();
		} 
		finally 
		{
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**This method finds an entry in a table based on its id
	 * @param id the id of the entry to be searched
	 * @return returns an object of the entry in the table
	 */
	public T findById(int id) 
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery("id");
		//System.out.println(query);
		try 
		{
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			List<T> list = null;
			list = createObjects(resultSet);
			if(list == null) return null;
			else if (list.isEmpty()) return null;
			else return list.get(0);
		} 
		catch (SQLException e) 
		{
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
		} 
		finally 
		{
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**Creates a List of objects based on the result of an SQL query
	 * @param resultSet the result returned by the SQL query
	 * @return returns a list of objects created based on the result of the SQL query
	 */
	private List<T> createObjects(ResultSet resultSet)
	{
		List<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) 
			{
				T instance = type.newInstance();
				for (Field field : type.getDeclaredFields())
				{
					Object value = resultSet.getObject(field.getName());
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(instance, value);
				}
				list.add(instance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if(list.isEmpty()) return null;
		return list;
	}

	/**	This method inserts an element in the database
	 * @param t The item which will be inserted
	 * @return returns true if the operation was successful or false otherwise
	 */
	public boolean insert(T t) 
	{
		Field idField;
		int id = -1;
		try {
			 idField = type.getDeclaredField("id");
			 idField.setAccessible(true);
			 id = idField.getInt(t);
			 idField.setAccessible(false);
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(findById(id) == null)
		{
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createInsertQuery(t);
		try 
		{
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
			//e.printStackTrace();
		} 
		finally 
		{
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		}
		return false;
	}

	/**	This method updates an element in the database
	 * @param t The item which will be updated
	 * @return returns true if the operation was successful or false otherwise
	 */
	public boolean update(T t) 
	{
		Field idField;
		int id = -1;
		try {
			 idField = type.getDeclaredField("id");
			 idField.setAccessible(true);
			 id = idField.getInt(t);
			 idField.setAccessible(false);
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(findById(id) != null)
		{
			Connection connection = null;
			PreparedStatement statement = null;
			String query = createUpdateQuery(t);
		//	System.out.println(query);
			try
			{
				connection = ConnectionFactory.getConnection();
				statement = connection.prepareStatement(query);
				statement.setInt(1, id);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionFactory.close(statement);
				ConnectionFactory.close(connection);
			}
		}
		return false;
	}
	
	/**	This method deletes an element in the database
	 * @param t The item which will be deleted
	 * @return returns true if the operation was successful or false otherwise
	 */
	public boolean delete(T t)
	{
		Field idField;
		int id = -1;
		try {
			 idField = type.getDeclaredField("id");
			 idField.setAccessible(true);
			 id = idField.getInt(t);
			 idField.setAccessible(false);
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(findById(id) != null)
		{
			Connection connection = null;
			PreparedStatement statement = null;
			String query = createDeleteQuery();
			try
			{
				connection = ConnectionFactory.getConnection();
				statement = connection.prepareStatement(query);
				statement.setInt(1, id);
				statement.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionFactory.close(statement);
				ConnectionFactory.close(connection);
			}
		}
			return false;
	}
}
