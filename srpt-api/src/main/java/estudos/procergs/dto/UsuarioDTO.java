package estudos.procergs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    
    private Long id;

    private Long matricula;

    private String nome;

    private String senha;

    private PerfilUsuarioDTO perfil;

    private Boolean ativo;
}
