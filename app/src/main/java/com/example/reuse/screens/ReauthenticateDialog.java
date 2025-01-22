package com.example.reuse.screens;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.reuse.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReauthenticateDialog extends DialogFragment {

    private String pw;
    private EditText passwordAuth;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public ReauthenticateDialog(String pass){
        pw = pass;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user==null) return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reauthenticate, null);
        builder.setView(view);

        passwordAuth = view.findViewById(R.id.passwordAuth);
        Button submit = view.findViewById(R.id.authButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = passwordAuth.getText().toString();
                reauthenticateUser(pw);
            }
        });
        return builder.create();
    }

    private void reauthenticateUser(String password){
        AuthCredential credit = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credit).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                updateEmail();
                Toast.makeText(getActivity(), "Login succerfull", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            else Toast.makeText(getActivity(), "Re-authentication failed.", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateEmail(){
        user.updatePassword(pw);
    }
}
