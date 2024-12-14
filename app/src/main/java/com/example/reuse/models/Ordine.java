package com.example.reuse.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Ordine {
    private String aid; //id acquirente
    private String pid; //id prodotto
    private String data; //data di acquisto







    //costruttori
    public Ordine() {
    }
    //costruttore completo
    public Ordine(String aid, String pid, String data) {
        this.aid=aid;
        this.pid=pid;
        this.data=data;
    }
    //costruttore per creazione ordine
    public Ordine(String pid, String aid) {
        this.pid=pid;
        this.aid=aid;
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.data=dateFormat.format(currentDate);
    }
    //crea oggetto Ordine dall'ID prendendo i dati dal database
    public Ordine(String oid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Ordini").child(oid);
        this.pid=dbr.child("idProdotto").get().toString();
        this.aid=dbr.child("idAcquirente").get().toString();
        this.data=dbr.child("data").get().toString();
    }









    //aggiungi ordine al database
    public void addOrdine(){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Ordini");
        String oid=dbr.push().getKey();
        dbr.child(oid).setValue(this);

        DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("Products").child(this.pid);
        dbr2.child("idOrdine").setValue(oid);

        DatabaseReference dbr3 = FirebaseDatabase.getInstance().getReference("Users").child(this.aid).child("productsBought");
        dbr3.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Recupera i dati esistenti (se presenti)
                List<String> productsBought = new ArrayList<>();
                if (task.getResult().exists()) {
                    productsBought = (List<String>) task.getResult().getValue();
                }
                // Aggiungi il nuovo productId alla lista
                productsBought.add(this.pid);
                // Aggiorna la lista nel database
                dbr3.setValue(productsBought)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                System.out.println("Product added successfully!");
                            } else {
                                System.out.println("Failed to add product: " + updateTask.getException().getMessage());
                            }
                        });
            } else {
                System.out.println("Error getting data: " + task.getException().getMessage());
            }
        });
    }





    //getter e setter dell'oggetto (non dal database)
    public String getIdAcquirente() {
        return aid;
    }
    public void setIdAcquirente(String aid) {
        this.aid = aid;
    }
    public String getIdProduct() {
        return pid;
    }
    public void setIdProduct(String pid) {
        this.pid = pid;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}

