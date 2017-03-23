package polarity.pos.neg.movie.review;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileAppendForPosNegPolarity {
	/**
	 * @param args
	 */

	// static Set<String> uniqueTerms = new HashSet<String>();
	// static List<String> AllWords = new ArrayList<String>();
	// static Map<String, Integer> posTermCount = new HashMap<String,
	// Integer>();
	static Map<String, Integer> negTermCount = new HashMap<String, Integer>();

	static Map<String, Integer> posTermCountMap = null;

	static Map<String, Integer> negTermCountMap = null;

	static Set<String> allUniqueWords = null;

	static int allPosWords = 0;

	static int allNegWords = 0;

	static List<String> AllPosWords = new ArrayList<String>();

	static List<String> AllNegWords = new ArrayList<String>();
	
	static Set<String> uniqueTerms = new HashSet<String>();

/*	public static void main(String[] args) {

		List<Map> PosNegMaps = getPosNegTermsProbMaps();

	}*/

	public static List<Map> getPosNegTermsProbMaps() {

		/*File folder = new File("C://txt files//data//train");
		List<List> fileList = listFilesForFolder(folder);*/
		List<Map> PosNegMaps = new ArrayList<Map>();

		// System.out.println(fileList.size());

		/*Change folder location to point to the training files.*/
		List<String> posFiles = ReadExcelFileForPosNegPolarity.readExcelSheetData("C://Movie Review//Train//Neg_Train.xlsx");
		List<String> negFiles = ReadExcelFileForPosNegPolarity.readExcelSheetData("C://Movie Review//Train//Pos_Train.xlsx");
		
		// System.out.println(posFiles.size());
		// System.out.println(negFiles.size());

		List<String> AllWords = new ArrayList<String>();

		posTermCountMap = getWordFreqCountForList(posFiles, AllWords);
		AllPosWords.addAll(AllWords);
		// System.out.println(AllWords.size());
		AllWords = new ArrayList<String>();

		negTermCountMap = getWordFreqCountForList(negFiles, AllWords);
		AllNegWords.addAll(AllWords);
		// System.out.println(AllWords.size());

		Map<String, Double> posTermCondProbMap = calculateCondProbablity(
				posTermCountMap, AllPosWords);
		
		Map<String, Double> negTermCondProbMap = calculateCondProbablity(
				negTermCountMap, AllNegWords);

		PosNegMaps.add(posTermCondProbMap);
		PosNegMaps.add(negTermCondProbMap);
		PosNegMaps.add(posTermCountMap);
		PosNegMaps.add(negTermCountMap);
		/*
		 * for (Map.Entry<String, Double> entry : posTermCondProbMap.entrySet())
		 * { System.out.println("PosKey = " + entry.getKey() + ", Value = " +
		 * entry.getValue()); }
		 * 
		 * for (Map.Entry<String, Double> entry : negTermCondProbMap.entrySet())
		 * { System.out.println("NegKey = " + entry.getKey() + ", Value = " +
		 * entry.getValue()); }
		 * 
		 * System.out.println("NegCount" + negTermCountMap.size());
		 * System.out.println("PosCount" + posTermCountMap.size());
		 * System.out.println("AllCount" + negTermCondProbMap.size());
		 * System.out.println("AllCount" + posTermCondProbMap.size());
		 */
System.out.println("Unique Terms **************"+uniqueTerms.size());
		return PosNegMaps;
	}

	public static Set<String> getAllUniqueWords(
			Map<String, Integer> posTermCountMap,
			Map<String, Integer> negTermCountMap) {

		allUniqueWords = new HashSet<String>();

		for (Map.Entry<String, Integer> entry : negTermCountMap.entrySet()) {
			allUniqueWords.add(entry.getKey());
		}

		for (Map.Entry<String, Integer> entry : posTermCountMap.entrySet()) {
			allUniqueWords.add(entry.getKey());
		}

		return allUniqueWords;
	}
	
	public static int getUniqueTermsSize(){
		
		return uniqueTerms.size();
	}

	public static int getAllPosWordsCount() {

		return AllPosWords.size();
	}

	public static int getAllNegWordsCount() {

		return AllNegWords.size();
	}

	public static Map<String, Double> calculateCondProbablity(
			Map<String, Integer> termCountMap, List<String> AllWords) {

		Map<String, Double> termCondProbMap = new HashMap<String, Double>();

		for (Map.Entry<String, Integer> entry : termCountMap.entrySet()) {
			Double condProb = ((entry.getValue().doubleValue() + 1) / (AllWords
					.size() + uniqueTerms.size()));

			termCondProbMap.put(entry.getKey(), condProb);
		}
		return termCondProbMap;
	}

	public static Map<String, Integer> getWordFreqCount(List<File> Files,
			List<String> AllWords) {

	
		Map<String, Integer> termCountMap = new HashMap<String, Integer>();

		for (File file : Files) {
			String str = readFile(file);
			String[] strWords = str.split(("[ \'\".,?&!:;()$%\\-_`=/@^+]+").toLowerCase());

			for (String st : strWords) {
				AllWords.add(st);
				uniqueTerms.add(st);
					if (termCountMap.containsKey(st)) {
						int count = termCountMap.get(st);
						count += 1;
						termCountMap.put(st, count);
					}
				else {
					termCountMap.put(st, 1);
				}

			}
		}
		return termCountMap;
	}

	public static Map<String, Integer> getWordFreqCountForList(List<String> description,
			List<String> AllWords) {

	
		Map<String, Integer> termCountMap = new HashMap<String, Integer>();

		for (String sentence : description) {
			String[] strWords = sentence.split(("[ \'\".,?&!:;()$%\\-_`=/@^+]+").toLowerCase());

			for (String st : strWords) {
				AllWords.add(st);
				uniqueTerms.add(st);
					if (termCountMap.containsKey(st)) {
						int count = termCountMap.get(st);
						count += 1;
						termCountMap.put(st, count);
					}
				else {
					termCountMap.put(st, 1);
				}

			}
		}
		return termCountMap;
	}

	
	public static String readAndAppendFiles(List<File> filList) {
		StringBuffer sb = new StringBuffer("");
		for (File file : filList) {
			BufferedReader br = null;

			try {

				String sCurrentLine;

				br = new BufferedReader(new FileReader(file));

				while ((sCurrentLine = br.readLine()) != null) {
					sb.append(sCurrentLine);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static String readFile(File file) {
		StringBuffer sb = new StringBuffer("");

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static List<List> listFilesForFolder(File folder) {

		List<File> filePosList = new ArrayList<File>();
		List<File> fileNegList = new ArrayList<File>();
		List<List> fullList = new ArrayList<List>();

		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				if (fileEntry.getName().equals("neg")) {
					for (File nf : fileEntry.listFiles())
						fileNegList.add(nf);
				} else if (fileEntry.getName().equals("pos")) {
					for (File pf : fileEntry.listFiles())
						filePosList.add(pf);
				}
			}
		}
		fullList.add(filePosList);
		fullList.add(fileNegList);
		return fullList;
	}
}
