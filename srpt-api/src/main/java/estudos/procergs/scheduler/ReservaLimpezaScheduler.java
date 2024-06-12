package estudos.procergs.scheduler;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.service.ReservaService;
import estudos.procergs.service.UsuarioService;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    @Scheduled(cron = "59 59 23 * * ?") 
    public void excluirReservasCanceladas(ScheduledExecution execution) {
        try {
            if (!"S".equalsIgnoreCase(rotinasHabilitadas)) {
                logger.info("[SRPT-LIMPEZA-RESERVAS] As rotinas do sistema estão desabilitadas."); //TODO: resolver encoding dos logs para exibir acentos corretamente
                return;
            }
            logger.info("[SRPT-LIMPEZA-RESERVAS] Iniciando rotina...");

            this.definirUsuariodeSistema();

            Long quantidade = reservaService.excluirReservasCanceladas();
            logger.info(String.format("[SRPT-LIMPEZA-RESERVAS] %s reservas foram canceladas excluídas.", quantidade));
            logger.info("[SRPT-LIMPEZA-RESERVAS] Rotina executada com sucesso.");
            
        } catch (Exception e) {
            logger.error("[SRPT-LIMPEZA-RESERVAS] Erro na execução da rotina.", e);
        }
    }

    private void definirUsuariodeSistema() {
        AutorizacaoDTO autorizacao = new AutorizacaoDTO();
        autorizacao.setUsuario(usuarioService.consultarUsuarioSistema());
        autorizacao.setIp("127.0.0.0");
        autorizacaoRepository.incluirAutorizacao(autorizacao);
    }
}
