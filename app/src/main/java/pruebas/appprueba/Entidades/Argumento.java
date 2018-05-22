package pruebas.appprueba.Entidades;

/**
 * Created by Paulo on 01/11/2016.
 */
public class Argumento {

    private String key;
    private String value;

    // Vamos a crear el constructor de la clase
    public Argumento(String llave, String valor){
        this.key = llave;
        this.value = valor;
    }

    // Creamos los métodos Get y Set de las propiedades de la clase

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}