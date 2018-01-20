package com.pe.notes.ui;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.pe.notes.ui.R.id.nav_close;
import static com.pe.notes.ui.R.id.nav_favourite;
import static com.pe.notes.ui.R.id.nav_hearted;
import static com.pe.notes.ui.R.id.nav_poem;
import static com.pe.notes.ui.R.id.nav_story;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CoordinatorLayout contentView;
    NavigationView navigationView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You have chosen mail option", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
}
