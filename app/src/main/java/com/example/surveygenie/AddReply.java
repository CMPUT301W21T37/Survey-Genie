package com.example.surveygenie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddReply extends DialogFragment {
    private EditText postedReply;
    private OnInteractionLisnter listener;


    public interface OnInteractionLisnter {
        void onOkPressed(Reply newReply);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddReply.OnInteractionLisnter){
            listener = (AddReply.OnInteractionLisnter) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_post_layout, null);
        postedReply = view.findViewById(R.id.post_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("New Post")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reply = postedReply.getText().toString();
                        listener.onOkPressed(new Reply(user.id,reply));
                    }
                }).create();
    }
}