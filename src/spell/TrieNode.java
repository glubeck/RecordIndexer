package spell;

public class TrieNode implements ITrie.INode {

	private TrieNode[] links;
	private int frequencyCount;
	
	public TrieNode() {
		
		links = new TrieNode[26];
		frequencyCount = 0;
	}
	
	public TrieNode[] getLinks() {
		return links;
	}

	public void setLinks(TrieNode[] links) {
		this.links = links;
	}

	public int getFrequencyCount() {
		return frequencyCount;
	}

	public void setFrequencyCount(int frequencyCount) {
		this.frequencyCount = frequencyCount;
	}
	
	public void incrementFC() {
		frequencyCount++;
	}

	@Override
	public int getValue() {
		return frequencyCount;
	}

}
