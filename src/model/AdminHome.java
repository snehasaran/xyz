package model;

import java.sql.*;
import java.util.Map;

public class AdminHome 
{


	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "hw5";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "admin"; 
	String password = "";

	public int count =0;
	public String[] first_name = new String[1000];
	public String[] group_name = new String[1000];
	public Integer[] group_id = new Integer[1000];
	public Integer[] person_id = new Integer[1000];
	public String[] group_descr = new String[1000];
	public String groupNameToInsert;
	public String groupDescrToInsert;

	public void setGroupNameToInsert(String groupNameToInsert) {
		this.groupNameToInsert = groupNameToInsert;
	}


	public void setGroupDescrToInsert(String groupDescrToInsert) {
		this.groupDescrToInsert = groupDescrToInsert;
	}




	public Connection getConnection() throws SQLException 
	{
		Connection conn = null;
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(url+dbName,userName,password);
		return conn;
	}


	public void displayRequests()
	{

		count = 0;
		PreparedStatement ps;
		PreparedStatement ps1;
		String q1 = "select first_name, group_name, gp.group_id, gp.person_id from person p, groups grp, group_person gp where gp.group_id = grp.group_id and gp.person_id = p.person_id and gp.flag = 'Pending'";
		String q2 = "select count(group_id) from group_person where flag = 'Pending'";

		//New query to display Groups
		// String varname1 = ""		+ "select group_id,group_name,group_descr from groups;";
		//String dispGroups = "select group_id,group_name,group_descr from groups";

		String dispGroups = ""
				+ "select group_id,group_name,group_descr from groups where activated_flag = true;";

		try{
			ps = getConnection().prepareStatement(dispGroups);
			ResultSet rs = ps.executeQuery();
			int i =0;
			while(rs.next()){
				group_id[i]=rs.getInt(1);
				group_name[i] =rs.getString(2);
				group_descr[i]=rs.getString(3);

				System.out.println(group_id[i] + " " + group_name[i]+""+group_descr[i]);
				i++;
				count++;
			}

			rs.close();
			ps.close();
		}
		catch(Exception e){
			System.out.println("Cannot run the query");
		}


		/*try
		{
			ps = getConnection().prepareStatement(q1);
			ps1 = getConnection().prepareStatement(q2);
			ResultSet rs1 = ps.executeQuery();
			ResultSet rs2 = ps1.executeQuery();
			int i = 0;
			rs2.next();
			count = rs2.getInt(1);
			System.out.println("count inside displayRequests method " + count);
			while (rs1.next())
			{

				first_name[i] = rs1.getString(1);
				group_name[i] = rs1.getString(2);
				group_id[i]=rs1.getInt(3);
				person_id[i]=rs1.getInt(4);

				System.out.println(first_name[i] + " " + group_name[i]);

				i++;
			}


			rs1.close();
			rs2.close();
			ps.close();
			ps1.close();

	    }
		catch (SQLException e) 
		{
			System.out.println("Cannot run the query");
			e.printStackTrace();

		}*/
	}

	//createGroup method
	public void createGroup(String group_name, String group_descr)
	{
		System.out.println("Inside createGroup() method");
		//PreparedStatement ps;
		String createGrpQuery;
		int j = 0;
		try{
			// INSERT INTO groups (group_id, group_name) VALUES (?, ?);
			System.out.println(group_name);
			System.out.println(group_descr);
			createGrpQuery = "INSERT INTO groups (group_name,group_descr) VALUES (?,?)";
			PreparedStatement preparedStatement = getConnection().prepareStatement(createGrpQuery);
			preparedStatement.setString(1, group_name);
			preparedStatement.setString(2, group_descr);

			preparedStatement.executeUpdate();
		}
		catch (Exception e) 
		{
			System.out.println("Cannot run the query");
			e.printStackTrace();

		}
	}


	//deactivateGroup method
	public void deactivateGroup(Map<Integer, String> chkbox) {

		System.out.println("Inside deactivateGroup() method");
		PreparedStatement ps;
		String deactivateGrpQuery;
		int j = 0;
		try{
			System.out.println("Inside Deactivate of deactivateGroup() method");
			
			for (Integer key : chkbox.keySet()) 
			{
				System.out.println("Key = " + key);
				System.out.println("Value = " + chkbox.get(key));
				j = Integer.parseInt(chkbox.get(key));
				
				 deactivateGrpQuery = ""
						+ "update groups set activated_flag = false where group_id = '"+ group_id[j]+"';";
				//deactivateGrpQuery = "Update group_person set flag = 'Approved' where group_id = '" + group_id[j] + "' and person_id = '" + person_id[j] + "'";
				ps = getConnection().prepareStatement(deactivateGrpQuery);
				int i = ps.executeUpdate();
				System.out.println("Updated " + i + " row as deactivated");
				ps.close();
			}
		}
		catch (Exception e) 
		{
			System.out.println("Cannot run the query");
			e.printStackTrace();

		}
	}


	public void addUpdate(Map<Integer, String> chkbox)
	{
		System.out.println("Inside addUpdate() method");
		PreparedStatement ps;
		String q1;
		int j = 0;
		try
		{
			System.out.println("Inside try of addUpdate() method");
			for (Integer key : chkbox.keySet()) 
			{
				System.out.println("Key = " + key);
				System.out.println("Value = " + chkbox.get(key));
				j = Integer.parseInt(chkbox.get(key));
				q1 = "Update group_person set flag = 'Approved' where group_id = '" + group_id[j] + "' and person_id = '" + person_id[j] + "'";
				ps = getConnection().prepareStatement(q1);
				int i = ps.executeUpdate();
				System.out.println("Updated " + i + " row as approved");
				ps.close();
			}

		}
		catch (SQLException e) 
		{
			System.out.println("Cannot run the query");
			e.printStackTrace();
		}
	}

	public void rejectUpdate(Map<Integer, String> chkbox)
	{
		System.out.println("Inside rejectUpdate() method");
		PreparedStatement ps;
		String q1;
		int j = 0;
		try
		{
			System.out.println("Inside try of rejectUpdate() method");
			for (Integer key : chkbox.keySet()) 
			{
				System.out.println("Key = " + key);
				System.out.println("Value = " + chkbox.get(key));
				j = Integer.parseInt(chkbox.get(key));
				q1 = "Update group_person set flag = 'Rejected' where group_id = '" + group_id[j] + "' and person_id = '" + person_id[j] + "'";
				ps = getConnection().prepareStatement(q1);
				int i = ps.executeUpdate();
				System.out.println("Updated " + i + " row as rejected");
				ps.close();
			}

		}
		catch (SQLException e) 
		{
			System.out.println("Cannot run the query");
			e.printStackTrace();

		}
	}



}
