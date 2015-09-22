package spell;

import java.util.ArrayList;

public class Trie implements ITrie {

	private TrieNode root;
	private int wordCount;
	private int nodeCount;
	
	public Trie() {
		
		root = new TrieNode();
		wordCount = 0;
		nodeCount = 1;
	}
	
	@Override
	public void add(String word) {
		
		TrieNode curNode = root;
		
		for(int i = 0; i < word.length(); i++) {
			
			TrieNode[] links = curNode.getLinks();
			char c = word.charAt(i);
			int placement = c - 'a';
			
			if(placement < 0) {
				placement = 25;
			}
			
			if(links[placement] == null) {
				links[placement] = new TrieNode();
				nodeCount++;
			}
			curNode = links[placement];
		}
		if(curNode.getFrequencyCount() == 0)
			wordCount++;
		curNode.incrementFC();
		
	}

	@Override
	public INode find(String word) {
		
		StringBuilder sb = new StringBuilder("");
		
		return findRecursively(word, sb, root, 0);
	}
	
	public TrieNode findRecursively(String word, StringBuilder sb, TrieNode curNode, int i) {
		
		if(sb.toString().equals(word) && curNode.getFrequencyCount() > 0) {
			return curNode;
		}
		
		if(!(i < word.length()))
			return null;
		
		TrieNode[] links = curNode.getLinks();
		char c = word.charAt(i);
		int placement = c - 'a';
		
		if(placement < 0) {
			placement = 25;
		}
		
		if(links[placement] == null)
			return null;
		
		if(links[placement] != null) {
			curNode = links[placement];
			i++;
			sb.append(c);
			return findRecursively(word, sb, curNode, i);
		}
		
		return null;	
	}

	public String toString() {
		
		StringBuilder sb = new StringBuilder("");
		StringBuilder temp = new StringBuilder("");
			
		return toStringRecursively(sb, temp, root).toString();
	}
	
	public StringBuilder toStringRecursively(StringBuilder sb, StringBuilder temp, TrieNode curNode) {
		
		if(curNode.getFrequencyCount() > 0) {
			sb.append(temp);
			sb.append("\n");
		}
		
		TrieNode[] links = curNode.getLinks();
		for(int j = 0; j < 26; j++) {
			
			if(links[j] != null) {
				
				temp.append((char)(j+'a'));
				sb = toStringRecursively(sb, temp, links[j]);
			}
			
		}
		
		if(temp.length() > 0) {
			temp.deleteCharAt(temp.length()-1);
		}
		
		return sb;
	}
	
	public String toStringForEquals() {
		
		StringBuilder sb = new StringBuilder("");
		StringBuilder temp = new StringBuilder("");
			
		return toStringRecursivelyForEquals(sb, temp, root).toString();
	}
	
	public StringBuilder toStringRecursivelyForEquals(StringBuilder sb, StringBuilder temp, TrieNode curNode) {
		
		if(curNode.getFrequencyCount() > 0) {
			for(int i = 0; i < curNode.getFrequencyCount(); i++) {
			sb.append(temp);
			sb.append("\n");
			}
		}
		
		TrieNode[] links = curNode.getLinks();
		for(int j = 0; j < 26; j++) {
			
			if(links[j] != null) {
				
				temp.append((char)(j+'a'));
				sb = toStringRecursivelyForEquals(sb, temp, links[j]);
			}		
		}		
		if(temp.length() > 0) {
			temp.deleteCharAt(temp.length()-1);
		}	
		return sb;
	}
	
	public ArrayList<String> deletion(String word) {
		
		StringBuilder sb = new StringBuilder("");
		sb.append(word);
		ArrayList<String> result = new ArrayList<>();
		
		for(int i = 0; i < word.length() + 1; i++) {
			
			for(int j = 0; j < 26; j++) {
				
				sb.insert(i, ((char)(j+'a')));
				result.add(sb.toString());
				sb.deleteCharAt(i);
			}
		}		
		return result;
	}
	
	public ArrayList<String> transposition(String word) {
		
		StringBuilder sb = new StringBuilder("");
		sb.append(word);
		ArrayList<String> result = new ArrayList<>();
		
		for(int i = 0; i < word.length()-1; i++) {
			
			char c = sb.charAt(i);
			sb.deleteCharAt(i);
			sb.insert(i+1, c);
			result.add(sb.toString());
			sb.deleteCharAt(i+1);
			sb.insert(i, c);
		}
		
		return result;
	}
	
	public ArrayList<String> alteration(String word) {
		
		StringBuilder sb = new StringBuilder("");
		sb.append(word);
		ArrayList<String> result = new ArrayList<>();
		
		for(int i = 0; i < word.length(); i++) {
			char c = sb.charAt(i);
			sb.deleteCharAt(i);
			for(int j = 0; j < 26; j++) {
				
				sb.insert(i, (char)(j+'a'));
				result.add(sb.toString());
				sb.deleteCharAt(i);
			}
			sb.insert(i, c);
		}
		
		return result;		
	}
	
	public ArrayList<String> insertion(String word) {
		
		StringBuilder sb = new StringBuilder("");
		sb.append(word);
		ArrayList<String> result = new ArrayList<>();
		
		for(int i = 0; i < word.length(); i++) {
			
			char c = sb.charAt(i);
			sb.deleteCharAt(i);
			result.add(sb.toString());
			sb.insert(i, c);
		}
		return result;
		
	}
	
	public ArrayList<String> transformation(String word) {
		
		ArrayList<String> deletions = deletion(word);
		ArrayList<String> transpositions = transposition(word);
		ArrayList<String> alterations = alteration(word);
		ArrayList<String> insertions = insertion(word);
		
		ArrayList<String> result = new ArrayList<>();
		
		result.addAll(deletions);
		result.addAll(transpositions);
		result.addAll(alterations);
		result.addAll(insertions);
		
		return result;
	}
	
	@Override
	public int hashCode() {
		return nodeCount + wordCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trie other = (Trie) obj;
		
		String first = toStringForEquals();
		String second = other.toStringForEquals();
		
		if(!first.equals(second))
			return false;
		
		return true;
	}

	@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodeCount;
	}

}
