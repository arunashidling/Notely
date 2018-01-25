package com.pe.notes.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;


public class ViewNoteActivity extends Activity {
    Button edit;
    ImageButton back;
    TextView note,date, header;
    private Uri mUri;
    private Cursor mCursor;

    private static final String TAG = "ViewNoteActivity";

    private static final String[] PROJECTION =
            new String[] {
                    NotePad.Notes._ID,
                    NotePad.Notes.COLUMN_NAME_TITLE,
                    NotePad.Notes.COLUMN_NAME_NOTE,
                    NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();


        /*
         *  Sets up for the edit, based on the action specified for the incoming Intent.
         */

        // Gets the action that triggered the intent filter for this Activity
        final String action = intent.getAction();

        // For an edit action:
        if (Intent.ACTION_VIEW.equals(action)) {

            // Sets the Activity state to EDIT, and gets the URI for the data to be edited.

            mUri = intent.getData();

            // For an insert or paste action:
        } else  {

            // Logs an error that the action was not understood, finishes the Activity, and
            // returns RESULT_CANCELED to an originating Activity.
            Log.e(TAG, "Unknown action, exiting");
            finish();
            return;
        }

        /*
         * Using the URI passed in with the triggering Intent, gets the note or notes in
         * the provider.
         * Note: This is being done on the UI thread. It will block the thread until the query
         * completes. In a sample app, going against a simple provider based on a local database,
         * the block will be momentary, but in a real app you should use
         * android.content.AsyncQueryHandler or android.os.AsyncTask.
         */
        mCursor = managedQuery(
                mUri,         // The URI that gets multiple notes from the provider.
                PROJECTION,   // A projection that returns the note ID and note content for each note.
                null,         // No "where" clause selection criteria.
                null,         // No "where" clause selection values.
                null          // Use the default sort order (modification date, descending)
        );


      //  getActionBar().show();



        setContentView(R.layout.activity_view_note);
        edit = (Button) findViewById(R.id.viewedit);
        back = (ImageButton) findViewById(R.id.viewback);
        note = (TextView) findViewById(R.id.noteview);
        date = (TextView) findViewById(R.id.dateview);
        header = (TextView) findViewById(R.id.headerview);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // long id = note.getId();
                //Uri uri = ContentUris.withAppendedId(data, id);
                Intent intent = new Intent(Intent.ACTION_EDIT, mUri);

                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCursor != null) {
            // Requery in case something changed while paused (such as the title)
            mCursor.requery();

            /* Moves to the first record. Always call moveToFirst() before accessing data in
             * a Cursor for the first time. The semantics of using a Cursor are that when it is
             * created, its internal index is pointing to a "place" immediately before the first
             * record.
             */
            mCursor.moveToFirst();
            int colTitleIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE);
            String title = mCursor.getString(colTitleIndex);
            header.setText(title);

            int colNotesIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_NOTE);
            String noteText = mCursor.getString(colNotesIndex);
            note.setText(noteText);

            int colDateIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE);
            long notedate = mCursor.getLong(colDateIndex);
            try {
                date.setText(getResources().getString(R.string.lastupdated) + CommonUtils.formatToYesterdayOrToday(notedate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
