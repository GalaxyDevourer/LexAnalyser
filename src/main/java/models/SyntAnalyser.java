package models;

import com.sun.deploy.util.StringUtils;
import entities.SyntEntity;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SyntAnalyser {
    private String input;
    private List<String> stack = new ArrayList<>();
    private List<SyntEntity> syntEntityList = new ArrayList<>();

    // Pair - rule and token, list of rules for adding to stack
    private HashMap<Pair<String, String>, List<String>> jumpTable;

    private int currentPosition = 0;
    private String addedRule = "";

    public SyntAnalyser(String input) {
        this.input = input;
        this.jumpTable = initJumpTable();

        stack.add("$");
        stack.add("<A>");
    }

    // 0 = successful, -1 = error
    public int startSyntAnalysis () {
        String currentInput = input.substring(currentPosition, currentPosition + 1);
        String currentStack = stack.get(stack.size() - 1);
        int number = 1;

        syntEntityList.add(new SyntEntity(number, stack, input.substring(currentPosition)));

        while (!stack.isEmpty()) {
            int code = changeStack (currentStack, currentInput);

            if (code == -1) {
                return -1;
            }
            else {
                number++;

                if (!addedRule.equals("") && stack.size() > 0) {
                    syntEntityList.add(new SyntEntity(number, stack, input.substring(currentPosition), currentStack, addedRule));
                    currentStack = stack.get(stack.size() - 1);
                }
                else if (stack.size() > 0) {
                    syntEntityList.add(new SyntEntity(number, stack, input.substring(currentPosition)));
                    currentStack = stack.get(stack.size() - 1);
                }

                if (input.length() > currentPosition) currentInput = input.substring(currentPosition, currentPosition + 1);
                addedRule = "";
            }
        }

        return 0;
    }

    // 0 or 1 = successful, -1 = error
    private int changeStack (String currentStack, String currentInput) {
        if (currentInput.equals(currentStack)) {
            stack.remove(stack.size() - 1);
            currentPosition++;

            return 0;
        }
        else {
            List<String> rules = findRules(currentStack, currentInput);

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

    private HashMap<Pair<String, String>, List<String>> initJumpTable () {
        HashMap<Pair<String, String>, List<String>> table = new HashMap<>();

        table.put(new Pair<>("<A>","a"), Arrays.asList("<C>", "<B>"));
        table.put(new Pair<>("<A>","b"), Arrays.asList("<C>", "<B>"));
        table.put(new Pair<>("<A>","c"), Arrays.asList("<C>", "<B>"));
        table.put(new Pair<>("<A>","d"), Arrays.asList("<C>", "<B>"));
        table.put(new Pair<>("<A>","("), Arrays.asList("<C>", "<B>"));

        table.put(new Pair<>("<B>","+"), Arrays.asList("+", "<C>", "<B>"));
        table.put(new Pair<>("<B>",")"), Collections.singletonList("ε"));
        table.put(new Pair<>("<B>","$"), Collections.singletonList("ε"));

        table.put(new Pair<>("<C>","a"), Arrays.asList("<F>", "<D>"));
        table.put(new Pair<>("<C>","b"), Arrays.asList("<F>", "<D>"));
        table.put(new Pair<>("<C>","c"), Arrays.asList("<F>", "<D>"));
        table.put(new Pair<>("<C>","d"), Arrays.asList("<F>", "<D>"));
        table.put(new Pair<>("<C>","("), Arrays.asList("<F>", "<D>"));

        table.put(new Pair<>("<D>","+"), Collections.singletonList("ε"));
        table.put(new Pair<>("<D>","/"), Arrays.asList("/", "<F>", "<D>"));
        table.put(new Pair<>("<D>",")"), Collections.singletonList("ε"));
        table.put(new Pair<>("<D>","$"), Collections.singletonList("ε"));

        table.put(new Pair<>("<F>","a"), Collections.singletonList("a"));
        table.put(new Pair<>("<F>","b"), Collections.singletonList("b"));
        table.put(new Pair<>("<F>","c"), Collections.singletonList("c"));
        table.put(new Pair<>("<F>","d"), Collections.singletonList("d"));
        table.put(new Pair<>("<F>","("), Arrays.asList("(", "<A>", ")"));

        /*
        table.put(new Pair<>("<E>","a"), Arrays.asList("<T>", "<E2>"));
        table.put(new Pair<>("<E>","b"), Arrays.asList("<T>", "<E2>"));
        table.put(new Pair<>("<E>","c"), Arrays.asList("<T>", "<E2>"));
        table.put(new Pair<>("<E>","d"), Arrays.asList("<T>", "<E2>"));
        table.put(new Pair<>("<E>","("), Arrays.asList("<T>", "<E2>"));

        table.put(new Pair<>("<E2>","+"), Arrays.asList("+", "<T>", "<E2>"));
        table.put(new Pair<>("<E2>","-"), Arrays.asList("-", "<T>", "<E2>"));
        table.put(new Pair<>("<E2>",")"), Collections.singletonList("ε"));
        table.put(new Pair<>("<E2>","$"), Collections.singletonList("ε"));

        table.put(new Pair<>("<T>","a"), Arrays.asList("<F>", "<T2>"));
        table.put(new Pair<>("<T>","b"), Arrays.asList("<F>", "<T2>"));
        table.put(new Pair<>("<T>","c"), Arrays.asList("<F>", "<T2>"));
        table.put(new Pair<>("<T>","d"), Arrays.asList("<F>", "<T2>"));
        table.put(new Pair<>("<T>","("), Arrays.asList("<F>", "<T2>"));

        table.put(new Pair<>("<T2>","+"), Collections.singletonList("ε"));
        table.put(new Pair<>("<T2>","-"), Collections.singletonList("ε"));
        table.put(new Pair<>("<T2>","*"), Arrays.asList("*", "<F>", "<T2>"));
        table.put(new Pair<>("<T2>","/"), Arrays.asList("/", "<F>", "<T2>"));
        table.put(new Pair<>("<T2>",")"), Collections.singletonList("ε"));
        table.put(new Pair<>("<T2>","$"), Collections.singletonList("ε"));

        table.put(new Pair<>("<F>","a"), Collections.singletonList("a"));
        table.put(new Pair<>("<F>","b"), Collections.singletonList("b"));
        table.put(new Pair<>("<F>","c"), Collections.singletonList("c"));
        table.put(new Pair<>("<F>","d"), Collections.singletonList("d"));
        table.put(new Pair<>("<F>","("), Arrays.asList("(", "<E>", ")"));
         */

        return table;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(ArrayList<String> stack) {
        this.stack = stack;
    }

    public List<SyntEntity> getSyntEntityList() {
        return syntEntityList;
    }

    public void setSyntEntityList(List<SyntEntity> syntEntityList) {
        this.syntEntityList = syntEntityList;
    }

    public HashMap<Pair<String, String>, List<String>> getJumpTable() {
        return jumpTable;
    }

    public void setJumpTable(HashMap<Pair<String, String>, List<String>> jumpTable) {
        this.jumpTable = jumpTable;
    }
}