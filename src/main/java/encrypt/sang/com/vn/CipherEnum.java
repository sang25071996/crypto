package encrypt.sang.com.vn;

public enum CipherEnum {

  ENCRYPT_MODE(1), DECRYPT_MODE(2);

  private int value;

  CipherEnum(int value) {
    this.value = value;
  }

  public static CipherEnum getCipherEnum(int value) {
    for (CipherEnum cipher : CipherEnum.values()) {
      if (cipher.value == value) {
        return cipher;
      }
    }
    return null;
  }

  public int getValue() {
    return value;
  }


}
