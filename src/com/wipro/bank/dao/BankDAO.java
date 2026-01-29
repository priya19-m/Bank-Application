package com.wipro.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.DBUtil;

public class BankDAO {
    public int generateSequenceNumber()
    {
    	Connection connection=DBUtil.getConnection();
    	String sql="select transaction_seq.NEXTVAL from dual";
    	try {
	        PreparedStatement ps = connection.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        int seqNumber=rs.getInt(1);
            return seqNumber;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
    }
    public boolean validateAccount(String accountNumber)
    {
    	Connection connection=DBUtil.getConnection();
    	String sql="select * from ACCOUNT_TBL where Account_Number=?";
    	try {
    	        PreparedStatement ps = connection.prepareStatement(sql);
    	        ps.setString(1,accountNumber);
    	        ResultSet rs = ps.executeQuery();
    	        if (rs.next()) {
    	            return true;   
    	        }

    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return false;  
    	}
    public float findBalance(String accountNumber) {
		Connection connection=DBUtil.getConnection();
		String sql="select Balance from ACCOUNT_TBL where Account_Number=?";
		try {
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, accountNumber);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				return rs.getFloat(1);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return -1;  
    }
    public boolean transferMoney(TransferBean transferBean)
    {
    	Connection connection=DBUtil.getConnection();
    	String sql="INSERT INTO TRANSFER_TBL " + "(Transaction_ID,Account_Number,Beneficiary_Acc_Num,Transaction_Date,Transaction_Amount)" + "VALUES(?,?,?,?,?)";   
    	try {
    		PreparedStatement ps=connection.prepareStatement(sql);
    		ps.setInt(1,generateSequenceNumber());
    		ps.setString(2,transferBean.getFromAccountNumber());
    		ps.setString(3,transferBean.getToAccountNumber());
    		ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
    		ps.setFloat(5,transferBean.getAmount());
    		int rows=ps.executeUpdate();
    		if(rows > 0)
    		{
    			return true;
    		}
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
		return false;
    }
    public boolean updateBalance(String accountNumber,float newBalance)
    {
    	Connection connection=DBUtil.getConnection();
    	String sql="UPDATE ACCOUNT_TBL SET Balance =? Where Account_Number=?";
    	try {
    		PreparedStatement ps=connection.prepareStatement(sql);
    		ps.setString(2, accountNumber);
    		ps.setFloat(1, newBalance);
    	    int update=ps.executeUpdate();
    	    if(update > 0)
    	    {
    	    	return true;
    	    }
    	    }catch(SQLException e)
    	{
    	    	e.printStackTrace();
    	}
    	return false;
    }

}