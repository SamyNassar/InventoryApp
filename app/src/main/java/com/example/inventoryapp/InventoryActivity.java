package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventoryapp.data.ProductContract;
import com.example.inventoryapp.data.ProductCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCTOR_LOADER = 0;

    ProductCursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


        // Create intent to EditorActivity by clicking on the Float Action Bar.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(i);
            }
        });


        ListView listView = findViewById(R.id.listView);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        cursorAdapter = new ProductCursorAdapter(this, null);

        listView.setAdapter(cursorAdapter);


        // Transfer Data to EditorActivity.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);

                Uri currentUri = ContentUris.withAppendedId(ProductContract.CONTENT_URI, id);

                intent.setData(currentUri);

                startActivity(intent);
            }
        });


        getSupportLoaderManager().initLoader(0, null, this);


    }


    // Inflate inventory option menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inventory, menu);
        return true;

    }

    // Handling click event.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_product:
                Intent i = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(i);
                return true;
            case R.id.delete_all:
                getContentResolver().delete(ProductContract.CONTENT_URI, null, null);
                showToast("All Product Deleted");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY
        };

        return new CursorLoader(this,
                ProductContract.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        cursorAdapter.swapCursor(null);
    }


    /**
     * @param msg : the message to be displayed.
     */
    private void showToast(String msg) {
        Toast toast = Toast.makeText(InventoryActivity.this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
