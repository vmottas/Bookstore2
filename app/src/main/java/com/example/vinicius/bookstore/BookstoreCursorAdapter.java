package com.example.vinicius.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_NAME;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_PRICE;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_QUANTITY;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_NAME;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_TEL;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.CONTENT_URI;
import static com.example.vinicius.bookstore.data.BookstoreContract.COLUMN_KEY;

public class BookstoreCursorAdapter extends CursorAdapter {

    public BookstoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView bookName = view.findViewById(R.id.book_name);
        TextView bookPrice = view.findViewById(R.id.book_price);
        TextView bookQuantity = view.findViewById(R.id.book_quantity);
        Button minusBtn = view.findViewById(R.id.minus_button);
        Button addBtn = view.findViewById(R.id.add_button);

        bookName.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME)));
        bookPrice.setText("$" + cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PRICE)));
        bookQuantity.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_QUANTITY)));

        final String name = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME));
        final String price = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PRICE));
        final String supplier = cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_NAME));
        final String tel = cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_TEL));

        final int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_QUANTITY));
        final int bookId = cursor.getInt(cursor.getColumnIndex(COLUMN_KEY));

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity > 0) {
                    int newQuantity = quantity - 1;
                    Uri quantityUri = ContentUris.withAppendedId(CONTENT_URI, bookId);
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_BOOK_QUANTITY, newQuantity);
                    values.put(COLUMN_BOOK_NAME, name);
                    values.put(COLUMN_BOOK_PRICE, price);
                    values.put(COLUMN_SUPPLIER_NAME, supplier);
                    values.put(COLUMN_SUPPLIER_TEL, tel);

                    int rowUpdated = context.getContentResolver().update(quantityUri, values, null, null);
                    if (!(rowUpdated > 0)) {
                        Toast.makeText(context, R.string.error_proceeding, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.book_sold, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, R.string.book_no_enought, Toast.LENGTH_SHORT).show();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity >= 0) {
                    int newQuantity = quantity + 1;
                    Uri quantityUri = ContentUris.withAppendedId(CONTENT_URI, bookId);
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_BOOK_QUANTITY, newQuantity);
                    values.put(COLUMN_BOOK_NAME, name);
                    values.put(COLUMN_BOOK_PRICE, price);
                    values.put(COLUMN_SUPPLIER_NAME, supplier);
                    values.put(COLUMN_SUPPLIER_TEL, tel);

                    int rowUpdated = context.getContentResolver().update(quantityUri, values, null, null);
                    if (!(rowUpdated > 0)) {
                        Toast.makeText(context, R.string.error_proceeding, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.book_added, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, R.string.error_proceeding, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
