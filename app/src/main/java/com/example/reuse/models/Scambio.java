package com.example.reuse.models;

import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Scambio {
    private String idOfferente = "";
    private double soldiOfferente = 0;
    private double soldiRicevente = 0;
    private List<String> listaOfferente = new ArrayList<>();
    private List<String> listaRicevente = new ArrayList<>();

    public Scambio(){}

    public Scambio(String idOfferente, double soldiOfferente, double soldiRicevente, List<String> listaOfferente,List<String> listaRicevente){
        this.idOfferente=idOfferente;
        this.soldiRicevente=soldiRicevente;
        this.soldiOfferente=soldiOfferente;
        this.listaRicevente.addAll(listaRicevente);
        this.listaOfferente.addAll(listaOfferente);
    }

    //Costruttore per nuova offerta (riempire prima di uploadare)
    public Scambio(String idOfferente){
        this.idOfferente=idOfferente;
        soldiOfferente=0;
        soldiRicevente=0;
        listaOfferente=new ArrayList<>();
        listaRicevente=new ArrayList<>();
    }

    public Scambio(Scambio s){
        if(s!=null){
            this.idOfferente=s.idOfferente;
            this.soldiOfferente=s.soldiOfferente;
            this.soldiRicevente=s.soldiRicevente;
            this.listaOfferente.addAll(s.listaOfferente);
            this.listaRicevente.addAll(s.listaRicevente);
        }
    }

    public double getSoldiOfferente(){
        return soldiOfferente;
    }
    public void setSoldiOfferente(double soldiOfferente){
        this.soldiOfferente=soldiOfferente;
    }

    public double getSoldiRicevente(){
        return soldiRicevente;
    }
    public void setSoldiRicevente(double soldiRicevente){
        this.soldiRicevente=soldiRicevente;
    }

    public List<String> getListaOfferente() {
        return listaOfferente;
    }
    public List<String> getListaRicevente() {
        return listaRicevente;
    }

    public String getIdOfferente() {
        return idOfferente;
    }

    public void addListaOfferente(String idProdotto){
        if(idProdotto!=null){
            if(!listaOfferente.contains(idProdotto)){
                listaOfferente.add(idProdotto);
            }
        }
    }
    public void addListaRicevente(String idProdotto){
        if(idProdotto!=null){
            if(!listaRicevente.contains(idProdotto)){
                listaRicevente.add(idProdotto);
            }
        }
    }

    public void removeListaOfferente(String idProdotto){
        if(idProdotto!=null){
            listaOfferente.remove(idProdotto);
        }

    }
    public void removeListaRicevente(String idProdotto){
        if(idProdotto!=null){
            listaRicevente.remove(idProdotto);
        }
    }
}