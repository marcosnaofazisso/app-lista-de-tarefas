package com.example.listadetarefas.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "DB_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlInit = "DROP TABLE " + TABELA_TAREFAS + " ;";

        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABELA_TAREFAS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL);";
//
//        try {
//            db.execSQL(sqlInit);
//
//        } catch (Exception erro) {
//            Log.i("INFO DB", "Erro ao limpar a tabela: " + erro.getMessage());
//
//        }

        try {
            db.execSQL(sql);
            Log.i("INFO DB", "Tabela criada com sucesso!");

        } catch (Exception e) {
            Log.i("INFO DB", "Erro ao criar a tabela: " + e.getMessage());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
