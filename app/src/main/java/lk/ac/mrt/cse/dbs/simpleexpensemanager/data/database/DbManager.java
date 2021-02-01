package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="180273L.db";
    public static final int DATABASE_VERSION=1;



    public static final String ACCOUNT_TABLE = "Account";
    public static final String COLUMN_ACCOUNT_NUMBER = "accountNo";
    public static final String COLUMN_BANK = "bankName";
    public static final String COLUMN_ACC_HOLDER = "accountHolderName";
    public static final String COLUMN_INIT_BAL = "balance";

    public static final String TRANSACTION_TABLE = "Transactions";
    public static final String COLUMN__ID="_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String COLUMN_AMOUNT = "amount";
    public DbManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableStatement1= "CREATE TABLE " + ACCOUNT_TABLE + " (" +
                COLUMN_ACCOUNT_NUMBER + " TEXT PRIMARY KEY, " +
                COLUMN_BANK + " TEXT, " +
                COLUMN_ACC_HOLDER + " TEXT, " +
                COLUMN_INIT_BAL + " REAL );";
        sqLiteDatabase.execSQL(createTableStatement1);


        String createTableStatement2="CREATE TABLE "+TRANSACTION_TABLE +"( " +
                COLUMN__ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                COLUMN_ACCOUNT_NUMBER + " TEXT NOT NULL REFERENCES Account(accountNo)," +
                COLUMN_DATE + " TEXT NOT NULL," +
                COLUMN_EXPENSE_TYPE + " TEXT NOT NULL," +
                COLUMN_AMOUNT + " REAL NOT NULL );";
        sqLiteDatabase.execSQL(createTableStatement2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String deleteTableStatement1="DROP TABLE IF EXISTS "+ACCOUNT_TABLE+"; ";
        sqLiteDatabase.execSQL(deleteTableStatement1);

        String deleteTableStatement2="DROP TABLE IF EXISTS "+TRANSACTION_TABLE+ "; ";
        sqLiteDatabase.execSQL(deleteTableStatement2);
        onCreate(sqLiteDatabase);
    }
    public boolean addOneAccount(Account account) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(COLUMN_ACCOUNT_NUMBER,account.getAccountNo());
        cv.put(COLUMN_BANK, account.getBankName());
        cv.put(COLUMN_ACC_HOLDER, account.getAccountHolderName());
        cv.put(COLUMN_INIT_BAL,account.getBalance());

        long insert=db.insert(ACCOUNT_TABLE, null, cv);

        if(insert==-1){
            return false;
        }else{
            return true;
        }


    }

    public ArrayList<String> getAccountNumbersList() {

        ArrayList<String> accountList = new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ACCOUNT_TABLE+" ;",null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String accountNo=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NUMBER));

            accountList.add(accountNo);
            cursor.moveToNext();
        }
        cursor.close();

        return accountList;

    }
    public ArrayList<Account> getAccountsList(){

        ArrayList<Account> accountList = new ArrayList<Account>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ACCOUNT_TABLE+" ;",null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String accountNo=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NUMBER));
            String bankName=cursor.getString(cursor.getColumnIndex(COLUMN_BANK));
            String accountHolderName=cursor.getString(cursor.getColumnIndex(COLUMN_ACC_HOLDER));
            Double balance=cursor.getDouble(cursor.getColumnIndex(COLUMN_INIT_BAL));
            Account account = new Account(accountNo,bankName,accountHolderName,balance);
            accountList.add(account);
            cursor.moveToNext();
        }
        cursor.close();

        return accountList;

        }




    public Account getAccount(String accountNo){
        Account account=null;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ACCOUNT_TABLE+" WHERE accountNo = ?",new String[]{accountNo});
        if(cursor.getCount()==0){
            account=null;

        }else{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String bankName=cursor.getString(cursor.getColumnIndex(COLUMN_BANK));
                String accountHolderName=cursor.getString(cursor.getColumnIndex(COLUMN_ACC_HOLDER));
                Double balance=cursor.getDouble(cursor.getColumnIndex(COLUMN_INIT_BAL));
                account = new Account(accountNo,bankName,accountHolderName,balance);

                cursor.moveToNext();
            }


        }
        cursor.close();
        db.close();
        return account;

    }

    public boolean removeAccount(String accountNo){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] params=new String[]{accountNo};
        long delete=db.delete(ACCOUNT_TABLE,COLUMN_ACCOUNT_NUMBER+"=?",params);
        if(delete==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateAccount(Account account){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv =new ContentValues();
        Boolean updated = false;

        try{

        cv.put(COLUMN_ACCOUNT_NUMBER,account.getAccountNo());
        cv.put(COLUMN_BANK, account.getBankName());
        cv.put(COLUMN_ACC_HOLDER, account.getAccountHolderName());
        cv.put(COLUMN_INIT_BAL,account.getBalance());


        String[] params=new String[]{account.getAccountNo()};
        long update=db.update(ACCOUNT_TABLE,cv,COLUMN_ACCOUNT_NUMBER+"=?",params);
        if (update==-1){
            updated= false;
        }else{
            updated= true;
        }}
        finally {

            return updated;
        }
    }


        public boolean logTransaction(Transaction transaction){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_DATE,new SimpleDateFormat("yyyy-MM-dd ").format(transaction.getDate()));
        cv.put(COLUMN_ACCOUNT_NUMBER,transaction.getAccountNo());
        cv.put(COLUMN_EXPENSE_TYPE,transaction.getExpenseType().toString());
        cv.put(COLUMN_AMOUNT, transaction.getAmount());

        long insert=db.insert(TRANSACTION_TABLE,COLUMN__ID,cv);
        if(insert==-1){
            return false;
        }else{
            return true;
        }

    }



    public ArrayList<Transaction> loadAllTransactionLogs() throws ParseException {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TRANSACTION_TABLE+";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            String accountNo=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NUMBER));
            ExpenseType expenseType=ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE)));
            double amount=cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
            Transaction transaction=new Transaction(date, accountNo,expenseType,amount);
            transactionList.add(transaction);
            cursor.moveToNext();

        }


        return transactionList;
    }


    public ArrayList<Transaction> loadPaginatedTransactionLogs(int limit) throws ParseException {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TRANSACTION_TABLE+" LIMIT "+limit+ ";", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            String accountNo=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NUMBER));
            ExpenseType expenseType=ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE)));
            double amount=cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
            Transaction transaction=new Transaction(date, accountNo,expenseType,amount);
            transactionList.add(transaction);
            cursor.moveToNext();

        }


        return transactionList;
    }
}
