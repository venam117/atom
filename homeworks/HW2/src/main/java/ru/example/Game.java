package ru.example;

import java.io.*;
import java.util.Scanner;
import java.util.Random;

import org.slf4j.LoggerFactory;

public class Game {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Game.class);

    public static void main(String[] args) {
        log.info("Логгер живой");
        BullsAndCows();
    }

    static void BullsAndCows() {
        String word = GetWord();
        //System.out.println(word);
        char[] myword = word.toCharArray();
        int count = myword.length;
        System.out.println("Welcome to Bulls and Cows game!");
        System.out.println("I offered a " + count + "-letter word, your guess?");
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < 10; i++) {//10 попыток
            String mm = scan.nextLine();
            char[] inword = mm.toCharArray();
            if (word.equals(mm)) {     //Сравнение 2х строк
                System.out.println("You Won!!!");
                return;
            }
            int bulls = 0, cows = 0;
            count = Math.min(count, inword.length);//Берем длинну более короткого слова
            for (int j = 0; j < count; j++) {//Считаем "Быков"
                if (myword[j] == inword[j])
                    bulls++;
            }
            System.out.println("Bulls: " + bulls);
            for (int j = 0; j < inword.length; j++) {//Счтаем"Коров"
                if (word.indexOf(inword[j]) > -1)
                    cows++;
            }
            System.out.println("Cows: " + cows);
        }
        System.out.println("You Lose: " + word);
    }//Игра Быки и Коровы


    static String GetWord() {
        try {
            Scanner scan = new Scanner(new File("dictionary.txt"));
            int count = 0;
            while (scan.hasNext()) {//Считаем количество строк(слов) в файле
                scan.nextLine();
                count++;
            }
            Random rnd = new Random(System.currentTimeMillis());
            int number = rnd.nextInt(count);//Выбираем случайное слово
            scan = new Scanner(new File("dictionary.txt"));
            for (int i = 0; i < number - 1; i++)
                scan.nextLine();
            return (scan.nextLine());//Выводим полученное слово
        } catch (FileNotFoundException ex) {//Если файл не найден выводит ошибку
            System.err.println("File not found: " + ex.getMessage());
            return null;
        }
    }

}