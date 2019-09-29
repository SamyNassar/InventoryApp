package com.example.inventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_product,
                parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView mProductName = view.findViewById(R.id.product_name_TextView);
        TextView mProductPrice = view.findViewById(R.id.product_price_TextView);
        TextView mQuantityName = view.findViewById(R.id.product_quantity_TextView);


        String productName = cursor.getString(
                cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));

        int productPrice = cursor.getInt(
                cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));

        int productQuantity = cursor.getInt(
                cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));


        mProductName.setText(productName);
        mProductPrice.setText("" + productPrice);
        mQuantityName.setText("" + productQuantity);



    }
}
