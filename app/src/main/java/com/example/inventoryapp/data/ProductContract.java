package com.example.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {

    private ProductContract() {}

    // Create a Uri to the database (products table).
    public static final String URI_AUTHORITY = "com.example.inventoryapp.data.ProductProvider";
    public static final Uri BASE_URI = Uri.parse("content://" + URI_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_PRODUCTS);



    public static class ProductEntry implements BaseColumns{

        public static final String TABLE_NAME = "products";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";


    }


}
