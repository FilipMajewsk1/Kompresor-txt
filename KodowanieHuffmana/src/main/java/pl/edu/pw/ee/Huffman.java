package pl.edu.pw.ee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Huffman {

    String TREE_FILE = "\\tree.txt";
    String CODED_FILE = "\\coded";
    String DECODED_FILE = "\\decoded.txt";

    private class Node {

        int items;
        char c;
        Node left;
        Node right;

        Node(int items, char c, Node left, Node right) {
            this.items = items;
            this.c = c;
            this.left = left;
            this.right = right;
        }

        Node(int items, char c) {
            this.items = items;
            this.c = c;
            this.left = null;
            this.right = null;
        }

        Node() {
        }
    }

    private class PreNode {

        int item;
        char c;

        PreNode() {

        }
    }

    public int huffman(String pathToRootDir, boolean compress) throws FileNotFoundException, IOException {

        if (pathToRootDir == null) {
            throw new IllegalArgumentException("path can`t be null!");
        }

        if (compress == true) {

            PreNode[] characters = new PreNode[256];
            for (int j = 0; j < 256; j++) {
                characters[j] = new PreNode();
            }

            File f = new File(pathToRootDir + DECODED_FILE);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            if (f.length() == 0) {
                throw new IllegalArgumentException("File can`t be empty!");
            }
            int c;
            int nElems = 0;

            while ((c = br.read()) != -1) {
                if (c > 127) {
                    throw new IllegalArgumentException("File can`t have char>127!");
                }
                char character = (char) c;
                if (isIn(character, nElems, characters)) {
                    int i = findPos(character, nElems, characters);
                    characters[i].item += 1;
                } else {
                    characters[nElems].c = character;
                    characters[nElems].item = 1;
                    nElems += 1;
                }
            }

            PriorityQueue<Node> queue = new PriorityQueue<>(nElems, new CustomComparator());

            for (int i = 0; i < nElems; i++) {
                Node node;
                node = new Node(characters[i].item, characters[i].c);
                queue.add(node);
            }

            Node root = new Node();
            if (nElems == 1) {
                root = new Node(queue.peek().items, queue.peek().c);
            } else {
                while (queue.size() > 1) {
                    Node a = queue.poll();
                    Node b = queue.poll();
                    Node t = new Node(a.items + b.items, '>', a, b);
                    root = t;
                    queue.add(t);
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToRootDir + TREE_FILE));
            HashMap<Character, String> codes = new HashMap<>();
            if (root.left == null && root.right == null) {
                int charr = (int) root.c;
                writer.write(charr + " " + "0");
                codes.put(root.c, "0");
            } else {
                printCode(root, "", codes, writer);
            }
            writer.close();

            File file = new File(pathToRootDir + DECODED_FILE);
            FileReader filer = new FileReader(file);
            BufferedReader bufr = new BufferedReader(filer);
            String temp = "";
            FileOutputStream wriiter = new FileOutputStream(pathToRootDir + CODED_FILE);
            while ((c = bufr.read()) != -1) {
                char character = (char) c;
                temp += codes.get(character);
            }
            byte[] bytes = stringToByte(temp);
            wriiter.write(bytes);
            wriiter.close();

            int result = bytes.length * 8;
            return result;

        } else {
            HashMap<String, Character> codes = readTree(pathToRootDir);
            String text = fileRead(pathToRootDir);

            int result = decode(pathToRootDir, codes, text);
            return result;
        }
    }

    private class CustomComparator implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {

            return a.items - b.items;
        }
    }

    private boolean isIn(char a, int n, PreNode[] t) {
        for (int i = 0; i < n; i++) {
            if (t[i].c == a) {
                return true;
            }
        }

        return false;
    }

    private int findPos(char a, int n, PreNode[] t) {
        for (int i = 0; i < n; i++) {
            if (t[i].c == a) {
                return i;
            }
        }

        return -1;
    }

    private void printCode(Node root, String s, HashMap<Character, String> codes, BufferedWriter writer) throws IOException {
        if (root.left == null && root.right == null) {
            codes.put(root.c, s);
            writer.write((int) root.c + " " + s + "\n");
            return;
        }
        printCode(root.left, s + "0", codes, writer);
        printCode(root.right, s + "1", codes, writer);
    }

    private byte[] stringToByte(String s) {
        byte remainBits = (byte) ((8 - s.length() % 8) % 8);
        for (byte i = 0; i < remainBits; i++) {
            s += "0";
        }
        int nBytes = s.length() / 8;
        byte[] bytes = new byte[nBytes + 1];
        bytes[0] = remainBits;
        for (int i = 0; i < s.length(); i += 8) {
            bytes[i / 8 + 1] = (byte) (Integer.parseInt(s.substring(i, i + 8), 2) - 128);
        }

        return bytes;
    }

    private HashMap<String, Character> readTree(String path) throws FileNotFoundException {
        HashMap<String, Character> codes = new HashMap<>();
        File file = new File(path + TREE_FILE);
        Scanner input = new Scanner(file);
        int counter = 0;
        int charCounter = 0;
        int valCounter = 0;
        String[] charac = new String[256];
        String[] value = new String[256];
        while (input.hasNext()) {
            if (counter % 2 == 0) {
                charac[charCounter] = input.next();
                charCounter += 1;
            } else {
                value[valCounter] = input.next();
                valCounter += 1;
            }
            counter += 1;
        }
        for (int i = 0; i < charCounter; i++) {
            codes.put(value[i], (char) Integer.parseInt(charac[i]));
        }

        return codes;
    }

    private String fileRead(String filepath) throws IOException {

        Path path = Paths.get(filepath + CODED_FILE);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File is not exists!");
        }
        byte[] bytes = Files.readAllBytes(path);
        int n = bytes.length;
        int remainBits = bytes[0];
        String s = "";
        for (int i = 1; i < n; i++) {
            s += String.format("%8s", Integer.toBinaryString((int) bytes[i] + 128)).replace(' ', '0');
        }

        return s.substring(0, s.length() - 1 - remainBits);
    }

    private int decode(String filepath, HashMap<String, Character> map, String s) throws IOException {
        int n = s.length();
        String text = "";
        String temp = "";
        for (int i = 0; i < n; i++) {
            temp += s.charAt(i);
            if (map.get(temp) != null) {
                text += map.get(temp);
                temp = "";
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath + DECODED_FILE));
        writer.write(text);
        writer.close();

        return text.length();
    }
}
