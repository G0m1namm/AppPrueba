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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        Button btnIngresarConocimiento = (Button) findViewById(R.id.btnIngresarConocimiento);

        // Mapeamos las variables creadas con los controles. De esta manera podemos setear valores u obtenerlos.
        edtId = (EditText) findViewById(R.id.edtId);
        edtConocimiento = (EditText) findViewById(R.id.edtConocimiento);

        btnIngresarConocimiento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InsertarConocimientos();
            }
        });

        btnRegresar = (ImageView) findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

    }

    private void InsertarConocimientos(){
        Argumento[] datos = new Argumento[1];
        datos[0].setKey("_id");
        datos[0].setValue(edtId.getText().toString());
        datos[1].setKey("Conocimientos");
        datos[1].setValue(edtConocimiento.getText().toString());

        TareaAsync tarea = new TareaAsync();
        tarea.setDatos(datos);
        tarea.setMetodo(getResources().getString(R.string.method_insertar_conocimientos));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            tarea.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
        else
        {
            tarea.execute();
        }

    }

    private void CargarConocimientos(){
        IngresarActivity.AdaptadorIntereses adapter = new IngresarActivity.AdaptadorIntereses(IngresarActivity.this, lstListadoRegistros);
        ListView lstListadoIntereses = (ListView) findViewById(R.id.lstIntereses);
        lstListadoIntereses.setAdapter(adapter);
    }
    public class TareaAsync extends AsyncTask<Object, Object, Object>{

        public Argumento[] getDatos() {
            return datos;
        }

        public void setDatos(Argumento[] datos) {
            this.datos = datos;
        }

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

        protected Object doInBackground(Object... params){
            List<Object> lstObject = new ArrayList<Object>();

            try{
                JSONArray lstResultado = HttpClientHelper.GET(getMetodo(), getDatos(), IngresarActivity.this);

                for(int i=0; i < lstResultado.length(); i++){
                    JSONObject item = lstResultado.getJSONObject(i);
                    Conocimientos registro = new Conocimientos(Integer.valueOf(item.getString("_id")), item.getString("Conocimientos"));
                    lstObject.add(registro);
                }
            }
            catch(Exception ex){
                Log.e("Consulta Conocimientos", ex.getMessage());
            }
            finally {
                return lstObject;
            }
        }


        public void onPreExecute(){

        }

        // Cuando finaliza la consulta, se ejecuta este método
        public void onPostExecute(Object result){
            processFinish(result);
            super.onPostExecute(result);
        }


    }

    public void processFinish(Object result){
        lstListadoRegistros = (List<Conocimientos>) result;
        CargarConocimientos();
    }

    // mapear a una entidad propia de la app y se entregue el resultado al listview
    class AdaptadorIntereses extends ArrayAdapter<Conocimientos> {

        AdaptadorIntereses(Context context, List<Conocimientos> datos){
            super(context, R.layout.listitem_intereses, datos);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View item = convertView;
            ConocimientosActivity.InteresesHolder holder;

            if(item == null)
            {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.listitem_intereses, null);

                // Se mapea cada item del listado con los controles del layout
                holder = new ConocimientosActivity.InteresesHolder();
                holder.id = (TextView) item.findViewById(R.id.txtId);
                holder.intereses = (TextView) item.findViewById(R.id.txtInteres);
                item.setTag(holder);
            } else{
                holder = (ConocimientosActivity.InteresesHolder)item.getTag();
            }

            // lstListadoRegistros es el listado con los resultados
            final Conocimientos registro = lstListadoRegistros.get(position);
            holder.intereses.setText(registro.getConocimientos());
            holder.id.setText(String.valueOf(registro.getId()));

            return(item);
        }
    }
    // Esto nos ayudará a tener un listview más completo
    // se crean los controles que se crearán por cada item del listview
    class InteresesHolder{
        TextView id;
        TextView intereses;
    }


}
