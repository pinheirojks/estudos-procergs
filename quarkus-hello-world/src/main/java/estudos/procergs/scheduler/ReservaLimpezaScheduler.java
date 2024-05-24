package estudos.procergs.scheduler;

import org.jboss.logging.Logger;

import estudos.procergs.entity.Usuario;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.service.ReservaService;
import estudos.procergs.service.UsuarioService;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ReservaLimpezaScheduler {

    @Inject
    private Logger logger;

    @Inject
    private ReservaService reservaService;

    @ConfigProperty(name = "rotinas.habilitadas")
    private String rotinasHabilitadas;

    @Inject
    private AutorizacaoRepository autorizacaoRepository;

    @Inject
    private UsuarioService usuarioService;    

    @Scheduled(cron = "0 28 14 * * ?") 
    public void excluirReservasCanceladas(ScheduledExecution execution) {
        try {
            if (!"S".equalsIgnoreCase(rotinasHabilitadas)) {
                logger.info("[LIMPEZA-RESERVAS] As rotinas do sistema estão desabilitadas."); //TODO: resolver encoding dos logs para exibir acentos corretamente
                return;
            }
            logger.info("[LIMPEZA-RESERVAS] Iniciando rotina...");

            this.definirUsuariodeSistema();

            Long quantidade = reservaService.excluirReservasCanceladas();
            logger.info(String.format("[LIMPEZA-RESERVAS] %s reservas foram canceladas excluídas.", quantidade));
            logger.info("[LIMPEZA-RESERVAS] Rotina executada com sucesso.");
            
        } catch (Exception e) {
            logger.error("[LIMPEZA-RESERVAS] Erro na execução da rotina.", e);
        }
    }

    private void definirUsuariodeSistema() {
        Usuario usuario = usuarioService.consultarUsuarioSistema();
        autorizacaoRepository.incluirAutorizacao(usuario, "127.0.0.0");
    }
}
