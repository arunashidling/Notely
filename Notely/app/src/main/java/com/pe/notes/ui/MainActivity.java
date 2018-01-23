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
import android.text.TextUtils;
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
import java.util.HashMap;
import java.util.List;

import static com.pe.notes.ui.R.id.bottom;
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
    MenuItem navHearted, navStar;
    CompoundButton navHeartCheckbox, navStarCheckbox;

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
                MenuItem heart = navigationView2.getMenu().findItem(nav_hearted);
                CompoundButton isHeart = (CompoundButton) MenuItemCompat.getActionView(heart);

                MenuItem star = navigationView2.getMenu().findItem(nav_favourite);
                CompoundButton isStar = (CompoundButton) MenuItemCompat.getActionView(star);

                CommonUtils.setFilters(getApplicationContext(), isHeart.isChecked(), isStar.isChecked());
                applyFilter(isHeart.isChecked(),isStar.isChecked());


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


         navHearted = navigationView2.getMenu().findItem(nav_hearted);
         navHeartCheckbox = (CompoundButton) MenuItemCompat.getActionView(navHearted);

        navStar = navigationView2.getMenu().findItem(nav_favourite);
        navStarCheckbox = (CompoundButton) MenuItemCompat.getActionView(navStar);

        Intent intent = getIntent();

        // If there is no data associated with the Intent, sets the data to the default URI, which
        // accesses a list of notes.
        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new NotesAdapter(mNotesList, getApplicationContext(), getIntent().getData());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void applyFilter(boolean isHeartChecked, boolean isStarChecked) {
        String where = null;
        String[] selection = null;
        if(isHeartChecked && isStarChecked){
            where = NotePad.Notes.COLUMN_NAME_FAVOURITE + " =?" + " AND " + NotePad.Notes.COLUMN_NAME_STAR + " =?";
            selection = new String[]{"1", "1"};
        }else if(isHeartChecked){
            where = NotePad.Notes.COLUMN_NAME_FAVOURITE + " =?";
            selection = new String[]{"1"};
        }else if(isStarChecked){
            where = NotePad.Notes.COLUMN_NAME_STAR + " =?";
            selection = new String[]{"1"};
        }

            Cursor cursor = managedQuery(
                    getIntent().getData(),            // Use the default content URI for the provider.
                    PROJECTION,                       // Return the note ID and title for each note.
                    where,                             // No where clause, return all records.
                    selection,                             // No where clause, therefore no where column values.
                    NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
            );

        prepareNoteData(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView.setAdapter(mAdapter);
        boolean isHeartFilter = false;
        boolean isStarFilter = false;
        if(CommonUtils.getIsFilterApplied(getApplicationContext())){

            HashMap<String, Boolean> filterMap = new HashMap<>();
            filterMap = CommonUtils.getAppliedFilters(getApplicationContext());
            if(filterMap.containsKey(CommonUtils.IS_FILTER_HEART_APPLIED)){
                isHeartFilter =filterMap.get(CommonUtils.IS_FILTER_HEART_APPLIED);
            }
            if(filterMap.containsKey(CommonUtils.IS_FILTER_STAR_APPLIED)){
                isStarFilter =filterMap.get(CommonUtils.IS_FILTER_STAR_APPLIED);
            }

        }
        applyFilter(isHeartFilter, isStarFilter);
        navHeartCheckbox.setChecked(isHeartFilter);
        navStarCheckbox.setChecked(isStarFilter);
     //   prepareNoteData(cursor);
    }

    private void prepareNoteData(Cursor cursor) {
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

                if (switchView.isChecked())
                    switchView.setChecked(false);
                else
                    switchView.setChecked(true);

            } else if (id == nav_favourite) {

                if (switchView.isChecked())
                    switchView.setChecked(false);
                else
                    switchView.setChecked(true);
            } else if (id == nav_poem) {
                text = getString(R.string.nav_poems);
                Toast.makeText(this, "Implementation in progress for " + text, Toast.LENGTH_LONG).show();
            } else if (id == nav_story) {
            text = getString(R.string.nav_story);
                Toast.makeText(this, "Implementation in progress for " + text, Toast.LENGTH_LONG).show();
        }
        } else {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.END);
        }



        return true;
    }


}
