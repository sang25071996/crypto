package encrypt.sang.com.vn.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import encrypt.sang.com.vn.CipherEnum;
import encrypt.sang.com.vn.CipherFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChannelStreamTest {

  public static Map<String, Object> properties;
  public String content = "To provide all citizens under general suspicion, secretly tap off their data, thereby violating national law and international agreements and to cover this defect rather than to clarify, is a crime of state security forces against their own people and foreigners that will not end sooner, than by the consistent use of encryption technology no longer acceptable monitoring results are achieved, tax dollars for improper supervision are withdrawn and all perpetrators were held personally accountable.";
  public static byte[] encrypted = new byte[1024];
  public static File persistentFile;

  @BeforeAll
  public static void init() throws IOException {
    persistentFile = File.createTempFile("FileSystemEncrypted",".test");
  }

  @Test
  @Order(1)
  void encrypt()
      throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
      InvalidAlgorithmParameterException, IOException {

    Cipher cipher = CipherFactory.getCipher(CipherEnum.ENCRYPT_MODE);
    ByteArrayOutputStream byArrayOutputStream = new ByteArrayOutputStream();
    CipherOutputStream outputStream = new CipherOutputStream(byArrayOutputStream, cipher);
    outputStream.write(content.getBytes());
    outputStream.close();
    encrypted = byArrayOutputStream.toByteArray();
    assertNotEquals(content, byArrayOutputStream.toString());
  }

  @Test
  @Order(2)
  void decrypt()
      throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
      InvalidAlgorithmParameterException, IOException {

    Cipher cipher = CipherFactory.getCipher(CipherEnum.DECRYPT_MODE);
    InputStream targetStream = new ByteArrayInputStream(encrypted);
    CipherInputStream inputStream = new CipherInputStream(targetStream, cipher);
    inputStream.close();
    byte[] actual = IOUtils.toByteArray(inputStream);
    assertEquals(content, new String(actual));
  }

  @Test
  @Order(3)
  void encryptFile()
      throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

    FileUtils.writeByteArrayToFile(persistentFile, content.getBytes());
    FileInputStream fileInputStream = new FileInputStream(persistentFile);
    Cipher cipher = CipherFactory.getCipher(CipherEnum.DECRYPT_MODE);
    CipherInputStream inputStream = new CipherInputStream(fileInputStream, cipher);
    byte[] actual = IOUtils.toByteArray(inputStream);
    inputStream.close();
    OutputStream outputStream = new FileOutputStream(persistentFile);
    IOUtils.write(actual, outputStream);
    assertNotEquals(content, new String(actual));
  }

  @Test
  @Order(4)
  void decryptFile()
      throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {

    Cipher cipher = CipherFactory.getCipher(CipherEnum.DECRYPT_MODE);
    ByteArrayOutputStream byArrayOutputStream = new ByteArrayOutputStream();
    CipherOutputStream outputStream = new CipherOutputStream(byArrayOutputStream, cipher);
    byte[] decrypt = FileUtils.readFileToByteArray(persistentFile);
    outputStream.write(decrypt);
    outputStream.close();
    assertEquals(content, byArrayOutputStream.toString());
  }

  @AfterAll
  public static void finish() {
    persistentFile.deleteOnExit();
  }
}
