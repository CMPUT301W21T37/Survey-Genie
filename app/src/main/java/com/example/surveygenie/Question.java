package com.example.surveygenie;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Question implements Parcelable {
    private String question;
    private ArrayList<Reply> replys;


    public Question(String question) {
        this.question = question;
        this.replys = new ArrayList<Reply>();
    }

    public ArrayList<Reply> getReplys() {
        return replys;
    }

    public void addToReplys(Reply reply){
        this.replys.add(reply);
    }

    public String getQuestion() {
        return question;
    }


    protected Question(Parcel in) {
        question = in.readString();
        if (in.readByte() == 0x01) {
            replys = new ArrayList<Reply>();
            in.readList(replys, Reply.class.getClassLoader());
        } else {
            replys = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        if (replys == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(replys);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
