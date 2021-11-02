package encrypt.sang.com.vn;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherFactory {

  private static final String SLASH = "/";
  private static final String MISSING_VARIABLE = "Missing variable: ";
  public static final String CIPHER_ALGORITHM = "CipherAlgorithm";
  public static final String CIPHER_ALGORITHM_MODE = "CipherAlgorithmMode";
  public static final String CIPHER_ALGORITHM_PADDING = "CipherAlgorithmPadding";
  public static final String SECRET_KEY = "SecretKey";
  public static final String IV_PARAMETER_SPEC = "1234567890123456";

  private CipherFactory() {

  }

  public static Map<String, Object> configureProperties() {
    Map<String, Object> properties = new HashMap<>();
    properties = new HashMap<>();
    properties.put(CipherFactory.CIPHER_ALGORITHM, "AES");
    properties.put(CipherFactory.CIPHER_ALGORITHM_MODE, "CTR");
    properties.put(CipherFactory.CIPHER_ALGORITHM_PADDING, "NoPadding");
    properties.put(CipherFactory.SECRET_KEY, "f31BmUS)&?O!19W:".getBytes());
    return properties;
  }

  public static Cipher getCipher(CipherEnum cipherEnum)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

    Map<String, Object> properties = configureProperties();
    String cipherAlgorithm = (String) properties.get(CIPHER_ALGORITHM);
    if (cipherAlgorithm == null) {
      throw new IllegalArgumentException(MISSING_VARIABLE + CIPHER_ALGORITHM);
    }

    String cipherAlgorithmMode = (String) properties.get(CIPHER_ALGORITHM_MODE);
    if (cipherAlgorithmMode == null) {
      throw new IllegalArgumentException(MISSING_VARIABLE + CIPHER_ALGORITHM_MODE);
    }

    String cipherAlgorithmPadding = (String) properties.get(CIPHER_ALGORITHM_PADDING);
    if (cipherAlgorithmPadding == null) {
      throw new IllegalArgumentException(MISSING_VARIABLE + CIPHER_ALGORITHM_PADDING);
    }

    String cipherTransformation =
        cipherAlgorithm + SLASH + cipherAlgorithmMode + SLASH + cipherAlgorithmPadding;

    byte[] secretKey = (byte[]) properties.get(SECRET_KEY);
    if (secretKey == null) {
      throw new IllegalArgumentException(MISSING_VARIABLE + SECRET_KEY);
    }

    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, cipherAlgorithm);

    Cipher cipher = Cipher.getInstance(cipherTransformation);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(IV_PARAMETER_SPEC.getBytes(StandardCharsets.UTF_8));

    // load the mode, symmetric key and IV into the Cipher
    cipher.init(cipherEnum.getValue(), secretKeySpec, ivParameterSpec);
    return cipher;
  }
}
