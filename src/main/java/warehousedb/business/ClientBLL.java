package warehousedb.business;

import java.util.List;

import warehousedb.dao.ClientDAO;
import warehousedb.model.Client;

public class ClientBLL 
{
	 static ClientDAO clientDAO = new ClientDAO();
	 
	 public int insert(Client c)
	 {
		 if (clientDAO.insert(c) == true) return 1;
		 return 0;
	 }
	 
	 public boolean delete(Client c)
	 {
		 return clientDAO.delete(c);
	 }
	 
	 public boolean edit(Client c)
	 {
		 return clientDAO.update(c);
	 }

	 public List<Client> findAll()
	 {
		 return clientDAO.findAll();
	 }
}
