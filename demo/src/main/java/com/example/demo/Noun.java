package com.example.demo;

import java.util.ArrayList;

public class Noun extends Word {
    private String pluralForm;
    private String singularForm;

    public Noun(ArrayList<Word> value, String line){
        String[] fields = line.split(";");
        if (fields.length == 3) {
            for(int i=0; i<value.size(); i++)
                if(value.get(i).getID().equals(fields[0])){
                    ID = value.get(i).getID();
                    word = value.get(i).getWord();
                    pronounce = value.get(i).getPronounce();
                    means = value.get(i).getMeans();
                    eg = value.get(i).getEg();
                    break;
                }
            pluralForm = fields[1];
            singularForm = fields[2];
        } else {
            throw new IllegalArgumentException("Invalid line format");
        }
    }
    public Noun(String ID, String word, String pronounce, String means, String eg, String pluralForm, String singularForm) {
        super(ID, word, pronounce, means, eg);
        this.pluralForm = pluralForm;
        this.singularForm = singularForm;
    }

    public String getPluralForm() {
        return pluralForm;
    }

    public void setPluralForm(String pluralForm) {
        this.pluralForm = pluralForm;
    }

    public String getSingularForm() {
        return singularForm;
    }

    public void setSingularForm(String singularForm) {
        this.singularForm = singularForm;
    }

    @Override
    public String toString() {
        return "Noun{" +
                "ID='" + ID + '\'' +
                ", word='" + word + '\'' +
                ", pronounce='" + pronounce + '\'' +
                ", means='" + means + '\'' +
                ", eg='" + eg + '\'' +
                ", pluralForm='" + pluralForm + '\'' +
                ", singularForm='" + singularForm + '\'' +
                '}';
    }
}