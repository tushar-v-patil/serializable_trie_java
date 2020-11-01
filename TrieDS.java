package trieLibrary;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TrieDS {

	public static void main(String[] args) throws FileNotFoundException {
		
		FileInputStream in = new FileInputStream("TEST_DATA.txt");
		FileOutputStream out = new FileOutputStream("SERIAL_DATA.txt");
		PrintWriter pw = new PrintWriter(out);
		Scanner sc = new Scanner(in);
		
		Trie trie = new Trie();
		while(sc.hasNext()) {
			trie.insert(sc.nextLine());
		}
		
		String serializedString = trie.trieToString();
		pw.print(serializedString);
		
	}
	
	
	static class Trie{
		
		static TrieNode root = new TrieNode();;
		static int totalWords;
		
		
		//helper function that traverse the trie and finds all word present in trie.
		private void treeDepthTraversal(TrieNode currentNode, List<String> trieToString,
				StringBuilder prefix) 
		{
			if(currentNode.isTerminating) {
				trieToString.add(prefix.toString());
			}
			Map<Character,TrieNode> map = currentNode.children;
			
			for(Map.Entry<Character, TrieNode> e : map.entrySet()) {
				prefix.append(e.getKey());
				treeDepthTraversal(e.getValue(),trieToString,prefix);
				prefix.replace(prefix.length()-1, prefix.length(), "");
			}
		}
		
		
		//helper function
		private void buildSerialTrie(TrieNode root, StringBuilder serialTrie) {
			if(root.isTerminating) {
				serialTrie.append(']');
			}
			Map<Character,TrieNode> children = root.children;
			for(Map.Entry<Character, TrieNode> child : children.entrySet()) {
				serialTrie.append(child.getKey());
				buildSerialTrie(child.getValue(), serialTrie);
			}
			serialTrie.append('>');
		}
		
		
		//helper function
		private int stringToTrie(TrieNode node, StringBuilder serialTrie, int read) {
			int wordSeen = 0;
			if(serialTrie.charAt(read) == ']') {
				node.isTerminating = true;
				wordSeen++;
				read++;
			}
			else {
				node.isTerminating = false;
			}
			
			Map<Character,TrieNode> childMap = node.children;
			while(serialTrie.charAt(read) != '>') {
				char ch = serialTrie.charAt(read++);
				childMap.put(ch, new TrieNode());
				wordSeen += stringToTrie(childMap.get(ch), serialTrie, read);
			}
			read++;
			node.wordsWithPrefix = wordSeen;
			return wordSeen;
		}
		
		
		//used to construct an empty trie.
		public Trie() {
			root = new TrieNode();
			totalWords = 0;
		}
		
		
		//gives the total number of words currently present in the trie.
		public static int numberOfWordsInTrie()
		{
			return totalWords;
		}
		
		
		//used to insert a word in the trie.
		public static void insert(String word) {
			
			TrieNode currentNode = root;
			for(int i = 0; i < word.length(); i++) {
				currentNode.wordsWithPrefix++;
				Map<Character,TrieNode> childMap = currentNode.children;
				boolean it = childMap.containsKey(word.charAt(i));
				if(!it) {
					currentNode = new TrieNode();
					childMap.put(word.charAt(i), currentNode);
				}
				else {
					currentNode = childMap.get(word.charAt(i));
				}
			}
			currentNode.wordsWithPrefix++;
			currentNode.isTerminating = true;
			totalWords++;
		}
		
		
		//check to see if a word is present in the trie.
		public static boolean findWord(String word) {
			int countOfWord = wordsWithThePrefix(word, true);
			if(countOfWord > 0) {
				return true;
			}
			return false;
		}
		
		
		// This Operation deletes the given word from the trie.
		public void delete(String s) {
			TrieNode current = root;
			for(int i = 0; i < s.length(); i++) {
				if(current == null) {
					throw new RuntimeException("String doesn't exist!");
				}
				current = current.children.get(s.charAt(i));
			}
			if(current.isTerminating == false) {
				throw new RuntimeException("String doesn't exist!");
			}
			else {
				current.isTerminating = false;
			}
		}
		
		//Used to convert the trie to a list of words(serialization).
		public List<String> trieToList(){
			List<String> trieToList = new ArrayList<>();
			treeDepthTraversal(root,trieToList,new StringBuilder());
			return trieToList;
		}
		
		
		//Number of words in the trie the have a given prefix.
		// calledBy will be true if we want to check whether word is available or not
		// otherwise it is false to check number words with prefix
		public static int wordsWithThePrefix(String prefix, boolean calledBy) {
			
			if(root == null) return 0;
			
			TrieNode currentNode = root;
			
			for(int i = 0; i < prefix.length(); i++) {
				
				Map<Character,TrieNode> childMap = currentNode.children;
				boolean it = childMap.containsKey(prefix.charAt(i));
				if(!it) {
					return 0;
				}
				else {
					currentNode = childMap.get(prefix.charAt(i));
				}
			}
			
			if(!calledBy || currentNode.isTerminating) {
				return currentNode.wordsWithPrefix;
			}
			
			return 0;
		}
		
		public String trieToString() {
			StringBuilder serializedTrie = new StringBuilder();
			buildSerialTrie(root, serializedTrie);
			return serializedTrie.toString();
		}
		
	}
	
	
	//This structure is used to represent a node of the trie.
	static class TrieNode{
		
		//indicates if the word ending here is a valid word.
		boolean isTerminating;
		
		//number of words have the prefix that is represented by this node.
	    int wordsWithPrefix;
	    
		//represent all child nodes of this node.
		Map<Character,TrieNode> children;
		
		public TrieNode(){
			isTerminating = false;
			wordsWithPrefix = 0;
			children = new HashMap<>();
		}
		
	}
}