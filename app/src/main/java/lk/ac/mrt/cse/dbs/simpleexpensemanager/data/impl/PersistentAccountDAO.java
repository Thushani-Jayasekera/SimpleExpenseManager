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

    DbManager databaseHelper;

    public PersistentAccountDAO(Context context){

        databaseHelper=new DbManager(context);
    }

    @Override
    public List<String> getAccountNumbersList() {

        List<String> accountNumList=databaseHelper.getAccountNumbersList();
        return accountNumList;
    }

    @Override
    public List<Account> getAccountsList() {

        ArrayList<Account> accountList=databaseHelper.getAccountsList();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        account=databaseHelper.getAccount(accountNo);
        return account;
    }

    @Override
    public void addAccount(Account account) {
        databaseHelper.addOneAccount(account);


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        databaseHelper.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account=databaseHelper.getAccount(accountNo);
        double balance=account.getBalance();
        if(ExpenseType.valueOf("EXPENSE")==expenseType){
            if(balance-amount<0){
                throw new InvalidAccountException("Insufficient balance ");
            }else{
                account.setBalance(balance-amount);
            }
        }
        else if(ExpenseType.valueOf("INCOME")==expenseType){
            account.setBalance(balance+amount);
        }
        databaseHelper.updateAccount(account);
    }


}
