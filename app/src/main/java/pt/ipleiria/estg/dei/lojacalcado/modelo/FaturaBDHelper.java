package pt.ipleiria.estg.dei.lojacalcado.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FaturaBDHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "BDFaturas", TABLE_NAME = "faturas", TABLE_NAME_LINHAS = "fatura_linhas";
    private static final String ID = "id", ID_USERDATA = "id_userdata", DATA = "data", ID_FATURA = "id_fatura", ID_PRODUTO = "id_produto", QUANTIDADE = "quantidade", PRECO = "preco";
    private final SQLiteDatabase db;

    public FaturaBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                ID_USERDATA + " INTEGER, " +
                DATA + " TEXT)";

        String sqlCreateTableLinhas = "CREATE TABLE " + TABLE_NAME_LINHAS + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                ID_FATURA + " INTEGER, " +
                ID_PRODUTO + " INTEGER, " +
                QUANTIDADE + " INTEGER, " +
                PRECO + " DOUBLE)";

        sqLiteDatabase.execSQL(sqlCreateTable);
        sqLiteDatabase.execSQL(sqlCreateTableLinhas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME_LINHAS);
        this.onCreate(sqLiteDatabase);
    }

    public void adicionarFaturaBD(Fatura fatura) {
        ContentValues values = new ContentValues();

        values.put(ID, fatura.getId());
        values.put(ID_USERDATA, fatura.getId_userdata());
        values.put(DATA, fatura.getData());

        this.db.insert(TABLE_NAME, null, values);
    }

    public void adicionarFaturaLinhaBD(FaturaLinha faturaLinha) {
        ContentValues values = new ContentValues();

        values.put(ID, faturaLinha.getId());
        values.put(ID_FATURA, faturaLinha.getId_fatura());
        values.put(ID_PRODUTO, faturaLinha.getId_produto());
        values.put(QUANTIDADE, faturaLinha.getQuantidade());
        values.put(PRECO, faturaLinha.getPreco());

        this.db.insert(TABLE_NAME_LINHAS, null, values);
    }

    public boolean removerFaturaBD(Fatura fatura) {
        Cursor cursor = this.db.query(TABLE_NAME_LINHAS, new String[]{ID}, ID_FATURA + " = ?", new String[]{String.valueOf(fatura.getId())}, null, null, null);
        if (cursor.moveToFirst()) {
            this.db.delete(TABLE_NAME_LINHAS, ID_FATURA + " = ?", new String[]{String.valueOf(fatura.getId())});
        }
        cursor.close();

        int nLinhasDelete = this.db.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(fatura.getId())});
        return nLinhasDelete > 0;
    }

    public List<Fatura> getAllFaturasBD() {
        ArrayList<Fatura> faturas = new ArrayList<>();

        Cursor cursor = this.db.query(TABLE_NAME, new String[]{ID, ID_USERDATA, DATA}, null, null, null, null, null);
        // TODO: buscar pelo nome da coluna
        if (cursor.moveToFirst()) {
            do {
                Fatura auxFatura = new Fatura(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        new ArrayList<>()
                );
                faturas.add(auxFatura);
            } while (cursor.moveToNext());
        }

        return faturas;
    }

    public List<FaturaLinha> getAllFaturaLinhasBD() {
        ArrayList<FaturaLinha> faturaLinhas = new ArrayList<>();

        Cursor cursor = this.db.query(TABLE_NAME_LINHAS, new String[]{ID, ID_FATURA, ID_PRODUTO, QUANTIDADE, PRECO}, null, null, null, null, null);
        // TODO: buscar pelo nome da coluna
        if (cursor.moveToFirst()) {
            do {
                FaturaLinha faturaLinha = new FaturaLinha(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getDouble(4)
                );
                faturaLinhas.add(faturaLinha);
            } while (cursor.moveToNext());
        }

        return faturaLinhas;
    }

    public void removerAllFaturasBD() {
        this.db.delete(TABLE_NAME, null, null);
        this.db.delete(TABLE_NAME_LINHAS, null, null);
    }
}