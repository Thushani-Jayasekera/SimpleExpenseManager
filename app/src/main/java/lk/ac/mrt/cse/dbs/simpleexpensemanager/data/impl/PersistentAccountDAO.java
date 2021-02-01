package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DbManager;

public class PersistentAccountDAO implements AccountDAO  {

    DbManager dbManager;

    public PersistentAccountDAO(Context context){

        dbManager=new DbManager(context);
    }

    @Override
    public List<String> getAccountNumbersList() {

        List<String> accountNumList=dbManager.getAccountNumbersList();
        return accountNumList;
    }

    @Override
    public List<Account> getAccountsList() {

        ArrayList<Account> accountList=dbManager.getAccountsList();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if(accountNo==null){
            throw new InvalidAccountException("Please choose an account");
        }
        Account account;
        account=dbManager.getAccount(accountNo);
        if (account==null){
            throw new InvalidAccountException("Account Number "+accountNo+ " is not valid!");
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        dbManager.addOneAccount(account);


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if(accountNo==null){
            throw new InvalidAccountException("Please choose an account");
        }
        this.getAccount(accountNo);
        dbManager.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo==null){
            throw new InvalidAccountException("Please choose an account");
        }
        Account account=dbManager.getAccount(accountNo);
        double balance=account.getBalance();
        if(ExpenseType.valueOf("EXPENSE")==expenseType){
            if(balance-amount<0){
                throw new InvalidAccountException("Insufficient balance. Your balance is Rs."+balance);
            }else{
                account.setBalance(balance-amount);
                dbManager.updateAccount(account);
            }
        }
        else if(ExpenseType.valueOf("INCOME")==expenseType){
            account.setBalance(balance+amount);
            dbManager.updateAccount(account);
        }

    }


}
