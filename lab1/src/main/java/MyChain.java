import dataStructures.Chain;
import dataStructures.ChainNode;
import java.util.Scanner;

import java.util.*;

public class MyChain extends Chain {

    public MyChain() {
        super();
    }

    public Object[] toArray() {
        Object[] arr = new Object[size];
        ChainNode current = firstNode;
        int i = 0;
        while (current != null) {
            arr[i++] = current.element;
            current = current.next;
        }
        return arr;
    }

    public void addRange(Object[] elements) {
        if (elements == null)
            throw new IllegalArgumentException("Elements array is null");
        for (Object e : elements) {
            // Try to convert to Integer if it's a string containing a number
            if (e instanceof String) {
                try {
                    Integer num = Integer.parseInt((String) e);
                    add(size, num);
                } catch (NumberFormatException ex) {
                    add(size, e);
                }
            } else {
                add(size, e);
            }
        }
    }

    public MyChain union(MyChain other) {
        if (other == null)
            throw new IllegalArgumentException("Other chain is null");

        MyChain result = new MyChain();

        ChainNode current = this.firstNode;
        while (current != null) {
            result.add(result.size, current.element);
            current = current.next;
        }

        ChainNode otherCurrent = other.firstNode;
        while (otherCurrent != null) {
            if (result.indexOf(otherCurrent.element) == -1) {
                result.add(result.size, otherCurrent.element);
            }
            otherCurrent = otherCurrent.next;
        }

        return result;
    }

    public MyChain intersection(MyChain other) {
        if (other == null)
            throw new IllegalArgumentException("Other chain is null");

        MyChain result = new MyChain();
        ChainNode current = this.firstNode;
        while (current != null) {
            if (current.element != null &&
                    other.indexOf(current.element) != -1 &&
                    result.indexOf(current.element) == -1) {
                result.add(result.size, current.element);
            }
            current = current.next;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MyChain list1 = new MyChain();
        MyChain list2 = new MyChain();

        while (true) {
            System.out.println("\n===== MyChain Interactive Menu =====");
            System.out.println("1. Add element to List1");
            System.out.println("2. Add range to List1");
            System.out.println("3. Display List1");
            System.out.println("4. Create List2 and test union & intersection");
            System.out.println("5. Convert List1 to array");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter element: ");
                    String el = sc.nextLine();
                    try {
                        Integer num = Integer.parseInt(el);
                        list1.add(list1.size(), num);
                        System.out.println("Added: " + num);
                    } catch (NumberFormatException e) {
                        list1.add(list1.size(), el);
                        System.out.println("Added: " + el);
                    }
                }
                case 2 -> {
                    System.out.print("Enter elements separated by spaces: ");
                    String[] arr = sc.nextLine().split("\\s+");
                    list1.addRange(arr);
                    System.out.println("Added " + arr.length + " elements.");
                }
                case 3 -> {
                    System.out.println("List1: " + list1);
                }
                case 4 -> {
                    System.out.print("Enter elements for List2 separated by spaces: ");
                    String[] arr2 = sc.nextLine().split("\\s+");
                    list2 = new MyChain();
                    list2.addRange(arr2);
                    System.out.println("List1: " + list1);
                    System.out.println("List2: " + list2);
                    System.out.println("Union: " + list1.union(list2));
                    System.out.println("Intersection: " + list1.intersection(list2));
                }
                case 5 -> {
                    System.out.println("List1 as array: " + Arrays.toString(list1.toArray()));
                }
                case 0 -> {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

}
