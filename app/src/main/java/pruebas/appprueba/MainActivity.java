package pruebas.appprueba;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Estas variables permitirán obtener los controles creados y así poder manipularlos
    EditText edtIdentificacion;
    EditText edtNombres;
    EditText edtApellidos;
    EditText edtTelefono;
    EditText edtDireccion;
    EditText edtCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeamos las variables creadas con los controles. De esta manera podemos setear valores u obtenerlos.
        edtIdentificacion = (EditText) findViewById(R.id.edtIdentificacion);
        edtNombres = (EditText) findViewById(R.id.edtNombres);
        edtApellidos = (EditText) findViewById(R.id.edtApellidos);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Estamos asignando el menu al activity
        getMenuInflater().inflate(R.menu.menu_clientes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // De acuerdo al icono seleccionado, se debe realizar una acción
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_add:

                // Acá debemos trabajar con todos los controles que definen el cliente para poderlo ingresar.
                String ident = edtIdentificacion.getText().toString();
                String nombres = edtNombres.getText().toString();
                String apellidos = edtApellidos.getText().toString();
                String telefono = edtTelefono.getText().toString();
                String direccion = edtDireccion.getText().toString();
                String correo = edtCorreo.getText().toString();

                // Validamos que se ingresen todos los campos
                if(ident.length() > 0 && nombres.length() > 0 && apellidos.length() > 0){
                    // Abrimos la base de datos de clientes
                    UsuarioSQLiteHelper usuario = new UsuarioSQLiteHelper(this, "DBClientes", null, 1);
                    SQLiteDatabase db = usuario.getWritableDatabase();

                    db.execSQL("INSERT INTO Cliente (Identificacion, Nombres, Apellidos, Telefonos, Direccion, Correo) VALUES(" + ident + ",'" + nombres + "','" + apellidos + "','" + telefono + "','" + direccion + "','" + correo + "')");
                    db.close();
                    Toast.makeText(this, "El usuario se ha creado exitosamente", Toast.LENGTH_SHORT).show();
                    edtIdentificacion.setText("");
                    edtNombres.setText("");
                    edtApellidos.setText("");
                    edtTelefono.setText("");
                    edtDireccion.setText("");
                    edtCorreo.setText("");
                }
                else{
                    Toast.makeText(this, "Debe ingresar todos los datos asociados al usuario.", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
