package com.example.reuse.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable {
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
    public Product(String nome, String descrizione, double prezzo, boolean baratto){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.idVenditore=currentUser.getUid();
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.baratto=baratto;
        this.imageUrl="gs://re-use-98b8a.firebasestorage.app/ProductImages/base.jpeg";
        this.idOrdine="";
    }
    //download dell'oggetto dal database
    public Product(String pid){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products").child(pid);
        this.idVenditore=dbr.child("idVenditore").get().toString();
        this.nome=dbr.child("nome").get().toString();
        this.descrizione=dbr.child("descrizione").get().toString();
        this.prezzo=Double.parseDouble(dbr.child("prezzo").get().toString());
        this.baratto= Boolean.parseBoolean(dbr.child("baratto").get().toString());
        this.imageUrl=dbr.child("imageUrl").get().toString();
        this.idOrdine=dbr.child("idOrdine").get().toString();
    }

    protected Product(Parcel in) {
        idVenditore = in.readString();
        nome = in.readString();
        descrizione = in.readString();
        prezzo = in.readDouble();
        baratto = in.readByte() != 0;
        imageUrl = in.readString();
        idOrdine = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    //metodo per recuperare lo username del venditore
    //agiungi l'oggetto al database
    public void addProduct(Uri imageUri){
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Products");

        // Generate a new product ID
        String pid = dbr.push().getKey();

        // Save the current product object under the new ID
        this.update(pid);
        /*dbr.child(pid).setValue(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Product saved to 'Products' successfully.");
                    } else {
                        System.out.println("Failed to save product: " + task.getException().getMessage());
                    }
                });*/

        // Ensure the current user is authenticated
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("User is not logged in.");
            return;
        }

        String uid = currentUser.getUid();
        DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("productsForSale");

        // Update the user's list of products for sale
        dbr2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> productsForSale = new ArrayList<>();

                // Retrieve the existing list, if any
                if (task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String productId = snapshot.getValue(String.class);
                        if (productId != null) {
                            productsForSale.add(productId);
                        }
                    }
                }
                // Aggiungi il nuovo productId alla lista
                this.uploadImage(pid, imageUri);
                productsForSale.add(pid);

                // Save the updated list back to Firebase
                dbr2.setValue(productsForSale)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                System.out.println("Product added to 'productsForSale' successfully.");
                            } else {
                                System.out.println("Failed to update 'productsForSale': " + updateTask.getException().getMessage());
                            }
                        });
            } else {
                System.out.println("Failed to retrieve 'productsForSale': " + task.getException().getMessage());
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



    public void uploadImage(String pid, Uri imageUri) {
        DatabaseReference databaseReference;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProductImages");

        // Inizializza Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("ProductImagesProducts").child(pid).child("imageUrl");
        if (imageUri != null) {
            String fileName = pid + ".jpg";
            StorageReference fileReference = storageReference.child(fileName);

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                // Salva l'URL nel Realtime Database
                                databaseReference.setValue(downloadUrl);
                                this.imageUrl=downloadUrl;
                            }))
                    .addOnFailureListener(e -> {
                        //Toast.makeText(MainActivity.this, "Caricamento fallito: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
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
        dbr.child("baratto").setValue(baratto);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUri() {
        return Uri.parse(imageUrl);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUrl = imageUri.toString();
    }
    public String username(){
        User user=new User(idVenditore);
        return user.getUsername();
    }

    // Metodi per Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(idVenditore);
        parcel.writeString(nome);
        parcel.writeString(descrizione);
        parcel.writeDouble(prezzo);
        parcel.writeByte((byte) (baratto ? 1 : 0));
        parcel.writeString(imageUrl);
        parcel.writeString(idOrdine);
    }
}
