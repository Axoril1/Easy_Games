#include <bits/stdc++.h>
using namespace std;

// art for Rock, Paper, Scissors
unordered_map<string, string> art = {
    {"rock",     "    _______\n---'   ____)\n      (_____)\n      (_____)\n      (____)\n---.__(___)\n"},
    {"paper",    "    _______\n---'   ____)____\n          ______)\n          _______)\n         _______)\n---.__________)\n"},
    {"scissors", "    _______\n---'   ____)____\n          ______)\n       __________)\n      (____)\n---.__(___)\n"}
};

// function to get random computer choice
string getComputerChoice() {
    vector<string> choices = {"rock", "paper", "scissors"};
    return choices[rand() % 3];
}

// function to determine winner
string getResult(string player, string computer) {
    if (player == computer) return "Draw!";
    if ((player == "rock" && computer == "scissors") ||
        (player == "paper" && computer == "rock") ||
        (player == "scissors" && computer == "paper")) {
        return "You Win!";
    }
    return "You Lose!";
}

// display leaderboard
void displayLeaderboard(int playerScore, int computerScore) {
    cout << "\n============= LEADERBOARD =============\n";
    cout << "You: " << playerScore << " | Computer: " << computerScore << endl;
    cout << "=====================================\n";
}

// main game
void playGame() {
    int rounds;
    cout << "\nWelcome to Rock, Paper, Scissors Game!\n";
    cout << "Enter number of rounds you want to play: ";
    cin >> rounds;

    int playerScore = 0, computerScore = 0;

    for (int i = 1; i <= rounds; i++) {
        cout << "\nRound " << i << " of " << rounds << endl;
        cout << "Choose: rock, paper, or scissors: ";
        string playerChoice;
        cin >> playerChoice;
        transform(playerChoice.begin(), playerChoice.end(), playerChoice.begin(), ::tolower);

        if (playerChoice != "rock" && playerChoice != "paper" && playerChoice != "scissors") {
            cout << "Invalid choice! Please enter rock, paper, or scissors.\n";
            i--;
            // retry the same round
            continue;
        }

        string computerChoice = getComputerChoice();
        cout << "\nYou chose:\n" << art[playerChoice] << endl;
        cout << "Computer chose:\n" << art[computerChoice] << endl;

        string result = getResult(playerChoice, computerChoice);
        cout << result << endl;

        if (result == "You Win!") playerScore++;
        else if (result == "You Lose!") computerScore++;

        displayLeaderboard(playerScore, computerScore);
    }

    // display result
    cout << "\n============= GAME OVER =============\n";
    if (playerScore > computerScore) cout << "Congratulations! You Won the Match!" << endl;
    else if (playerScore < computerScore) cout << "You Lost! Better Luck Next Time!" << endl;
    else cout << "It's a Tie! Play Again?" << endl;
    cout << "=====================================\n";

    // ask for replay
    cout << "Do you want to play again? (yes/no): ";
    string replay;
    cin >> replay;
    transform(replay.begin(), replay.end(), replay.begin(), ::tolower);

    if (replay == "yes") playGame();
}

int main() {
    srand(time(0));
    playGame();
    return 0;
}
