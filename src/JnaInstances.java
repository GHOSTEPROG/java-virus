import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

public class JnaInstances {
  public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer data);

    int GetWindowTextW(WinDef.HWND hWnd, char[] lpString, int nMaxCount);

    boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);

  }
}