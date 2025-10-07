![Java CI](https://github.com/KeiraHancock/MTSRS/actions/workflows/java-ci.yml/badge.svg)


# Movie Theater Seat Reservation System (Java 2D Arrays)

This program simulates a movie theater seating system using **2D arrays** in Java.  
Each seat is represented as an element in a two-dimensional array (`char[][]`), where `'O'` indicates an **available seat** and `'X'` indicates a **reserved seat**.

---

## Features

~ Display the seating chart  
~ Reserve a specific seat (e.g., `B7`)  
~ If a seat is taken, the system suggests the **nearest available seat**  
~ Cancel a reservation to make the seat available again  
~ Always show the **updated seating chart** after each operation  

---

## How It Works

1. **Initialize Theater** – Creates a 2D array with all seats set to `'O'`.  
2. **Display Seating Chart** – Prints rows (`A–E`) and columns (`1–10`) in a grid layout.  
3. **Reserve a Seat** –  
   - User enters a seat code like `B7`.  
   - If it’s available → marked as `'X'`.  
   - If taken → program finds the closest open seat and offers it to the user.  
4. **Cancel a Reservation** – User enters a seat to free it back to `'O'`.  
5. **Auto-Refresh** – The updated chart is displayed after each operation.  

---

## Example Run
```bash
=== Movie Theater Seat Reservation System ===
Current Seating Chart
1 2 3 4 5 6 7 8 9 10
A O O O O O O O O O O
B O O O O O O O O O O
C O O O O O O O O O O
D O O O O O O O O O O
E O O O O O O O O O O
Legend: O = available, X = reserved
Menu:
Display seating chart
Reserve a seat
Cancel a reservation
Exit
Choose an option: 2
Enter seat to reserve (e.g., B7): B7
Reserved B7.
```

---

## How to Run

1. Compile:
   ```bash
   javac Main.java
   ```
2. Run:
3. ```bash
   java Main
   ```
4. Follow the on-screen menu.

## Clean Code Examples
1. Single Responsibility
Each method handles one job:
```bash
private static void reserveSeat(String seatCode, Scanner sc) { ... }
private static void cancelReservation(String seatCode) { ... }
private static void printSeating() { ... }
```Easier to maintain and debug.

2. Validation and Error Handling
Every seat code is validated for correct format and range:
```bash
if (code == null || code.length() < 2) return null;
```
Prevents invalid user input from breaking up the program.

3. Readable, Self-Documenting Names
Variables like ```ROWS```, ```COLS```, and ```seats``` clearly describe their purpose.
Improves clarity without needing excessive comments.

---

## Dependencies
This project uses only core Java (no external libraries).
It can be compiled and run on any standard JDK installation (Java 17 or later recommended).

---

## Challenges Faced
Getting seat parsing (like "```B7```") to correctly map to array coordinates.
Implementing the seat-suggestion logic to find the nearest available seat by distance.
Ensuring the seating chart updates correctly after each action.

---

## Author
Keira Hancock
Keyin College – Software Development - SD13
October 2025
