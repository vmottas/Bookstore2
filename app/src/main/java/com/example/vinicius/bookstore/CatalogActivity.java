package com.example.vinicius.bookstore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.vinicius.bookstore.data.BookstoreContract;
import com.example.vinicius.bookstore.data.BookstoreDbHelper;

import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_NAME;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_PRICE;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_QUANTITY;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.CONTENT_URI;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry._ID;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String LOG_TAG = CatalogActivity.class.getSimpleName();
    public final static int BOOK_LOADER = 0;
    private BookstoreDbHelper mDbHelper;
    private BookstoreCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Button button = findViewById(R.id.add_main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogActivity.this, EditorActivity.class));
            }
        });

        ListView bookListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        mCursorAdapter = new BookstoreCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentBookUri = ContentUris.withAppendedId(CONTENT_URI, id);

                intent.setData(currentBookUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null, this);

    }

    //Inserir dados no BD
    private void insertBook() {

        ContentValues values = new ContentValues();
        values.put(BookstoreContract.BookstoreEntry.COLUMN_BOOK_NAME,"O Sofista");
        values.put(BookstoreContract.BookstoreEntry.COLUMN_BOOK_PRICE,"120");
        values.put(BookstoreContract.BookstoreEntry.COLUMN_BOOK_QUANTITY, "4");
        values.put(BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_NAME,"AMAZON");
        values.put(BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_TEL, "55555555");

        Uri newUri = getContentResolver().insert(CONTENT_URI, values);
    }

    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from the database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                _ID,
                COLUMN_BOOK_NAME,
                COLUMN_BOOK_PRICE,
                COLUMN_BOOK_QUANTITY};

        return new CursorLoader(this,
                CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}







