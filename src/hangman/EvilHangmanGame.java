package hangman;

import java.io.*;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

	private Set<String> dictionary;
	private StringBuilder word;
	private Set<Character> guessesMade;
	
	public EvilHangmanGame(){}
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		try{
			this.dictionary = new TreeSet<String>();
			buildDictionary(dictionary, wordLength);
			StringBuilder wordSb = new StringBuilder();
			for(int x=0; x<wordLength; x++)
				wordSb.append('-');
			setPattern(wordSb.toString());
			guessesMade = new TreeSet<Character>();
			
		}
		catch(IOException e){
			
		}
		
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		
		//if guess has already been made, throw exception
		if(guessesMade.contains(guess))
				throw new GuessAlreadyMadeException();
		guessesMade.add(guess);
		
		Map<String, Set<String>> subsets = new TreeMap<String, Set<String>>();
		
		//add words to respective subsets (creating new subsets when necessary)
		Iterator<String> it = dictionary.iterator();
		while(it.hasNext()){
			String currentWord = it.next();
			StringBuilder pattern = new StringBuilder();
			
			for(int x=0; x<currentWord.length(); x++){
				if(currentWord.charAt(x)==guess)
					pattern.append(guess);
				else if(word.charAt(x)!='-')
					pattern.append(word.charAt(x));
				else
					pattern.append('-');
			}
		
			if(subsets.get(pattern.toString())!=null){
				subsets.get(pattern.toString()).add(currentWord);
			}
			else{
				Set<String> s = new TreeSet<String>();
				s.add(currentWord);
				subsets.put(pattern.toString(), s);
			}
		}
		
		//iterate through subsets to find the subset with greatest number of words
		int largestSize = 0;
		String largestKey = "";

		for(Map.Entry<String, Set<String>> entry: subsets.entrySet()) {
		
			if(entry.getValue().size()>largestSize){
				largestSize = entry.getValue().size();
				largestKey = entry.getKey();
			}
			else if(entry.getValue().size()==largestSize){
				//choose with fewest letters
				String newKey = entry.getKey();
				int countLargest=0;
				int countNew=0;
				for(int x=0; x<newKey.length();x++){
					if(largestKey.charAt(x)==guess)
						countLargest++;
					if(newKey.charAt(x)==guess)
						countNew++;
				}
				if(countNew<countLargest)
					largestKey=newKey;
				else if(countNew==countLargest){
					//choose subset with the rightmost guessed letter
					boolean changed = false;
					for(int x=newKey.length()-1; x>0; x--){
						if(newKey.charAt(x)==guess){
							if(largestKey.charAt(x)!=guess){
								largestKey=newKey;
								changed = true;
							}
						}
						else if(largestKey.charAt(x)==guess){
								changed = true;
						}
						if(changed)
							break;
					}
				}
			}
		}
		
		setPattern(largestKey);
		dictionary = subsets.get(largestKey);
		return subsets.get(largestKey);
	}
	
	public void buildDictionary (File dictionary, int wordLength) throws IOException{
		try{
			Scanner s = new Scanner(dictionary);
			
			while(s.hasNext()){
				String next = s.next();
				if(next.length()==wordLength)
					this.dictionary.add(next);
			}
			s.close();
		}
		catch(IOException e){
			
		}
		
	}
	
	private void setPattern(String newPattern){
		if(word==null){
			word = new StringBuilder();
			word.append(newPattern);
		}
		else{
			for(int x=0; x<word.length(); x++){
				if(newPattern.charAt(x)!='-')
					word.setCharAt(x, newPattern.charAt(x));
			}
		}
	}
	
	public String getPattern(){
		return word.toString();
	}
	
}

