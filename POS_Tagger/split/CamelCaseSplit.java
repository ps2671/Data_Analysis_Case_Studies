package code.summaries.xml.camel.split;
import java.util.LinkedList;

public class CamelCaseSplit {
	// accept a string, like aCamelString
	// return a list containing strings, in this case, [a, Camel, String]
	public static LinkedList<String> splitCamelCaseString(String s) {
		LinkedList<String> result = new LinkedList<String>();
		for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
			result.add(w);
		}
		return result;
	}
}
