package com.example.MyFirstListView;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.provider.ContactsContract;

import static android.widget.LinearLayout.LayoutParams;

public class MainActivity extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor>
{

    // Reads contacts from the contacts database
    SimpleCursorAdapter contactListAdapter;

    // Fields to select from the contacts content provider
    static final String[] PROJECTION = new String[] {
        ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME};

    // Contact selection criteria
    static final String SELECTION = ("((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))");

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        installProgressBar();
        installListAdapter();
        
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    private void installProgressBar() {
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
    }

    private void installListAdapter() {
        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {R.id.textView};

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        contactListAdapter = new SimpleCursorAdapter(this, 
                R.layout.simple_list_item, null,
                fromColumns, toViews, 0);
        setListAdapter(contactListAdapter);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        contactListAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        contactListAdapter.swapCursor(null);
    }

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
