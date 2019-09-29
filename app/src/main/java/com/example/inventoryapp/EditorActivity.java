package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.inventoryapp.data.ProductContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Uri mCurrentProductUri;

    private String productName;
    private int productPrice;
    private int productQuantity = 0;

    private EditText mProductName;
    private EditText mProductPrice;
    private TextView mProductQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent intent = getIntent();

        Uri currentProductUri = intent.getData();

        if (currentProductUri == null) {
            setTitle("Add Product");
        } else {
            setTitle("Edit Product");
            mCurrentProductUri = currentProductUri;
            getSupportLoaderManager().initLoader(0, null, this);
        }


        mProductName = findViewById(R.id.product_name_edit_text);
        mProductPrice = findViewById(R.id.price_edit_text);
        mProductQuantity = findViewById(R.id.quantity_text_view);
        Button mIncrement = findViewById(R.id.increment_btn);
        Button mDecrement = findViewById(R.id.decrement_btn);

        // Handle the Increment button event to add one to Quantity TeXtView.
        mIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productQuantity++;
                mProductQuantity.setText("" + productQuantity);
            }
        });

        // Handle the Decrement button event to subtract one from Quantity TeXtView.
        mDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQuantity > 0) {
                    productQuantity--;
                    mProductQuantity.setText("" + productQuantity);
                } else {
                    showToast("Quantity can't be less than 0");
                }
            }
        });


    }

    /**
     * Inflate editor option menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        if (mCurrentProductUri == null){
            invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;

    }

    /**
     * Handling click events.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (checkValues()) {
                    // Insert a new Product.
                    if (mCurrentProductUri == null) {
                        Uri insertUri = getContentResolver().insert(ProductContract.CONTENT_URI, getValues());
                        // Show a Toast about the insertion state.
                        getInsertionState(insertUri);
                    } else {
                        // Update a current product.
                        getContentResolver().update(mCurrentProductUri, getValues(),
                                null, null);
                        showToast("Product Updated");
                    }


                    // Close EditorActivity and back to InventoryActivity.
                    finish();
                }
                return true;
            case R.id.delete:
                // Delete a Specific Product.
                if (mCurrentProductUri != null) {
                    getContentResolver().delete(mCurrentProductUri, null, null);
                    showToast("Product Deleted");

                    // Close EditorActivity and back to InventoryActivity.
                    finish();
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * @return the ContentValues of the product info.
     */
    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);

        return values;
    }

    /**
     * @return values Validation.
     */
    private boolean checkValues() {

        // Check the product name.
        String mName = mProductName.getText().toString().trim();
        if (mName.equals("")) {
            showToast("Product Name is required");
            return false;
        }
        productName = mName;

        // Check the product price.
        String mPrice = mProductPrice.getText().toString();
        if (mPrice.equals("")) {
            showToast("Product Price is required");
            return false;
        } else {
            productPrice = Integer.parseInt(mPrice);
            if (productPrice <= 0) {
                showToast("Minimum price is 1$");
                return false;
            }
        }

        // Check the product quantity.
        productQuantity = Integer.parseInt(mProductQuantity.getText().toString());
        if (productQuantity <= 0) {
            showToast("Minimum Quantity is 1");
            return false;
        }
        return true;
    }


    /**
     * @param uri : uri that return from insert() method.
     */
    private void getInsertionState(Uri uri) {

        // Show a Toast about the insertion state.
        if (uri == null) {
            showToast("Insertion Failed");
        } else {
            showToast("Product ID : " + ContentUris.parseId(uri));
        }

    }


    /**
     * @param msg : the message to be displayed.
     */
    private void showToast(String msg) {
        Toast toast = Toast.makeText(EditorActivity.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {


        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY
        };

        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            productQuantity = quantity;

            mProductName.setText(name);
            mProductPrice.setText("" + price);
            mProductQuantity.setText("" + quantity);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


}
