package estudos.procergs.integration.soe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import javax.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procergs.soeauth.client.SOEAuthClient;

import estudos.procergs.util.TextoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UsuarioSoeService {

  @ConfigProperty(name = "url.ws.soe")
  private String urlSoe;

  @ConfigProperty(name = "ws.soeauth.client.id")
  private String propWsSoeAuthId;

  @ConfigProperty(name = "ws.soeauth.client.secret")
  private String propWsSoeAuthSenha;

  //@Inject
  private SOEAuthClient soeAuthClient = new SOEAuthClient(); // TODO: Esta parte não deve funcionar

  private final static ObjectMapper objectMapper = new ObjectMapper();

  public List<UsuarioSoe> listar(String siglaOrgao, Long matricula, String nome) {
    
    StringBuilder urlConexao = new StringBuilder(urlSoe.concat("/usuarios"));

    if (StringUtils.isBlank(siglaOrgao)) {
      throw new WebApplicationException("Infome o órgão.");
    }

    urlConexao.append("?siglaOrganizacao=").append(siglaOrgao);
    if (Objects.nonNull(matricula)) {
      urlConexao.append("&matricula=").append(matricula);
    }
    if (StringUtils.isNotBlank(nome)) {
      nome = TextoUtil.removerAcentos(nome); //Necessário remover acentos, pois causam erro no servico. Além disso, a pesquisa sem acentos encontra palavras com acentos
      urlConexao.append("&nomeUsuario=").append("*").append(nome).append("*");
    }
    
    try {
      URL url = new URL(urlConexao.toString());
      HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
      conexao.setRequestMethod("GET");
      JsonObject jsonObject = soeAuthClient.clientCredentials(propWsSoeAuthId, propWsSoeAuthSenha);
      conexao.setRequestProperty("Authorization", String.format("Bearer %s", jsonObject.getString("access_token")));
      
      BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
      String json = br.readLine();
      
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      List<UsuarioSoe> usuarios = objectMapper.readValue(json, new TypeReference<List<UsuarioSoe>>(){}); 
      return usuarios;
      
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public UsuarioSoe consultar(Long codigoUsuario) {
    StringBuilder urlConexao = new StringBuilder(urlSoe.concat("/usuarios"));
    urlConexao.append("/").append(codigoUsuario);
    urlConexao.append("?tipo=4");
    
    try {
      URL url = new URL(urlConexao.toString());
      HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
      conexao.setRequestMethod("GET");
      JsonObject jsonObject = soeAuthClient.clientCredentials(propWsSoeAuthId, propWsSoeAuthSenha);
      conexao.setRequestProperty("Authorization", String.format("Bearer %s", jsonObject.getString("access_token")));
      
      BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
      String json = br.readLine();
      
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      UsuarioSoe usuario = objectMapper.readValue(json, new TypeReference<UsuarioSoe>(){}); 
      return usuario;
      
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
