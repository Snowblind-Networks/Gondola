package au.net.snowblind.gondola;

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
	
	/**
	 * Gets all png files in a folder.
	 * @param folder The folder to search
	 * @return a list of all files with the PNG extension in the folder.
	 */
	public static ArrayList<File> listFilesForFolder(File folder) {
		ArrayList<File> list = new ArrayList<>();
		File[] files = folder.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (!file.isDirectory() && 
					file.getName().substring(file.getName().length() - 3).equalsIgnoreCase("PNG"))
				list.add(file); 
		} 
		return list;
	}
}
