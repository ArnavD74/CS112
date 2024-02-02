package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList < CharFreq > sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) {
        fileName = f;
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        sortedCharFreqList = new ArrayList < CharFreq > ();

        int[] occurances = new int[128];
        int charcount = 0;

        while (StdIn.hasNextChar()) {
            char temp = StdIn.readChar();
            occurances[temp]++;
            charcount++;
        }

        for (char i = (char) 0; i < occurances.length; i++) {
            if (occurances[i] != 0) {
                CharFreq actualFreq = new CharFreq(i, (double) occurances[i] / charcount);
                sortedCharFreqList.add(actualFreq);
            }
        }

        if (sortedCharFreqList.size() == 1) {
            CharFreq fix = new CharFreq((char)((sortedCharFreqList.get(0).getCharacter() + 1) % 128), 0);
            sortedCharFreqList.add(fix);
        }

        Collections.sort(sortedCharFreqList);
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

        Queue < TreeNode > tree = new Queue < > ();
        Queue < TreeNode > finalTree = new Queue < TreeNode > ();

        for (CharFreq CharFreq: sortedCharFreqList) {
            tree.enqueue(new TreeNode(CharFreq, null, null));
        }

        TreeNode left;
        TreeNode right;
        while (!tree.isEmpty()) {

            TreeNode temp1L = null;
            TreeNode temp1R = null;
            if (!tree.isEmpty()) {
                temp1L = tree.peek();
            }
            if (!finalTree.isEmpty()) {
                temp1R = finalTree.peek();
            }
            TreeNode temp;
            if (temp1L != null && temp1R != null) {
                if (temp1L.getData().getProbOcc() <= temp1R.getData().getProbOcc()) {
                    temp = tree.dequeue();
                } else {
                    temp = finalTree.dequeue();
                }
            } else if (temp1L != null) {
                temp = tree.dequeue();
            } else if (temp1R != null) {
                temp = finalTree.dequeue();
            } else {
                temp = null;
            }

            left = temp;

            TreeNode temp2L = null;
            TreeNode temp2R = null;
            if (!tree.isEmpty()) {
                temp2L = tree.peek();
            }
            if (!finalTree.isEmpty()) {
                temp2R = finalTree.peek();
            }
            TreeNode temp2;

            if (temp2L != null && temp2R != null) {
                if (temp2L.getData().getProbOcc() <= temp2R.getData().getProbOcc()) {
                    temp2 = tree.dequeue();
                } else {
                    temp2 = finalTree.dequeue();
                }
            } else if (temp2L != null) {
                temp2 = tree.dequeue();
            } else if (temp2R != null) {
                temp2 = finalTree.dequeue();
            } else {
                temp2 = null;
            }
            right = temp2;

            if (left != null && right != null) {
                double combined_freq = left.getData().getProbOcc() + right.getData().getProbOcc();
                CharFreq combined_CharFreq_node = new CharFreq(null, combined_freq);
                TreeNode temp_combined = new TreeNode(combined_CharFreq_node, left, right);
                TreeNode combined_node = temp_combined;
                finalTree.enqueue(combined_node);
            } else if (left != null)
                finalTree.enqueue(left);
            else {
                finalTree.enqueue(right);
            }

        }
        while (finalTree.size() > 1) {
            left = finalTree.dequeue();
            right = finalTree.dequeue();
            double combined_freq = left.getData().getProbOcc() + right.getData().getProbOcc();
            CharFreq combined_CharFreq_node = new CharFreq(null, combined_freq);
            TreeNode temp_combined = new TreeNode(combined_CharFreq_node, left, right);
            TreeNode combined_node = temp_combined;

            finalTree.enqueue(combined_node);
        }

        huffmanRoot = finalTree.dequeue();

    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    private void traversal(TreeNode node, String[] codes, ArrayList < String > data) {
        if (node.getData().getCharacter() != null) {
            codes[node.getData().getCharacter()] = String.join("", data);
            data.remove(data.size() - 1);
            return;
        }

        if (node.getLeft() != null) {
            data.add("0");
        }

        traversal(node.getLeft(), codes, data);
        if (node.getRight() != null) {
            data.add("1");
        }

        traversal(node.getRight(), codes, data);
        if (!data.isEmpty()) {
            data.remove(data.size() - 1);
        }
    }

    public void makeEncodings() {

        String[] codes = new String[128];
        ArrayList < String > data = new ArrayList < > ();
        traversal(huffmanRoot, codes, data);
        encodings = codes;
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

        // for(int i = 0; i < encodings.length; i++) {
        //     writeBitString(encodedFile, encodings[i]);
        // }

        String data = "";
        while (StdIn.hasNextChar()) {
            data += encodings[(int) StdIn.readChar()];
        }
        writeBitString(encodedFile, data);

    }

    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding - 1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c: bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7 - byteIndex);
            byteIndex++;

            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }

        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        } catch (Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

        String data = readBitString(encodedFile);
        TreeNode level = huffmanRoot;
        String answer = "";

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '0') {
                level = level.getLeft();
            } else {
                level = level.getRight();
            }

            if (level.getData().getCharacter() != null) {
                answer += level.getData().getCharacter();
                level = huffmanRoot;
            }
        }

        StdOut.print(answer);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";

        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()]; in .read(bytes); in .close();

            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b: bytes) {
                bitString = bitString +
                    String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i + 1);
            }

            return bitString.substring(8);
        } catch (Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() {
        return fileName;
    }

    public ArrayList < CharFreq > getSortedCharFreqList() {
        return sortedCharFreqList;
    }

    public TreeNode getHuffmanRoot() {
        return huffmanRoot;
    }

    public String[] getEncodings() {
        return encodings;
    }
}