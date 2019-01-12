package com.example.administrador.updatetemps;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String ARR = "array";

    private EditText editTextTitle;
    private EditText editTextDescription;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new doit().execute();

    }

    public class doit extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            String mes= "";
            //Map<String,Object> tempz = new HashMap<>();

            DocumentReference docRef = db.collection("Temperaturas").document("Estaciones");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "xyz DOCSNAPSHOT data: " + document.getData());

                            String mes = new SimpleDateFormat("dd-MM-yyyy").format(new Date()).split("-")[1];

                            Map<String,Object> tempz = document.getData();
                            Map<String,String> stations = new HashMap<>();

                            stations.put("E0","https://www.avamet.org/mxo_i.php?id=c04m139e01"); //Vistabella
                            stations.put("E1","https://www.avamet.org/mxo_i.php?id=c04m055e02"); //Xodos
                            stations.put("E2","https://www.avamet.org/mxo_i.php?id=c04m001e02"); //Atzaneta
                            stations.put("E3","https://www.avamet.org/mxo_i.php?id=c02m129e02"); //Villafranca
                            stations.put("E4","https://www.avamet.org/mxo_i.php?id=c99m044e15"); //Valdelinares
                            stations.put("E5","https://www.avamet.org/mxo_i.php?id=c08m131e01"); //Villamalur
                            stations.put("E6","https://www.avamet.org/mxo_i.php?id=c06m084e02"); //Onda
                            stations.put("E7","https://www.avamet.org/mxo_i.php?id=c99m044e01"); //Alcalá S.
                            stations.put("E8","https://www.avamet.org/mxo_i.php?id=c07m115e01"); //Toro
                            stations.put("E9","https://www.avamet.org/mxo_i.php?id=c09m201e01"); //Puebla S.M.
                            stations.put("E10","http://www.aemet.es/es/eltiempo/observacion/ultimosdatos?k=arn&l=8486X&w=1&datos=img");//Mosqueruela
                            stations.put("E11","http://www.aemet.es/es/eltiempo/observacion/ultimosdatos?k=val&l=8472A&w=1&datos=img&f=tmax");//Montanejos
                            stations.put("E12","https://meteosabi.es/el-tiempo-en-bronchales-teruel");//Bronchales



                            Document webPage;
                            String [] temps = {"","","","","","","","","","","","","","","","",""};

                            try {
                                Log.d(TAG, "xyz PROOF data: " + "HOLAaa");

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m139e01").get();
                                temps[0] = cutBeforeData(webPage.getElementById("prec").text());
                                Log.d(TAG, "xyz PROOF data: " + "ADIOssaa");

/*
                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m055e02").get();
                                temps[1] = cutBeforeData(webPage.getElementById("prec").text());

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m001e02").get();
                                temps[2] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c02m129e02").get();
                                temps[3] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c99m044e15").get();
                                temps[4] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c08m131e01").get();
                                temps[5] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c06m084e02").get();
                                temps[6] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c99m044e01").get();
                                temps[7] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c07m115e01").get();
                                temps[8] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c09m201e01").get();
                                temps[9] = webPage.getElementById("prec").text();

                                webPage = Jsoup.connect("http://www.aemet.es/es/eltiempo/observacion/ultimosdatos?k=arn&l=8486X&w=1&datos=img").get();
                                temps[10] = webPage.getElementsByClass("fila_impar").get(6).text();

                                Log.d(TAG, "xyz PROOF data: " + temps[10]);
*/

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            Log.d(TAG, "DATOS_E0: " + tempz.get("E0").toString());

                        } else {
                            Log.d(TAG, "xyz No SUCH DOCUMENT");
                        }
                    } else {
                        Log.d(TAG, "xyz GET FAILED WITH DATA ", task.getException());
                    }
                }
            });
                /*
                DocumentReference docRef = db.collection("Temperaturas").document("Estaciones");

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tempz = documentSnapshot.toObject(HashMap.class);
                    }
                });

                /*
                db.collection("Temperaturas").document("Estaciones").set(tempz)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "ARRAY GUARDADO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error GUARDANDO!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });*/

            return null;
        }
    }

    public void resetDB(View v){
        ArrayList<Integer> aF= new ArrayList<>(Collections.nCopies(12, 0));
        Map<String,Object> tempz = new HashMap<>();
        for (int i=0; i<13; i++) {
            tempz.put("E"+Integer.toString(i), aF);

        }

        db.collection("Temperaturas").document("Estaciones").set(tempz)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "ARRAY GUARDADO", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error GUARDANDO!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

    }

    public String cutBeforeData(String orgData){
        int idx = 0;
        for (int i=0;i<orgData.length();i++){
            if (Character.isDigit(orgData.charAt(i))) {
                idx = i;
                break;
            }
        }
        // Contemplar temperaturas negativas
        if (idx != 0 && orgData.charAt(idx-1) == '-'){
            return orgData.substring(idx-1,orgData.length()-2);
        }
        return orgData.substring(idx,orgData.length()-2);
    }


}
