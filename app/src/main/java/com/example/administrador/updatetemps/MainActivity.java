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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

            final int mes= Integer.parseInt(new SimpleDateFormat("dd-MM-yyyy").format(new Date()).split("-")[1]);;
            final Double [] rains = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            Map<String,Object> tempz = new HashMap<>();
            try {
                Document webPage;

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m139e01").get();
                rains[0] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m055e02").get();
                rains[1] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c04m001e02").get();
                rains[2] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c02m129e02").get();
                rains[3] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c99m044e15").get();
                rains[4] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c08m131e01").get();
                rains[5] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c06m084e02").get();
                rains[6] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c99m044e01").get();
                rains[7] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c07m115e01").get();
                rains[8] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("https://www.avamet.org/mxo_i.php?id=c09m201e01").get();
                rains[9] = formatTemps(webPage.getElementById("prec").text());

                webPage = Jsoup.connect("http://www.aemet.es/es/eltiempo/observacion/ultimosdatos?k=arn&l=8486X&w=1&datos=img").get();
                rains[10] = formatTemps(webPage.getElementsByClass("fila_impar").get(4).text());

                webPage = Jsoup.connect("http://www.aemet.es/es/eltiempo/observacion/ultimosdatos?k=val&l=8472A&w=1&datos=img&f=tmax").get();
                rains[11] = formatTemps( webPage.getElementsByClass("fila_impar").get(4).text());

                webPage = Jsoup.connect("https://meteosabi.es/el-tiempo-en-bronchales-teruel").get();
                rains[12] = formatTemps(webPage.getElementById("RainToday").text());

                Log.d(TAG, "xyz_vect RAINS MODIFIIED: : "+Arrays.toString(rains));

            } catch (IOException e) {
                e.printStackTrace();
            }



            DocumentReference docRef = db.collection("Temperaturas").document("Estaciones");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "xyz: BASE_DE_DATOS_ENCONTRADA data: " + document.getData());

                            Map<String,Object> tempz = document.getData();
                            ArrayList<Double> ad = null;
                            Log.d(TAG,"xyz_AQUI LLEGA");


                            Log.d(TAG, "xyz_vect RAINS??: "+Arrays.toString(rains));


                            //MISTERIO DE LOS GORDOS
                            for (int i = 0; i<rains.length; i++){
                                Log.d("xyzRRR: "+TAG,Double.toString(rains[i]));
                                /*
                                if(rains[i] > 0.0 ){

                                    ad =(ArrayList<Double>) tempz.get("E"+Integer.toString(i));
                                    Double suma = ad.get(mes)+rains[i];
                                    Log.d(TAG,"xyzSUMA: "+Double.toString(suma));
                                    ad.set(mes, suma);
                                    Log.d(TAG,"xyz_ARRAY ACTUALISADO");
                                }
                                */
                            }
                            Log.d(TAG, "xyzSALE_DEL_FOR: "+ad.toString());

                           // ad.set(mes, ad.get(mes) + rains[0] );


                        } else {
                            Log.d(TAG, "xyz No ENCUENTRA LA BBDD");
                        }
                    } else {
                        Log.d(TAG, "xyz ERROR DE DATOS ", task.getException());
                    }
                }
            });


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

    public Double formatTemps(String orgData){
        Double result;
        int idx = 0;
        for (int i=0;i<orgData.length();i++){
            if (Character.isDigit(orgData.charAt(i))) {
                idx = i;
                break;
            }
        }
        // Contemplar temperaturas negativas
        if (idx != 0 && orgData.charAt(idx-1) == '-'){
            //Log.d(TAG,"xyZNEGA: "+orgData.substring(idx-1,orgData.length()-2));

            try{
                result = Double.parseDouble(orgData.substring(idx-1,orgData.length()-2).replace(',','.'));
            }catch( NumberFormatException e){
                //Log.d("xyz_TRY/CATCH_NEGA: "+TAG, orgData.substring(idx-1,orgData.length()-2).replace(',','.') );
                return 0.0;
            }
            return result;
        }

        //TEMPERATURAS POSITIVAS
        //Log.d("xyZPOSI: "+TAG,orgData.substring(idx,orgData.length()-2));
        try{
            result = Double.parseDouble(orgData.substring(idx,orgData.length()-2).replace(',','.'));
        }catch( NumberFormatException e){
            //Log.d("xyz_TRY/CATCH_POSI: "+TAG, orgData.substring(idx,orgData.length()-2).replace(',','.') );
            return 0.0;
        }
        return result;
    }


}
