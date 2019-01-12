package com.example.administrador.updatetemps;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
            String [] temps = {"-1","-1","-1","-1"};
            String mes= "";

            try {
                Document docVista = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m139e01").get();
                temps[0] = cutBeforeData(docVista.getElementById("prec").text());

                Document docXodos = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m055e02").get();
                temps[1] = cutBeforeData(docXodos.getElementById("prec").text());

                Document docAtz = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m001e02").get();
                temps[2] = docAtz.getElementById("prec").text();

                //Document docVf = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c02m129e02").get();

                //PILLAR EL MES EN EL QUE ESTAMOS
                mes = new SimpleDateFormat("dd-MM-yyyy").format(new Date()).split("-")[1];

                temps[3] = mes;

                Map<String,Object> tempz = new HashMap<>();

                ArrayList<Integer> aF= new ArrayList<>(Collections.nCopies(12, 0));

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

            } catch (IOException e) {
                e.printStackTrace();
            }
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
