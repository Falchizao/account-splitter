package com.example.suficiencia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class CadastroPessoaActivity extends AppCompatActivity {

    private static AutoCompleteTextView edt_nome;
    private static MainDatabaseSqLite dbSQLITE;
    private static SQLiteDatabase sqLiteDatabase;
    private static String []nomes;
    private static Button btn_cadastrarPessoa;
    int []id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoa);
        dbSQLITE = new MainDatabaseSqLite(CadastroPessoaActivity.this);
        findId();
        getNomes();
        getData();
    }

    private void findId(){
        edt_nome = findViewById(R.id.edt_nome);
        btn_cadastrarPessoa = findViewById(R.id.btn_cadastrarPessoa);
    }

    private void getData(){
        btn_cadastrarPessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_nome.getText().toString().length() < 1){
                    handleToast("Insira um nome aceitável!");
                    return;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("nome", edt_nome.getText().toString());
                sqLiteDatabase= dbSQLITE.getWritableDatabase();
                Long returnIdInsert = sqLiteDatabase.insert("pessoa", null, contentValues);
                if(returnIdInsert!=null){
                    handleToast("Pessoa inserida no banco com sucesso");
                    cleanFields();
                }else{
                    handleToast("Algo de errado aconteceu na inserção, tente novamente...");
                }
            }
        });
    }

    private void cleanFields(){
        edt_nome.setText("");
    }

    private void handleToast(String message){
        Toast.makeText(CadastroPessoaActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void getNomes(){
        //GetAll
        dbSQLITE = new MainDatabaseSqLite(CadastroPessoaActivity.this);
        sqLiteDatabase= dbSQLITE.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from pessoa", null);

        if(cursor.getCount()>0){
            id = new int[cursor.getCount()];
            nomes = new String[cursor.getCount()];
            int i=0;
            while(cursor.moveToNext()){
                id[i]= cursor.getInt(0);
                nomes[i]=cursor.getString(1);
                i++;
            }

            ArrayAdapter nomesAdapter = new ArrayAdapter(CadastroPessoaActivity.this, android.R.layout.simple_list_item_1, nomes);
            edt_nome.setAdapter(nomesAdapter);
            edt_nome.setThreshold(1);
        }
    }
}