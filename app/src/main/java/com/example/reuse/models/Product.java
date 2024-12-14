package com.example.reuse.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class Product {
    private String idVenditore;
    private String nome;
    private String descrizione;
    private double prezzo;
    private boolean baratto;
    private String imageUrl;

    public Product(){}

    //costruttore con tutti i dati
    public Product(String idVenditore, String nome, String descrizione, double prezzo, boolean baratto, String imageUrl){
        this.idVenditore=idVenditore;
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.baratto=baratto;
        this.imageUrl=imageUrl;
    }

    //costruttore oggetto senza idvenditore per aggiunta nuovo oggetto
    public Product(String nome, String descrizione, double prezzo, boolean baratto, String imageUrl){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.idVenditore=currentUser.getUid();
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.baratto=baratto;
        this.imageUrl=imageUrl;
    }

    //costruttore oggetto a partire dall'ID
    public Product(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        this.idVenditore=dbr.child("idVenditore").get().toString();
        this.nome=dbr.child("nome").get().toString();
        this.descrizione=dbr.child("descrizione").get().toString();
        this.prezzo=Double.parseDouble(dbr.child("prezzo").get().toString());
        this.baratto=Boolean.parseBoolean(dbr.child("baratto").get().toString());
        this.imageUrl=dbr.child("imageUrl").get().toString();
    }

    //update dell'oggetto sul database
    public void update(String pid, Product product){
        updateIdVentirore(pid, product.idVenditore);
        updateNome(pid,product.nome);
        updateDescrizione(pid, product.descrizione);
        updatePrezzo(pid, product.prezzo);
        updateBaratto(pid, product.baratto);
        updateImagineUrl(pid, product.imageUrl);
    }

    public void updateIdVentirore(String pid, String idVenditore){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("idVenditore").setValue(idVenditore);
    }
    public void updateNome(String pid, String nome){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("nome").setValue(nome);
    }
    public void updateDescrizione(String pid, String descrizione){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("descrizione").setValue(descrizione);
    }
    public void updatePrezzo(String pid, double prezzo){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("prezzo").setValue(prezzo);
    }
    public void updateBaratto(String pid, boolean baratto){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("idVenditore").setValue(baratto);
    }
    public void updateImagineUrl(String pid, String imageUrl){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        dbr.child("imageUrl").setValue(imageUrl);
    }

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

    public Double getPrezzo() {
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
}
