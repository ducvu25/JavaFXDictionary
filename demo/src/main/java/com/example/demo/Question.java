package com.example.demo;
import java.util.Random;

public class Question {
    private String ID;
    private String question;
    private String idA;
    private String idB;
    private String idC;
    private String idD;
    private String key;
    private String explain;
    public Question(){}
    public Question(String line){
        String[] fields = line.split(";");
        if (fields.length == 8) {
            ID = fields[0];
            question = fields[1];
            idA = fields[2];
            idB = fields[3];
            idC = fields[4];
            idD = fields[5];
            key = fields[6];
            explain = fields[7];
        }
    }
    public Question(String ID, String question, String idA, String idB, String idC, String idD, String key, String explain) {
        this.ID = ID;
        this.question = question;
        this.idA = idA;
        this.idB = idB;
        this.idC = idC;
        this.idD = idD;
        this.key = key;
        this.explain = explain;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String value) {
        this.question = value;
    }

    public String getIdA() {
        return idA;
    }

    public void setIdA(String idA) {
        this.idA = idA;
    }

    public String getIdB() {
        return idB;
    }

    public void setIdB(String idB) {
        this.idB = idB;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getIdD() {
        return idD;
    }

    public void setIdD(String idD) {
        this.idD = idD;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public boolean checkAnswer(String selectedOption) {
        return selectedOption.equals(key);
    }

    public void shuffleOptions() {
        String[] options = {idA, idB, idC, idD};
        Random random = new Random();

        for (int i = options.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String temp = options[i];
            options[i] = options[j];
            options[j] = temp;
        }

        idA = options[0];
        idB = options[1];
        idC = options[2];
        idD = options[3];
    }

    public String[] getOptions() {
        return new String[]{idA, idB, idC, idD};
    }

    public void displayOptions() {
        String[] options = getOptions();
        for (int i = 0; i < options.length; i++) {
            System.out.println((char) ('A' + i) + ". " + options[i]);
        }
    }
}