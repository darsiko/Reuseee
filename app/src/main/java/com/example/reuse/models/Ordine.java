package com.example.reuse.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ordine {
    private String aid; //id acquirente
    private String pid; //id prodotto
    private String data; //data di acquisto
    private String status; //status della spedizione

    //costruttori
    public Ordine(){}

    public Ordine(String aid, String pid, String data, String status){
        this.aid=aid;
        this.pid=pid;
        this.data=data;
        this.status=status;
    }

}

