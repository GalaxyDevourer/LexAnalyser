package entities;

public class LexisEntity {
    private String token;
    private String lexis;
    private Integer start;
    private Integer real_start;
    private Integer length;
    private Integer postition;

    public LexisEntity () {}

    public LexisEntity(String token, String lexis, Integer start, Integer real_start, Integer length, Integer postition) {
        this.token = token;
        this.lexis = lexis;
        this.start = start;
        this.real_start = real_start;
        this.length = length;
        this.postition = postition;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLexis() {
        return lexis;
    }

    public void setLexis(String lexis) {
        this.lexis = lexis;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getReal_start() {
        return real_start;
    }

    public void setReal_start(Integer real_start) {
        this.real_start = real_start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPostition() {
        return postition;
    }

    public void setPostition(Integer postition) {
        this.postition = postition;
    }
}
