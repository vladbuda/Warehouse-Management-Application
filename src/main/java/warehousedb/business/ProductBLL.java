package warehousedb.business;

import java.util.List;

import warehousedb.dao.ProductDAO;
import warehousedb.model.Product;

public class ProductBLL 
{
	static ProductDAO productDAO = new ProductDAO();
	 
	 public int insert(Product c)
	 {
		 if (productDAO.insert(c) == true) return 1;
		 return 0;
	 }
	 
	 public boolean delete(Product c)
	 {
		 return productDAO.delete(c);
	 }
	 
	 public boolean edit(Product c)
	 {
		 return productDAO.update(c);
	 }
	 
	 public List<Product> findAll()
	 {
		 return productDAO.findAll();
	 }
}
