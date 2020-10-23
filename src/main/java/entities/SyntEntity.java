package main.java.entities;

import java.util.List;

public class SyntEntity {
    private int number;
    private String stack;
    private String input;
    private String production = "";

    public SyntEntity(int number, List<String> stack, String input, String left, String right) {
        this(number, stack, input);
        this.production = left + " -> " + right;
    }

    public SyntEntity(int number, List<String> stack, String input) {
        this.number = number;
        this.stack = stack.toString();
        this.input = input;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }
}
