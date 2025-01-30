package com.example.reuse.models;


import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Messaggio{
    private String id="";
    private String idMittente;
    private String dataora;
    private boolean tipo;
    private String contenuto;


    //costruttore dell'ggetto a partire da Id Chat e Id Messaggio
    public Messaggio(String mid, String cid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Chats").child(cid).child("messaggi").child(mid);
        this.id=mid;
        dbr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                this.idMittente = snapshot.child("idMittente").getValue(String.class);
                this.dataora = snapshot.child("dataora").getValue(String.class);
                this.tipo = snapshot.child("tipo").getValue(Boolean.class);
                this.contenuto = snapshot.child("contenuto").getValue(String.class);
            }
        });
    }

    //costruttore vuoto per richiamare metodi anche con oggetto vuoto
    public Messaggio(){
        idMittente="";
        dataora="";
        tipo=false;
        contenuto="";
    }

    //costruttore manuale
    public Messaggio(String idMittente, String dataora, boolean tipo, String contenuto){
        this.idMittente=idMittente;
        this.dataora=dataora;
        this.tipo=tipo;
        this.contenuto=contenuto;
    }

    //costruttore data automatica
    public Messaggio(String idMittente, boolean tipo, String contenuto){
        this.idMittente=idMittente;
        this.tipo=tipo;
        this.contenuto=contenuto;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.dataora=formatter.format(date);
    }

    //per caricare un oggetto Messaggio con contenuto di testo sul database
    //Messaggio m=new Messaggio()
    //m.addDatabase(...)
    public void addDatabase(String idMittente, String contenuto, String idChat){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats").child(idChat).child("messaggi");
        this.id = dbRef.push().getKey();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dataora=formatter.format(date);
        Messaggio m=new Messaggio(idMittente, dataora, false, contenuto);
        m.add(idChat, this.id);
    }

    //per caricare un oggetto Messaggio con contenuto immagine sul database
    //Messaggio m=new Messaggio()
    //m.addDatabase(...)
    public String addDatabase(String idMittente, Uri imageUri, String idChat){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats").child(idChat).child("messaggi");
        this.id = dbRef.push().getKey();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dataora=formatter.format(date);
        String imageUrl=uploadImage(imageUri, this.id);
        Messaggio m=new Messaggio(idMittente, dataora, true, imageUrl);
        return m.add(idChat, this.id);
    }
    private String uploadImage(Uri imageUri, String mid){
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("ChatImages/" + mid);
        if (imageUri != null) {
            ref.putFile(imageUri);
            return ref.getDownloadUrl().toString();
        }
        return "";
    }

    private String add(String idChat, String mid){
        DatabaseReference messaggio=FirebaseDatabase.getInstance().getReference("Chats").child(idChat).child("messaggi").child(mid);
        messaggio.child("idMittente").setValue(idMittente);
        messaggio.child("dataora").setValue(dataora);
        messaggio.child("tipo").setValue(tipo);
        messaggio.child("contenuto").setValue(contenuto);
        return mid;
    }
}
