package estudos.procergs.integration.soe;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSoe {
  private Long         codUsuario;

  private Long         codOrganizacao;

  private String       siglaOrganizacao;

  private String       nomeUsuario;

  private Long         matricula;

  private Long         idSetor;

  private String       siglaSetor;

  private String       uidLdap;

  private Long         codOrgPai;

  private String       siglaOrgPai;

  private String       dtPrazoOperacao;

  private String       dthUltSessao;

  private List<String> emails;
}
