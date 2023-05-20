package com.example.light_bulb_game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private static final int GRID_SIZE = 4;
    private static final int STATE_OFF = 0;
    private static final int STATE_ON = 1;

    private int[][] gridState;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(GRID_SIZE);
        gridLayout.setRowCount(GRID_SIZE);

        initializeGrid();
        drawGrid();
    }

    private void initializeGrid() {
        gridState = new int[GRID_SIZE][GRID_SIZE];
        Random random = new Random();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int initialState = random.nextInt(2);
                gridState[i][j] = initialState;
            }
        }
    }

    private void drawGrid() {
        gridLayout.removeAllViews();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                View view = new View(this);

                if (gridState[i][j] == STATE_ON) {
                    view.setBackgroundColor(Color.YELLOW);
                } else {
                    view.setBackgroundColor(Color.WHITE);
                }

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = 260;
                layoutParams.height = 260;
                layoutParams.columnSpec = GridLayout.spec(j);
                layoutParams.rowSpec = GridLayout.spec(i);
                view.setLayoutParams(layoutParams);

                final int row = i;
                final int col = j;

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleLights(row, col);
                        checkGameEnd();
                    }
                });

                gridLayout.addView(view);
            }
        }
    }

    private void toggleLights(int row, int col) {
        toggleLight(row, col);
        toggleLight(row - 1, col);
        toggleLight(row + 1, col);
        toggleLight(row, col - 1);
        toggleLight(row, col + 1);
        drawGrid();
    }

    private void toggleLight(int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            gridState[row][col] = (gridState[row][col] == STATE_ON) ? STATE_OFF : STATE_ON;
        }
    }

    private void checkGameEnd() {
        int firstState = gridState[0][0];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gridState[i][j] != firstState) {
                    return;
                }
            }
        }

        // All lights are the same, game ends
        Toast.makeText(this, "Оказывается не все потеряно:D С победой!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Игра окончена")
                .setMessage("Хочешь еще свое время потратить?")
                .setPositiveButton("Начать заново", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initializeGrid();
                        drawGrid();
                    }
                })
                .setNegativeButton("Выйти (рекомендуется)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }
}