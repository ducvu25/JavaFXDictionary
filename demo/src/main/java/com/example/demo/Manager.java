package com.example.demo;
import javafx.scene.control.cell.CheckBoxListCell;

import java.io.*;
import java.util.ArrayList;
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
        String filePath = "E:\\Hoc tap\\Other\\java\\JavaFX\\JavaFXDictionary\\demo\\src\\main\\resources/" + paths[type] + ".txt"; // Đường dẫn tới file txt của bạn

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
    static void SaveFile(int type) {
        String[] paths = {"Word", "Question"};
        String filePath = "E:\\Hoc tap\\Other\\java\\JavaFX\\JavaFXDictionary\\demo\\src\\main\\resources/" + paths[type] + ".txt"; // Đường dẫn tới file txt của bạn

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            if (type == 0) {
                for (Word word : words) {
                    writer.write(word.toString());
                    writer.newLine();
                }
            } else {
                for (Question question : questions) {
                    writer.write(question.toString());
                    writer.newLine();
                }
            }

            writer.close();
            System.out.println("Xuất file thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Xuất file thất bại!");
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
    public static ArrayList<Word> searchVietnameseWord(String mean) {
        ArrayList<Word> result = new ArrayList<>();
        for(int i=0; i<words.size(); i++) {
            //System.out.println(words.get(i).getMeans());
            if (words.get(i).getMeans().indexOf(mean) != -1)
                result.add(words.get(i));
        }
        return result;
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
        if(!words.contains(word)) {
            words.add(word);
            SaveFile(0);
        }
    }

    public static void updateWord(Word word) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getID().equals(word.getID())) {
                words.set(i, word);
                SaveFile(0);
                break;
            }
        }
    }
    public static boolean deleteWord(String id) {
        for(int i=0; i<words.size(); i++){
            if(words.get(i).getID().equals(id)){
                words.remove(i);
                SaveFile(0);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Question> createQuiz(int n) {
        ArrayList<Question> result = new ArrayList<>();
        ArrayList<Integer> indexs = new ArrayList<>();
        for(int i=0; i<questions.size(); i++)
            indexs.add(i);

        Collections.shuffle(indexs);
        for(int i=0; i<n && i < questions.size(); i++){
            result.add(questions.get(indexs.get(i)));
            result.get(i).shuffleOptions();
        }
        return result;
    }
}