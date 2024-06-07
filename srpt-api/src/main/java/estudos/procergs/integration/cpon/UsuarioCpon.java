package estudos.procergs.integration.cpon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCpon {

    private Long id;

    private String matricula;

    private String nome;

    private SetorCpon setor;

    private String senha;

    private PerfilCpon perfil;

    private String token;
}
