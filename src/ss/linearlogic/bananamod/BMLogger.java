package ss.linearlogic.bananamod;

import java.util.logging.Logger;

public class BMLogger
{
  private static final Logger log = Logger.getLogger("Minecraft");
  
  
  public static void logInfo(String message)
  {
    log.info("[BananaMod] " + message);
  }
  
  public static void logWarning(String message) {
    log.warning("[BananaMod] " + message);
  }

  public static void logSevere(String message) {
    log.severe("[BananaMod] " + message);
  }
}