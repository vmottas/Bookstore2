package com.example.vinicius.bookstore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.vinicius.bookstore.data.BookstoreContract;

import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_NAME;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_PRICE;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_BOOK_QUANTITY;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_NAME;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry.COLUMN_SUPPLIER_TEL;
import static com.example.vinicius.bookstore.data.BookstoreContract.BookstoreEntry._ID;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri mCurrentBookUri;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierTelEditText;

    private String name;
    private String price;
    private String quantity;
    private String supplier;
    private String tel;

    private boolean mBookHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEVent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if (mCurrentBookUri == null) {
            setTitle(getString(R.string.title_add));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.title_edit));

            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        mNameEditText = findViewById(R.id.edit_book_name);
        mPriceEditText = findViewById(R.id.edit_book_price);
        mQuantityEditText = findViewById(R.id.edit_book_qunatity);
        mSupplierNameEditText = findViewById(R.id.edit_book_supplier);
        mSupplierTelEditText = findViewById(R.id.edit_book_phone);
    }

    private void saveBook() {
        ContentValues values = new ContentValues();

        name = mNameEditText.getText().toString().trim();
        price = mPriceEditText.getText().toString().trim();
        quantity = mQuantityEditText.getText().toString().trim();
        supplier = mSupplierNameEditText.getText().toString().trim();
        tel = mSupplierTelEditText.getText().toString().trim();

        values.put(COLUMN_BOOK_NAME, mNameEditText.getText().toString().trim());
        values.put(COLUMN_BOOK_PRICE, mPriceEditText.getText().toString().trim());
        values.put(COLUMN_BOOK_QUANTITY, mQuantityEditText.getText().toString().trim());
        values.put(COLUMN_SUPPLIER_NAME, mSupplierNameEditText.getText().toString().trim());
        values.put(COLUMN_SUPPLIER_TEL, mSupplierTelEditText.getText().toString().trim());

        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(mNameEditText.getText().toString().trim()) &&
                TextUtils.isEmpty(mPriceEditText.getText().toString().trim()) &&
                TextUtils.isEmpty(mQuantityEditText.getText().toString().trim()) &&
                TextUtils.isEmpty(mSupplierNameEditText.getText().toString().trim()) &&
                TextUtils.isEmpty(mSupplierTelEditText.getText().toString().trim())) {
            finish();
        } else {

            if (!TextUtils.isEmpty(name)) {
                values.put(COLUMN_BOOK_NAME, name);
            } else {
                displayError();
                return;
            }

            if (!TextUtils.isEmpty(price)) {
                values.put(COLUMN_BOOK_PRICE, price);
            } else {
                displayError();
                return;
            }

            if (!TextUtils.isEmpty(quantity)) {
                values.put(COLUMN_BOOK_QUANTITY, quantity);
            } else {
                displayError();
                return;
            }

            if (!TextUtils.isEmpty(supplier)) {
                values.put(COLUMN_SUPPLIER_NAME, supplier);
            } else {
                displayError();
                return;
            }

            if (!TextUtils.isEmpty(tel)) {
                values.put(COLUMN_SUPPLIER_TEL, tel);
            } else {
                displayError();
                return;
            }

            if (mCurrentBookUri == null) {
                Uri newUri = getContentResolver().insert(BookstoreContract.BookstoreEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.save_done), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    finish();
                }
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBook();
                //finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                _ID,
                COLUMN_BOOK_NAME,
                COLUMN_BOOK_PRICE,
                COLUMN_BOOK_QUANTITY,
                COLUMN_SUPPLIER_NAME,
                COLUMN_SUPPLIER_TEL
        };

        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            mNameEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME)));
            mPriceEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PRICE)));
            mQuantityEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_QUANTITY)));
            mSupplierNameEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_NAME)));
            mSupplierTelEditText.setText(cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_TEL)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierTelEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.discard_changes));
        builder.setPositiveButton(getString(R.string.dc_yes), discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.dc_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.book_delete_dialog));
        builder.setPositiveButton(getString(R.string.bdd_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBook();
            }
        });
        builder.setNegativeButton(getString(R.string.bdd_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void displayError() {
        if (TextUtils.isEmpty(mNameEditText.getText().toString().trim())) {
            mNameEditText.requestFocus();
            mNameEditText.setError(getString(R.string.set_erro_name));
        }

        if (TextUtils.isEmpty(mPriceEditText.getText().toString().trim())) {
            mPriceEditText.requestFocus();
            mPriceEditText.setError(getString(R.string.set_erro_price));
        }

        if (TextUtils.isEmpty(mQuantityEditText.getText().toString().trim())) {
            mQuantityEditText.requestFocus();
            mQuantityEditText.setError(getString(R.string.set_erro_quantity));
        }

        if (TextUtils.isEmpty(mSupplierNameEditText.getText().toString().trim())) {
            mSupplierNameEditText.requestFocus();
            mSupplierNameEditText.setError(getString(R.string.set_erro_supplier));
        }

        if (TextUtils.isEmpty(mSupplierTelEditText.getText().toString().trim())) {
            mSupplierTelEditText.requestFocus();
            mSupplierTelEditText.setError(getString(R.string.set_erro_supllier_tel));
        }
    }

    private void deleteBook() {
        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_done), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

