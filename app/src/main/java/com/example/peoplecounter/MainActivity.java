package com.example.peoplecounter;

import android.os.Bundle; // Bundle used to pass data between Activity states
import android.view.View; // Basic building block for UI components
import android.widget.Button; // UI button widget
import android.widget.TextView; // UI text display widget

import androidx.appcompat.app.AppCompatActivity; // Base class for activities using the support library action bar features
import androidx.core.content.ContextCompat; // Helper to access resources, such as colors, safely
import androidx.lifecycle.ViewModelProvider; // Factory class to create ViewModel instances

public class MainActivity extends AppCompatActivity {
    private PeopleViewModel viewModel; // Reference to ViewModel managing UI data
    private TextView totalText;         // TextView showing total people count
    private TextView currentText;       // TextView showing current people count
    private Button plusBtn;             // Button to increment count
    private Button minusBtn;            // Button to decrement count
    private Button resetBtn;            // Button to reset counts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call superclass implementation
        setContentView(R.layout.activity_main); // Set the UI layout for this activity

        // Initialize the ViewModel, tied to this Activity's lifecycle
        viewModel = new ViewModelProvider(this).get(PeopleViewModel.class);

        // Find UI components in the layout by their IDs
        totalText = findViewById(R.id.totalText);
        currentText = findViewById(R.id.currentText);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        resetBtn = findViewById(R.id.resetBtn);

        // Observe totalCount LiveData: update totalText whenever total changes
        viewModel.getTotalCount().observe(this, total -> {
            totalText.setText(getString(R.string.total_people, total)); // Update total count text
        });

        // Observe currentCount LiveData: update currentText and UI state on changes
        viewModel.getCurrentCount().observe(this, current -> {
            int safeCurrent = current != null ? current : 0; // Avoid null by defaulting to 0

            currentText.setText(getString(R.string.current_people, safeCurrent)); // Update current count text

            // Change text color to red if current > 15, else blue
            if (safeCurrent > 15) {
                currentText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                currentText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
            }

            // Hide minus button if current count is 0, otherwise show it
            minusBtn.setVisibility(safeCurrent == 0 ? View.GONE : View.VISIBLE);
        });

        // Set up button click listeners to trigger ViewModel actions

        plusBtn.setOnClickListener(v -> viewModel.increment());  // Increment count on plus button press
        minusBtn.setOnClickListener(v -> viewModel.decrement()); // Decrement count on minus button press
        resetBtn.setOnClickListener(v -> viewModel.reset());     // Reset counts on reset button press
    }
}