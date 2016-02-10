package hangman;
import java.io.*;
import java.util.*;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

public class Main {
	
	public static void main(String[] args) {
		
		EvilHangmanGame game = new EvilHangmanGame();
		File dictionaryFile = new File(args[0]);
		
		game.startGame(dictionaryFile, Integer.valueOf(args[1]));
		
		//input and make guesses
		int guessesLeft = Integer.valueOf(args[2]);
		Set<Character> guessesMade = new TreeSet<Character>();
		Set<String> guessResult = new TreeSet<String>();
		String word = "";
		
		Scanner in = new Scanner(System.in);
		while(guessesLeft>0){
			if(guessesLeft>1)
				System.out.println("You have " + guessesLeft + " guesses left.");
			else
				System.out.println("You have " + guessesLeft + " guess left.");
			System.out.print("Used letters: ");
			Iterator<Character> it = guessesMade.iterator();
			while(it.hasNext()){
				System.out.print(it.next() + " ");
			}
			System.out.println("\nWord:" + word);
			System.out.println("Enter guess:");
			while(!in.hasNext("[a-zA-Z]")){
				System.out.println("Invalid input.\nEnter guess:");
				in.next();
			}
			String nextGuess = in.next().toLowerCase();
			guessesMade.add(nextGuess.charAt(0));
			try {
				guessResult = game.makeGuess(nextGuess.charAt(0));
				
			
					word = game.getPattern();
					int count = 0;
					for(int x=0; x<word.length(); x++)
						if(word.charAt(x)==nextGuess.charAt(0))
							count++;
				if(count==0)
						System.out.println("Sorry there are no " + nextGuess + "'s");
				else if(count==1){
					System.out.println("Yes, there is 1 " + nextGuess);
					guessesLeft++;
					if(!(word.contains("-"))){
						System.out.println("You win!\nThe word was: " + word);
						guessesLeft=0;
					}
				}
				else{
					System.out.println("Yes, there are " + count + " " + nextGuess + "'s");
					guessesLeft++;
					if(!(word.contains("-"))){
						System.out.println("You win!\nThe word was: " + word);
						guessesLeft=0;
					}
				}
				
			} catch (GuessAlreadyMadeException e) {
				System.out.println("You already used that letter");
				guessesLeft++;
			}
			
			guessesLeft--;
		}
		if(word.contains("-")){
			Iterator<String> i = guessResult.iterator();
			System.out.println("You lose!\nThe word was: " + i.next());
		}
		in.close();
	}
	
}

