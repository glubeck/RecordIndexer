package spell;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import spell.ISpellCorrector.NoSimilarWordFoundException;

public class SpellTest {

	Trie trie;
	ISpellCorrector sc;
	
	@Before
	public void setup() {
		trie = new Trie();
		sc = new SpellCorrector();
	}
	
	@Test
	public void testAdd() {
		
		trie.add("apple");
		
		assertTrue(trie.find("apple") != null);
	}
	
	@Test 
	public void testAddWithSpace() {
		
		trie.add("Utah State");
		
		assertTrue(trie.find("Utah State") != null);
	}
	
	@Test 
	public void testAddWithHyphen() {
		
		trie.add("Utah-State");
		
		assertTrue(trie.find("Utah-State") != null);
	}
	
	@Test 
	public void useDictionary() throws IOException, NoSimilarWordFoundException {
		
		sc.useDictionary("words.txt");
		
		String similarWord = sc.suggestSimilarWord("apple");
		assertTrue(similarWord.equals("apple"));
		
		similarWord = sc.suggestSimilarWord(" ");
		assertTrue(similarWord.equals(" "));
		
		similarWord = sc.suggestSimilarWord("zszsqqrz");
		assertTrue(similarWord.equals(""));
		
		similarWord = sc.suggestSimilarWord("WHAT");
		assertTrue(similarWord.equals("what"));
		
		similarWord = sc.suggestSimilarWord("appl");
		assertTrue(similarWord.equals("apply"));
	}
	
	
}
