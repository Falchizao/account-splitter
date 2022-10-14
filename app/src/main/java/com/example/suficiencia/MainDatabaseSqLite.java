package com.example.suficiencia;

import android.content.Context;
import androidx.annotation.Nullable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//Definição Banco
public class MainDatabaseSqLite extends SQLiteOpenHelper {
    public MainDatabaseSqLite(@Nullable Context context){
        super(context, "AccountSplitter", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase dbSQLITE) {
        //Pessoa (ID, NOME, VALORDESPESAS)
        String sql = "create table pessoa(id integer primary key, nome text, despesas decimal)";
        dbSQLITE.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbSQLITE, int oldVersion, int newVersion) {
        String sqlLite = "drop table if exists pessoa";
        dbSQLITE.execSQL(sqlLite);
        onCreate(dbSQLITE);
    }
}
