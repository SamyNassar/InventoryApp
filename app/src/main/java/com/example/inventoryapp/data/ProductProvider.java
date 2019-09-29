package com.example.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.inventoryapp.data.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider {

    private ProductDbHelper productDbHelper;

    private static final int PRODUCT = 100;
    private static final int PRODUCT_ID = 101;


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(ProductContract.URI_AUTHORITY,
                ProductContract.PATH_PRODUCTS, PRODUCT);

        sURIMatcher.addURI(ProductContract.URI_AUTHORITY,
                ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }


    @Override
    public boolean onCreate() {
        // Create database instance.
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = productDbHelper.getReadableDatabase();

        Cursor cursor;

        int uriMatch = sURIMatcher.match(uri);

        switch (uriMatch) {
            case PRODUCT:
                cursor = database.query(ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = productDbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = productDbHelper.getWritableDatabase();

        int uriMatch = sURIMatcher.match(uri);
        switch (uriMatch) {
            case PRODUCT:
                // Delete all rows.
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
            case PRODUCT_ID:
                // Delete a single row given by the ID in the URI.
                getContext().getContentResolver().notifyChange(uri, null);
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = productDbHelper.getWritableDatabase();
        selection = ProductEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
