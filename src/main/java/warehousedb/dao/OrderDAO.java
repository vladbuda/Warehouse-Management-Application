package warehousedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import warehousedb.connection.ConnectionFactory;
import warehousedb.model.Order;

/**This class corresponds to the orders table and extends AbstractDAO class for performing SQL operations on orders table.
 * @author Vlad-Cristian Buda
 *
 */
public class OrderDAO extends AbstractDAO<Order>
{	
	/**This method find the id of the last placed order
	 * @return returns the id of the last placed order
	 */
	public static int getOrderId()
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM warehousedb.order";
		int id = 0;
		try
		{
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				id = resultSet.getInt("id");
				resultSet.getInt("clientid");
				resultSet.getInt("productid");
				resultSet.getInt("quantity");
				resultSet.getInt("total");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		ConnectionFactory.close(connection);
		ConnectionFactory.close(statement);
		ConnectionFactory.close(resultSet);
		return id;
	}
}
