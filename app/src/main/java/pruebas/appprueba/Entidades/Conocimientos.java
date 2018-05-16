package pruebas.appprueba.Entidades;

/**
 * Created by Paulo on 01/11/2016.
 */
public class Conocimientos {

    private int id;
    private String conocimientos;

    public Conocimientos(int id, String conocimiento){
        this.id = id;
        this.conocimientos = conocimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConocimientos() {
        return conocimientos;
    }

    public void setConocimientos(String conocimiento) {
        this.conocimientos = conocimiento;
    }
}
