package de.hsfd.binary_tree;

import de.hsfd.binary_tree.services.RBTree;
import de.hsfd.binary_tree.services.exceptions.TreeException;
import de.hsfd.binary_tree.services.wrapper.CharComparable;
import de.hsfd.binary_tree.services.wrapper.IntComparable;

import java.util.Arrays;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!\nPlease refer yourself to the test files for more information about the implementation.\n");
        System.out.println("Below are implementation's examples for Testat2 Red Black Tree");

        try {
            RBTree rbTreeInt = new RBTree();
            Random rand = new Random(15);
            for (int i = 1; i <= 15; i++) {
                //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
                // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
                int randomValue = rand.nextInt(100);
                try {
                    rbTreeInt.insert(new IntComparable(randomValue));
                } catch (TreeException e) {
                    if(e.getMessage().equals("The value is already in the tree. No Parent will be returned")) {
                        i--;
                        continue;
                    }
                }
                System.out.println(i + ") inserted: " + randomValue);
                rbTreeInt.exportDOT("15_nodes/input_" + i + ".dot");
            }
            System.out.println("File exported in folder 15_nodes!");

            RBTree tree = new RBTree();
            tree.insert(new CharComparable('h'));
            tree.insert(new CharComparable('a'));
            tree.insert(new CharComparable('l'));
            tree.insert(new CharComparable('o'));
            tree.exportDOT("halo.dot");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}