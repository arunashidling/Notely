package com.pe.notes.ui;

import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by aruna on 22/01/18.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>  {

    private List<Notes> mNotesList;
    public Context mContext;
    public Uri data;


    public class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView mHeader, mSubHeading, mDate;

        public LinearLayout mcontentPanel;
        public CheckBox mFav, mStar;
        public RelativeLayout viewBackground;
        public RelativeLayout  viewForeground;


        public NotesViewHolder(View view) {
            super(view);
            mHeader = (TextView) view.findViewById(R.id.text1);
            mSubHeading = (TextView) view.findViewById(R.id.subheading);
            mDate = (TextView) view.findViewById(R.id.date);
            mFav = (CheckBox) view.findViewById(R.id.fav);
            mStar = (CheckBox) view.findViewById(R.id.star);
            mcontentPanel = (LinearLayout) view.findViewById(R.id.contentPanel);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);

        }
    }

    public NotesAdapter(List<Notes> mNotesList, Context mContext, Uri data) {
        this.mNotesList = mNotesList;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noteslist_item, parent, false);



        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotesViewHolder holder, int position) {
        final Notes note = mNotesList.get(position);
        holder.mHeader.setText(note.getmHeader());
        holder.mSubHeading.setText(note.getmSubHeading());
        try {
            holder.mDate.setText(CommonUtils.formatToYesterdayOrToday(note.getmDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(note.getmFavourite() == 1) {
            holder.mFav.setChecked(true);
            holder.mFav.setButtonDrawable(R.drawable.heart_red);
        }
        else {
            holder.mFav.setChecked(false);
            holder.mFav.setButtonDrawable(R.drawable.heart_gray);
        }

        holder.mFav.setTag(note);

        if(note.getmStar() == 1) {
            holder.mStar.setChecked(true);
            holder.mStar.setButtonDrawable(R.drawable.start_yello);
        }
        else {
            holder.mStar.setChecked(false);
            holder.mStar.setButtonDrawable(R.drawable.start_gray);
        }
        holder.mStar.setTag(note);

        holder.mcontentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = note.getId();
                Uri uri = ContentUris.withAppendedId(data, id);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.mFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Notes note = (Notes) compoundButton.getTag();
                if(compoundButton.isChecked()){
                    compoundButton.setButtonDrawable(R.drawable.heart_red);
                    note.setmFavourite(1);
                }else{
                    compoundButton.setButtonDrawable(R.drawable.heart_gray);
                    note.setmFavourite(0);
                }

                updateNote(note);
            }
        });


        holder.mStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Notes note = (Notes) compoundButton.getTag();
                if(compoundButton.isChecked()){
                    compoundButton.setButtonDrawable(R.drawable.start_yello);
                    note.setmStar(1);
                }else{
                    compoundButton.setButtonDrawable(R.drawable.start_gray);
                    note.setmStar(0);
                }

                updateNote(note);
            }
        });
    }

    private void updateNote(Notes note) {
               // Sets up a map to contain values to be updated in the provider.
            ContentValues values = new ContentValues();
            values.put(NotePad.Notes.COLUMN_NAME_FAVOURITE, note.getmFavourite());
            values.put(NotePad.Notes.COLUMN_NAME_STAR, note.getmStar());

            mContext.getContentResolver().update(
                    data,    // The URI for the record to update.
                    values,  // The map of column names and new values to apply to them.
                    NotePad.Notes.COLUMN_NAME_TITLE +" =?",    // No selection criteria are used, so no where columns are necessary.
                    new String[] {note.getmHeader()}     // No where columns are used, so no where arguments are necessary.
            );



    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    public void removeItem(int position) {
        deleteNote(mNotesList.get(position));
        mNotesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Notes item, int position) {
        mNotesList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    private final void deleteNote(Notes notes) {
        Uri mUri = ContentUris.withAppendedId(data, notes.getId());
        mContext.getContentResolver().delete(mUri, null, null);

    }
}



