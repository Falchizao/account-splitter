package com.example.suficiencia;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowDespesasList extends AppCompatActivity {
    MainDatabaseSqLite dbSQLITE;
    SQLiteDatabase sqLiteDatabase;
    ListView listView;
    String []nomes;
    Double []values;
    int []id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_despesas_list);

        dbSQLITE= new MainDatabaseSqLite(ShowDespesasList.this);
        findind();
        showDespesas();
    }

    private void findind(){
        listView=findViewById(R.id.listview);
    }

    private void showDespesas(){

        //GetAll
        sqLiteDatabase= dbSQLITE.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from pessoa", null);

        if(cursor.getCount()>0){

            id = new int[cursor.getCount()];
            nomes = new String[cursor.getCount()];
            values = new Double[cursor.getCount()];
            int i=0;

            while(cursor.moveToNext()){
                id[i]= cursor.getInt(0);
                nomes[i]=cursor.getString(1);
                values[i]= cursor.getDouble(2);
                i++;
            }

            AdapterLista adapter = new AdapterLista();
            listView.setAdapter(adapter);
        }
    }

    //Set do layout custom
    private class AdapterLista extends BaseAdapter {

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return nomes.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView nameAndValues;

            //Layout atualmente apenas com um textfield, porém dá pra colocar image ou algum button de edit/delete
            convertView = LayoutInflater.from(ShowDespesasList.this).inflate(R.layout.layout_show_despesas, parent, false);
            nameAndValues = convertView.findViewById(R.id.despesasPessoais);
            nameAndValues.setText(nomes[position] + "- R$ " + values[position].toString());
            return convertView;
        }
    }

}