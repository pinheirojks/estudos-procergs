package estudos.procergs.infra.soe;

import com.procergs.soeweb.AutenticacaoRN;
import com.procergs.soeweb.SOEException;
import com.procergs.soeweb.ed.AutenticacaoED;
import com.procergs.util.autentica.LogonED;
import com.procergs.util.autentica.SessionED;
import com.procergs.util.exception.ProcergsINTException;

import estudos.procergs.infra.excecao.NaoAutorizadoException;

public class Autentica {

    public Autentica() {
    }

    public SessionED logon(LogonED logonED) throws ProcergsINTException {
        return this.logonInterno(logonED);
    }

    @SuppressWarnings({ "removal", "deprecation" })
    private SessionED logonInterno(LogonED logonED) throws ProcergsINTException {
        String sCampo = "";

        ProcergsINTException pINTExc;
        try {
            AutenticacaoED autenticacaoED = new AutenticacaoED();
            autenticacaoED.setMaqAtu(logonED.getNro_IP());

            autenticacaoED.setSiglaOrganizacao(logonED.getOrganizacao());
            autenticacaoED.setIdeNaOrganizacao(new Long(logonED.getMatricula()));
            autenticacaoED.setSenha(logonED.getSenha());
            sCampo = "organizacao";

            AutenticacaoRN soeAutenticacao = new AutenticacaoRN(autenticacaoED);
            autenticacaoED = soeAutenticacao.logon();

            SessionED sessionED = autenticacaoED.getSessionED();
            int iCodErro = autenticacaoED.getCodRetorno();

            if (iCodErro != 101 && iCodErro != 127) {
                throw new NaoAutorizadoException("Matricula e senha inválidos: Código: " + iCodErro);
            }

            if (sessionED.getSES_Nro_IP() == null || sessionED.getSES_Nro_IP().equals(" ")) {
                sessionED.setSES_Nro_IP(logonED.getNro_IP());
            }

            sessionED.setSOE_TipoAutenticacao(logonED.getTipoAutenticacao());
            return sessionED;
            
        } catch (SOEException var9) {
            pINTExc = new ProcergsINTException("SOE");
            pINTExc.setClasse("Autentica");
            pINTExc.setMetodo("logon");
            pINTExc.setMensagem(var9.getDescription());
            pINTExc.setCampo(var9.getnomeCampo().toLowerCase());
            pINTExc.setException(var9);
            pINTExc.setSeveridade(10000);
            throw pINTExc;
        } catch (Exception var10) {
            pINTExc = new ProcergsINTException("SOE");
            pINTExc.setClasse("Autentica");
            pINTExc.setMetodo("logon");
            pINTExc.setMensagem("Problemas na inicializa\u00e7\u00e3o da Sess\u00e3o do usu\u00e1rio");
            pINTExc.setCampo(sCampo);
            pINTExc.setException(var10);
            pINTExc.setSeveridade(10000);
            throw pINTExc;
        }
    }
}
