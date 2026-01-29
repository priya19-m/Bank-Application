package com.wipro.bank.service;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.dao.BankDAO;
import com.wipro.bank.util.InsufficientFundsException;

public class BankService {
    public String  checkBalance(String accountNumber)
    {
    	BankDAO bankDAO = new BankDAO();
    	boolean isValid = bankDAO.validateAccount(accountNumber);
    	if(!isValid)
    	{
    		return "ACCOUNT NUMBER INVALID";    	
    	}
    	float balance = bankDAO.findBalance(accountNumber);
    	return "BALANCE IS:" + balance;
     }
    public String transfer(TransferBean transferBean)
    {
    	if(transferBean == null)
    	{
    		return "INVALID";
    	}
    	BankDAO dao = new BankDAO();
    	if(!dao.validateAccount(transferBean.getFromAccountNumber()) || !dao.validateAccount(transferBean.getToAccountNumber()))
    	{
    		return "INVALID ACCOUNT";
    	}
    	try {
    		float fromBalance=dao.findBalance(transferBean.getFromAccountNumber());
        	if(fromBalance<transferBean.getAmount())
        	{
        		throw new InsufficientFundsException() ;
        	}
        	boolean debit=dao.updateBalance(transferBean.getFromAccountNumber(),fromBalance-transferBean.getAmount());
        	float toBalance=dao.findBalance(transferBean.getToAccountNumber());
        	boolean credit=dao.updateBalance(transferBean.getToAccountNumber(), toBalance+transferBean.getAmount());
        	boolean transaction =dao.transferMoney(transferBean);
        	if(debit && credit && transaction)
        	{
        		return "SUCCESS";
        	}
    	}catch(InsufficientFundsException e)
    	{
    		e.printStackTrace();
    	}
    	return "FAILED";
    }
}