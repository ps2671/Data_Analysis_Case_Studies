package polarity.pos.neg.dbpedia;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestNaiveClassifierForDbpedia {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		/*
		 * List<Map> PosNegMaps = FileAppend.getPosNegTermsProbMaps();
		 * 
		 * Map<String, Double> posTermCondProbMap = PosNegMaps.get(0);
		 * Map<String, Double> negTermCondProbMap = PosNegMaps.get(1);
		 * 
		 * Map<String, Integer> posTermCountMap = PosNegMaps.get(2); Map<String,
		 * Integer> negTermCountMap = PosNegMaps.get(3);
		 * 
		 * Set<String> allUniqueWords = FileAppend.getAllUniqueWords(
		 * posTermCountMap, negTermCountMap);
		 * 
		 * int allPosWordCount = FileAppend.getAllPosWordsCount(); int
		 * allNegWordCount = FileAppend.getAllNegWordsCount();
		 * 
		 * System.out.println(posTermCondProbMap.size());
		 * System.out.println(negTermCondProbMap.size());
		 * System.out.println(posTermCountMap.size());
		 * System.out.println(negTermCountMap.size());
		 * System.out.println(allUniqueWords.size());
		 * System.out.println(allPosWordCount);
		 * System.out.println(allNegWordCount);
		 */

		List<String> sentenceClassificationList = testClassifier();

		for (String sentence : sentenceClassificationList) {
			System.out.println(sentence);
		}
/*		
		List<Map> PosNegMaps = FileAppendForDbpedia.getPosNegTermsProbMaps();
		 
		  Map<String, Double> posTermCondProbMap = PosNegMaps.get(0);
		  Map<String, Double> negTermCondProbMap = PosNegMaps.get(1);*/
		  
		  /*System.out.println(posTermCondProbMap.get("edward"));
		  System.out.println(negTermCondProbMap.get("edward"));
		  System.out.println(posTermCondProbMap.get("norton"));
		  System.out.println(negTermCondProbMap.get("norton"));*/
	}

	// (count in training map )/(pos neg map size); for every word.

	private static double getPosPosteriorProbablity(
			Map<String, Double> posTermCondProbMap, String str, int uniWrdSize,
			int allPosWordCount,int uniqueTermsSize) {

		double posProb = 0;
		if (posTermCondProbMap.containsKey(str)) {
			
			
			posProb = Math.log(posTermCondProbMap.get(str));
		//	System.out.println(str +", "+posTermCondProbMap.get(str));
			

			//System.out.println(str +","+uniWrdSize+","+allPosWordCount+","+posTermCondProbMap.get(str)+","+posProb);
			
		} else {
			posProb = Math.log(1.0 / (allPosWordCount + uniqueTermsSize));
			//System.out.println(str +","+uniWrdSize+","+allPosWordCount+","+posProb);
		}
	//	double dposProb = posProb/Math.log(uniWrdSize);
		return posProb;

	}

	private static double getNegPosteriorProbablity(
			Map<String, Double> negTermCondProbMap, String str, int uniWrdSize,
			int allNegWordCount,int uniqueTermsSize) {

		double negProb = 0;
		
		if (negTermCondProbMap.containsKey(str)) {
			
			
			negProb = Math.log(negTermCondProbMap.get(str));
			//System.out.println(str +", "+negTermCondProbMap.get(str));

			//System.out.println(str +","+uniWrdSize+","+allNegWordCount+","+negTermCondProbMap.get(str)+","+negProb);
		} else {
			negProb = Math.log(1.0 /(allNegWordCount + uniqueTermsSize));
			//System.out.println(str +","+uniWrdSize+","+allNegWordCount+","+negProb);
		}
		
	//	double dnegProb = negProb/Math.log(uniWrdSize);
		return negProb;

	}

	private static String classifyPositiveNegative(double posProb,
			double negProb) {

		String str = "neg";
		if (posProb > negProb)
			str = "pos";
		return str;

	}

	private static List<String> testClassifier() {

		String fileName = "";
		List<String> sentenceClassificationList = new ArrayList<String>();

		List<Map> PosNegMaps = FileAppendForDbpedia.getPosNegTermsProbMaps();

		Map<String, Double> posTermCondProbMap = PosNegMaps.get(0);
		Map<String, Double> negTermCondProbMap = PosNegMaps.get(1);

		Map<String, Integer> posTermCountMap = PosNegMaps.get(2);
		Map<String, Integer> negTermCountMap = PosNegMaps.get(3);

		Set<String> allUniqueWords = FileAppendForDbpedia.getAllUniqueWords(
				posTermCountMap, negTermCountMap);

		int allPosWordCount = FileAppendForDbpedia.getAllPosWordsCount();
		int allNegWordCount = FileAppendForDbpedia.getAllNegWordsCount();
		
		int uniqueTermsSize = FileAppendForDbpedia.getUniqueTermsSize();

		/*Change folder location to point to the training files.*/
		List<String> posFiles = ReadExcelFile.readExcelSheetData("C://DBpedia Files//Test//dbpedia_test_csv_1.xlsx");
		List<String> negFiles = ReadExcelFile.readExcelSheetData("C://DBpedia Files//Test//dbpedia_test_csv_2.xlsx");

		
		List<File> allFiles=new ArrayList<File>();


		for (String sentence : posFiles) {

			double posProb = 0;
			double negProb = 0;
			
			String[] strWords = sentence.split(("[ \'\".,?&!:;()$%\\-_`=/@^+]+").toLowerCase());

			for (String st : strWords) {
				posProb += getPosPosteriorProbablity(posTermCondProbMap, st,
						allUniqueWords.size(), allPosWordCount,uniqueTermsSize);
				//System.out.println("here : "+posProb);
				/*System.out.println(st +","+allUniqueWords.size()+","+allPosWordCount);
				System.out.println(posProb);*/
				negProb += getNegPosteriorProbablity(negTermCondProbMap, st,
						allUniqueWords.size(), allNegWordCount,uniqueTermsSize);
				/*System.out.println(st +","+allUniqueWords.size()+","+allNegWordCount);
				System.out.println(negProb);*/

			}
			posProb += Math.log(0.5);
			negProb += Math.log(0.5);
			//System.out.println("::::"+posProb+"::::"+negProb);
			String classify = classifyPositiveNegative(posProb, negProb);
			sentenceClassificationList.add(classify);
		}

		return sentenceClassificationList;

	}
}
