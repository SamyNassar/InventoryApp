package com.example.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inventory.db";

    /**
     * Create a table in the Database.
     *
     * It's equal to (in SQLite) :
     * CREATE TABLE product (_ID INTEGER PRIMARY KEY AUTOINCREMENT,
     *                       name TEXT NOT NULL, price INTEGER NOT NULL,
     *                       quantity INTEGER NOT NULL);
     *
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, " +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL)";

    /**
     * Delete a table in the Database.
     *
     * It's equal to (in SQLite) :
     * DROP TABLE IF EXISTS product;
     *
     */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;




    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
