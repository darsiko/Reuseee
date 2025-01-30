package com.example.reuse.models;

import static java.security.AccessController.getContext;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, nome, cognome, stato, citta, indirizzo, data, telefono, imageUrl;
    private int cap;
    private List<String> productsForSale = new ArrayList<>();
    private List<String> chats = new ArrayList<>();

    //costruttori
    public User(){}

    //costruttore prende dati dal database
    public User(String uid, UserCallback callback) {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        // Fetch data asynchronously
        dbr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                this.username = snapshot.child("username").getValue(String.class);
                this.nome = snapshot.child("nome").getValue(String.class);
                this.cognome = snapshot.child("cognome").getValue(String.class);
                this.telefono = snapshot.child("telefono").getValue(String.class);
                this.indirizzo = snapshot.child("indirizzo").getValue(String.class);
                this.data = snapshot.child("data").getValue(String.class);
                this.imageUrl = snapshot.child("imageUrl").getValue(String.class);
                this.stato=snapshot.child("stato").getValue(String.class);
                this.citta=snapshot.child("citta").getValue(String.class);

                // Safely parse "cap" field
                Long capValue = snapshot.child("cap").getValue(Long.class); // Retrieve as Long
                this.cap = (capValue != null) ? capValue.intValue() : 0;

                // Populate productsForSale
                this.productsForSale = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.child("productsForSale").getChildren()) {
                    String pid = productSnapshot.getValue(String.class);
                    if (pid != null) {
                        this.productsForSale.add(pid);
                    }
                }
                this.chats = new ArrayList<>();
                for (DataSnapshot chatSnapshot : snapshot.child("chats").getChildren()) {
                    String cid = chatSnapshot.getValue(String.class);
                    if (cid != null) {
                        this.chats.add(cid);
                    }
                }

                // Invoke the callback once data is loaded
                callback.onUserLoaded(this);
                //QUI C'è UN ERRORE, DIOPORCO
            } else {
                callback.onError(task.getException());
            }
        });
    }

    public User(final User other){
        this.username=other.getUsername();
        this.nome=other.getNome();
        this.cognome=other.getCognome();
        this.stato=other.getStato();
        this.citta=other.getCitta();
        this.indirizzo=other.getIndirizzo();
        this.data=other.getData();
        this.telefono=other.getTelefono();
        this.imageUrl=other.imageUrl;
        this.cap=other.cap;
        this.productsForSale.addAll(other.productsForSale);
        this.chats.addAll(other.chats);
    }

    public User(String uid) {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        // Fetch data asynchronously
        dbr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                this.username = snapshot.child("username").getValue(String.class);
                this.nome = snapshot.child("nome").getValue(String.class);
                this.cognome = snapshot.child("cognome").getValue(String.class);
                this.telefono = snapshot.child("telefono").getValue(String.class);
                this.indirizzo = snapshot.child("indirizzo").getValue(String.class);
                this.data = snapshot.child("data").getValue(String.class);
                this.imageUrl = snapshot.child("imageUrl").getValue(String.class);
                this.stato=snapshot.child("stato").getValue(String.class);
                this.citta=snapshot.child("citta").getValue(String.class);

                // Safely parse "cap" field
                Long capValue = snapshot.child("cap").getValue(Long.class); // Retrieve as Long
                this.cap = (capValue != null) ? capValue.intValue() : 0;

                // Populate productsForSale
                this.productsForSale = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.child("productsForSale").getChildren()) {
                    String pid = productSnapshot.getValue(String.class);
                    if (pid != null) {
                        this.productsForSale.add(pid);
                    }
                }

                this.chats = new ArrayList<>();
                for (DataSnapshot chatSnapshot : snapshot.child("chats").getChildren()) {
                    String cid = chatSnapshot.getValue(String.class);
                    if (cid != null) {
                        this.chats.add(cid);
                    }
                }
            }
        });
    }
    public interface UserCallback {
        void onUserLoaded(User user);
        void onError(Exception e);

    }
    //costruttore per creazione User
    public User(String username, String nome, String cognome, String telefono, String stato, String citta, int cap, String indirizzo, String data){
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.telefono=telefono;
        this.cap=cap;
        this.indirizzo=indirizzo;
        this.data=data;
        this.productsForSale=new ArrayList<>();
        this.chats=new ArrayList<>();
        this.stato=stato;
        this.citta=citta;
        this.imageUrl="imageUrl";
    }

    //costruttore
    public User(String username, String nome, String cognome, String telefono, String stato, String citta, int cap, String indirizzo, String data, String imageUrl, List<String> productsForSale, List<String> chats){
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.telefono=telefono;
        this.stato=stato;
        this.citta=citta;
        this.cap=cap;
        this.indirizzo=indirizzo;
        this.data=data;
        this.productsForSale.addAll(productsForSale);
        this.chats.addAll(chats);
        this.imageUrl=imageUrl;
    }


    //update per i dati del profilo
    public void updateProfilo(String uid){
        updateUsername(uid);
        updateNome(uid);
        updateCognome(uid);
        updateTelefono(uid);
        updateStato(uid);
        updateCitta(uid);
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
    private void updateStato(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("stato").setValue(stato);
    }
    private void updateCitta(String uid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        dbr.child("citta").setValue(citta);
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

    public List<String> getChats() {
        return chats;
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

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
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

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }
}