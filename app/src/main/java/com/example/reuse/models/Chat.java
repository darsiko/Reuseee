package com.example.reuse.models;


import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String id="";
    private String idUtente1;
    private String idUtente2;
    private List<Messaggio> messaggi = new ArrayList<>();
    private  Scambio scambio = new Scambio();

    //costruttore se servono metodi anche con oggetto vuoto
    public Chat() {
        this.id="";
        this.idUtente1="";
        this.idUtente2="";
        this.messaggi=new ArrayList<>();
    }
    //costruttore manuale

    public Chat(String cId, String u1Id, String u2Id, List<Messaggio> mex, Scambio s){
        this.id = cId;
        this.idUtente1 = u1Id;
        this.idUtente2 = u2Id;
        this.messaggi.addAll(mex);
        this.scambio = new Scambio(s);
    }

    public Chat(String idUtente1, String idUtente2, List<Messaggio> messaggi) {
        this.idUtente1 = idUtente1;
        this.idUtente2 = idUtente2;
        this.messaggi.addAll(messaggi);
    }


    public void copy(final Chat other){
        this.id = other.id;
        this.idUtente1 = other.idUtente1;
        this.idUtente2 = other.idUtente2;
        this.messaggi.addAll(other.messaggi);
        this.scambio = new Scambio(other.scambio);
    }

    //UTILIZZARE
    //crea oggetto chat a partire dall'id (prendetelo dalla lista delle chat dell'utente)
    public Chat(String id){
        this.id=id;
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Chats").child(this.id);
        dbr.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                this.idUtente1 = snapshot.child("idUtente1").getValue(String.class);
                this.idUtente2 = snapshot.child("idUtente2").getValue(String.class);

                this.messaggi = new ArrayList<>();
                for (DataSnapshot mSnapshot : snapshot.child("chats").getChildren()) {
                    String content = mSnapshot.child("contenuto").getValue(String.class);
                    addMessaggio(idUtente1, content);
                }
                if (snapshot.hasChild("scambio")) {
                    Scambio s=new Scambio(snapshot.child("scambio").child("idOfferente").getValue(String.class));
                    s.setSoldiOfferente(snapshot.child("scambio").child("soldiOfferente").getValue(Double.class));
                    s.setSoldiRicevente(snapshot.child("scambio").child("soldiRicevente").getValue(Double.class));
                    for (DataSnapshot oSnapshot : snapshot.child("scambio").child("listaOfferente").getChildren()) {
                        String pid = oSnapshot.getValue(String.class);
                        if (pid != null) {
                            s.addListaOfferente(pid);
                        }
                    }
                    for (DataSnapshot rSnapshot : snapshot.child("scambio").child("listaRicevente").getChildren()) {
                        String pid = rSnapshot.getValue(String.class);
                        if (pid != null) {
                            s.addListaRicevente(pid);
                        }
                    }
                    this.scambio=new Scambio(s);
                }
            }
        });
    }

    //UTILIZZARE
    //costruttore per nuova chat
    public Chat(String idUtente1, String idUtente2){
        this.idUtente1=idUtente1;
        this.idUtente2=idUtente2;
        this.messaggi=new ArrayList<>();
    }

    //UTILIZZARE
    //Chat c=new Chat(idUtente1, idUtente2);
    //c.uploadChat() per caricare la chat nel database
    public void uploadChat(){
        //check se esiste già chat tra utenti
        DatabaseReference refU1 = FirebaseDatabase.getInstance().getReference("Users").child(idUtente1).child("chats");
        refU1.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if (task.getResult().exists()) {
                    DatabaseReference refU2 = FirebaseDatabase.getInstance().getReference("Users").child(idUtente2).child("chats");
                    refU2.get().addOnCompleteListener(task2 -> {
                        if(task2.isSuccessful()) {
                            if (task2.getResult().exists()){
                                for (DataSnapshot snapshot1 : task.getResult().getChildren()) {
                                    for (DataSnapshot snapshot2 : task2.getResult().getChildren()) {
                                        String chatId1 = snapshot1.getValue(String.class);
                                        String chatId2 = snapshot2.getValue(String.class);
                                        if (chatId1 != null && chatId1.equals(chatId2)) {
                                            Chat c = new Chat(snapshot2.getValue(String.class));
                                            this.id = c.getId();
                                            this.idUtente1 = c.getIdUtente1();
                                            this.idUtente2 = c.getIdUtente2();
                                            this.messaggi.addAll(c.getMessaggi());
                                            if(c.getScambio()!=null){
                                                this.scambio=new Scambio(c.getScambio());
                                            }
                                            System.out.println("La Chat esiste già");
                                            return;
                                        }
                                    }
                                }
                                uploadChatSupporto();
                            }else {
                                uploadChatSupporto();
                            }
                        }else{
                            uploadChatSupporto();
                        }
                    });
                }else{
                    uploadChatSupporto();
                }
            }else{
                uploadChatSupporto();
            }
        });
    }

    private void uploadChatSupporto(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        id = dbRef.push().getKey();
        if(id!=null){
            DatabaseReference ref=dbRef.child(id);
            ref.child("idUtente1").setValue(idUtente1);
            ref.child("idUtente2").setValue(idUtente2);
            ref.child("messaggi").setValue(messaggi);
            if(scambio!=null){
                uploadScambio(scambio);
            }
            DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users").child(idUtente1).child("chats");
            dbr.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<String> chats = new ArrayList<>();
                    // Retrieve the existing list, if any
                    if (task.getResult().exists()) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            String chatId = snapshot.getValue(String.class);
                            if (chatId != null) {
                                chats.add(chatId);
                            }
                        }
                    }
                    chats.add(id);
                    dbr.setValue(chats);
                }
            });
            DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("Users").child(idUtente2).child("chats");
            dbr2.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<String> chats = new ArrayList<>();
                    // Retrieve the existing list, if any
                    if (task.getResult().exists()) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            String chatId = snapshot.getValue(String.class);
                            if (chatId != null) {
                                chats.add(chatId);
                            }
                        }
                    }
                    chats.add(id);
                    dbr2.setValue(chats);
                }
            });
        }

    }
    public void deleteChat(){
        if(id==null || id.isEmpty()){
            System.out.println("risulta id nullo" + id);
        }else {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Chats").child(id);
            dbref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        deleteChatSupporto(dbref);
                    } else {
                        System.out.println("Chat(result) non trovata per id: " + id);
                    }
                } else {
                    System.out.println("Chat(ref) non trovata per id: " + id);
                }
            });
        }

    }

    private void deleteChatSupporto(DatabaseReference dbref){
        dbref.removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                deleteChatSupportoLista(true);
                deleteChatSupportoLista(false);
            }
        });
    }

    private void deleteChatSupportoLista(boolean t){
        String idUtente;
        if(t){
            idUtente=idUtente1;
        }else{
            idUtente=idUtente2;
        }
        DatabaseReference userChatRef = FirebaseDatabase.getInstance().getReference("Users").child(idUtente).child("chats");
        userChatRef.get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful() && userTask.getResult().exists()) {
                List<String> chats = new ArrayList<>();

                for (DataSnapshot snapshot : userTask.getResult().getChildren()) {
                    String chatId = snapshot.getValue(String.class);
                    if (chatId != null && !chatId.equals(id)) {
                        chats.add(chatId);
                    }
                }

                // Aggiorna la lista senza il prodotto rimosso
                userChatRef.setValue(chats)
                        .addOnSuccessListener(aVoid -> System.out.println("Chat rimossa con successo dalla lista dell'utente."))
                        .addOnFailureListener(e -> System.err.println("Errore durante l'aggiornamento della lista: " + e.getMessage()));
            } else {
                System.out.println("Nessuna chat trovata nella lista dell'utente.");
            }
        });
    }



    //UTILIZZARE
    //aggiungi messaggio testuale alla chat;
    public void addMessaggio(String idMittente, String contenuto){
        Messaggio m=new Messaggio();
        m.addDatabase(idMittente, contenuto, id);
    }

    //UTILIZZARE
    //aggiungi messaggio immagine alla chat;
    public void addMessaggio(String idMittente, Uri imageUri){
        Messaggio m=new Messaggio();
        m.addDatabase(idMittente, imageUri, id);
    }

    public void uploadScambio(Scambio scambio){
        this.scambio=scambio;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(id).child("scambio");
        ref.child("idOfferente").setValue(scambio.getIdOfferente());
        ref.child("soldiOfferente").setValue(scambio.getSoldiOfferente());
        ref.child("soldiRicevente").setValue(scambio.getSoldiRicevente());
        List<String> listaOfferente = new ArrayList<>(scambio.getListaOfferente());
        List<String> listaRicevente = new ArrayList<>(scambio.getListaRicevente());
        ref.child("listaOfferente").setValue(listaOfferente);
        ref.child("listaRicevente").setValue(listaRicevente);
    }



    public String getId(){
        return id;
    }
    public String getIdUtente1(){
        return idUtente1;
    }
    public String getIdUtente2(){
        return idUtente2;
    }
    public List<Messaggio> getMessaggi(){
        return messaggi;
    }
    public Scambio getScambio(){
        return scambio;
    }
    public void checkOfferta(CheckOffertaCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(id);

        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.child("idOfferente").exists()) {
                    System.out.println("Offerta già esistente");
                    callback.onResult(true); // Invoca il callback con true
                } else {
                    System.out.println("Nessuna offerta già esistente");
                    callback.onResult(false); // Invoca il callback con false
                }
            } else {
                System.out.println("Nessuna chat trovata");
                callback.onResult(false); // Anche in caso di errore restituisce false
            }
        });
    }
    public interface CheckOffertaCallback {
        void onResult(boolean exists);
    }


}
