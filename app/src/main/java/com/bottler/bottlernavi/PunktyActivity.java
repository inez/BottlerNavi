package com.bottler.bottlernavi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PunktyActivity extends AppCompatActivity {

    private Realm realm;

    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realmConfig = new RealmConfiguration.Builder(this)
                .name("myrealm.realm.enc")
                .encryptionKey(Installation.key(this))
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkty);

        Intent i = getIntent();
        String id = (String) i.getSerializableExtra("TRASA_ID");

        List<Punkt> punkty = realm.where(Punkt.class).equalTo("trasa.id", id).findAll().sort("sequence");
        PunktAdapter adapter = new PunktAdapter(this, punkty);
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(adapter);
    }
}
