package com.example.mindstep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mindstep.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_REGISTROS = "registros";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_TITULO_FECHA = "titulo_fecha";
    public static final String COLUMN_RIESGO = "riesgo";
    public static final String COLUMN_RIESGO_COLOR = "riesgo_color";
    public static final String COLUMN_SUENO = "sueno";
    public static final String COLUMN_ESTRES = "estres";
    public static final String COLUMN_ANIMO = "animo";
    public static final String COLUMN_TAGS = "tags";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_REGISTROS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_TITULO_FECHA + " TEXT, " +
                COLUMN_RIESGO + " TEXT, " +
                COLUMN_RIESGO_COLOR + " TEXT, " +
                COLUMN_SUENO + " TEXT, " +
                COLUMN_ESTRES + " TEXT, " +
                COLUMN_ANIMO + " TEXT, " +
                COLUMN_TAGS + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);

        // Pre-poblar datos de ejemplo según la especificación si está vacía
        insertarRegistroInicial(db, "08 Octubre", "Hoy - 08 Octubre", "Riesgo Bajo", "#22C55E", "Sueño 7.5h", "Estrés 4/10", "Ánimo 🙂 Bien", "#Trabajo #Ejercicio");
        insertarRegistroInicial(db, "07 Octubre", "Ayer - 07 Octubre", "Riesgo Moderado", "#F59E0B", "Sueño 5.0h", "Estrés 8/10", "Ánimo 😫 Agotado", "#Exámenes #Insomnio");
        insertarRegistroInicial(db, "06 Octubre", "06 Octubre", "Riesgo Bajo", "#22C55E", "Sueño 8.0h", "Estrés 3/10", "Ánimo 😊 Tranquilo", "#Descanso #Meditacion");
    }

    public long insertarRegistro(String fecha, String tituloFecha, String riesgo, String color, String sueno, String estres, String animo, String tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_TITULO_FECHA, tituloFecha);
        values.put(COLUMN_RIESGO, riesgo);
        values.put(COLUMN_RIESGO_COLOR, color);
        values.put(COLUMN_SUENO, sueno);
        values.put(COLUMN_ESTRES, estres);
        values.put(COLUMN_ANIMO, animo);
        values.put(COLUMN_TAGS, tags);
        return db.insert(TABLE_REGISTROS, null, values);
    }

    private void insertarRegistroInicial(SQLiteDatabase db, String fecha, String tituloFecha, String riesgo, String color, String sueno, String estres, String animo, String tags) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_TITULO_FECHA, tituloFecha);
        values.put(COLUMN_RIESGO, riesgo);
        values.put(COLUMN_RIESGO_COLOR, color);
        values.put(COLUMN_SUENO, sueno);
        values.put(COLUMN_ESTRES, estres);
        values.put(COLUMN_ANIMO, animo);
        values.put(COLUMN_TAGS, tags);
        db.insert(TABLE_REGISTROS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTROS);
        onCreate(db);
    }

    public List<RegistroModel> obtenerHistorialRegistros() {
        List<RegistroModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REGISTROS + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
                String tituloFecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO_FECHA));
                String riesgo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RIESGO));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RIESGO_COLOR));
                String sueno = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUENO));
                String estres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTRES));
                String animo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANIMO));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGS));

                lista.add(new RegistroModel(id, fecha, tituloFecha, riesgo, color, sueno, estres, animo, tags));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Si la base de datos estaba vacía por algún motivo, retornar datos por defecto
        if (lista.isEmpty()) {
            lista.add(new RegistroModel(1, "08 Octubre", "Hoy - 08 Octubre", "Riesgo Bajo", "#22C55E", "Sueño 7.5h", "Estrés 4/10", "Ánimo 🙂 Bien", "#Trabajo #Ejercicio"));
            lista.add(new RegistroModel(2, "07 Octubre", "Ayer - 07 Octubre", "Riesgo Moderado", "#F59E0B", "Sueño 5.0h", "Estrés 8/10", "Ánimo 😫 Agotado", "#Exámenes #Insomnio"));
            lista.add(new RegistroModel(3, "06 Octubre", "06 Octubre", "Riesgo Bajo", "#22C55E", "Sueño 8.0h", "Estrés 3/10", "Ánimo 😊 Tranquilo", "#Descanso #Meditacion"));
        }

        return lista;
    }

    public String obtenerResumenMensual() {
        return "24 de 31 días registrados (77% consistencia)";
    }

    public void limpiarHistorial() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REGISTROS, null, null);
    }
}
