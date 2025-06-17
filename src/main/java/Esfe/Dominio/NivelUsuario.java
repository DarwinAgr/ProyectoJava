package Esfe.Dominio;

public class NivelUsuario {
    private int idNivel;
    private String name;
    private String description;
    private int minPoint;
    private int maxPoint;
    private int status;
    private int idPrivilegio;

    private String privilegioName;

    public String getPrivilegioName(){ return privilegioName;}
    public void setPrivilegioName(String privilegioName){this.privilegioName = privilegioName;}

    public NivelUsuario() {
    }

    public NivelUsuario(int idNivel, String name, String description, int minPoint, int maxPoint, int status, int idPrivilegio) {
        this.idNivel = idNivel;
        this.name = name;
        this.description = description;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.status = status;
        this.idPrivilegio = idPrivilegio;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
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

    public int getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(int minPoint) {
        this.minPoint = minPoint;
    }

    public int getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdPrivilegio() {
        return idPrivilegio;
    }

    public void setIdPrivilegio(int idPrivilegio) {
        this.idPrivilegio = idPrivilegio;
    }

    public String getStrStatus(){
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
