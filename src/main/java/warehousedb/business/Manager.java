package warehousedb.business;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**This class implements the application logic for performing SQL operations on a database
 * @author Vlad-Cristian Buda
 * @version 1.0
 */
public class Manager 
{
	
	/**This method obtains a string representation of all the entries in the corresponding table
	 * @param c the class corresponding to the table in the database
 	 * @return returns an ArrayList of strings containing all the entries in the table
	 */
	public static ArrayList<String[]> getData(List<? extends Object> list) 
	{
		ArrayList<String[]> data = new ArrayList<String[]>();
		String[] row = new String[10];
		int i = 0;
		if(list != null)
			{
				for(Object obj : list)
				{
					i = 0;
					Class c = obj.getClass();
					Field[] fields = c.getDeclaredFields();
					for(Field f : fields)
					{
						f.setAccessible(true);
						try
						{
							row[i] = ""+f.get(obj);
						}
						catch (IllegalAccessException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						f.setAccessible(false);
						i++;
					}
					data.add(row);
					row = new String[10];
				}
			}
		return data;
	}
	
}
