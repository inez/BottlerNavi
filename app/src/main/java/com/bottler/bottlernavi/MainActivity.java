package com.bottler.bottlernavi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private TrasaAdapter adapter;

    private Realm realm;

    private RealmConfiguration realmConfig;

    public void onDownloadDataClick(View v) {
        new AlertDialog.Builder(this)
                .setMessage("Pobrania nowych danych spowoduje nadpisanie wszystkich starych danych. Czy kontynować?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteAndDownloadData();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    public void DeleteAndDownloadData() {
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Proszę czekać");
        mDialog.setCancelable(false);
        mDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://159.203.228.176/json.json?id=" + Installation.id(this), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArray trasy = response;

                long now = System.currentTimeMillis()/1000;

                realm.beginTransaction();

                realm.where(Punkt.class).findAll().deleteAllFromRealm();
                realm.where(Trasa.class).findAll().deleteAllFromRealm();

                for (int i = 0; i < trasy.length(); i++) {
                    try {
                        JSONObject trasa = trasy.getJSONObject(i);

                        Trasa trasaDB = realm.createObject(Trasa.class);
                        trasaDB.setId(UUID.randomUUID().toString());
                        trasaDB.setNazwa(trasa.getString("nazwa"));
                        trasaDB.setDataDodania(now);

                        JSONArray punkty = trasa.getJSONArray("punkty");

                        for (int j = 0; j < punkty.length(); j++) {
                            JSONObject punkt = punkty.getJSONObject(j);
                            Punkt punktDB = realm.createObject(Punkt.class);
                            punktDB.setId(UUID.randomUUID().toString());
                            punktDB.setNazwa(punkt.getString("nazwa"));
                            punktDB.setSzerokosc(punkt.getDouble("szerokosc"));
                            punktDB.setDlugosc(punkt.getDouble("wysokosc"));
                            punktDB.setTelefon(punkt.getString("telefon"));
                            punktDB.setTrasa(trasaDB);
                            punktDB.setSequence(j);
                        }
                    } catch (org.json.JSONException e) {
                        // FIXME
                    }
                }

                realm.commitTransaction();

                adapter.notifyDataSetChanged();

                mDialog.dismiss();
            }
        });
    }

    public void DeleteExpired() {
        Long now = System.currentTimeMillis()/1000;
        Long limit = now - 60 * 60 * 12; // 12 hours

        realm.beginTransaction();
        realm.where(Trasa.class).lessThan("dataDodania", limit) .findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realmConfig = new RealmConfiguration.Builder(this)
                .name("myrealm.realm.enc")
                .encryptionKey(Installation.key(this))
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        DeleteExpired();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Trasa> trasy = realm.where(Trasa.class).findAll().sort("nazwa");
        adapter = new TrasaAdapter(this, trasy);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
