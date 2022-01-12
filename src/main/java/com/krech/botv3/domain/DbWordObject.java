package com.krech.botv3.domain;

public class DbWordObject {


    int id;
    String name;
    String firstLetter;

    DbWordObject(){

    }

    public DbWordObject(int id, String name, String firstLetter) {
        this.id = id;
        this.name = name;
        this.firstLetter = firstLetter;
    }




}
