package com.pe.notes.ui;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by aruna on 22/01/18.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Notes> mNotesList;
    public Context mContext;
    public Uri data;


    public class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView mHeader, mSubHeading, mDate;
        public ImageButton mFav, mStar;
        public LinearLayout mcontentPanel;


        public NotesViewHolder(View view) {
            super(view);
            mHeader = (TextView) view.findViewById(R.id.text1);
            mSubHeading = (TextView) view.findViewById(R.id.subheading);
            mDate = (TextView) view.findViewById(R.id.date);
            mFav = (ImageButton) view.findViewById(R.id.fav);
            mStar = (ImageButton) view.findViewById(R.id.star);
            mcontentPanel = (LinearLayout) view.findViewById(R.id.contentPanel);

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
        holder.mDate.setText(note.getmDate());
        if(note.getmFavourite() == 1)
             holder.mFav.setBackgroundResource(R.drawable.heart_red);
        else
            holder.mFav.setBackgroundResource(R.drawable.heart_gray);

        if(note.getmStar() == 1)
            holder.mStar.setBackgroundResource(R.drawable.start_yello);
        else
            holder.mStar.setBackgroundResource(R.drawable.start_gray);

        holder.mcontentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = note.getId();
                Uri uri = ContentUris.withAppendedId(data, id);
                Intent intent = new Intent(Intent.ACTION_EDIT, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mFav.setBackgroundResource(R.drawable.heart_red);
            }
        });

        holder.mStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mStar.setBackgroundResource(R.drawable.start_yello);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }


}
