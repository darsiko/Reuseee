package com.example.reuse.models;


import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private String id="";
    private String idUtente1;
    private String idUtente2;
    private List<Messaggio> messaggi;




    //costruttore se servono metodi anche con oggetto vuoto
    public Chat() {
        this.id="";
        this.idUtente1="";
        this.idUtente2="";
        this.messaggi=new ArrayList<>();
    }
    //costruttore manuale
    public Chat(String idUtente1, String idUtente2, List<Messaggio> messaggi){
        this.idUtente1=idUtente1;
        this.idUtente2=idUtente2;
        this.messaggi=messaggi;
    }








    //UTILIZZARE
    //crea oggetto chat a partire dall'id (prendetelo dalla lista delle chat dell'utente)
    public Chat(String id){
        this.id=id;
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Chats").child(this.id);
        dbr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                this.idUtente1 = snapshot.child("idUtente1").getValue(String.class);
                this.idUtente2 = snapshot.child("idUtente2").getValue(String.class);

                this.messaggi = new ArrayList<>();
                for (DataSnapshot mSnapshot : snapshot.child("chats").getChildren()) {
                    String mid = mSnapshot.getKey();
                    Messaggio m = new Messaggio(mid, this.id);
                    messaggi.add(m);
                }
            }
        });
    }

    //UTILIZZARE
    //costruttore per nuova chat
    public Chat(String idUtente1, String idUtente2){
        this.idUtente1=idUtente1;
        this.idUtente2=idUtente2;
        this.messaggi=new ArrayList<>();
    }


    //UTILIZZARE
    //Chat c=new Chat(idUtente1, idUtente2);
    //c.uploadChat() per caricare la chat nel database
    public void uploadChat(){
        //check se esiste già chat tra utenti
        DatabaseReference refU1 = FirebaseDatabase.getInstance().getReference("Users").child(idUtente1).child("chats");
        refU1.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if (task.getResult().exists()) {
                    DatabaseReference refU2 = FirebaseDatabase.getInstance().getReference("Users").child(idUtente2).child("chats");
                    refU2.get().addOnCompleteListener(task2 -> {
                        for (DataSnapshot snapshot1 : task.getResult().getChildren()) {
                            for (DataSnapshot snapshot2 : task2.getResult().getChildren()) {
                                if (snapshot1.getValue(String.class)==snapshot2.getValue(String.class)){
                                    Chat c=new Chat(snapshot2.getValue(String.class));
                                    this.id=c.getId();
                                    this.idUtente1=c.getIdUtente1();
                                    this.idUtente2=c.getIdUtente2();
                                    this.messaggi=c.getMessaggi();
                                    System.out.println("La Chat esiste già");
                                    return;
                                }
                            }
                        }
                    });
                }
            }
        });

        //se non esiste
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        id = dbRef.push().getKey();
        DatabaseReference ref=dbRef.child(id);
        ref.child("idUtente1").setValue(idUtente1);
        ref.child("idUtente2").setValue(idUtente2);
        ref.child("messaggi").setValue(messaggi);

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(idUtente1).child("chats");
        dbr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> chats = new ArrayList<>();

                // Retrieve the existing list, if any
                if (task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String chatId = snapshot.getValue(String.class);
                        if (chatId != null) {
                            chats.add(chatId);
                        }
                    }
                }
                chats.add(id);
                dbr.setValue(chats)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                System.out.println("Product added to 'productsForSale' successfully.");
                            } else {
                                System.out.println("Failed to update 'productsForSale': " + updateTask.getException().getMessage());
                            }
                        });
            }else{
                System.out.println("Failed to retrieve 'productsForSale': " + task.getException().getMessage());
            }
        });
        DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("Users").child(idUtente2).child("chats");
        dbr2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> chats = new ArrayList<>();

                // Retrieve the existing list, if any
                if (task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String chatId = snapshot.getValue(String.class);
                        if (chatId != null) {
                            chats.add(chatId);
                        }
                    }
                }
                chats.add(id);
                dbr2.setValue(chats)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                System.out.println("Product added to 'productsForSale' successfully.");
                            } else {
                                System.out.println("Failed to update 'productsForSale': " + updateTask.getException().getMessage());
                            }
                        });
            }else{
                System.out.println("Failed to retrieve 'productsForSale': " + task.getException().getMessage());
            }
        });



        //FirebaseDatabase.getInstance().getReference("Users").child(idUtente1).child("chats").push().setValue(id);
        //FirebaseDatabase.getInstance().getReference("Users").child(idUtente2).child("chats").push().setValue(id);
    }


    //UTILIZZARE
    //aggiungi messaggio testuale alla chat;
    public void addMessaggio(String idMittente, String contenuto){
        Messaggio m=new Messaggio();
        m.addDatabase(idMittente, contenuto, id);
    }

    //UTILIZZARE
    //aggiungi messaggio immagine alla chat;
    public void addMessaggio(String idMittente, Uri imageUri){
        Messaggio m=new Messaggio();
        m.addDatabase(idMittente, imageUri, id);
    }


    //ancora da implementare
    public String trovaId(String idUtente1, String idUtente2){ return "";}

    public String getId(){
        return id;
    }
    public String getIdUtente1(){
        return idUtente1;
    }
    public String getIdUtente2(){
        return idUtente2;
    }
    public List<Messaggio> getMessaggi(){
        return messaggi;
    }
}
