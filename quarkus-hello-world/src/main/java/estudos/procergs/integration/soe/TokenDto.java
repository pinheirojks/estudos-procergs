package estudos.procergs.integration.soe;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class TokenDto implements Serializable {

  private static final long serialVersionUID = 487327463248L;

  private JsonObject json;
  private LocalDateTime dthExpiracao;

  public TokenDto(JsonObject json) {
    this.json = json;
    this.dthExpiracao = LocalDateTime.now().plusSeconds(json.getInt("expires_in"));
  }

  public boolean isValid() {
    long secondsToExpire = ChronoUnit.SECONDS.between(LocalDateTime.now(), dthExpiracao);
    return secondsToExpire >= 30;
  }

  public JsonObject toJson() {
    long secondsToExpire = ChronoUnit.SECONDS.between(LocalDateTime.now(), dthExpiracao);
    return Json.createObjectBuilder()
        .add("access_token", json.getString("access_token"))
        .add("token_type", json.getString("token_type"))
        .add("expires_in", secondsToExpire)
        .build();
  }
}
