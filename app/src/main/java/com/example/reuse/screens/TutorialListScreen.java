package com.example.reuse.screens;

import static java.util.Locale.filter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.AdapterTutorial;
import com.example.reuse.models.Tutorial;

import java.util.ArrayList;
import java.util.List;


public class TutorialListScreen extends Fragment implements AdapterTutorial.OnItemClickListener {
    private RecyclerView recyclerViewTutorial;
    private AdapterTutorial adapter;
    private List<Tutorial> tutorialList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_list_screen, container, false);
        recyclerViewTutorial = view.findViewById(R.id.recycleViewTutorial);
        recyclerViewTutorial.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tutorialList = new ArrayList<>();
        tutorialList.add(new Tutorial(R.drawable.shoes, "Sneakers", R.drawable.user, "Ecco una guida su come riparare delle scarpe sneakers, in base ai problemi più comuni:\n" +
                "\n" +
                "Guida alla Riparazione delle Sneakers\n" +
                "La riparazione delle sneakers dipende dal tipo di danno che hanno subito. Di seguito, trovi una guida passo per passo con alcune operazioni base che puoi tentare da solo prima di rivolgerti a un esperto.\n" +
                "\n" +
                "1. Riparazione della Suola\n" +
                "La suola è una delle parti più soggette a usura. Se si stacca o si usura troppo, puoi provare a:\n" +
                "\n" +
                "Reincollare la Suola: Se la suola si è staccata dalla scarpa, pulisci entrambe le superfici (suola e tomaia) con alcool per rimuovere polvere e residui. Utilizza un colla speciale per scarpe (es. colla per gomma o calzature) e applica uniformemente. Pressa le due parti insieme e lascia asciugare per almeno 24 ore.\n" +
                "\n" +
                "Riparare una Suola Usurata: Se la suola è molto consumata, puoi portare le sneakers da un calzolaio per una sostituzione completa, oppure acquistare dei tappetini adesivi antiscivolo da applicare sopra le aree usurate.\n" +
                "\n" +
                "2. Riparare i Lacci\n" +
                "I lacci sono uno degli accessori più facili da sostituire, ma se si rompono o si danneggiano, puoi:\n" +
                "\n" +
                "Sostituire i Lacci: Se i lacci si sono rotti o si sono usurati, acquista nuovi lacci della lunghezza giusta per le tue sneakers. Puoi scegliere lacci in cotone, sintetici o in materiale riflettente per uno stile più moderno.\n" +
                "\n" +
                "Rinforzare i Lacci: Se i lacci sono leggermente danneggiati, ma non rotti, prova a usare una piccola dose di colla o un rivestimento protettivo sugli angoli per prevenire ulteriori danni.\n" +
                "\n" +
                "3. Riparazione della Tomaia (Superficie della Scarpa)\n" +
                "Se la tomaia delle sneakers è danneggiata (soprattutto in pelle o materiali sintetici), puoi:\n" +
                "\n" +
                "Pulire e Restaurare: Pulisci bene la tomaia con un detergente delicato. Usa un prodotto specifico per il tipo di materiale (per esempio, per la pelle usa un balsamo o una crema nutriente). Per le scarpe in tessuto, una buona spazzola o una soluzione di acqua e sapone possono essere utili.\n" +
                "\n" +
                "Riparare Tagli o Lacerazioni: Se c'è un piccolo taglio o strappo sulla tomaia, usa una colla per tessuti o pelle per sigillarlo temporaneamente. Se il danno è maggiore, potresti dover usare un kit di riparazione o affidarti a un calzolaio per una riparazione professionale.\n" +
                "\n" +
                "4. Riparare il Tacco\n" +
                "Se il tacco delle tue sneakers è danneggiato o rotto, puoi:\n" +
                "\n" +
                "Riparare con la Colla: Se il tacco è solo parzialmente staccato, applica una colla forte per scarpe e premi il tacco contro la scarpa finché non si asciuga.\n" +
                "\n" +
                "Sostituire il Tacco: Se il tacco è danneggiato in modo significativo, puoi farlo sostituire da un professionista che può sostituire o rinforzare la base della scarpa.\n" +
                "\n" +
                "5. Pulizia e Manutenzione Generale\n" +
                "Una buona manutenzione può prevenire danni e prolungare la vita delle tue sneakers.\n" +
                "\n" +
                "Pulizia Regolare: Lava le sneakers regolarmente seguendo le istruzioni del produttore, per evitare che si accumuli sporco e polvere che potrebbero danneggiarle. Usa uno spazzolino morbido per rimuovere polvere e macchie superficiali.\n" +
                "\n" +
                "Trattamento Impermeabilizzante: Se le tue sneakers sono in tessuto o pelle, considera l'uso di uno spray impermeabilizzante per proteggerle dall'umidità e dalle macchie.\n" +
                "\n" +
                "6. Riparare la Cucitura\n" +
                "Se la cucitura della tua scarpa si è rotta o si è allentata, puoi:\n" +
                "\n" +
                "Rifare la Cucitura: Usa un ago robusto e del filo resistente (preferibilmente nylon) per ricucire il punto danneggiato. Se non ti senti sicuro, potresti voler portare le scarpe da un calzolaio.\n", "Topo Gigio", "https://www.youtube.com/watch?v=iAPVWEixnWQ" ));
        tutorialList.add(new Tutorial(R.drawable.caldaia, "Riparare la caldaia", R.drawable.user, "Guida alla Riparazione della Caldaia\n" +
                "\n" +
                "La riparazione della caldaia dipende dal tipo di guasto e dalla causa del malfunzionamento. Tuttavia, ci sono alcune operazioni base che puoi tentare prima di chiamare un tecnico. Ecco una guida passo per passo:\n" +
                "\n" +
                "1. Verifica l'Accensione della Caldaia\n" +
                "Controlla il display o il pannello di controllo: Se la caldaia non si accende, verifica che non ci siano errori visualizzati sul display. Molte caldaie moderne hanno codici di errore che ti possono indicare quale sia il problema.\n" +
                "Controlla l'interruttore di alimentazione: Assicurati che la caldaia sia accesa e che l'interruttore di alimentazione sia nella posizione corretta.\n" +
                "2. Controlla la Pressione dell'Acqua\n" +
                "La caldaia potrebbe non funzionare correttamente se la pressione dell'acqua è troppo bassa. Puoi verificare la pressione dell'acqua sul manometro della caldaia.\n" +
                "Se la pressione è inferiore a 1 bar, è necessario riempire il circuito idraulico.\n" +
                "Come aumentare la pressione: Individua il rubinetto di riempimento (di solito sotto la caldaia) e aprilo lentamente fino a raggiungere la pressione corretta (di solito tra 1 e 1,5 bar). Chiudi il rubinetto quando hai finito.\n" +
                "3. Controlla il Termostato e le Impostazioni\n" +
                "Verifica il termostato: Se la caldaia non produce acqua calda o riscaldamento, potrebbe esserci un'impostazione errata sul termostato. Assicurati che sia impostato alla temperatura desiderata.\n" +
                "Controlla le modalità di funzionamento: Alcune caldaie hanno modalità diverse, come \"riscaldamento\" o \"acqua calda sanitaria\". Verifica che la caldaia sia impostata correttamente in base alle tue esigenze.\n" +
                "4. Sfiatare i Radiatori\n" +
                "Se la caldaia sembra funzionare correttamente ma non riscalda l'ambiente come dovrebbe, potrebbe esserci aria nei radiatori. L'aria impedisce il flusso dell'acqua calda, riducendo l'efficienza del sistema.\n" +
                "Come sfiatare un radiatore: Utilizza una chiave per radiatori per aprire la valvola di sfiato e lascia fuoriuscire l'aria. Quando l'acqua inizia a uscire senza bolle d'aria, chiudi la valvola.\n" +
                "5. Controlla il Filtro della Caldaia\n" +
                "Se la caldaia ha un filtro, questo potrebbe essere ostruito da detriti o calcare, impedendo il corretto funzionamento del sistema.\n" +
                "Pulizia del filtro: Spegni la caldaia, rimuovi il filtro e puliscilo accuratamente. Se non sei sicuro di come fare, consulta il manuale della tua caldaia.\n" +
                "6. Controlla la Ventilazione\n" +
                "Se la caldaia non si accende o emette rumori strani, potrebbe esserci un problema di ventilazione. Assicurati che la presa d'aria della caldaia non sia ostruita da polvere, detriti o altri ostacoli.\n" +
                "7. Verifica il Fumo di Scarico\n" +
                "Se la caldaia è a gas, assicurati che non ci siano blocchi nel tubo di scarico dei fumi. Un blocco potrebbe impedire il corretto funzionamento della caldaia e presentare un rischio di sicurezza.\n" +
                "8. Errore del Circuito Elettrico\n" +
                "Se la caldaia continua a non accendersi, potrebbe esserci un guasto nel circuito elettrico. Verifica che non ci siano fusibili bruciati o interruttori scattati. In tal caso, il problema potrebbe essere più complesso e richiedere l'intervento di un tecnico specializzato.", "Topo Gigetto", "https://www.youtube.com/watch?v=cpzytyuSbNI" ));
        tutorialList.add(new Tutorial(R.drawable.margherita, "Riparare la lavatrice", R.drawable.user, "Guida alla Riparazione della Lavatrice\n" +
                "La riparazione della lavatrice dipende dal tipo di guasto che si verifica. Di seguito, trovi una guida passo per passo con le operazioni base che puoi tentare da solo per risolvere i problemi più comuni.\n" +
                "\n" +
                "1. La Lavatrice Non Si Accende\n" +
                "Se la lavatrice non si accende, prima di chiamare un tecnico, prova i seguenti controlli:\n" +
                "\n" +
                "Verifica la presa di corrente: Assicurati che la lavatrice sia correttamente collegata alla presa e che l'alimentazione sia attiva.\n" +
                "Controlla il cavo di alimentazione: Se il cavo è danneggiato, dovrai sostituirlo.\n" +
                "Verifica il fusibile o l'interruttore: Se il fusibile è saltato o l'interruttore è scattato, sostituisci il fusibile o ripristina l'interruttore.\n" +
                "2. La Lavatrice Non Scarica l'Acqua\n" +
                "Se la lavatrice non scarica l'acqua, prova i seguenti passaggi:\n" +
                "\n" +
                "Controlla il tubo di scarico: Assicurati che il tubo di scarico non sia piegato o ostruito. Se è intasato, rimuovi l'ostruzione.\n" +
                "Controlla la pompa di scarico: La pompa potrebbe essere bloccata. Puoi smontarla e pulirla, oppure sostituirla se necessario.\n" +
                "Verifica il filtro di scarico: Molte lavatrici hanno un filtro di scarico che può ostruirsi. Rimuovi il filtro e puliscilo.\n" +
                "3. La Lavatrice Vibra Troppo o Fa Rumori Strani\n" +
                "Se la lavatrice vibra troppo durante il ciclo o fa rumori strani, verifica:\n" +
                "\n" +
                "Livellamento della lavatrice: Controlla che la lavatrice sia correttamente livellata. Se necessario, regola i piedini per renderla stabile.\n" +
                "Carico sbilanciato: Se hai caricato troppo o in modo non uniforme la lavatrice, rimuovi parte del carico e riprova.\n" +
                "Controlla i cuscinetti del tamburo: Se la lavatrice emette un rumore di sfregamento, i cuscinetti potrebbero essere usurati. Questo problema richiede solitamente un intervento da parte di un tecnico.\n" +
                "4. La Lavatrice Non Raggiunge la Temperatura Impostata\n" +
                "Se la lavatrice non riscalda l'acqua alla temperatura corretta:\n" +
                "\n" +
                "Verifica il termostato: Il termostato potrebbe non funzionare correttamente. Se sospetti che sia il problema, potrebbe essere necessario sostituirlo.\n" +
                "Controlla la resistenza elettrica: La resistenza potrebbe essere difettosa. Se non riscalda l'acqua, potrebbe essere necessario sostituirla.\n" +
                "5. La Lavatrice Perde Acqua\n" +
                "Se la lavatrice perde acqua, prova a:\n" +
                "\n" +
                "Controllare le guarnizioni: Verifica che le guarnizioni della porta siano in buone condizioni. Se sono danneggiate, potrebbero causare perdite d'acqua.\n" +
                "Controllare il tubo di alimentazione: Assicurati che il tubo di alimentazione non sia danneggiato o allentato.\n" +
                "Verifica la vaschetta del detersivo: Se la vaschetta del detersivo è ostruita, potrebbe provocare fuoriuscite d'acqua.\n" +
                "6. Il Cassetto del Detersivo Non Funziona Bene\n" +
                "Se il cassetto del detersivo non si svuota correttamente, verifica:\n" +
                "\n" +
                "Pulizia del cassetto: Rimuovi e pulisci il cassetto del detersivo per evitare che residui di detersivo blocchino il flusso.\n" +
                "Verifica il tubo di alimentazione: Controlla se il tubo di alimentazione che porta l'acqua al cassetto è ostruito o piegato.\n" +
                "7. La Lavatrice Non Raggiunge la Centrifuga\n" +
                "Se la lavatrice non completa il ciclo di centrifuga, prova questi passaggi:\n" +
                "\n" +
                "Verifica il carico: Un carico sbilanciato o troppo pesante potrebbe impedire alla lavatrice di raggiungere la velocità di centrifuga. Rimuovi o bilancia il carico.\n" +
                "Controlla il motore e la cinghia: Se la cinghia del motore è allentata o rotta, la lavatrice non riuscirà a centrifugare. La cinghia va sostituita in caso di rottura.\n" +
                "8. La Lavatrice Rilascia Odori Sgradevoli\n" +
                "Se la lavatrice emette odori sgradevoli, segui questi passaggi:\n" +
                "\n" +
                "Pulizia del tamburo: Esegui un ciclo di lavaggio a vuoto con aceto o bicarbonato di sodio per eliminare i cattivi odori.\n" +
                "Pulizia del filtro: Pulisci il filtro per evitare che i residui di detersivo o i capelli possano causare odori sgradevoli.\n" +
                "Controlla le guarnizioni della porta: Le guarnizioni possono trattenere umidità e sporco. Puliscile regolarmente per evitare la formazione di muffa.", "Topo Gigetto", "https://www.youtube.com/watch?v=brs9OTU9Yh0" ));
        tutorialList.add(new Tutorial(R.drawable.telefono, "Riparare lo schermo dell'Iphone 11", R.drawable.user, "Guida alla Riparazione dell'iPhone 11\n" +
                "1. Schermo Rotto o Crinato\n" +
                "Se il display dell'iPhone 11 è rotto o crinato, puoi tentare di risolvere in due modi:\n" +
                "\n" +
                "Sostituire il Display da Solo (solo per esperti): Puoi acquistare un display di ricambio online e sostituirlo da solo. Tuttavia, questo richiede strumenti specifici come una ventosa per sollevare il vetro, una pentalobe per rimuovere le viti, e attenzione per non danneggiare altri componenti.\n" +
                "\n" +
                "Spegni l'iPhone e rimuovi tutte le viti (pentalobe) ai lati del dispositivo.\n" +
                "Usa la ventosa per sollevare delicatamente il vetro, facendo attenzione a non danneggiare i cavi collegati.\n" +
                "Sostituisci lo schermo con uno nuovo e ricollega i cavi correttamente.\n" +
                "Rivolgiti a un Professionista: Se non ti senti sicuro, è meglio rivolgersi a un centro di assistenza autorizzato Apple o un tecnico certificato.\n" +
                "\n" +
                "2. Batteria che Non Si Carica\n" +
                "Se l'iPhone 11 non si carica, segui questi passaggi:\n" +
                "\n" +
                "Controlla il Cavo e l'Adattatore: Assicurati che il cavo Lightning e l'adattatore di ricarica siano in buone condizioni. Se sono danneggiati, sostituiscili con ricambi originali.\n" +
                "Pulisci la Porta Lightning: La porta di ricarica può accumulare polvere o detriti. Usa un pennello morbido o una cannuccia di plastica per pulire delicatamente la porta.\n" +
                "Prova un Riavvio: Se il telefono non si carica, prova a riavviarlo. A volte un semplice riavvio può risolvere piccoli problemi legati alla ricarica.\n" +
                "Sostituzione della Batteria: Se la batteria non tiene più la carica o è danneggiata, può essere necessario sostituirla. Puoi farlo rivolgendoti a un centro di assistenza Apple o un centro assistenza terzo autorizzato.\n" +
                "3. Problemi con la Fotocamera\n" +
                "Se la fotocamera dell'iPhone 11 non funziona correttamente, prova questi passaggi:\n" +
                "\n" +
                "Riavvia l'iPhone: A volte un semplice riavvio può risolvere bug legati all'app Fotocamera.\n" +
                "Controlla se ci sono Detriti: Se l'obiettivo della fotocamera è sporco o coperto da polvere, puliscilo delicatamente con un panno morbido.\n" +
                "Aggiorna iOS: Assicurati che il tuo iPhone 11 abbia l'ultima versione di iOS, poiché gli aggiornamenti possono risolvere bug software legati alla fotocamera.\n" +
                "Problema Hardware: Se la fotocamera continua a non funzionare, potrebbe esserci un problema hardware (come una fotocamera rotta). In questo caso, dovrai sostituirla, e per farlo è consigliabile rivolgersi a un tecnico.\n" +
                "4. Problemi con il Suono\n" +
                "Se non senti il suono o il volume è basso, prova questi suggerimenti:\n" +
                "\n" +
                "Controlla il Volume: Assicurati che il volume sia alzato e che la modalità \"Silenzioso\" non sia attivata.\n" +
                "Pulizia della Porta Audio e dell'Altoparlante: Se il suono è disturbato, prova a pulire la porta audio (se presente) e l'altoparlante con un piccolo pennello o una bomboletta di aria compressa.\n" +
                "Ripristina le Impostazioni: Se il problema persiste, prova a ripristinare le impostazioni audio nelle impostazioni dell'iPhone.\n" +
                "Problema Hardware: Se nessuno di questi passaggi risolve il problema, potrebbe esserci un guasto nell'altoparlante o nella scheda madre, e potrebbe essere necessario sostituire il componente difettoso.\n" +
                "5. Tasti o Touchscreen che Non Funzionano\n" +
                "Se i tasti fisici o il touchscreen non rispondono, prova questi passaggi:\n" +
                "\n" +
                "Riavvia il Dispositivo: A volte un riavvio può risolvere piccoli problemi con i tasti.\n" +
                "Pulizia dello Schermo: Se lo schermo è sporco o coperto da impronte, puliscilo delicatamente con un panno morbido.\n" +
                "Ripristino del Software: Se il problema persiste, prova a fare un ripristino del software (assicurati di eseguire un backup prima di farlo).\n" +
                "Problemi Hardware: Se il problema è hardware, potrebbe essere un problema con il circuito del touchscreen o un guasto ai tasti fisici. In questo caso, la sostituzione di componenti come la scheda logica o il modulo display potrebbe essere necessaria.\n" +
                "6. iPhone Che Surriscalda\n" +
                "Se l'iPhone 11 si surriscalda, prova questi passaggi:\n" +
                "\n" +
                "Controlla l'App in Esecuzione: Se l'iPhone è surriscaldato, potrebbe esserci un'app che consuma eccessivamente risorse. Chiudi le app non necessarie.\n" +
                "Disattiva la Localizzazione e il Wi-Fi: Disattiva la localizzazione e il Wi-Fi quando non li stai usando, per ridurre il carico sulla batteria.\n" +
                "Spegni e Lascia Raffreddare: Se l'iPhone si surriscalda, spegnilo e lascialo raffreddare per alcuni minuti.\n" +
                "Verifica la Batteria: Se la batteria è vecchia o difettosa, può causare il surriscaldamento. In tal caso, la sostituzione della batteria potrebbe risolvere il problema.", "Topo Gigetto", "https://www.youtube.com/watch?v=CMkgypeOa-4"));
        // Set the adapter for both RecyclerViews
        adapter = new AdapterTutorial(getContext(), tutorialList, this);
        recyclerViewTutorial.setAdapter(adapter);
        // Inflate the layout for this fragment
        EditText searchBar = view.findViewById(R.id.editTextText);
        searchBar.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

        });
        return view;
    }
    private void filter(String text) {
        List<Tutorial> filteredList = new ArrayList<>();

        for (Tutorial tutorial : tutorialList) {
            if (tutorial.getName().toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(tutorial);
            }
        }

        adapter.updateList(filteredList);
    }
    @Override
    public void onItemClick(Tutorial tutorial) {

        Bundle bundle = new Bundle();
        bundle.putString("nameTutorial", tutorial.getName());
        bundle.putInt("imageTutorial", tutorial.getImageResId());
        bundle.putString("author", tutorial.getAuthor());
        bundle.putString("descriptionTutorial", tutorial.getDescription());
        bundle.putInt("avatarTutorial", tutorial.getUserAvatarResId()); // For ImageView
        // Add other product details to the bundle
        bundle.putString("url", tutorial.getUrlTutorial());
        TutorialScreen tutorialScreen = new TutorialScreen();
        tutorialScreen.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, tutorialScreen);
        transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }
}