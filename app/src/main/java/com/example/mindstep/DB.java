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
    private static final int DATABASE_VERSION = 3;

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
    public static final String COLUMN_NOTAS = "notas";

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
                COLUMN_TAGS + " TEXT, " +
                COLUMN_NOTAS + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
        // Base de datos limpia sin registros por defecto: el usuario registra sus propios días
    }

    public long insertarRegistro(String fecha, String tituloFecha, String riesgo, String color, String sueno, String estres, String animo, String tags, String notas) {
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
        values.put(COLUMN_NOTAS, notas != null ? notas : "");
        return db.insert(TABLE_REGISTROS, null, values);
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
        return lista;
    }

    public int obtenerTotalRegistros() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REGISTROS, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    public String obtenerResumenMensual() {
        int total = obtenerTotalRegistros();
        if (total == 0) {
            return "Aún no tienes registros este mes. ¡Agrega el primero!";
        }
        int porcentaje = Math.min(100, Math.max(1, (int) ((total / 31.0f) * 100)));
        return total + " de 31 días registrados este mes (" + porcentaje + "% consistencia)";
    }

    public float obtenerPromedioSueno() {
        List<RegistroModel> lista = obtenerHistorialRegistros();
        if (lista.isEmpty()) return 0.0f;
        float suma = 0;
        int count = 0;
        for (RegistroModel r : lista) {
            try {
                String s = r.getSueno().replace("Sueño", "").replace("h", "").trim();
                suma += Float.parseFloat(s);
                count++;
            } catch (Exception ignored) {}
        }
        return count > 0 ? (suma / count) : 0.0f;
    }

    public float obtenerPromedioEstres() {
        List<RegistroModel> lista = obtenerHistorialRegistros();
        if (lista.isEmpty()) return 0.0f;
        float suma = 0;
        int count = 0;
        for (RegistroModel r : lista) {
            try {
                String s = r.getEstres().replace("Estrés", "").replace("/10", "").trim();
                suma += Float.parseFloat(s);
                count++;
            } catch (Exception ignored) {}
        }
        return count > 0 ? (suma / count) : 0.0f;
    }

    public void limpiarHistorial() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REGISTROS, null, null);
    }
}
