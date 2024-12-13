package com.example.reuse.models;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class User {

    private String username;
    private String nome;
    private String cognome;
    private Integer cap;
    private String indirizzo;
    private String data;
    private List<Product> productsForSale;
    private Uri imageUri;

    public User(){}
    public User(String username, String nome, String cognome, Integer cap, String indirizzo, String data, Uri imageUri){
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.cap=cap;
        this.indirizzo=indirizzo;
        this.data=data;
        this.productsForSale = new ArrayList<>();
        this.imageUri=imageUri;
    }

    public List<Product> getProductsForSale() {
        return productsForSale;
    }
    // Metodo per aggiungere un prodotto
    public void addProduct(Product product) {
        productsForSale.add(product);
    }
    // Metodo per rimuovere un prodotto
    public void removeProduct(Product product) {
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

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}