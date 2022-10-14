package com.example.suficiencia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static Button btn_cadastro, btn_despesas, btn_ratear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findId();
        getData();
    }

    private void findId(){
        btn_cadastro = findViewById(R.id.btn_cadastrar);
        btn_despesas = findViewById(R.id.btn_despesas);
        btn_ratear = findViewById(R.id.btn_ratear);
    }

    private void getData(){
        btn_ratear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowDespesasList.class));
            }
        });

        btn_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroPessoaActivity.class));
            }
        });

        btn_despesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroDespesaActivity.class));
            }
        });
    }

}