package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemFormActivity extends AppCompatActivity {
    private EditText txtTexto;
    private RadioGroup radioGroup;
    private Button btnInsere;
    private Button btnCancela;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);

        dbHelper = new DatabaseHelper(this);
        txtTexto = findViewById(R.id.txtTexto);
        radioGroup = findViewById(R.id.radio_group);
        btnInsere = findViewById(R.id.btnInsere);
        btnInsere.setBackgroundColor(Color.RED);
        btnCancela = findViewById(R.id.btnCancela);
        btnCancela.setBackgroundColor(Color.RED);
        btnCancela.setOnClickListener(v -> finish());

        btnInsere.setOnClickListener(v -> {
            criarItem();

        });
    }

    private void criarItem() {
        String textoInserido = txtTexto.getText().toString();
        int RadioId = radioGroup.getCheckedRadioButtonId();

        if (textoInserido.isEmpty()) {
            Toast.makeText(this, "Digite um texto", Toast.LENGTH_SHORT).show();
            return;
        }

        if (RadioId == -1) {
            Toast.makeText(this, "Escolha uma cor", Toast.LENGTH_SHORT).show();
            return;
        }


        int corSelecionada = Color.BLACK;

        if (RadioId == R.id.radVermelho) {
            corSelecionada = Color.RED;
        } else if (RadioId == R.id.radVerde) {
            corSelecionada = Color.GREEN;
        } else if (RadioId == R.id.radAzul) {
            corSelecionada = Color.BLUE;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("texto", textoInserido);
        values.put("cor", corSelecionada);
        long newRowId = db.insert("itens", null, values);

        if (newRowId != -1) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("TEXTO_INSERIDO", textoInserido);
            resultIntent.putExtra("COR_SELECIONADA", corSelecionada);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }


}
