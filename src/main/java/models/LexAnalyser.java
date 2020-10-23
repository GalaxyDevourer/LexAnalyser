package main.java.models;

import main.java.entities.LexisEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class LexAnalyser {
    private String query;
    private List<LexisEntity> lexisEntityList = new ArrayList<>();

    private static final Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("[;,()]*");

    private static final Pattern KEYWORD = Pattern.compile("\\b(?:ADD|ALL|ALTER|ANALYZE|AND|AS|ASC|AUTO_INCREMENT|BDB|" +
            "BERKELEYDB|BETWEEN|BIGINT|BINARY|BLOB|BOTH|BTREE|BY|CASCADE|CASE|CHANGE| CHAR|CHARACTER|CHECK|COLLATE|COLUMN|" +
            "COLUMNS|CONSTRAINT|CREATE|CROSS|CURRENT_DATE|CURRENT_TIME|CURRENT_TIMESTAMP|DATABASE|DATABASES|DAY_HOUR|" +
            "DAY_MINUTE|DAY_SECOND|DEC|DECIMAL|DEFAULT|DELAYED|DELETE|DESC|DESCRIBE|DISTINCT|DISTINCTROW|DIV|DOUBLE|DROP|" +
            "ELSE|ENCLOSED|ERRORS|ESCAPED|EXISTS|EXPLAIN|FALSE|FIELDS|FLOAT|FOR|FORCE|FOREIGN|FROM|FULLTEXT|FUNCTION|GEOMETRY|" +
            "GRANT|GROUP|HASH|HAVING|HELP|HIGH_PRIORITY|HOUR_MINUTE|HOUR_SECOND|IF|IGNORE|IN|INDEX|INFILE|INNER|INNODB|" +
            "INSERT|INT|INTEGER|INTERVAL|INTO|IS|JOIN|KEY| KEYS|KILL|LEADING|LEFT|LIKE|IMIT|LINES|LOAD|LOCALTIME|LOCALTIMESTAMP|" +
            "LOCK|LONG|LONGBLOB|LONGTEXT|LOW_PRIORITY|MASTER_SERVER_ID|MATCH|MEDIUMBLOB|MEDIUMINT|MEDIUMTEXT|MIDDLEINT|" +
            "MINUTE_SECOND|MOD|MRG_MYISAM|NATURAL|NOT|NULL|NUMERIC|ON|OPTIMIZE|OPTION|OPTIONALLY|OR|ORDER|OUTER|OUTFILE|" +
            "PRECISION|PRIMARY|PRIVILEGES|PROCEDURE|PURGE|READ|REAL|REFERENCES|REGEXP|RENAME|REPLACE|REQUIRE|RESTRICT|" +
            "RETURNS|REVOKE|RIGHT|RLIKE|RTREE|SELECT|SET|SHOW|SMALLINT|SOME|SONAME|SPATIAL|SQL_BIG_RESULT|SQL_CALC_FOUND_ROWS|" +
            "SQL_SMALL_RESULT|SSL|STARTING|STRAIGHT_JOIN|STRIPED|TABLE|TABLES|TERMINATED|THEN|TINYBLOB|TINYINT|TINYTEXT|TO|" +
            "TRAILING|TRUE|TYPES|UNION|UNIQUE|UNLOCK|UNSIGNED|UPDATE| USAGE|USE|USER_RESOURCES|USING|VALUES|VARBINARY|VARCHAR|" +
            "VARCHARACTER|VARYING|WARNINGS|WHEN|WHERE|WITH|WRITE|XOR|YEAR_MONTH|ZEROFILL)\\b");

    private static final Pattern ID = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
    private static final Pattern REAL_NUMBER = Pattern.compile("[0-9]+");
    private static final Pattern NUMBER = Pattern.compile("[0-9]+\\.[0-9]*");
    private static final Pattern STRING = Pattern.compile("'[^']*'");
    private static final Pattern OPERATION = Pattern.compile("[+\\-*/.=()]");
    private TreeMap<String, Pattern> patterns = new TreeMap<>();

    private static Character SHIFT = ' ';
    private static Character SINGLE_QUOTE = '\'';

    public LexAnalyser(String query) {
        this.query = query;
        clearQuery();

        patterns.put("Id", ID);
        patterns.put("Number", REAL_NUMBER);
        patterns.put("Real number", NUMBER);
        patterns.put("String", STRING);
        patterns.put("Operator", OPERATION);
        patterns.put("Keyword", KEYWORD);
    }

    private void clearQuery() {
        query = CLEAR_PATTERN.matcher(query).replaceAll(" ").trim();
        System.out.println(query);
    }

    public void startLexisSplit () {
        int index = 0, start = 0, last = 0, next = 0, position = 1, end = query.length()-1;
        int length, start_pos = 1;
        String lexis = "";

        while ( last < end) {
            index = query.indexOf(SHIFT, start);
            Character ch = query.substring(start, start+1).charAt(0);

            if (!ch.equals(SINGLE_QUOTE)) {
                if ( index == -1 ) {
                    index = last;
                    last = end+1;
                }
                else last = index;
            }
            else {
                index = query.indexOf(SINGLE_QUOTE, start+1);
                index = query.indexOf(SHIFT, index);
                last = index;
            }

            next = index + 1;
            lexis = query.substring(start, last);
            length = lexis.length();

            LexisEntity entity = new LexisEntity(tokenChooser(lexis.toUpperCase()), lexis, start_pos, start, length, position);
            position += positionIfExist(entity);

            if (!SYMBOL_PATTERN.matcher(lexis).matches()) {
                lexisEntityList.add(entity);
            }

            start_pos += length;
            start = next;
        }

    }

    private String tokenChooser (String lexis) {
        AtomicReference<String> token = new AtomicReference<>("None");

        patterns.forEach( (x,y) -> {
            if (y.matcher(lexis).matches()) token.set(x);
        });

        return token.get();
    }

    private int positionIfExist(LexisEntity entity) {
        AtomicInteger i = new AtomicInteger(1);

        if (!lexisEntityList.isEmpty()) {
            lexisEntityList.forEach(x -> {
                if (x.getLexis().equals(entity.getLexis())) {
                    entity.setPostition(x.getPostition());
                    if ( i.get() == 1) i.set(0);
                }
            });
        }

        return i.get();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<LexisEntity> getLexisEntityList() {
        return lexisEntityList;
    }

    public void setLexisEntityList(List<LexisEntity> lexisEntityList) {
        this.lexisEntityList = lexisEntityList;
    }
}