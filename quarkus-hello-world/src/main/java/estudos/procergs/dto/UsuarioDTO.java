package estudos.procergs.dto;

import estudos.procergs.entity.Usuario;

public class UsuarioDTO {
    public Long id;

    public String login;

    public String senha;

    public Boolean ativo;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Usuario u) {
        this.id = u.id;
        this.login = u.login;
        this.senha = u.senha;
        this.ativo = u.ativo;
    }
}
