package estudos.procergs.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextoUtil {
  
  public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
  public static final Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("^\\+?[0-9. ()-]{7,}$");
  
  public static String removerAcentos(String texto) {
    String txt = texto.replaceAll("Á", "&Aacute;");
    txt = txt.replaceAll("á", "a");
    txt = txt.replaceAll("É", "E");
    txt = txt.replaceAll("é", "e");
    txt = txt.replaceAll("Í", "I");
    txt = txt.replaceAll("í", "i");
    txt = txt.replaceAll("Ó", "O");
    txt = txt.replaceAll("ó", "o");
    txt = txt.replaceAll("Ú", "U");
    txt = txt.replaceAll("ú", "u");
    txt = txt.replaceAll("Ç", "C");
    txt = txt.replaceAll("ç", "c");
    txt = txt.replaceAll("Â", "A");
    txt = txt.replaceAll("â", "a");
    txt = txt.replaceAll("Ê", "E");
    txt = txt.replaceAll("ê", "e");
    txt = txt.replaceAll("Ô", "O");
    txt = txt.replaceAll("ô", "o");
    txt = txt.replaceAll("À", "A");
    txt = txt.replaceAll("à", "a");
    txt = txt.replaceAll("Ü", "U");
    txt = txt.replaceAll("ü", "u");
    txt = txt.replaceAll("Ã", "A");
    txt = txt.replaceAll("ã", "a");
    txt = txt.replaceAll("Õ", "O");
    txt = txt.replaceAll("õ", "o");
    txt = txt.replaceAll("Ñ", "N");
    txt = txt.replaceAll("ñ", "n");    
    return txt;
  }
  
  public static String formatar(BigDecimal valor) {
    return Objects.isNull(valor) ? "" : "R$ " + new DecimalFormat("#,##0.00").format(valor);
  }

  public static Boolean validarEmail(String email) {    
    if (Objects.isNull(email) || email.isEmpty()) {
      return true;
    }
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
    return matcher.matches();
  }

  public static Boolean validarTelefone(String telefone) {  
    if (Objects.isNull(telefone) || telefone.isEmpty()) {
      return true;
    }
    Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(telefone);
    return matcher.matches();
  }
}
