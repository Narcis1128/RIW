package parser;

import java.util.HashSet;
import java.util.List;
import other.WordsTemplate;
import other.DirectIndexJsonTemplate;
import mongo.MongoDB;
public class Head {

	public static void main(String[] args) {
		List<DirectIndexJsonTemplate> ListOfDirectTemplates = DirectIndex.directIndex("Test");
		MongoDB base = new MongoDB();
		base.populateCollectionDirect(ListOfDirectTemplates);
		List<WordsTemplate> indirectIndexTemplate= IndirectIndex.indirectIndex();
		base.populateCollectionIndirect(indirectIndexTemplate);
		CautareBooleana ca = new CautareBooleana();
		HashSet<String> a = ca.booleanSearch(ca.readQueryFromUser());
		System.out.print(a);
	}
}
