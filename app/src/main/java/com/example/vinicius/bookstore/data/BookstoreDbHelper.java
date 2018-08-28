package com.example.vinicius.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookstoreDbHelper extends SQLiteOpenHelper{

    public final static String LOG_TAG = BookstoreDbHelper.class.getSimpleName();
    public final static String DATABASE_NAME = "store.db";
    public final static int DATABASE_VERSION = 1;

    public BookstoreDbHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Crie uma String que contenha a instrução SQL para criar a tabela bookstore
    @Override
    public void onCreate (SQLiteDatabase db) {

        String SQL_CREATE_BOOKSTORE_TABLE = " CREATE TABLE " + BookstoreContract.BookstoreEntry.TABLE_NAME + " ("
                + BookstoreContract.BookstoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookstoreContract.BookstoreEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BookstoreContract.BookstoreEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + BookstoreContract.BookstoreEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_TEL + " INTEGER NOT NULL DEFAULT 0); ";

        // Execute a instrução SQL
        db.execSQL(SQL_CREATE_BOOKSTORE_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
