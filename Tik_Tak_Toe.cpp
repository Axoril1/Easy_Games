#include <iostream>
#include <cstdlib>
using namespace std;

char grid[10] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

int checkWinner()
{
    const int winningPatterns[8][3] = {
        {1, 2, 3}, {4, 5, 6}, {7, 8, 9}, // Rows
        {1, 4, 7},
        {2, 5, 8},
        {3, 6, 9}, // Columns
        {1, 5, 9},
        {3, 5, 7} // Diagonals
    };

    for (auto &pattern : winningPatterns)
    {
        if (grid[pattern[0]] == grid[pattern[1]] && grid[pattern[0]] == grid[pattern[2]])
        {
            return 1;
        }
    }

    for (int i = 1; i <= 9; i++)
    {
        if (grid[i] == '0' + i)
            return -1; // Game still ongoing
    }

    return 0; // Draw
}

void displayBoard()
{
    system("cls");
    cout << "\n\n\t\t\tTIC-TAC-TOE\n";
    cout << "\t\t**************************\n";
    cout << "\t\tPlayer 1 (X)  -  Player 2 (O)\n\n";

    for (int i = 1; i <= 9; i += 3)
    {
        cout << "\t\t     |     |     \n";
        cout << "\t\t  " << grid[i] << "  |  " << grid[i + 1] << "  |  " << grid[i + 2] << "  \n";
        if (i < 7)
            cout << "\t\t_____|_____|_____\n";
    }
    cout << "\t\t     |     |     \n\n";
}
int main()
{
    int player = 1, result, choice;
    char mark;
    do
    {
        displayBoard();
        cout << "Player " << player << ", enter your choice: ";
        cin >> choice;
        mark = (player == 1) ? 'X' : 'O';
        switch (choice)
        {
        case 1:
            if (grid[1] == '1')
                grid[1] = mark;
            else
                goto invalid;
            break;
        case 2:
            if (grid[2] == '2')
                grid[2] = mark;
            else
                goto invalid;
            break;
        case 3:
            if (grid[3] == '3')
                grid[3] = mark;
            else
                goto invalid;
            break;
        case 4:
            if (grid[4] == '4')
                grid[4] = mark;
            else
                goto invalid;
            break;
        case 5:
            if (grid[5] == '5')
                grid[5] = mark;
            else
                goto invalid;
            break;
        case 6:
            if (grid[6] == '6')
                grid[6] = mark;
            else
                goto invalid;
            break;
        case 7:
            if (grid[7] == '7')
                grid[7] = mark;
            else
                goto invalid;
            break;
        case 8:
            if (grid[8] == '8')
                grid[8] = mark;
            else
                goto invalid;
            break;
        case 9:
            if (grid[9] == '9')
                grid[9] = mark;
            else
                goto invalid;
            break;
        default:
        invalid:
            cout << "INVALID MOVE! Try again.\n";
            cin.ignore();
            cin.get();
            continue;
        }
        result = checkWinner();
        player = (player == 1) ? 2 : 1;
    } while (result == -1);
    displayBoard();
    if (result == 1)
    {
        cout << "\aCONGRATULATIONS! Player " << ((player == 1) ? 2 : 1) << " wins!\n";
    }
    else
    {
        cout << "\aIT'S A DRAW!\n";
    }
    cin.ignore();
    cin.get();
    return 0;
}
