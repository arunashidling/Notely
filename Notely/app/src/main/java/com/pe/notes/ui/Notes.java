package com.pe.notes.ui;

/**
 * Created by aruna on 22/01/18.
 */

public class Notes {
    private long id;
    private String mHeader, mSubHeading, mDate;
    private int mFavourite, mStar;

    public Notes(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Notes(String mHeader, String mSubHeading, String mDate, int mFavourite,
                 int mStar, long id){
        this.id = id;
        this.mHeader = mHeader;
        this.mSubHeading = mSubHeading;
        this.mDate = mDate;
        this.mFavourite = mFavourite;
        this.mStar = mStar;
    }


    public String getmHeader() {
        return mHeader;
    }

    public void setmHeader(String mHeader) {
        this.mHeader = mHeader;
    }

    public String getmSubHeading() {
        return mSubHeading;
    }

    public void setmSubHeading(String mSubHeading) {
        this.mSubHeading = mSubHeading;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmFavourite() {
        return mFavourite;
    }

    public void setmFavourite(int mFavourite) {
        this.mFavourite = mFavourite;
    }

    public int getmStar() {
        return mStar;
    }

    public void setmStar(int mStar) {
        this.mStar = mStar;
    }


}
