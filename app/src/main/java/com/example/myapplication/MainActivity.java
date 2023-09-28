package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ItemLista> listaItens;
    private static final int REQUEST_CODE = 1;
    private ItemListaAdapter adapter;

    private ActivityResultLauncher<Intent> startActivityForResultLauncher;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        Button btnCriar = findViewById(R.id.btnCriar);
        btnCriar.setBackgroundColor(Color.RED);

        ListView listView = findViewById(R.id.list_view);
        listaItens = new ArrayList<>();
        adapter = new ItemListaAdapter(this, listaItens);
        listView.setAdapter(adapter);

        btnCriar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemFormActivity.class);
            startActivityForResultLauncher.launch(intent);
        });

        startActivityForResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        reloadItemsFromDatabase();
                    }
                }
        );

        reloadItemsFromDatabase();

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ItemLista itemToDelete = listaItens.get(position);
            deleteItemFromDatabase(itemToDelete.getId());
            listaItens.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ItemLista item = listaItens.get(position);
            String texto = item.getTexto();
            int cor = item.getCor();
            showItemDetails(texto, cor);
        });
    }

    private void reloadItemsFromDatabase() {
        listaItens.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"id", "texto", "cor"};
        Cursor cursor = db.query("itens", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String texto = cursor.getString(cursor.getColumnIndexOrThrow("texto"));
            int cor = cursor.getInt(cursor.getColumnIndexOrThrow("cor"));
            listaItens.add(new ItemLista(id, texto, cor));
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void deleteItemFromDatabase(int itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("itens", "id=?", new String[]{String.valueOf(itemId)});
    }

    private void showItemDetails(String texto, int cor) {
        String nomeCor = null;
        if (cor == Color.RED) {
            nomeCor = "Vermelho";
        } else if (cor == Color.BLUE) {
            nomeCor = "Azul";
        } else if (cor == Color.GREEN) {
            nomeCor = "Verde";
        }
        String message = texto + "\n" + nomeCor;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String textoInserido = data.getStringExtra("TEXTO_INSERIDO");
            int corSelecionada = data.getIntExtra("COR_SELECIONADA", Color.BLACK);

            ItemLista item = new ItemLista(textoInserido, corSelecionada);

            listaItens.add(item);
            adapter.notifyDataSetChanged();
        }
    }
}