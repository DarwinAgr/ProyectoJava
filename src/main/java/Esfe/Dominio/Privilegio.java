package Esfe.Dominio;

public class Privilegio {
    private int idPrivilegio;
    private String name;
    private String description;
    private int status;

    public Privilegio() {
    }

    public Privilegio(int idPrivilegio, String name, String description, int status) {
        this.idPrivilegio = idPrivilegio;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getIdPrivilegio() {
        return idPrivilegio;
    }

    public void setIdPrivilegio(int idPrivilegio) {
        this.idPrivilegio = idPrivilegio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
