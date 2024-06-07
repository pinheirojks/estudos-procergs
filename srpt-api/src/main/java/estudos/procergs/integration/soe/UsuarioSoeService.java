package estudos.procergs.integration.soe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.procergs.util.autentica.Autentica;
import com.procergs.util.autentica.LogonED;
import com.procergs.util.autentica.SessionED;
import com.procergs.util.exception.ProcergsINTException;

import estudos.procergs.infra.excecao.NaoAutorizadoException;
import estudos.procergs.infra.framework.SOEAuthClient;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.infra.soe.Autentica;
import estudos.procergs.util.TextoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UsuarioSoeService {

  @ConfigProperty(name = "url.ws.soe")
  private String urlSoe;

  @ConfigProperty(name = "ws.soeauth.client.id")
  private String soeAuthClientId;

  @ConfigProperty(name = "ws.soeauth.client.secret")
  private String soeAuthSecret;

  @Inject
  private SOEAuthClient soeAuthClient;

  @Inject
  private AutorizacaoRepository autorizacaoRepository;

  private final static ObjectMapper objectMapper = new ObjectMapper();

  public List<UsuarioSoe> listar(String siglaOrgao, Long matricula, String nome) {
    
    StringBuilder urlConexao = new StringBuilder(urlSoe.concat("/usuarios"));

    if (StringUtils.isBlank(siglaOrgao)) {
      throw new WebApplicationException("Infome o órgão.");
    }
    siglaOrgao = siglaOrgao.toUpperCase();
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
      JsonObject jsonObject = soeAuthClient.consulta(soeAuthClientId, soeAuthSecret);
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
      JsonObject jsonObject = soeAuthClient.consulta(soeAuthClientId, soeAuthSecret);
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

  public SessionED autenticar(String matricula, String senha) {
    try {       
      LogonED logonED = new LogonED();
      logonED.setTipoAutenticacao(LogonED.AUT_POR_MATRICULA);
      logonED.setOrganizacao("PROCERGS");
      logonED.setMatricula(Long.valueOf(matricula).longValue());
      logonED.setSenha(senha);
      
      SessionED session = new Autentica().logon(logonED);
      
      return session;
        
    } catch (ProcergsINTException e) {
      throw new NaoAutorizadoException(e.getMensagem());
      
    } catch (Exception e) {
      throw new NaoAutorizadoException(e.getMessage());
    }
  }

  public void verificarLogin(String chave) {
    if (StringUtils.isBlank(chave)) {
            throw new NaoAutorizadoException("Usuário, senha e IP não informados.");
        }
        chave = chave.substring(7); // Remove a string "Bearier " da chave
        String[] loginSenhaIp = chave.split(":");
        String login = loginSenhaIp[0];
        String senha = loginSenhaIp[1];
        SessionED session = this.autenticar(login, senha);
        if (session == null) {
            throw new NaoAutorizadoException("Usuário e senha não encontrados.");
        }  
        if (loginSenhaIp.length < 3) {
            throw new NaoAutorizadoException("IP não informado.");
        }
        String ip = loginSenhaIp[2];
        autorizacaoRepository.incluirAutorizacao(session, ip);
  }
}
