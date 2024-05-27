package estudos.procergs.infra.framework;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.oauth2.sdk.ClientCredentialsGrant;
import com.nimbusds.oauth2.sdk.GeneralException;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import estudos.procergs.integration.soe.TokenDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@ApplicationScoped
public class SOEAuthClient {

  private static final Integer  DEFAULT_CONNECT_TIMEOUT = 1000;

  private static final Integer  DEFAULT_READ_TIMEOUT    = 5000;

  @ConfigProperty(name = "procergs.soeauth.issuer")
  private String issuerString;

  private Logger                log                     = LoggerFactory.getLogger(this.getClass());

  private Map<String, TokenDto> cache                   = new ConcurrentHashMap<>();

  private HTTPRequest           httpRequest;

  public JsonObject consulta(String id, String secret) {
    TokenDto cachedToken = cache.get(id);
    if (Objects.nonNull(cachedToken) && cachedToken.isValid()) {
      log.debug("Usando accessToken do cache para o clientId {}", id);
      return cachedToken.toJson();
    }
    return processaRetry(id, secret);
  }

  private JsonObject processaRetry(String id, String secret) {
    try {
      JsonObject json = atualizaToken(id, secret);
      cacheToken(id, json);
      return json;
    } catch (ParseException | IOException e) {
      log.debug(e.getMessage(), e);
    }
    return Json.createObjectBuilder().build();
  }

  private void cacheToken(String id, JsonObject json) {
    TokenDto token = new TokenDto(json);
    cache.put(id, token);
  }

  private JsonObject atualizaToken(String id, String secret) throws ParseException, IOException {
    TokenResponse tokenResponse = getTokenResponse(id, secret);
    verificaResponse(tokenResponse);
    return buildToken(tokenResponse);
  }

  private void verificaResponse(TokenResponse tokenResponse) {
    if (!tokenResponse.indicatesSuccess()) {
      TokenErrorResponse errorResponse = tokenResponse.toErrorResponse();
      log.debug(errorResponse.toJSONObject().toJSONString());
    }
  }

  private TokenResponse getTokenResponse(String id, String secret) throws ParseException, IOException {
    TokenRequest tokenRequest = buildTokenRequest(id, secret);
    HTTPRequest httpRequest = buildHttpRequest(tokenRequest);
    return OIDCTokenResponseParser.parse(processaRequest(httpRequest));
  }

  private TokenRequest buildTokenRequest(String id, String secret) {
    try {
      Issuer issuer = new Issuer(issuerString);
      OIDCProviderMetadata metadata = OIDCProviderMetadata.resolve(issuer, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);

      ClientAuthentication clientAuth = new ClientSecretPost(new ClientID(id), new Secret(secret));
      return new TokenRequest(metadata.getTokenEndpointURI(), clientAuth, new ClientCredentialsGrant());
    } catch (GeneralException | IOException e) {
      log.debug(e.getMessage(), e);
    }
    return null;
  }

  private HTTPRequest buildHttpRequest(TokenRequest tokenRequest) {
    httpRequest = tokenRequest.toHTTPRequest();
    httpRequest.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
    httpRequest.setReadTimeout(DEFAULT_READ_TIMEOUT);
    return httpRequest;
  }

  private HTTPResponse processaRequest(HTTPRequest httpRequest) throws IOException {
    return httpRequest.send();
  }

  private JsonObject buildToken(TokenResponse tokenResponse) {
    OIDCTokenResponse successResponse = (OIDCTokenResponse) tokenResponse.toSuccessResponse();
    AccessToken accessToken = successResponse.getOIDCTokens().getAccessToken();
    Long expiresIn = successResponse.getOIDCTokens().getAccessToken().getLifetime();
    return Json.createObjectBuilder()
        .add("access_token", accessToken.getValue())
        .add("token_type", accessToken.getType().getValue())
        .add("expires_in", expiresIn)
        .build();
  }
}
