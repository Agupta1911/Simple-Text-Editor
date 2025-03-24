//A text editor in Java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;


public class TextEditor {

    private StringBuilder content;
    private Stack<EditAction> undoStack;
    private Stack<EditAction> redoStack;
    private int cursor;
    private String copyAndPaste;

    public TextEditor() {
        content = new StringBuilder();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        copyAndPaste = "";
        cursor = 0;
    }

    //Insert text
    public void insert(String text) {
        content.insert(cursor, text);
        undoStack.push(new EditAction("insert", cursor, text));
        redoStack.clear();
    }

    //delete text
    public void delete(int length) {
        if (cursor+length<=content.length()) {
            String deletedText = content.substring(cursor, cursor + length);
            content.delete(cursor, cursor + length);
            undoStack.push(new EditAction("delete", cursor, deletedText));
            redoStack.clear();
        }
    }

    //move the cursor
    public void moveCursor(int position) {
        if (position >= 0 && position <= content.length()) {
            cursor = position;
        }
    }

    //returns the content of the text buffer as a string
    public String getContent() {
        return content.toString();
    }

    //returns the current cursor position
    public int getCursorPosition() {
        return cursor;
    }

    // undos the latest action
    public void undo() {
        if (!undoStack.isEmpty()) {
            EditAction latestAction = undoStack.pop();
            redoStack.push(latestAction);
            if (latestAction.getAction().equals("insert")) {
                content.delete(latestAction.getPosition(), latestAction.getPosition() + latestAction.getText().length());
                cursor = latestAction.getPosition();
            } else if (latestAction.getAction().equals("delete")) {
                content.insert(latestAction.getPosition(), latestAction.getText());
                cursor = latestAction.getPosition();
            }
        }
    }

    public void redo() {
    if (!redoStack.isEmpty()) {
        EditAction undoneAction = redoStack.pop();
        undoStack.push(undoneAction);

        if (undoneAction.getAction().equals("insert")) {
            content.insert(undoneAction.getPosition(), undoneAction.getText());
            cursor = undoneAction.getPosition();
        } else if (undoneAction.getAction().equals("delete")) {
            content.delete(undoneAction.getPosition(), undoneAction.getPosition() + undoneAction.getText().length()); // Directly delete without modifying undoStack
            cursor = undoneAction.getPosition();
        }
    }
}

    //Opens a file
    public void openFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder fileContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            fileContent.append(line).append("\n");
        }
        reader.close();
        content.append(fileContent.toString());
        undoStack.clear();
        redoStack.clear();
    }

    //Saves current content to a file named results
    public void saveFile(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content.toString());
        writer.close();
    }

    // Saves the content to a new file
    public void saveFileAs(String filePath) throws IOException {
        saveFile(filePath);
    }

    // Copy selected text
    public void copy(int start, int end) {
        if (start >= 0 && end+1 <= content.length() && start < end) {
            copyAndPaste = content.substring(start, end+1);
        }
    }

    // Pastes copied text
    public void paste(int position) {
        if (!copyAndPaste.isEmpty() && position >= 0 && position <= content.length()) {
            cursor = position;
            content.insert(cursor, copyAndPaste);
        }
    }

    // Searchs for a string
    public int[] searchText(String searchTerm) {
        int index = content.indexOf(searchTerm);
        Stack<Integer> positions = new Stack<>();
        while (index != -1) {
            positions.push(index);
            index = content.indexOf(searchTerm, index + searchTerm.length());
        }
        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
        result[i] = positions.get(i);
        }
    
        return result;
    }

    // Replaces all terms with another
    public void replaceText(String searchTerm, String replacement) {
        int[] positions = searchText(searchTerm);
        for (int i = positions.length - 1; i >= 0; i--) {
            content.replace(positions[i], positions[i] + searchTerm.length(), replacement);
        }
        undoStack.clear();
        redoStack.clear();
    }

    public static void main(String[] args) throws Exception {
        TextEditor editor = new TextEditor();
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        boolean flag = false;
        while (!flag) {
            System.out.println("Position of cursor: " + editor.getCursorPosition());
            System.out.println("Content of Editor: " + editor.getContent());
            System.out.println("Please choose a command: insert, delete, move, undo, redo, open, save, save as, search, replace, copy, paste, exit");
            System.out.println("Please type 1 to insert text");
            System.out.println("Please type 2 to delete text");
            System.out.println("Please type 3 to move cursor");
            System.out.println("Please type 4 to undo action");
            System.out.println("Please type 5 to redo action");
            System.out.println("Please type 6 to search");
            System.out.println("Please type 7 to replace");
            System.out.println("Please type 8 to open and read in a file");
            System.out.println("Please type 9 to save file (to results.txt)");
            System.out.println("Please type 10 to save file as to a new file");
            System.out.println("Please type 11 to copy text");
            System.out.println("Please type 12 to paste copied text");
            System.out.println("Please type 13 to exit");

            int command = Integer.parseInt(scanner.nextLine());


            switch (command) {
                case 1:
                    System.out.println("Please enter the text to insert: ");
                    String text = scanner.nextLine();
                    editor.insert(text);
                    break;

                case 2:
                    System.out.println("Please enter the specified number of characters to delete: ");
                    int length = Integer.parseInt(scanner.nextLine());
                    editor.delete(length);
                    break;

                case 3:
                    System.out.println("Please enter the new cursor position: ");
                    int position = Integer.parseInt(scanner.nextLine());
                    editor.moveCursor(position);
                    break;

                case 4:
                    editor.undo();
                    break;

                case 5:
                    editor.redo();
                    break;

                case 6:
                    System.out.println("Please enter the term to search: ");
                    String searchTerm = scanner.nextLine();
                    int[] positions = editor.searchText(searchTerm);
                    System.out.println("Term found at positions: " + java.util.Arrays.toString(positions));
                    break;

                case 7:
                    System.out.println("Please term to replace: ");
                    String term = scanner.nextLine();
                    System.out.println("Please enter the replacement text: ");
                    String replacement = scanner.nextLine();
                    editor.replaceText(term, replacement);
                    break;

                case 8:
                    System.out.println("Please enter filepath to open: ");
                    String openPath = scanner.nextLine();
                    editor.openFile(openPath);
                    break;

                case 9:
                    editor.saveFile("results.txt");
                    break;
                
                case 10:
                    System.out.println("Please enter the file path to save as to: ");
                    String savePathAs = scanner.nextLine();
                    editor.saveFile(savePathAs);
                    break;

                case 11:
                    System.out.println("Please enter start and end positions to copy: ");
                    System.out.println("Please enter start: ");
                    int start = Integer.parseInt(scanner.nextLine());
                    System.out.println("Please enter end: ");
                    int end = Integer.parseInt(scanner.nextLine());
                    editor.copy(start, end);
                    break;

                case 12:
                    System.out.println("Please enter position to paste: ");
                    int pastePosition = Integer.parseInt(scanner.nextLine());
                    editor.paste(pastePosition);
                    break;

                case 13:
                    flag = true;
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid input given.");
                    break;
            }
        }
    }
}

//stores details for each insert and delete action
class EditAction {
    private String action;
    private int position;
    private String text;

    public EditAction(String type, int position, String text) {
        this.action = type;
        this.position = position;
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }
}

