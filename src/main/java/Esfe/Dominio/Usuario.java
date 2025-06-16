package Esfe.Dominio;

public class Usuario {
    private int idUsuario;
    private String name;
    private String passwordHash;
    private String email;
    private int status;
    private int idNivelUsuario;

    public Usuario() {
    }

    public Usuario(int idUsuario, String name, String passwordHash, String email, int status, int idNivelUsuario) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.status = status;
        this.idNivelUsuario = idNivelUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdNivelUsuario() {
        return idNivelUsuario;
    }

    public void setIdNivelUsuario(int idNivelUsuario) {
        this.idNivelUsuario = idNivelUsuario;
    }

    public String getStrEstatus(){
        String str="";
        switch (status) {
            case 1:
                str = "ACTIVO";
                break;
            case 2:
                str = "INACTIVO";
                break;
            default:
                str = "";
        }
        return str;
    }
}
