package tictactoe;

public class Board {
    private String[] board = new String[9];

    Board() {
        board = new String[9];
    }

    public Board(String s) {
        for (int i = 0; i < 9; i++) {
            board[i] = String.valueOf(s.charAt(i));
            System.out.print(board[i]);
        }
    }

    public int checkWin() {
        int win = -1;
        System.out.print("\n");
        for (int a = 0; a < 8; a++) {
            String line = "";

            switch (a) {
                case 0:
                    line = board[0] + board[1] + board[2];
                    break;
                case 1:
                    line = board[3] + board[4] + board[5];
                    break;
                case 2:
                    line = board[6] + board[7] + board[8];
                    break;
                case 3:
                    line = board[0] + board[3] + board[6];
                    break;
                case 4:
                    line = board[1] + board[4] + board[7];
                    break;
                case 5:
                    line = board[2] + board[5] + board[8];
                    break;
                case 6:
                    line = board[0] + board[4] + board[8];
                    break;
                case 7:
                    line = board[2] + board[4] + board[6];
                    break;
            }
            // For X winner
            if (line.equals("XXX")) {
                win = 1;
            }

            // For O winner
            else if (line.equals("OOO")) {
                win = 2;
            }
        }
        if (win == -1) {
            for (int a = 0; a < 9; a++) {
                if (board[a].equals("-")) {
                    break;
                } else if (a == 8) {
                    win = 0;
                }
            }
        }
        return win;
    }

}
