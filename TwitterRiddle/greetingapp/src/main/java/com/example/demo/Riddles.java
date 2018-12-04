package com.example.demo;

import javax.persistence.*;

/**
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 */
@Entity
@Table(name = "riddles")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public class Riddles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "riddle")
    private String riddle;

    @Column(name = "answer")
    private String answer;

    private String content;

    public Riddles(){}

    public Riddles(int id, String riddle, String answer) {
        this.id = id;
        this.riddle = riddle;
        this.answer = answer;

    }

    public String getRiddle() {
        return riddle;
    }

    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Riddles{" +
                "id=" + id +
                ", riddle='" + riddle + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
