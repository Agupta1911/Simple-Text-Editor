# Simple-Text-Editor
A Java program that simulates a text editor in Java.


Key Features:

Insert text at the current cursor position

Delete text (a specified number of characters) at the current cursor position

Move the cursor to a specified position

Use a StringBuilder to store and manipulate the text content

Implement a simple command-line interface to interact with the text editor

Include undo and redo functionality.

Implemented Methods which are used:

insert(String text)

delete(int length)

moveCursor(int position)

getContent() - Get the contents of the text buffer (String)

getCursorPosition()

undo()

redo()

Approach used:

Perform the operations using an Edit Action class.

An EditAction class that contains the following attributes:

The type of action (insert or delete)

The position where the action occurred

The text that was inserted or deleted

Data Structures:

Stack<EditAction> for undo stack

Stack<EditAction> for redo stack

Add text search functionality:

Search for occurrences of a given text string

Return a list of positions where the text was found

Implement a replace function:

Replace all occurrences of a search term with a given replacement text

Methods:

searchText(String searchTerm)

replaceText(String searchTerm, String replacement)

Implement file I/O operations:

Open a file and load its content into the editor

Save the current content to a file

Save the current content to a new file (Save As)

Copy and paste operations:

Allow the user to copy from one part of the text buffer to another part 

Save the current content to a file

Methods:

openFile(String filePath)

saveFile()

saveFileAs(String filePath)

copy(int start, int end)

paste(int position)


Instructions:

Follow prompts for user inputs.

The save function automatically saves it to a file called results.txt, whereas the saveas function allows you to save it to a new file.
