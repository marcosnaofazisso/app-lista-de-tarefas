package com.example.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.listadetarefas.R;
import com.example.listadetarefas.adapter.TarefaAdapter;
import com.example.listadetarefas.helper.DbHelper;
import com.example.listadetarefas.helper.RecyclerItemClickListener;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    private TextView textInit;
    private Button buttonDeletarTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurar recycler
        recyclerView = findViewById(R.id.recyclerView);

        //Usar o helper do Banco de Dados
        DbHelper db = new DbHelper(getApplicationContext());

//        ContentValues cv = new ContentValues();
//        cv.put("nome", "Teste");
//        db.getWritableDatabase().insert("tarefas", null, cv);

        textInit = findViewById(R.id.textInit);
        buttonDeletarTodos = findViewById(R.id.buttonDeletarTodos);

        //Adicionar evento de clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Recuperar tarefa para edição
                                Tarefa tarefaSelecionada = listaTarefas.get(position);

                                //Envia tarefa para nova acitivity
                                Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                                intent.putExtra("tarefaSelecionada", tarefaSelecionada);

                                startActivity(intent);

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {

                                //Recuperar a tarefa que usuário quer deletar
                                tarefaSelecionada = listaTarefas.get(position);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                                //Configurar titulo e mensagem
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + " ?");

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                                        if (tarefaDAO.deletar(tarefaSelecionada)) {

                                            carregarListaTarefas();
                                            Toast.makeText(getApplicationContext(), "Tarefa excluída com sucesso!", Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(getApplicationContext(), "Não foi possível excluir essa tarefa...", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                //Colocando o segundo parametro como nulo, não teremos um evento disparado
                                dialog.setNegativeButton("Não", null);

                                //Exibit a dialog
                                dialog.create();
                                dialog.show();


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }

                        }
                )
        );

        buttonDeletarTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if (listaTarefas.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Não há tarefas cadastradas", Toast.LENGTH_SHORT).show();

                } else {
                    if (tarefaDAO.deletarTodos(listaTarefas)) {
                        carregarListaTarefas();
                        Toast.makeText(getApplicationContext(), "Tarefas apagadas com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro!", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void carregarListaTarefas() {

        //Listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        //Exibir a lista

        // configurar adapter
        tarefaAdapter = new TarefaAdapter(listaTarefas);


        // configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);

        if (listaTarefas.size() < 1) {
            textInit.setText("Lista de tarefas vazia...");
        } else {
            textInit.setText("");

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarListaTarefas();


    }
}
