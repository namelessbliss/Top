package adolfopardo.top;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrador on 08/02/2018.
 */

@Table(database = TopDB.class)

public class Artista extends BaseModel{
    public static final String ORDEN = "orden";
    public static final String ID = "id";

    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String nombre;
    @Column
    private String apellido;
    @Column
    private String genero;
    @Column
    private long fechaNacimiento;
    @Column
    private String lugarNacimiento;
    @Column
    private short etatura;
    @Column
    private String notas;
    @Column
    private int orden;
    @Column
    private String fotoUrl;

    public Artista() {
    }

    public Artista(String nombre, String apellido, String genero, long fechaNacimiento,
                   String lugarNacimiento, short etatura, String notas, int orden, String fotoUrl) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.etatura = etatura;
        this.notas = notas;
        this.orden = orden;
        this.fotoUrl = fotoUrl;
    }

    public Artista(long id, String nombre, String apellido, String genero, long fechaNacimiento,
                   String lugarNacimiento, short etatura, String notas, int orden, String fotoUrl) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.etatura = etatura;
        this.notas = notas;
        this.orden = orden;
        this.fotoUrl = fotoUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public long getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(long fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public short getEtatura() {
        return etatura;
    }

    public void setEtatura(short etatura) {
        this.etatura = etatura;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
    public String getNombreCompleto() {
        return this.nombre+ " " + this.apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artista artista = (Artista) o;

        return id == artista.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
