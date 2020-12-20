package models.hybrid;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public interface JumpTableHolder {
    default HashMap<Pair<String, String>, List<String>> initCrossJumpTable() {
        HashMap<Pair<String, String>, List<String>> table = new HashMap<>();

        return table;
    }

    default HashMap<Pair<String, String>, List<String>> initLeftJumpTable() {
        HashMap<Pair<String, String>, List<String>> table = new HashMap<>();

        return table;
    }

    default HashMap<Pair<String, String>, List<String>> initSelectJumpTable() {
        HashMap<Pair<String, String>, List<String>> table = new HashMap<>();

        return table;
    }

    default HashMap<Pair<String, String>, List<String>> initDropJumpTable() {
        HashMap<Pair<String, String>, List<String>> table = new HashMap<>();

        table.put(new Pair<>("<drop_table>","Keyword"), Arrays.asList("DROP", "TABLE", "<table_name>"));

        table.put(new Pair<>("<table_name>","Id"), Collections.singletonList("Id"));

        return table;
    }

    default HashMap<Pair<String, String>, List<String>> initUpdateJumpTable() {
        HashMap<Pair<String, String>, List<String>> table = new HashMap<>();

        table.put(new Pair<>("<update_table>","Keyword"), Arrays.asList("UPDATE", "<table_name>", "SET", "<columns_expr_list>", "WHERE", "<condition>"));

        table.put(new Pair<>("<table_name>","Id"), Collections.singletonList("Id"));

        table.put(new Pair<>("<columns_expr_list>","Id"), Arrays.asList("<column_expr>", "<columns_expr>"));

        table.put(new Pair<>("<columns_expr>","Keyword"), Collections.singletonList("ε"));
        table.put(new Pair<>("<columns_expr>","Symbol"), Arrays.asList(",", "<column_expr>", "<columns_expr>"));

        table.put(new Pair<>("<column_expr>","Id"), Arrays.asList("<column_name>", "<equal_operator>" , "<value>"));

        table.put(new Pair<>("<column_name>","Id"), Collections.singletonList("Id"));

        table.put(new Pair<>("<equal_operator>","BoolOperator"), Collections.singletonList("BoolOperator"));

        table.put(new Pair<>("<value>","String"), Collections.singletonList("String"));

        table.put(new Pair<>("<condition>","Id"), Arrays.asList("Id", "<operator>"));

        table.put(new Pair<>("<operator>","BoolOperator"), Arrays.asList("BoolOperator", "<id_value>"));
        table.put(new Pair<>("<operator>","Symbol"), Collections.singletonList("ɛ"));

        table.put(new Pair<>("<id_value>","Number"), Collections.singletonList("Number"));

        return table;
    }
}
