import dataStructures.LinkedBinaryTree;

import java.util.Stack;
import java.util.Scanner;

public class Expression extends LinkedBinaryTree {

    // Конструктор
    public Expression() {
        super();
    }

    // Infix хэлбэрээр хэвлэх
    public void printInfix() {
        System.out.print("Infix: ");
        printInfixHelper(this);
        System.out.println();
    }

    private void printInfixHelper(LinkedBinaryTree tree) {
        if (tree == null || tree.isEmpty()) {
            return;
        }

        // Дэд модыг салгах
        LinkedBinaryTree leftTree = (LinkedBinaryTree) tree.removeLeftSubtree();
        LinkedBinaryTree rightTree = (LinkedBinaryTree) tree.removeRightSubtree();

        Object rootElement = tree.root();

        // Хэрэв оператор бол хаалт хэрэгтэй
        boolean isOp = false;
        if (rootElement != null) {
            String value = rootElement.toString();
            isOp = isOperator(value);
            if (isOp) {
                System.out.print("(");
            }
        }

        // Зүүн дэд мод
        if (!leftTree.isEmpty()) {
            printInfixHelper(leftTree);
        }

        // Үндэс элемент
        if (rootElement != null) {
            System.out.print(rootElement + " ");
        }

        // Баруун дэд мод
        if (!rightTree.isEmpty()) {
            printInfixHelper(rightTree);
        }

        // Хаалт хаах
        if (isOp) {
            System.out.print(")");
        }

        // Дэд модыг дахин холбох
        if (rootElement != null) {
            tree.makeTree(rootElement, leftTree, rightTree);
        }
    }

    // Prefix хэлбэрээр хэвлэх
    public void printPrefix() {
        System.out.print("Prefix: ");
        printPrefixHelper(this);
        System.out.println();
    }

    private void printPrefixHelper(LinkedBinaryTree tree) {
        if (tree == null || tree.isEmpty()) {
            return;
        }

        // Дэд модыг салгах
        LinkedBinaryTree leftTree = (LinkedBinaryTree) tree.removeLeftSubtree();
        LinkedBinaryTree rightTree = (LinkedBinaryTree) tree.removeRightSubtree();

        Object rootElement = tree.root();

        // Үндэс элемент
        if (rootElement != null) {
            System.out.print(rootElement + " ");
        }

        // Зүүн дэд мод
        if (!leftTree.isEmpty()) {
            printPrefixHelper(leftTree);
        }

        // Баруун дэд мод
        if (!rightTree.isEmpty()) {
            printPrefixHelper(rightTree);
        }

        // Дэд модыг дахин холбох
        if (rootElement != null) {
            tree.makeTree(rootElement, leftTree, rightTree);
        }
    }

    // Postfix хэлбэрээр хэвлэх
    public void printPostfix() {
        System.out.print("Postfix: ");
        printPostfixHelper(this);
        System.out.println();
    }

    private void printPostfixHelper(LinkedBinaryTree tree) {
        if (tree == null || tree.isEmpty()) {
            return;
        }

        // Дэд модыг салгах
        LinkedBinaryTree leftTree = (LinkedBinaryTree) tree.removeLeftSubtree();
        LinkedBinaryTree rightTree = (LinkedBinaryTree) tree.removeRightSubtree();

        Object rootElement = tree.root();

        // Зүүн дэд мод
        if (!leftTree.isEmpty()) {
            printPostfixHelper(leftTree);
        }

        // Баруун дэд мод
        if (!rightTree.isEmpty()) {
            printPostfixHelper(rightTree);
        }

        // Үндэс элемент
        if (rootElement != null) {
            System.out.print(rootElement + " ");
        }

        // Дэд модыг дахин холбох
        if (rootElement != null) {
            tree.makeTree(rootElement, leftTree, rightTree);
        }
    }

    // Prefix хэлбэрээс мод байгуулах
    public void buildFromPrefix(String expression) {
        String[] tokens = expression.trim().split("\\s+");
        Stack<LinkedBinaryTree> stack = new Stack<>();

        // Буцаан унших
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];

