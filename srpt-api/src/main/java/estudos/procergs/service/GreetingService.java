package estudos.procergs.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class GreetingService {

    @ConfigProperty(name = "mensagem.saudacoes")
    private String mensagemSaudacoes;

    public String hello() {
        return mensagemSaudacoes;
    }
}
