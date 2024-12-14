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

    public User(){}

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

        DataSnapshot pfs= dbr.child("productsForSale").get().getResult();
        for (DataSnapshot snapshot : pfs.getChildren()) {
            String pid = snapshot.getValue(String.class);
            if (pid != null) {
                productsForSale.add(pid);
            }
        }
    }
    public User(String username, String nome, String cognome, String telefono, Integer cap, String indirizzo, String data, String imageUrl){
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.telefono=telefono;
        this.cap=cap;
        this.indirizzo=indirizzo;
        this.data=data;
        this.productsForSale=new ArrayList<>();
        this.imageUrl=imageUrl;
    }
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



    public void updateUsername(String uid, String username){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("username").setValue(username);
    }
    public void updateNome(String uid, String nome){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("nome").setValue(nome);
    }
    public void updateCognome(String uid, String cognome){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("cognome").setValue(cognome);
    }
    public void updateTelefono(String uid, String telefono){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("telefono").setValue(telefono);
    }
    public void updateCap(String uid, Integer cap){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("cap").setValue(cap);
    }
    public void updateIndirizzo(String uid, String indirizzo){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("indirizzo").setValue(indirizzo);
    }
    public void updateData(String uid, String data){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("data").setValue(data);
    }
    public void updateImageUrl(String uid, String imageUrl){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("imageUrl").setValue(imageUrl);
    }
    /*public void removeProductsForSale(String uid, String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("productsForSale").child();
    }*/





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