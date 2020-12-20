package models.hybrid;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class TokensRegEx {
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
    private static final Pattern OPERATION = Pattern.compile("[+\\-*/]");
    private static final Pattern OPERATORS = Pattern.compile("[><=]");
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("[;,()$]*");
    private TreeMap<String, Pattern> patterns = new TreeMap<>();

    TokensRegEx () {
        patterns.put("Keyword", KEYWORD);
        patterns.put("Id", ID);
        patterns.put("Number", REAL_NUMBER);
        patterns.put("RealNumber", NUMBER);
        patterns.put("String", STRING);
        patterns.put("Operator", OPERATION);
        patterns.put("BoolOperator", OPERATORS);
        patterns.put("Symbol", SYMBOL_PATTERN);
    }

    String tokenChooser (String lexis) {
        AtomicReference<String> token = new AtomicReference<>("None");

        patterns.forEach( (x,y) -> {
            if (y.matcher(lexis).matches()) token.set(x);
        });

        return token.get();
    }
}
