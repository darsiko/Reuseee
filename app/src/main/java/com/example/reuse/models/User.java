package com.example.reuse.models;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;



public class User {

    private String username;
    private String nome;
    private String cognome;
    private Integer cap;
    private String indirizzo;
    private String data;
    private String telefono;
    private List<String> productsForSale;
    private String imageUrl;


    //costruttori
    public User(){}

    //costruttore prende dati dal database
    public User(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        this.username=dbr.child("username").get().toString();
        this.nome=dbr.child("nome").get().toString();
        this.cognome=dbr.child("cognome").get().toString();
        this.telefono=dbr.child("telefono").get().toString();
        this.cap=Integer.parseInt(dbr.child("cap").get().toString());
        this.indirizzo=dbr.child("indirizzo").get().toString();
        this.data=dbr.child("data").get().toString();
        this.imageUrl=dbr.child("imageUrl").get().toString();

        this.productsForSale=new ArrayList<>();
        DataSnapshot pfs= dbr.child("productsForSale").get().getResult();
        for (DataSnapshot snapshot : pfs.getChildren()) {
            String pid = snapshot.getValue(String.class);
            if (pid != null) {
                this.productsForSale.add(pid);
            }
        }
    }

    //costruttore per creazione User
    public User(String username, String nome, String cognome, String telefono, Integer cap, String indirizzo, String data){
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.telefono=telefono;
        this.cap=cap;
        this.indirizzo=indirizzo;
        this.data=data;
        this.productsForSale=new ArrayList<>();
        this.imageUrl="imageUrl";
    }

    //costruttore
    public User(String username, String nome, String cognome, String telefono, Integer cap, String indirizzo, String data, String imageUrl, List<String> productsForSale){
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.telefono=telefono;
        this.cap=cap;
        this.indirizzo=indirizzo;
        this.data=data;
        this.productsForSale=productsForSale;
        this.imageUrl=imageUrl;
    }


    //update per i dati del profilo
    public void updateProfilo(String uid){
        updateUsername(uid);
        updateNome(uid);
        updateCognome(uid);
        updateTelefono(uid);
        updateCap(uid);
        updateIndirizzo(uid);
        updateImageUrl(uid);
        updateData(uid);
    }


    //rimuovi oggetto dalla lista di quelli venduti
    public void removeProductForSale(String pid, String uid){
        DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("productsForSale");
        dbr2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Recupera i dati esistenti (se presenti)
                List<String> productsForSale = new ArrayList<>();
                if (task.getResult().exists()) {
                    productsForSale = (List<String>) task.getResult().getValue();
                }
                // Aggiungi il nuovo productId alla lista
                productsForSale.remove(pid);
                // Aggiorna la lista nel database
                dbr2.setValue(productsForSale)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                System.out.println("Product removed successfully!");
                                //elima prodotto dal database
                                Product product=new Product(pid);
                                product.delete(pid);
                            } else {
                                System.out.println("Failed to remove product: " + updateTask.getException().getMessage());
                            }
                        });
            } else {
                System.out.println("Error getting data: " + task.getException().getMessage());
            }
        });
    }

    //non vi servono
    private void updateUsername(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("username").setValue(username);
    }
    private void updateNome(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("nome").setValue(nome);
    }
    private void updateCognome(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("cognome").setValue(cognome);
    }
    private void updateTelefono(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("telefono").setValue(telefono);
    }
    private void updateCap(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("cap").setValue(cap);
    }
    private void updateIndirizzo(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("indirizzo").setValue(indirizzo);
    }
    private void updateData(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("data").setValue(data);
    }
    private void updateImageUrl(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("imageUrl").setValue(imageUrl);
    }




    //getter e setter dell'oggetto (non dal database)
    public List<String> getProductsForSale() {
        return productsForSale;
    }
    // Metodo per aggiungere un prodotto
    public void addProduct(String product) {
        productsForSale.add(product);
    }
    // Metodo per rimuovere un prodotto
    public void removeProduct(String product) {
        productsForSale.remove(product);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}