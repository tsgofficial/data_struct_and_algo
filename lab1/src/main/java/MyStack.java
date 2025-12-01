import dataStructures.Stack;
import java.util.*;

public class MyStack implements Stack {
    private Object[] stack;
    private int top;

    public MyStack(int capacity) {
        stack = new Object[capacity];
        top = -1;
    }

    public MyStack() {
        this(10);
    }

    @Override
    public boolean empty() {
        return top == -1;
    }

    @Override
    public Object peek() {
        if (empty())
            throw new RuntimeException("Stack хоосон байна!");
        return stack[top];
    }

    @Override
    public void push(Object theObject) {
        if (top == stack.length - 1)
            expandCapacity();
        stack[++top] = theObject;
    }

    @Override
    public Object pop() {
        if (empty())
            throw new RuntimeException("Stack хоосон байна!");
        Object obj = stack[top];
        stack[top--] = null;
        return obj;
    }

    public int size() {
        return top + 1;
    }

    public void inputStack(Scanner sc) {
        System.out.print("Оруулах элементийн тоо: ");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.print((i + 1) + "-р элемент: ");
            push(sc.next());
        }
    }

    public void printStack() {
        if (empty()) {
            System.out.println("Stack хоосон байна.");
            return;
        }
        System.out.println("Stack-ийн элементүүд:");
        for (int i = top; i >= 0; i--) {
            System.out.println("| " + stack[i] + " |");
        }
        System.out.println("-----");
    }

    public MyStack[] splitStack() {
        if (size() < 2)
            throw new RuntimeException("Хуваахад хангалтгүй элемент байна.");
        int mid = size() / 2;

        MyStack first = new MyStack(mid);
        MyStack second = new MyStack(size() - mid);

        for (int i = 0; i < mid; i++)
            first.push(stack[i]);
        for (int i = top; i >= mid; i--)
            second.push(stack[i]);

        return new MyStack[] { first, second };
    }

    public static MyStack combineStack(MyStack s1, MyStack s2) {
        MyStack result = new MyStack(s1.size() + s2.size());
        for (int i = 0; i <= s1.top; i++)
            result.push(s1.stack[i]);
        for (int i = s2.top; i >= 0; i--)
            result.push(s2.stack[i]);
        return result;
    }

    private void expandCapacity() {
        Object[] newStack = new Object[stack.length * 2];
        System.arraycopy(stack, 0, newStack, 0, stack.length);
        stack = newStack;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<MyStack> stacks = new ArrayList<>();

        while (true) {
            System.out.println("\n=== STACK МЕНЮ ===");
            System.out.println("1. Шинэ stack үүсгэх");
            System.out.println("2. Stack хэвлэх (нэг эсвэл бүгд)");
            System.out.println("3. Stack хуваах");
            System.out.println("4. Хоёр stack нийлүүлэх");
            System.out.println("5. Stack-аас pop хийх");
            System.out.println("6. Stack-ийн оройг харах (peek)");
            System.out.println("7. Stack-ийн хэмжээг харах");
            System.out.println("0. Гарах");
            System.out.print("➡ Сонголтоо оруулна уу: ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> {
                        MyStack newStack = new MyStack();
                        System.out.println("👉 Шинэ стек үүсгэж байна...");
                        newStack.inputStack(sc);
                        stacks.add(newStack);
                        System.out.println("✅ Stack #" + stacks.size() + " амжилттай үүслээ.");
                    }

                    case 2 -> {
                        if (stacks.isEmpty()) {
                            System.out.println("Одоогоор ямар ч стек байхгүй байна.");
                            break;
                        }
                        for (int i = 0; i < stacks.size(); i++) {
                            System.out.println("\n📦 Stack #" + (i + 1) + ":");
                            stacks.get(i).printStack();
                        }
                    }

                    case 3 -> {
                        int idx = chooseStack(sc, stacks);
                        MyStack target = stacks.get(idx);
                        MyStack[] split = target.splitStack();
                        stacks.set(idx, split[0]);
                        stacks.add(split[1]);
                        System.out.println(
                                "✅ Stack #" + (idx + 1) + " хуваагдлаа. Шинэ Stack #" + stacks.size() + " нэмэгдлээ.");
                    }

                    case 4 -> {
                        if (stacks.size() < 2) {
                            System.out.println("Нийлүүлэхэд хангалттай стек алга байна!");
                            break;
                        }

                        System.out.println("Нийлүүлэх 2 стекийн дугаар оруулна уу:");
                        int idx1 = chooseStack(sc, stacks);
                        int idx2 = chooseStack(sc, stacks);

                        if (idx1 == idx2) {
                            System.out.println("⚠️ Ижил стек сонгогдсон байна!");
                            break;
                        }

                        MyStack s1 = stacks.get(idx1);
                        MyStack s2 = stacks.get(idx2);
                        MyStack combined = MyStack.combineStack(s1, s2);

                        int first = Math.min(idx1, idx2);
                        int second = Math.max(idx1, idx2);

                        stacks.remove(second);
                        stacks.remove(first);

                        stacks.add(first, combined);

                        System.out.println("✅ Stack #" + (idx1 + 1) + " ба Stack #" + (idx2 + 1) +
                                " нийлж шинэ Stack #" + (first + 1) + " болж солигдлоо.");
                    }

                    case 5 -> {
                        int idx = chooseStack(sc, stacks);
                        System.out.println("Pop хийв: " + stacks.get(idx).pop());
                    }

                    case 6 -> {
                        int idx = chooseStack(sc, stacks);
                        System.out.println("Peek: " + stacks.get(idx).peek());
                    }

                    case 7 -> {
                        for (int i = 0; i < stacks.size(); i++) {
                            System.out.println("Stack #" + (i + 1) + " → Хэмжээ: " + stacks.get(i).size());
                        }
                    }

                    case 0 -> {
                        System.out.println("Програм дууслаа.");
                        return;
                    }

                    default -> System.out.println("⚠️ Буруу сонголт байна!");
                }

            } catch (Exception e) {
                System.out.println("⚠️ Алдаа: " + e.getMessage());
                if (sc.hasNextLine())
                    sc.nextLine();
            }
        }
    }

    private static int chooseStack(Scanner sc, List<MyStack> stacks) {
        System.out.print("Stack-ийн дугаар (1-" + stacks.size() + "): ");
        int idx = sc.nextInt() - 1;
        if (idx < 0 || idx >= stacks.size())
            throw new RuntimeException("Ийм стек байхгүй!");
        return idx;
    }
}
