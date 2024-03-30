package com.example.demo;

import java.util.ArrayList;

public class Noun extends Word {
    private String pluralForm;
    private String singularForm;
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
        return ID + ';' + word + ';' + pronounce + ';' +  means + ';' + eg + ";" + pluralForm + ';' + singularForm;
    }
}