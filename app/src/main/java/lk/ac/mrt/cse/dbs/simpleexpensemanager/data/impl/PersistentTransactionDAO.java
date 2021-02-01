package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DbManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    DbManager dbManager;
    public PersistentTransactionDAO(Context context) {
        dbManager=new DbManager(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo==null){
            throw new InvalidAccountException("Please choose an account");
        }
        Transaction transaction=new Transaction(date,accountNo,expenseType,amount);
        dbManager.logTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {

        ArrayList<Transaction> transactionLogs=dbManager.loadAllTransactionLogs();
        return transactionLogs;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        ArrayList<Transaction> transactionLogs=dbManager.loadPaginatedTransactionLogs(limit);
        return transactionLogs;

    }
}
