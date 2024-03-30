package com.example.demo;

public class Word {
    protected String ID;
    protected String word;
    protected String pronounce;
    protected String means;
    protected String eg;

    public Word() {
    }
    public Word(String line){
        String[] fields = line.split(";");
        if (fields.length == 5) {
            ID = fields[0];
            word = fields[1];
            pronounce = fields[2];
            means = fields[3];
            eg = fields[4];
        } else {
            throw new IllegalArgumentException("Invalid line format");
        }
    }
    public Word(String ID, String word, String pronounce, String means, String eg) {
        this.ID = ID;
        this.word = word;
        this.means = means;
        this.eg = eg;
        this.pronounce = pronounce;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getPronounce(){return pronounce;}
    public void setPronounce(String value){pronounce = value;}

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getEg() {
        return eg;
    }

    public void setEg(String eg) {
        this.eg = eg;
    }

    @Override
    public String toString() {
        return ID + ';' + word + ';' + pronounce + ';' +  means + ';' + eg ;
    }
}