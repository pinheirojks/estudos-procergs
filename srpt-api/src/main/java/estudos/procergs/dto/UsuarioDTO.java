package estudos.procergs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    
    private Long id;

    private String login;

    private String senha;

    private PerfilUsuarioDTO perfil;

    private Boolean ativo;
}
