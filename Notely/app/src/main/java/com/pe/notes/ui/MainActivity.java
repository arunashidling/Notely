package com.pe.notes.ui;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.pe.notes.ui.R.id.nav_close;
import static com.pe.notes.ui.R.id.nav_favourite;
import static com.pe.notes.ui.R.id.nav_hearted;
import static com.pe.notes.ui.R.id.nav_poem;
import static com.pe.notes.ui.R.id.nav_story;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Notes> mNotesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter mAdapter;


    CoordinatorLayout contentView;
    NavigationView navigationView2;
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_NOTE,
            NotePad.Notes.COLUMN_NAME_CREATE_DATE,
            NotePad.Notes.COLUMN_NAME_FAVOURITE,
            NotePad.Notes.COLUMN_NAME_STAR
    };

    /** The index of the title column */
    private static final int COLUMN_INDEX_TITLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "You have chosen mail option", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));

            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        contentView = (CoordinatorLayout) findViewById(R.id.main_content_view);

        ImageButton menuRight = (ImageButton) findViewById(R.id.menuRight);

        navigationView2 = (NavigationView) findViewById(R.id.nav_view2);
        navigationView2.setNavigationItemSelectedListener(this);

        Button apply = (Button) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItem home = navigationView2.getMenu().findItem(nav_hearted);
                CompoundButton isHome = (CompoundButton) MenuItemCompat.getActionView(home);

                MenuItem bar = navigationView2.getMenu().findItem(nav_favourite);
                CompoundButton isBar = (CompoundButton) MenuItemCompat.getActionView(bar);

                if (isHome.isChecked()) {
                    Log.i("NOtely", "Filter home true");
                }
                if (isBar.isChecked()) {
                    Log.i("NOtely", "Filter bar true");
                }


                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });


        menuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                contentView.setTranslationX(-1 * ((navigationView2.getWidth()) * slideOffset));

            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        MenuItem switchItem = navigationView2.getMenu().findItem(nav_hearted);
        CompoundButton switchView = (CompoundButton) MenuItemCompat.getActionView(switchItem);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("Notely", "FINALLY ");
            }
        });


        Intent intent = getIntent();

        // If there is no data associated with the Intent, sets the data to the default URI, which
        // accesses a list of notes.
        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI);
        }

        /*
         * Sets the callback for context menu activation for the ListView. The listener is set
         * to be this Activity. The effect is that context menus are enabled for items in the
         * ListView, and the context menu is handled by a method in NotesList.
         */
       // getListView().setOnCreateContextMenuListener(this);

        /* Performs a managed query. The Activity handles closing and requerying the cursor
         * when needed.
         *
         * Please see the introductory note about performing provider operations on the UI thread.
         */
       /* Cursor cursor = managedQuery(
                getIntent().getData(),            // Use the default content URI for the provider.
                PROJECTION,                       // Return the note ID and title for each note.
                null,                             // No where clause, return all records.
                null,                             // No where clause, therefore no where column values.
                NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
        );*/

        /*
         * The following two arrays create a "map" between columns in the cursor and view IDs
         * for items in the ListView. Each element in the dataColumns array represents
         * a column name; each element in the viewID array represents the ID of a View.
         * The SimpleCursorAdapter maps them in ascending order to determine where each column
         * value will appear in the ListView.
         */

       /* // The names of the cursor columns to display in the view, initialized to the title column
        String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE } ;

        // The view IDs that will display the cursor columns, initialized to the TextView in
        // noteslist_item.xml
        int[] viewIDs = { android.R.id.text1 };

        // Creates the backing adapter for the ListView.
        SimpleCursorAdapter adapter
                = new SimpleCursorAdapter(
                this,                             // The Context for the ListView
                R.layout.noteslist_item,          // Points to the XML for a list item
                cursor,                           // The cursor to get items from
                dataColumns,
                viewIDs
        );

        // Sets the ListView's adapter to be the cursor adapter that was just created.
        setListAdapter(adapter);
*/

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new NotesAdapter(mNotesList, getApplicationContext(), getIntent().getData());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



       // prepareMovieData(cursor);

       /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Notes movie = mNotesList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = managedQuery(
                getIntent().getData(),            // Use the default content URI for the provider.
                PROJECTION,                       // Return the note ID and title for each note.
                null,                             // No where clause, return all records.
                null,                             // No where clause, therefore no where column values.
                NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
        );
        recyclerView.setAdapter(mAdapter);
        prepareMovieData(cursor);
    }

    private void prepareMovieData(Cursor cursor) {
        mNotesList.clear();
        if(cursor != null && cursor.moveToFirst()){
            do{
                long id = cursor.getLong(cursor.getColumnIndex(NotePad.Notes._ID));
                String mHeader = cursor.getString(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE));
                String mSubHeader = cursor.getString(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_NOTE));
                String mDate = cursor.getString(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_CREATE_DATE));
                int mFav = cursor.getInt(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_FAVOURITE));
                int mStar = cursor.getInt(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_STAR));

                Notes mNote = new Notes(mHeader, mSubHeader, mDate, mFav, mStar, id);
                mNotesList.add(mNote);

            }while (cursor.moveToNext());


        }
        //cursor.close();
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String text = "";
        if (id != nav_close) {
            CompoundButton switchView = (CompoundButton) MenuItemCompat.getActionView(item);

            if (id == nav_hearted) {
                text = getString(R.string.nav_hearted);
                if (switchView.isChecked())
                    switchView.setChecked(false);
                else
                    switchView.setChecked(true);

            } else if (id == nav_favourite) {
                text = getString(R.string.nav_favourite);
                if (switchView.isChecked())
                    switchView.setChecked(false);
                else
                    switchView.setChecked(true);
            } else if (id == nav_poem) {
                text = getString(R.string.nav_poems);
                if (switchView.isChecked())
                    switchView.setChecked(false);
                else
                    switchView.setChecked(true);
            } else if (id == nav_story) {
            text = getString(R.string.nav_story);
            if (switchView.isChecked())
                switchView.setChecked(false);
            else
                switchView.setChecked(true);
        }
        } else {
            text = "close";
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.END);
        }
        Toast.makeText(this, "You have chosen " + text, Toast.LENGTH_LONG).show();
        //      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //   drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    /**
     * This method is called when the user clicks a note in the displayed list.
     *
     * This method handles incoming actions of either PICK (get data from the provider) or
     * GET_CONTENT (get or create data). If the incoming action is EDIT, this method sends a
     * new Intent to start NoteEditor.
     * @param l The ListView that contains the clicked item
     * @param v The View of the individual item
     * @param position The position of v in the displayed list
     * @param id The row ID of the clicked item
     */
  /*  @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        // Constructs a new URI from the incoming URI and the row ID
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);

        // Gets the action from the incoming Intent
        String action = getIntent().getAction();

        // Handles requests for note data
        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {

            // Sets the result to return to the component that called this Activity. The
            // result contains the new URI
            setResult(RESULT_OK, new Intent().setData(uri));
        } else {

            // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
            // Intent's data is the note ID URI. The effect is to call NoteEdit.
            startActivity(new Intent(Intent.ACTION_EDIT, uri));
        }
    }*/





}
