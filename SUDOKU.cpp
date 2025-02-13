#include<bits/stdc++.h>
using namespace std;
using namespace std::chrono;
const int size = 9;
int Board[size][size], Solution[size][size];
stack<pair<int, int>> History;
steady_clock::time_point Starttime;

#define Red "\033[31m"
#define Green "\033[32m"
#define Yellow "\033[33m"
#define Blue "\033[34m"
#define Cyan "\033[36m"
#define Reset "\033[0m"
#define Clear_screen "\033[2J\033[H"

// Function to print the board
void Print_board_runi() {
    cout << Clear_screen;
    cout << "\n" << Blue << "                     SUDOKU GAME\n" << Reset;
    cout << "       " << Yellow << " 1   2   3     4   5   6     7   8   9  " << Reset << endl;
    cout << "       " << Yellow << "---------------------------------------" << Reset << endl;

    for (int i = 0; i < size; i++) {
        if (i % 3 == 0 && i != 0)
            cout << "       " << Yellow << "---------------------------------------" << Reset << endl;

        cout << Yellow << "   " << i + 1 << " | " << Reset;
        for (int j = 0; j < size; j++) {
            if (j % 3 == 0 && j != 0) cout << Yellow << "| " << Reset;
            if (Board[i][j] == 0)
                cout << " .  ";
            else if (Board[i][j] == Solution[i][j])
                cout << Green << " " << Board[i][j] << "  " << Reset;
            else
                cout << Cyan << " " << Board[i][j] << "  " << Reset;
        }
        cout << Yellow << "|" << Reset << endl;
    }
    cout << "       " << Yellow << "---------------------------------------" << Reset << endl;
    cout << endl;
}

// Function to validate the input number in the board
bool is_runi_valid(int Grid[size][size], int Row, int Column, int number) {
    for (int i = 0; i < size; i++) {
        if (Grid[Row][i] == number || Grid[i][Column] == number) return false;
    }
    int startingR = (Row / 3) * 3, startingC = (Column / 3) * 3;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (Grid[startingR + i][startingC + j] == number) return false;
        }
    }
    return true;
}

// Function to solve the Sudoku
bool solve_runi(int Grid[size][size], int Row = 0, int Col = 0) {
    if (Row == size) return true;
    if (Col == size) return solve_runi(Grid, Row + 1, 0);
    if (Grid[Row][Col] != 0) return solve_runi(Grid, Row, Col + 1);
    for (int num = 1; num <= size; num++) {
        if (is_runi_valid(Grid, Row, Col, num)) {
            Grid[Row][Col] = num;
            if (solve_runi(Grid, Row, Col + 1)) return true;
            Grid[Row][Col] = 0;
        }
    }
    return false;
}

// Function to generate a random Sudoku board
void runi_get_board(int level) {
    srand(time(0));
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) Board[i][j] = 0;
    }
    for (int i = 0; i < 15; i++) {
        int row = rand() % size, col = rand() % size, num = (rand() % size) + 1;
        if (Board[row][col] == 0 && is_runi_valid(Board, row, col, num)) Board[row][col] = num;
    }
    int tempb[size][size];
    memcpy(tempb, Board, sizeof(Board));
    solve_runi(tempb);
    memcpy(Solution, tempb, sizeof(Solution));
    int ctr = (level == 1) ? 40 : (level == 2) ? 50 : 60;
    for (int i = 0; i < ctr; i++) {
        int row = rand() % size, col = rand() % size;
        Board[row][col] = 0;
    }
}

// Function to check if the Sudoku is solved
bool isitsolved() {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (Board[i][j] == 0) return false;
        }
    }
    return true;
}

// Function to give a hint for the Sudoku
void help_runi() {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (Board[i][j] == 0) {
                Board[i][j] = Solution[i][j];
                cout << "Hint:(" << i + 1 << "," << j + 1 << ")=" << Solution[i][j] << endl;
                return;
            }
        }
    }
}

// Function to undo the last move
void undo() {
    if (!History.empty()) {
        auto lm = History.top();
        History.pop();
        Board[lm.first][lm.second] = 0;
        cout << "Last move undone!" << endl;
    }
    else cout << "NO moves to undo!" << endl;
}

// Function to show the solved Sudoku board
void show_solution() {
    cout << Blue << "                       SOLUTION\n" << Reset;
    cout << "       " << Yellow << " 1   2   3     4   5   6     7   8   9  " << Reset << endl;
    cout << "       " << Yellow << "---------------------------------------" << Reset << endl;

    for (int i = 0; i < size; i++) {
        if (i % 3 == 0 && i != 0)
            cout << "       " << Yellow << "---------------------------------------" << Reset << endl;

        cout << Yellow << "   " << i + 1 << " | " << Reset;
        for (int j = 0; j < size; j++) {
            if (j % 3 == 0 && j != 0) cout << Yellow << "| " << Reset;
            cout << Green << " " << Solution[i][j] << "  " << Reset;
        }
        cout << Yellow << "|" << Reset << endl;
    }
    cout << "       " << Yellow << "---------------------------------------" << Reset << endl;
    cout << endl;
}

// Main game loop
void runi_letsplay() {
    int dif;
    cout << "Choose difficulty :\n1.Easy\n2.Medium\n3.Hard\n";
    cin >> dif;
    runi_get_board(dif);
    Starttime = steady_clock::now();

    while (!isitsolved()) {
        Print_board_runi();
        cout << "Options :\n1.Enter number\n2.Undo move\n3.Hint\n4.Solve\n5.Quit\nChoice: ";
        int ch;
        cin >> ch;
        if (ch == 1) {
            int row, col, num;
            cout << "Enter row(1-9), column(1-9) and number(1-9): ";
            cin >> row >> col >> num;
            row--, col--;
            if (row < 0 || row >= size || col < 0 || col >= size || num < 1 || num > 9 || Board[row][col] != 0) {
                cout << Red << "Invalid move! Try again.\n" << Reset;
                continue;
            }
            if (is_runi_valid(Board, row, col, num)) {
                Board[row][col] = num;
                History.push({row, col});
            } else {
                cout << Red << "Invalid number placement!\n" << Reset;
            }
        }
        else if (ch == 2) undo();
        else if (ch == 3) help_runi();
        else if (ch == 4) {
            show_solution();
            return;
        }
        else if (ch == 5) {
            cout << "Game Over. Exiting...\n";
            return;
        }
    }
    Print_board_runi();
    auto end_time = steady_clock::now();
    auto duration = duration_cast<seconds>(end_time - Starttime).count();
    cout << Green << "Congratulations! You solved the Sudoku in " << duration << " seconds!\n" << Reset;
}

// Main function
int main() {
    cout << "Welcome to the Advanced Sudoku Game!\n";
    runi_letsplay();
    return 0;
}
