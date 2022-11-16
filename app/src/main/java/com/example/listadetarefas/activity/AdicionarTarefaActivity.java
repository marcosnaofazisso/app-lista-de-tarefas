package com.example.listadetarefas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.listadetarefas.R;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        editTarefa = findViewById(R.id.textTarefa);


        //Recuperar tarefa, caso seja edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Colocar tarefa recuperada na caixa de texto
        if (tarefaAtual != null) {
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemSalvar) {
            //Executa a ação para o item salvar
            //Aqui vamos utilizar um DAO

            TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

            if (tarefaAtual != null) { //Editar Antiga Tarefa
                String tarefaDigitada = editTarefa.getText().toString();

                if (!tarefaDigitada.isEmpty()) {

                    Tarefa tarefa = new Tarefa();
                    tarefa.setId(tarefaAtual.getId());
                    tarefa.setNomeTarefa(tarefaDigitada);

                    if (tarefaDAO.atualizar(tarefa)) {
                        finish();
                        Toast.makeText(this, "Tarefa editada com sucesso!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Erro ao editar tarefa!", Toast.LENGTH_SHORT).show();

                    }

                }

            } else { //Salvar Nova Tarefa


                String tarefaDigitada = editTarefa.getText().toString();
                if (!tarefaDigitada.isEmpty()) {
                    Tarefa tarefa = new Tarefa();
                    tarefa.setNomeTarefa(tarefaDigitada);

                    if (tarefaDAO.salvar(tarefa)) {
                        //Finaliza uma activity
                        finish();
                        Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(this, "Digite uma tarefa para salvar", Toast.LENGTH_SHORT).show();
                }
            }


        }

        return super.onOptionsItemSelected(item);
    }
}
