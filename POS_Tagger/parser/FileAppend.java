package code.summaries.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileAppend {
	public static List<File> listFilesForFolder(File folder) {

		List<File> fullList = new ArrayList<File>();

		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				for (File nf : fileEntry.listFiles()) {
					fullList.add(nf);

				}
			}
		}
		return fullList;
	}
}
