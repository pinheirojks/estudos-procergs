package estudos.procergs.dto;

import jakarta.ws.rs.QueryParam;

public class UsuarioPesqDTO {

    @QueryParam(value = "login")
    private String login;

    @QueryParam(value = "ativo")
    private Boolean ativo;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
