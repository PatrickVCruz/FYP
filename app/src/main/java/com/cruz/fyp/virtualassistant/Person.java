package com.cruz.fyp.virtualassistant;

class Person {

    public enum PersonType {
        ONE_ITEM, TWO_ITEM
    }

    private String name;

    public PersonType getType() {
        return type;
    }

    public void setType(PersonType type) {
        this.type = type;
    }

    private PersonType type;

    public String getName() {
        return name;
    }

    Person(String name, PersonType type) {
        this.name = name;
        this.type = type;
    }
}