            if (isOperator(token)) {
                // Операторын хувьд 2 дэд мод шаардлагатай
                LinkedBinaryTree left = stack.pop();
                LinkedBinaryTree right = stack.pop();
                LinkedBinaryTree tree = new LinkedBinaryTree();
                tree.makeTree(token, left, right);
                stack.push(tree);
            } else {
                // Тоо эсвэл хувьсагчийн хувьд
                LinkedBinaryTree leaf = new LinkedBinaryTree();
                leaf.makeTree(token, new LinkedBinaryTree(), new LinkedBinaryTree());
                stack.push(leaf);
            }
        }

        // Үр дүнгийн модыг өөрийн мод болгох
        if (!stack.isEmpty()) {
            LinkedBinaryTree result = stack.pop();
            this.makeTree(result.root(),
                    result.removeLeftSubtree(),
                    result.removeRightSubtree());
        }
    }

    // Postfix хэлбэрээс мод байгуулах
    public void buildFromPostfix(String expression) {
        String[] tokens = expression.trim().split("\\s+");
        Stack<LinkedBinaryTree> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                // Операторын хувьд 2 дэд мод шаардлагатай
                LinkedBinaryTree right = stack.pop();
                LinkedBinaryTree left = stack.pop();
                LinkedBinaryTree tree = new LinkedBinaryTree();
                tree.makeTree(token, left, right);
                stack.push(tree);
            } else {
                // Тоо эсвэл хувьсагчийн хувьд
                LinkedBinaryTree leaf = new LinkedBinaryTree();
                leaf.makeTree(token, new LinkedBinaryTree(), new LinkedBinaryTree());
                stack.push(leaf);
            }
        }

        // Үр дүнгийн модыг өөрийн мод болгох
        if (!stack.isEmpty()) {
            LinkedBinaryTree result = stack.pop();
            this.makeTree(result.root(),
                    result.removeLeftSubtree(),
                    result.removeRightSubtree());
        }
    }

    // Infix хэлбэрээс мод байгуулах
    public void buildFromInfix(String expression) {
        // Хаалттай илэрхийллийн хувьд
        if (expression.contains("(") || expression.contains(")")) {
            buildFromFullyParenthesizedInfix(expression);
        } else {
            // Хаалтгүй илэрхийллийн хувьд postfix руу хөрвүүлэх
            String postfix = infixToPostfix(expression);
            buildFromPostfix(postfix);
        }
    }

    // Хаалттай infix-ээс мод байгуулах
    private void buildFromFullyParenthesizedInfix(String expression) {
        expression = expression.replaceAll("\\s+", "");

        if (expression.length() == 0) {
            return;
        }

        // Хамгийн гаднах хаалтыг арилгах
        if (expression.charAt(0) == '(' &&
                expression.charAt(expression.length() - 1) == ')') {
            expression = expression.substring(1, expression.length() - 1);
        }

        // Хамгийн гаднах операторыг олох
        int bracketCount = 0;
        int operatorIndex = -1;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(')
                bracketCount++;
            else if (c == ')')
                bracketCount--;
            else if (bracketCount == 0 && isOperator(String.valueOf(c))) {
                operatorIndex = i;
                break;
            }
        }

        if (operatorIndex != -1) {
            String operator = String.valueOf(expression.charAt(operatorIndex));
            String leftExpr = expression.substring(0, operatorIndex);
            String rightExpr = expression.substring(operatorIndex + 1);

            // Дэд моднуудыг байгуулах
            Expression leftTree = new Expression();
            Expression rightTree = new Expression();

            leftTree.buildFromFullyParenthesizedInfix(leftExpr);
            rightTree.buildFromFullyParenthesizedInfix(rightExpr);

            // Гол мод байгуулах
            this.makeTree(operator, leftTree, rightTree);
        } else {
            // Хэрэв оператор олдоогүй бол энэ нь тоо эсвэл хувьсагч
            this.makeTree(expression, new LinkedBinaryTree(), new LinkedBinaryTree());
        }
    }

    // Infix-ээс Postfix руу хөрвүүлэх
    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        infix = infix.replaceAll("\\s+", "");

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                postfix.append(c).append(" ");
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty())
                    stack.pop();
            } else if (isOperator(String.valueOf(c))) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }

        return postfix.toString().trim();
    }

    // Операторын урьдчилсан
    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    // Илэрхийллийн утгыг бодох
    public double evaluate() {
        return evaluateHelper(this);
    }

    private double evaluateHelper(LinkedBinaryTree tree) {
        if (tree == null || tree.isEmpty()) {
            return 0;
        }

        Object rootElement = tree.root();
        if (rootElement == null) {
            return 0;
        }

        String value = rootElement.toString();

        // Хэрэв тоо бол
        if (isNumber(value)) {
            return Double.parseDouble(value);
        }
        // Хэрэв хувьсагч бол
        else if (isVariable(value)) {
            // Гараас утга авах
            return getVariableValue(value);
        }
        // Хэрэв оператор бол
        else if (isOperator(value)) {
            // Дэд модыг салгах
            LinkedBinaryTree leftTree = (LinkedBinaryTree) tree.removeLeftSubtree();
            LinkedBinaryTree rightTree = (LinkedBinaryTree) tree.removeRightSubtree();

            double leftVal = evaluateHelper(leftTree);
            double rightVal = evaluateHelper(rightTree);

            double result = 0;
            switch (value) {
                case "+":
                    result = leftVal + rightVal;
                    break;
                case "-":
                    result = leftVal - rightVal;
                    break;
                case "*":
                    result = leftVal * rightVal;
                    break;
                case "/":
                    if (rightVal != 0) {
                        result = leftVal / rightVal;
                    } else {
                        throw new ArithmeticException("Тоог 0-д хуваах боломжгүй!");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Буруу оператор: " + value);
            }

            // Дэд модыг дахин холбох
            tree.makeTree(rootElement, leftTree, rightTree);

            return result;
        }

        return 0;
    }

    // Хувьсагчийн утга авах
    private double getVariableValue(String variableName) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(variableName + " хувьсагчийн утгыг оруулна уу: ");
        double value = scanner.nextDouble();
        return value;
    }

    // Хаалтуудаар бүрэн хэлбэржсэн infix хэлбэрийг хэвлэх
    public void printFullyParenthesizedInfix() {
        System.out.print("Бүрэн хаалттай Infix: ");
        printFullyParenthesizedHelper(this);
        System.out.println();
    }

    private void printFullyParenthesizedHelper(LinkedBinaryTree tree) {
        if (tree == null || tree.isEmpty()) {
            return;
        }

        Object rootElement = tree.root();
        if (rootElement == null) {
            return;
        }

        String value = rootElement.toString();
        boolean isOp = isOperator(value);

        // Дэд модыг салгах
        LinkedBinaryTree leftTree = (LinkedBinaryTree) tree.removeLeftSubtree();
        LinkedBinaryTree rightTree = (LinkedBinaryTree) tree.removeRightSubtree();

        if (isOp) {
            System.out.print("(");
        }

        if (!leftTree.isEmpty()) {
            printFullyParenthesizedHelper(leftTree);
        }

        System.out.print(rootElement + " ");

        if (!rightTree.isEmpty()) {
            printFullyParenthesizedHelper(rightTree);
        }

        if (isOp) {
            System.out.print(")");
        }

        // Дэд модыг дахин холбох
        tree.makeTree(rootElement, leftTree, rightTree);
    }

    // Туслах функцүүд
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/");
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isVariable(String token) {
        // Ганц үсэг эсвэл үсгээс эхэлсэн нэр
        return token.matches("[a-zA-Z][a-zA-Z0-9]*") && !isOperator(token);
    }

    // Туршилтын үндсэн функц
    public static void main(String[] args) {
        System.out.println("=== Expression Tree Test ===");

        try {
            // Test 1: Prefix
            System.out.println("\n1. Testing prefix expression: * + 3 4 5");
            Expression exp1 = new Expression();
            exp1.buildFromPrefix("* + 3 4 5");
            exp1.printFullyParenthesizedInfix();
            exp1.printInfix();
            exp1.printPrefix();
            exp1.printPostfix();
            System.out.println("Утга: " + exp1.evaluate());

            System.out.println("\n" + "=".repeat(50));

            // Test 2: Postfix
            System.out.println("\n2. Testing postfix expression: 3 4 + 5 *");
            Expression exp2 = new Expression();
            exp2.buildFromPostfix("3 4 + 5 *");
            exp2.printFullyParenthesizedInfix();
            exp2.printInfix();
            exp2.printPrefix();
            exp2.printPostfix();
            System.out.println("Утга: " + exp2.evaluate());

            System.out.println("\n" + "=".repeat(50));

            // Test 3: Хаалттай infix
            System.out.println("\n3. Testing parenthesized infix: (a + b) * c");
            Expression exp3 = new Expression();
            exp3.buildFromInfix("(a + b) * c");
            exp3.printFullyParenthesizedInfix();
            exp3.printInfix();
            exp3.printPrefix();
            exp3.printPostfix();
            System.out.println("\nУтгыг бодох (тоон утга оруулна уу):");
            double result3 = exp3.evaluate();
            System.out.println("Үр дүн: " + result3);

            System.out.println("\n" + "=".repeat(50));

            // Test 4: Хаалтгүй infix
            System.out.println("\n4. Testing infix without parentheses: a + b * c");
            Expression exp4 = new Expression();
            exp4.buildFromInfix("a + b * c");
            exp4.printFullyParenthesizedInfix();
            exp4.printInfix();
            exp4.printPrefix();
            exp4.printPostfix();
            System.out.println("\nУтгыг бодох (тоон утга оруулна уу):");
            double result4 = exp4.evaluate();
            System.out.println("Үр дүн: " + result4);

            System.out.println("\n" + "=".repeat(50));

            // Test 5: Нарийн төвөгтэй илэрхийлэл
            System.out.println("\n5. Testing complex expression: (x + y) * (a - b) / 2");
            Expression exp5 = new Expression();
            exp5.buildFromInfix("(x + y) * (a - b) / 2");
            exp5.printFullyParenthesizedInfix();
            exp5.printInfix();
            exp5.printPrefix();
            exp5.printPostfix();
            System.out.println("\nУтгыг бодох (тоон утга оруулна уу):");
            double result5 = exp5.evaluate();
            System.out.println("Үр дүн: " + result5);

        } catch (Exception e) {
            System.out.println("Алдаа гарлаа: " + e.getMessage());
            e.printStackTrace();
        }
    }
}