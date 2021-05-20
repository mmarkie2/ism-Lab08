package pollub.ism.lab07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pollub.ism.lab07.databinding.ActivityMainBinding;
import pollub.ism.lab07.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;
    String wybraneWarzywoNazwa = null;
    Integer wybraneWarzywoIlosc = null;

    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ}

    private BazaMagazynowa bazaDanych;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);


        binding.przyciskSkladuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                zmienStan(OperacjaMagazynowa.SKLADUJ);

            }
        });

        binding.przyciskWydaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                zmienStan(OperacjaMagazynowa.WYDAJ);

            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                wybraneWarzywoNazwa = adapter.getItem(i).toString();
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY)
                .allowMainThreadQueries().build();

        if (bazaDanych.pozycjaMagazynowaDAO().size() == 0) {
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for (String nazwa : asortyment) {
                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa;
                pozycjaMagazynowa.QUANTITY = 0;
                pozycjaMagazynowa.OLDQUANTITY = 0;
                pozycjaMagazynowa.DATE = new Date(System.currentTimeMillis());
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }
    }

    private void aktualizuj() {
        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        binding.tekstStanMagazynu.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);
        binding.historia.setText("");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String date;
        Date dateOfChangeByName = bazaDanych.pozycjaMagazynowaDAO().findDateOfChangeByName(wybraneWarzywoNazwa);
        if (dateOfChangeByName != null) {
            date = dateFormat.format(dateOfChangeByName);
        } else {
            date = "no date";
        }
        binding.tekstData.setText(date);
        List<PozycjaMagazynowa> wybraneWarzywoHistoria = bazaDanych.pozycjaMagazynowaDAO().findHistoryByName(wybraneWarzywoNazwa);
        for (PozycjaMagazynowa pozycjaMagazynowa : wybraneWarzywoHistoria) {
             dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

            if (pozycjaMagazynowa.getDATE() != null) {
                date = dateFormat.format(pozycjaMagazynowa.getDATE());
            } else {
                date = "no date";
            }


            String line = date + " " + pozycjaMagazynowa.getOLDQUANTITY() + "->" + pozycjaMagazynowa.getQUANTITY() + "\n";
            binding.historia.append(line);
        }
    }

    private void zmienStan(OperacjaMagazynowa operacja) {

        Integer zmianaIlosci = null, nowaIlosc = null;

        try {
            zmianaIlosci = Integer.parseInt(binding.edycjaIlosc.getText().toString());
        } catch (NumberFormatException ex) {
            return;
        } finally {
            binding.edycjaIlosc.setText("");
        }

        switch (operacja) {
            case SKLADUJ:
                nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci;
                break;
            case WYDAJ:
                nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci;
                break;
        }

        PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
        pozycjaMagazynowa.NAME = wybraneWarzywoNazwa;
        pozycjaMagazynowa.QUANTITY = nowaIlosc;
        pozycjaMagazynowa.OLDQUANTITY = wybraneWarzywoIlosc;
        pozycjaMagazynowa.DATE = new Date(System.currentTimeMillis());
        bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
        aktualizuj();
    }
}