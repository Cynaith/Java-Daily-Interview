package com.ly.interview.设计模式;

import java.awt.*;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class Build {
    public static void main(String[] args) {
        Car audi = new Car.Builder()
                .color(Color.BLACK)
                .money(300000)
                .kind("A4")
                .year(2020)
                .build();
        System.out.println(audi.getMoney());
    }
}

class Car{
    String kind;
    Color color;
    int year;
    int money;

    private Car(Builder builder) {
        setKind(builder.kind);
        setColor(builder.color);
        setYear(builder.year);
        setMoney(builder.money);
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public static final class Builder {
        private String kind;
        private Color color;
        private int year;
        private int money;

        public Builder() {
        }

        public Builder kind(String val) {
            kind = val;
            return this;
        }

        public Builder color(Color val) {
            color = val;
            return this;
        }

        public Builder year(int val) {
            year = val;
            return this;
        }

        public Builder money(int val) {
            money = val;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
