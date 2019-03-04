package org.laeq.settings;

import java.io.File;

public class Settings {
    public static String defaultPath = String.format("%s%s%s%s",System.getProperty("user.home"), File.separator,"vifeco", File.separator);
    public static String dbPath = String.format("%s%s%s", defaultPath, File.separator, "db");
    public static String exportPath = String.format("%s%s%s",defaultPath,File.separator, "export");
}