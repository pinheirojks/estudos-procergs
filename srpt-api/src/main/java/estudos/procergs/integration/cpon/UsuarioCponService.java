package estudos.procergs.integration.cpon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import estudos.procergs.infra.framework.AbstractService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioCponService extends AbstractService {

    @ConfigProperty(name = "url.ws.cpon")
    private String urlCpon;

    @ConfigProperty(name = "versao.cpon.robo")
    private String versaoCponRobo;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public UsuarioCpon autenticar(Long matricula, String senha, String ip) {
        super.exigir(matricula, "Informe a matrícula.");
        super.exigirString(senha, "Informe a senha.");
        super.exigirString(ip, "Informe o IP.");

        StringBuilder urlConexao = new StringBuilder(urlCpon.concat("/usuarios/login"));
        try {
            URL url = new URL(urlConexao.toString());
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("matricula", matricula.toString());
            conexao.setRequestProperty("senha", senha);
            conexao.setRequestProperty("ip", ip);
            conexao.setRequestProperty("versao", versaoCponRobo);

            BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
            String json = br.readLine();

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UsuarioCpon usuario = objectMapper.readValue(json, new TypeReference<UsuarioCpon>() {
            });
            return usuario;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
