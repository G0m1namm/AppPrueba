package pruebas.appprueba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paulo on 04/05/2016.
 */
public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    // Creamos una variable que contendrá la sentencia SQL de creación de la tabla
    String sql = "CREATE TABLE Cliente (Identificacion INTEGER, Nombres TEXT, Apellidos TEXT)";

    public UsuarioSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Este método se ejecuta automáticamente cuando la base de datos no existe
        // es decir, que la primera vez que se hace llamado a la clase, crea la BD
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Este método se ejecuta cuando se detecta que la versión de la base de datos
        // cambió, por lo que se debe definir todos los procesos de migración de la estructura
        // anterior a la estructura nueva. Por simplicidad del ejemplo, lo que haremos es eliminar la
        // tabla existente y crearla nuevamente.

        db.execSQL("DROP TABLE IF EXISTS Cliente");
        db.execSQL(sql);
    }
}
