package pruebas.appprueba;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pruebas.appprueba.Data.HttpClientHelper;
import pruebas.appprueba.Entidades.Argumento;
import pruebas.appprueba.Entidades.Conocimientos;

public class IngresarActivity extends AppCompatActivity {

    // Estas variables permitirán obtener los controles creados y así poder manipularlos
    EditText edtId;
    EditText edtConocimiento;
    Button btnIngresar;
    ImageView btnRegresar;
    List<Conocimientos> lstListadoRegistros = new ArrayList<Conocimientos>();
    Argumento[] datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        Button btnIngresarConocimiento = (Button) findViewById(R.id.btnIngresarConocimiento);

        // Mapeamos las variables creadas con los controles. De esta manera podemos setear valores u obtenerlos.
        edtId = (EditText) findViewById(R.id.edtId);
        edtConocimiento = (EditText) findViewById(R.id.edtConocimiento);

        btnIngresarConocimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertarConocimientos();
            }
        });

        btnRegresar = (ImageView) findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    // Se crea un método que realiza la consulta.
    private void InsertarConocimientos() {
        // El método que llamaremos no contiene parámetros
        datos = new Argumento[2];
        datos[0] = new Argumento("id",edtId.getText().toString());
        datos[1] = new Argumento("nombre",edtConocimiento.getText().toString());

        // La clase TareaAsync se crea para realizar el llamado asincrono al servicio.
        TareaAsync tarea = new TareaAsync();
        // Se asignan los parámetros
        tarea.setDatos(datos);
        // Se asigna el método. Este método fue el configurado en el paso inicial (archivo strings.xml)
        tarea.setMetodo(getResources().getString(R.string.method_insertar_conocimientos));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tarea.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            tarea.execute();
        }
    }

    private void CargarIntereses() {
        AdaptadorIntereses adapter = new AdaptadorIntereses(IngresarActivity.this, lstListadoRegistros);
        ListView lstListadoIntereses = (ListView) findViewById(R.id.lstIntereses);
        lstListadoIntereses.setAdapter(adapter);
    }

    // Se debe crear una clase que realice la operación asincrona en un hilo diferente al hilo de ejecución principal de la app
    public class TareaAsync extends AsyncTask<Object, Object, Object> {

        // Posee unos argumentos
        public Argumento[] getDatos() {
            return datos;
        }

        public void setDatos(Argumento[] datos) {
            this.datos = datos;
        }

        // Posee un método
        public String getMetodo() {
            return Metodo;
        }

        public void setMetodo(String metodo) {
            Metodo = metodo;
        }

        // Variable que almacena los argumentos que puede tener el método del WCF
        public Argumento[] datos;

        // Variable que contiene el nombre del método a llamar
        public String Metodo;

        // Es el evento que se dispara cuando se hace el llamado a la clase.

        protected Object doInBackground(Object... params) {
            // Se crea una lista de objetos, no conocemos su estructura
            List<Object> lstObject = new ArrayList<Object>();

            try {
                // Se hace el llamado a la clase HttpClientHelper con los valores configurados para la instancia
                // TaskAsync
                JSONArray lstResultado = HttpClientHelper.GET(getMetodo(), getDatos(), IngresarActivity.this);

                // Se adiciona objeto por objeto del resultado al listado de objetos tipo Interes.
                // Si se va a realizar varios llamados al servicio, es necesario crear la lógica
                // puede ser a través de un switch(getMetodo()) y de acuerdo a cada método crear la
                // lista o el objeto a manipular.
                for (int i = 0; i < lstResultado.length(); i++) {
                    JSONObject item = lstResultado.getJSONObject(i);
                    Conocimientos registro = new Conocimientos(Integer.valueOf(item.getString("id")), item.getString("Conocimientos"));
                    lstObject.add(registro);
                }
            } catch (Exception ex) {
                Log.e("Ingreso de Intereses", ex.getMessage());
            } finally {
                return lstObject;
            }
        }

        // Este es un evento que se ejecuta antes del evento doInBackground
        // Puede por ejemplo iniciar un Progress para mostrarle al usuario que inició un proceso
        public void onPreExecute() {

        }

        // Cuando finaliza la consulta, se ejecuta este método
        // Por ejemplo, acá también es necesario tener un switch de acuerdo al método
        // para establecer el comportamiento de la pantalla a partir del llamado al servicio.
        public void onPostExecute(Object result) {
            processFinish(result);
            super.onPostExecute(result);
        }

    }

    // Al llamar al processFinish, se entrega el resultado a la variable global creada inicialmente
    // y se llama al método que crea los registros en pantalla
    public void processFinish(Object result) {
        lstListadoRegistros = (List<Conocimientos>) result;
        CargarIntereses();
    }

    // Se debe crear el adapter para que al momento de obtener los resultados desde el WCF se puede
    // mapear a una entidad propia de la app y se entregue el resultado al listview
    class AdaptadorIntereses extends ArrayAdapter<Conocimientos> {

        // Con este adapter, lo que hacemos es leer cada uno de los objetos resultantes del llamado al
        // servicio y renderizarlos en la pantalla
        AdaptadorIntereses(Context context, List<Conocimientos> datos) {
            super(context, R.layout.listitem_intereses, datos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            InteresesHolder holder;

            if (item == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.listitem_intereses, null);

                // Se mapea cada item del listado con los controles del layout
                holder = new InteresesHolder();
                // Se asigna a cada control del holder, su respectivo control del layout listitem_intereses
                holder.id = (TextView) item.findViewById(R.id.txtId);
                holder.intereses = (TextView) item.findViewById(R.id.txtInteres);
                item.setTag(holder);
            } else {
                holder = (InteresesHolder) item.getTag();
            }

            // lstListadoRegistros es el listado con los resultados
            final Conocimientos registro = lstListadoRegistros.get(position);
            // Se asignan los textos a cada control del holder
            holder.intereses.setText(registro.getConocimientos());
            holder.id.setText(String.valueOf(registro.getId()));

            return (item);
        }
    }


    // Esto nos ayudará a tener un listview más completo
    // se crean los controles que se crearán por cada item del listview
    static class InteresesHolder {
        // Este Holder debe ser consistente con los controles que se están
        // configurando para cada uno de los objetos de la lista resultante
        // Tiene los mismos componentes que el layout listitem_intereses
        TextView id;
        TextView intereses;
    }
}