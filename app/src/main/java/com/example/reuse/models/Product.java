package com.example.reuse.models;

import java.io.Serializable;
import java.util.List;

public class Product {
    private String idVenditore;
    private String nome;
    private String descrizione;
    private double prezzo;
    private boolean baratto;

    public Product(){}
    public Product(String idVenditore, String nome, String descrizione, double prezzo, boolean baratto){
        this.idVenditore=idVenditore;
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.baratto=baratto;
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
}
