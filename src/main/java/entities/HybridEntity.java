package entities;

import javafx.util.Pair;

public class HybridEntity {
    private Integer position;
    private String token;
    private String lexem;
    private Integer start;
    private Integer length;

    private String stack;
    private String input;
    private String production = "";

    public HybridEntity () {
    }

    public HybridEntity (Integer position, String token, String lexem, Integer start, Integer length, String stack, String input, Pair<String, String> prod) {
        this(position, token, lexem, start, length, stack, input);
        this.production = prod.getKey() + " -> " + prod.getValue();
    }

    public HybridEntity (Integer position, String token, String lexem, Integer start, Integer length, String stack, String input) {
        this.position = position;
        this.token = token;
        this.lexem = lexem;
        this.start = start;
        this.length = length;
        this.stack = stack;
        this.input = input;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLexem() {
        return lexem;
    }

    public void setLexem(String lexem) {
        this.lexem = lexem;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
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