package com.example.suficiencia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

//Despesas
public class CadastroDespesaActivity extends AppCompatActivity {

    private static EditText edt_value;
    private static MainDatabaseSqLite dbSQLITE;
    private static SQLiteDatabase sqLiteDatabase;
    private static String []nomes;
    private static Double []values;
    private static Spinner spNomes;
    private CheckBox cbDespesaCompartilhada;
    private static Integer personSelected;
    int []id;
    private static Button btn_cadastrarDespesa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_despesa);
        dbSQLITE = new MainDatabaseSqLite(CadastroDespesaActivity.this);
        findId();
        getNomes();
        getData();
    }

    private void findId(){
        edt_value = findViewById(R.id.edt_valueDespesa);
        btn_cadastrarDespesa = findViewById(R.id.btn_cadastrarDespesa);
        cbDespesaCompartilhada = findViewById(R.id.cb_despesaCompartilhada);
    }

    private void getData(){
        spNomes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                personSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Disable spinner when checkbox is checked
        cbDespesaCompartilhada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbDespesaCompartilhada.isChecked()){
                    spNomes.setVisibility(View.GONE);
                }else{
                    spNomes.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_cadastrarDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validation Null
                if(edt_value.getText().toString().length() == 0){
                    handleToast("Insira um valor diferente de null!");
                    return;
                }
                //Validation Negative or Zero
                if(Double.parseDouble(edt_value.getText().toString()) <= 0){
                    handleToast("Insira um valor diferente!");
                    return;
                }

                if(cbDespesaCompartilhada.isChecked()){ //Compartilhada

                    //Buscar todas as infos de cada pessoa
                    sqLiteDatabase= dbSQLITE.getReadableDatabase();
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from pessoa", null);

                    if(cursor.getCount()>0){
                        id = new int[cursor.getCount()];
                        values = new Double[cursor.getCount()];
                        int i=0;
                        while(cursor.moveToNext()){
                            id[i]= cursor.getInt(0);
                            values[i]=cursor.getDouble(2);
                            i++;
                        }

                        //Após pegar todos os valores das despesas das pessoas, vou fazer um update com o novo value

                        Double distribuicaoDespesa = Double.parseDouble(edt_value.getText().toString()) / cursor.getCount();
                        ContentValues contentValues = new ContentValues();
                        Integer identificationPerson = 1;
                        for(Double valorAtual: values){
                            contentValues.put("despesas", valorAtual + distribuicaoDespesa);
                            sqLiteDatabase=dbSQLITE.getWritableDatabase();
                            long returnSQLITE= sqLiteDatabase.update("pessoa", contentValues, "id=" +identificationPerson, null);
                            identificationPerson++;
                        }
                        handleToast("Despesa inserida com sucesso!");
                    }else{
                        handleToast("Ocorreu um erro ao buscar os usuários, tavez esteja vazia sua lista!");
                    }
                }else { //Única
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from pessoa WHERE id=" + (personSelected+1), null);
                    if (cursor.getCount() > 0) {
                        double value = 0.0;
                        try{
                            while(cursor.moveToNext()){
                                value= cursor.getInt(2);
                            }
                        }catch (Exception e){
                            handleToast(e.getMessage());
                        }
                        value += Double.parseDouble(edt_value.getText().toString());
                        spNomes = findViewById(R.id.nomes_spinner);

                        ArrayAdapter spinnerAdapter = new ArrayAdapter(CadastroDespesaActivity.this, android.R.layout.simple_spinner_item, nomes);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spNomes.setAdapter(spinnerAdapter);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("despesas", value);
                        sqLiteDatabase=dbSQLITE.getWritableDatabase();
                        long returnSQLITE= sqLiteDatabase.update("pessoa", contentValues, "id=" +(personSelected+1), null);
                        if(returnSQLITE!= -1){
                            handleToast("Despesa registrada com sucesso!");
                        }else{
                            handleToast("Algo de errado aconteceu ao salvar a despesa!");
                        }
                    }else{
                        handleToast("Ocorreu um erro ao encontrar este usuário!");
                    }
                }
                cleanFields();
            }
        });
    }

    private void cleanFields(){
        edt_value.setText("");
        cbDespesaCompartilhada.setSelected(false);
    }

    private void handleToast(String message){
        Toast.makeText(CadastroDespesaActivity.this, message, Toast.LENGTH_LONG).show();
    }

    //Pegar todos os nomes e colocar no spinner
    private void getNomes(){
        dbSQLITE = new MainDatabaseSqLite(CadastroDespesaActivity.this);
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
            spNomes = findViewById(R.id.nomes_spinner);

            ArrayAdapter spinnerAdapter = new ArrayAdapter(CadastroDespesaActivity.this, android.R.layout.simple_spinner_item, nomes);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spNomes.setAdapter(spinnerAdapter);
        }
    }

}