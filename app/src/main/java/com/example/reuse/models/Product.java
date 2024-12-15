package com.example.reuse.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private String idVenditore;
    private String nome;
    private String descrizione;
    private double prezzo;
    private boolean baratto;
    private String imageUrl;
    private String idOrdine;



    //costruttori
    public Product(){}
    //costruttore con tutti i dati
    public Product(String idVenditore, String nome, String descrizione, double prezzo, boolean baratto, String imageUrl, String idOrdine){
        this.idVenditore=idVenditore;
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.baratto=baratto;
        this.imageUrl=imageUrl;
        this.idOrdine=idOrdine;
    }
    //costruttore per aggiunta oggetto in vendita
    public Product(String nome, String descrizione, double prezzo, boolean baratto, String imageUrl){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.idVenditore=currentUser.getUid();
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.baratto=baratto;
        this.imageUrl=imageUrl;
        this.idOrdine="";
    }
    //download dell'oggetto dal database
    public Product(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        this.idVenditore=dbr.child("idVenditore").get().toString();
        this.nome=dbr.child("nome").get().toString();
        this.descrizione=dbr.child("descrizione").get().toString();
        this.prezzo=Double.parseDouble(dbr.child("prezzo").get().toString());
        this.baratto=Boolean.parseBoolean(dbr.child("baratto").get().toString());
        this.imageUrl=dbr.child("imageUrl").get().toString();
        this.idOrdine=dbr.child("idOrdine").get().toString();
    }
    //metodo per recuperare lo username del venditore
    //agiungi l'oggetto al database
    public void addProduct(){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products");
        String pid=dbr.push().getKey();
        dbr.child(pid).setValue(this);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("productsForSale");
        dbr2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Recupera i dati esistenti (se presenti)
                List<String> productsForSale = new ArrayList<>();
                if (task.getResult().exists()) {
                    productsForSale = (List<String>) task.getResult().getValue();
                }
                // Aggiungi il nuovo productId alla lista
                productsForSale.add(pid);
                // Aggiorna la lista nel database
                dbr2.setValue(productsForSale)
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
    //modifica dell'oggetto sul database
    public void update(String pid){
        updateIdVentirore(pid);
        updateNome(pid);
        updateDescrizione(pid);
        updatePrezzo(pid);
        updateBaratto(pid);
        updateImagineUrl(pid);
        updateIdOrdine(pid);
    }










    //non utilizzare, servono ad altro
    public void delete(String pid){
        DatabaseReference userToDeleteRef = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        userToDeleteRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Nodo rimosso con successo
                System.out.println("Nodo " + pid + " rimosso con successo.");
            } else {
                // Gestione dell'errore
                System.err.println("Errore nella rimozione del nodo: " + task.getException());
            }
        });
    }
    private void updateIdOrdine(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("idOrdine").setValue(idOrdine);
    }
    //non vi servono
    private void updateIdVentirore(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("idVenditore").setValue(idVenditore);
    }
    private void updateNome(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("nome").setValue(nome);
    }
    private void updateDescrizione(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("descrizione").setValue(descrizione);
    }
    private void updatePrezzo(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("prezzo").setValue(prezzo);
    }
    private void updateBaratto(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("idVenditore").setValue(baratto);
    }
    private void updateImagineUrl(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("imageUrl").setValue(imageUrl);
    }







    //getter e setter dell'oggetto (non dal database)
    public String getIdVenditore() {
        return idVenditore;
    }

    

    public void setIdVenditore(String idVenditore) {
        this.idVenditore = idVenditore;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public boolean isBaratto() {
        return baratto;
    }

    public void setBaratto(boolean baratto) {
        this.baratto = baratto;
    }

    public String getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(String idOrdine) {
        this.idOrdine = idOrdine;
    }
}
