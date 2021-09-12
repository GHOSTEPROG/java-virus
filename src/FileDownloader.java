import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {

  interface OnCompleteListener {
    void onCompleteDownloadListener(String filePath) throws InvocationTargetException, IllegalAccessException;
  }

  public static void download(final String downloadPath, final String filepath, OnCompleteListener onCompleteListener) {
    Thread thread = new Thread(() -> {
      try {
        URL url = new URL(downloadPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();

        File file = new File(filepath);
        if (file.exists()) {
          file.delete();
        }

        FileOutputStream outputStream = new FileOutputStream(filepath);

        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
          outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        onCompleteListener.onCompleteDownloadListener(filepath);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    });

    thread.start();
  }
}
