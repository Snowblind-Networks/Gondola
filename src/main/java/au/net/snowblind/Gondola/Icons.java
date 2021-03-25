package au.net.snowblind.Gondola;

import java.io.File;
import java.util.ArrayList;

public class Icons {
	public static ArrayList<File> icons;
	
	public static void loadIcons() {
		File iconFolder = new File(Gondola.plugin.getDataFolder(), "/gondolas");
		if (!iconFolder.exists())
			iconFolder.mkdirs();
		icons = listFilesForFolder(iconFolder);
	}

	public static ArrayList<File> listFilesForFolder(File folder) {
		ArrayList<File> list = new ArrayList<>();
		File[] files = folder.listFiles();
		
		for (byte b = 0; b < files.length; b++) {
			File file = files[b];
			if (!file.isDirectory() && 
					file.getName().substring(file.getName().length() - 3).equalsIgnoreCase("PNG"))
				list.add(file); 
		} 
		return list;
	}
}
