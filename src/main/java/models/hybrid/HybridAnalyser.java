package models.hybrid;

import com.sun.deploy.util.StringUtils;
import dao.TableDAO;
import entities.HybridEntity;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class HybridAnalyser extends TokensRegEx implements JumpTableHolder {

    private String input;
    private List<String> splitedInput;

    private List<String> stack;
    private HashMap<Pair<String, String>, List<String>> jumpTable;
    private String startRule = "";
    private String addedRule = "";

    private List<HybridEntity> hybridEntities = new ArrayList<>();

    private static String SHIFT = " ";
    private static String SINGLE_QUOTE = "\'";

    private boolean isTableExist;
    private boolean isColumnExist;

    public HybridAnalyser (String input) {
        this.input = input;
        this.splitedInput = splitInput();
        this.jumpTable = initTable();
        this.stack = new ArrayList<>(Arrays.asList("$", startRule));

        this.isTableExist = checkTable();
        this.isColumnExist = checkColumn();

        System.out.println(startRule);
    }

    private boolean checkTable() {
        String tableName = splitedInput.get(splitedInput.indexOf("UPDATE") + 1);
        TableDAO dao = new TableDAO();

        return dao.isTableExist(tableName);
    }

    private boolean checkColumn() {
        String tableName = splitedInput.get(splitedInput.indexOf("UPDATE") + 1);
        String columnName = splitedInput.get(splitedInput.indexOf("SET") + 1);
        TableDAO dao = new TableDAO();

        return dao.isColumnExist(tableName, columnName);
    }

    public Integer startAnalysis () {
        if (jumpTable != null) {
            String currentInput = splitedInput.get(0);
            String currentStack = stack.get(stack.size() - 1);

            int position = 1, start = 1;

            //hybridEntities.add(new HybridEntity(position, tokenChooser(currentInput), currentInput, start,
            //        currentInput.length(), stack.toString(), splitedInput.toString(), new Pair<>(currentStack, addedRule)));

            while (!stack.isEmpty()) {
                int code = changeStack (currentStack, currentInput);

                if (code == -1) {
                    return -1;
                }
                else {
                    if (!addedRule.equals("") && stack.size() > 0) {
                        hybridEntities.add(new HybridEntity(position, tokenChooser(currentInput), currentInput, start,
                                currentInput.length(), stack.toString(), splitedInput.toString(), new Pair<>(currentStack, addedRule)));
                        currentStack = stack.get(stack.size() - 1);
                    }
                    else if (stack.size() > 0) {
                        hybridEntities.add(new HybridEntity(position, tokenChooser(currentInput), currentInput, start,
                                currentInput.length(), stack.toString(), splitedInput.toString()));
                        currentStack = stack.get(stack.size() - 1);
                    }

                    position++;
                    boolean change = isExist(currentInput);
                    if (!change) start+=currentInput.length();

                    if (!splitedInput.isEmpty()) currentInput = splitedInput.get(0);
                    addedRule = "";
                }
            }

            return 0;
        }
        else return -2;
    }

    private boolean isExist (String lexem) {
        int count = (int) hybridEntities.stream().filter(x -> x.getLexem().equals(lexem)).count();

        System.out.println(lexem + " " + count);
        return count > 1;
    }

    private int changeStack (String currentStack, String currentInput) {
        if (currentInput.equals(currentStack) || tokenChooser(currentInput).equals(currentStack)) {
            stack.remove(stack.size() - 1);
            splitedInput.remove(0);

            return 0;
        }
        else {
            List<String> rules = findRules(currentStack, tokenChooser(currentInput));

            if (rules != null) {
                stack.remove(stack.size() - 1);

                if (!joinList(rules).equals("ε")) {
                    Collections.reverse(rules);
                    stack.addAll(rules);
                }

                Collections.reverse(rules);
                addedRule = joinList(rules);

                return 1;
            }
            else return -1;
        }
    }

    private List<String> findRules (String rule, String token) {
        AtomicReference<List<String>> rules = new AtomicReference<>();

        jumpTable.forEach( (r, t) -> {
            if (r.getKey().equals(rule) && r.getValue().equals(token)) {
                rules.set(new ArrayList<>(t));
            }
        });

        return rules.get();
    }

    private String joinList (List<String> list) {
        return StringUtils.join(list, "");
    }

    private List<String> splitInput() {
        List<String> splited = new ArrayList<>();

        int start = 0; int end = 0;
        String token;

        while (end != input.length()) {
            end = input.indexOf(SHIFT, start);
            if (end == -1) end = input.length();

            if (input.substring(start, start+1).equals(SINGLE_QUOTE)) {
                end = input.indexOf(SINGLE_QUOTE, start+1);
                if (end == -1) end = input.length();

                token = input.substring(start, end+1);
                splited.add(token);

                start = end + 2;
            }
            else {
                token = input.substring(start, end);
                splited.add(token);

                start = end + 1;
            }
        }

        return splited;
    }

    private HashMap<Pair<String, String>, List<String>> initTable () {
        HashMap<Pair<String, String>, List<String>> table;

        if (splitedInput.contains("CROSS")) {
            table = initCrossJumpTable();
            startRule = "<times>";
        }
        else if (splitedInput.contains("LEFT")) {
            table = initLeftJumpTable();
            startRule = "<semiminus>";
        }
        else if (splitedInput.contains("SELECT")) {
            table = initSelectJumpTable();
            startRule = "<select>";
        }
        else if (splitedInput.contains("DROP")) {
            table = initDropJumpTable();
            startRule = "<drop_table>";
        }
        else if (splitedInput.contains("UPDATE")) {
            table = initUpdateJumpTable();
            startRule = "<update_table>";
        }
        else table = null;

        return table;
    }

    public List<HybridEntity> getHybridEntities() {
        return hybridEntities;
    }

    public boolean isTableExist() {
        return isTableExist;
    }

    public boolean isColumnExist() {
        return isColumnExist;
    }
}
