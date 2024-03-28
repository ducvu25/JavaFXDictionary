package com.example.demo;
import javafx.scene.control.cell.CheckBoxListCell;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public final class Manager {
    static ArrayList<Word> words;
    static ArrayList<Question> questions;
    public static void Connect(){
        words = new ArrayList<>();
        questions = new ArrayList<>();
        ReadFile(0);
        ReadFile(1);
        System.out.println("Doc file thanh cong");
        print();
    }
    static void ReadFile(int type){
        String []paths = {"Word", "Question"};
        String filePath = "E:\\Hoc tap\\Other\\java\\JavaFX\\demo\\src\\main\\resources/" + paths[type] + ".txt"; // Đường dẫn tới file txt của bạn

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                switch (type){
                    case 0:{
                        String[] fields = line.split(";");
                        if (fields.length == 7) {
                            words.add(new Noun(fields[0], fields[1], fields[2], fields[3],
                                    fields[4], fields[5], fields[6]));
                        } else {
                            words.add(new Verb(fields[0], fields[1], fields[2], fields[3],
                                    fields[4], fields[5], fields[6], fields[7]));
                        }
                        break;
                    }
                    case 1:{
                        questions.add(new Question(line));
                        break;
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void print(){
        for(int i=0; i<words.size(); i++)
            System.out.println(words.get(i).toString());
    }
    public static Word searchEnglishWord(String word) {
        for(int i=0; i<words.size(); i++)
            if(words.get(i).getWord().indexOf(word) != -1)
                return words.get(i);
        return null;
    }
    public static Word searchVietnameseWord(String mean) {
        for(int i=0; i<words.size(); i++) {
            //System.out.println(words.get(i).getMeans());
            if (words.get(i).getMeans().indexOf(mean) != -1)
                return words.get(i);
        }
        return null;
    }
    public static Word findWord(String ID){
        for(int i=0; i<words.size(); i++) {
            //System.out.println(words.get(i).getMeans());
            if (words.get(i).getID().equals(ID))
                return words.get(i);
        }
        return null;
    }
    public static void addWord(Word word) {
        if(!words.contains(word))
            words.add(word);
    }

    public static void updateWord(Word word) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getID().equals(word.getID())) {
                words.set(i, word);
                break;
            }
        }
    }
    public static void deleteWord(Word word) {
        for(int i=0; i<words.size(); i++){
            if(words.get(i).getID().equals(word.getID())){
                words.remove(i);
                break;
            }
        }
    }

    public static ArrayList<Question> createQuiz(int n) {
        ArrayList<Question> result = new ArrayList<>();
        ArrayList<Integer> indexs = new ArrayList<>();
        for(int i=0; i<questions.size(); i++)
            indexs.add(i);

        Collections.shuffle(indexs);
        for(int i=0; i<n && i < questions.size(); i++){
            result.add(questions.get(i));
            result.get(i).shuffleOptions();
        }
        return result;
    }
}