package estudos.procergs.scheduler;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import estudos.procergs.entity.Usuario;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.service.UsuarioService;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioImportacaoScheduler {

    @Inject
    private Logger logger;

    @Inject
    private UsuarioService usuarioService; 

    @ConfigProperty(name = "rotinas.habilitadas")
    private String rotinasHabilitadas;

    @Inject
    private AutorizacaoRepository autorizacaoRepository;

    //@Scheduled(cron = "59 59 22 * * ?") 
    @Scheduled(cron = "0 19 20 * * ?") 
    public void excluirReservasCanceladas(ScheduledExecution execution) {
        try {
            if (!"S".equalsIgnoreCase(rotinasHabilitadas)) {
                logger.info("[SRPT-IMPORTACAO-USUARIOS] As rotinas do sistema estão desabilitadas."); //TODO: resolver encoding dos logs para exibir acentos corretamente
                return;
            }
            logger.info("[SRPT-IMPORTACAO-USUARIOS] Iniciando rotina...");

            this.definirUsuariodeSistema();

            Long quantidade = usuarioService.importarNovosUsuariosSOE();

            logger.info(String.format("[SRPT-IMPORTACAO-USUARIOS] %s novos usuários foram importados.", quantidade));
            logger.info("[SRPT-IMPORTACAO-USUARIOS] Rotina executada com sucesso.");
            
        } catch (Exception e) {
            logger.error("[SRPT-IMPORTACAO-USUARIOS] Erro na execução da rotina.", e);
        }
    }

    private void definirUsuariodeSistema() {
        Usuario usuario = usuarioService.consultarUsuarioSistema();
        autorizacaoRepository.incluirAutorizacao(usuario, "127.0.0.0");
    }
}
