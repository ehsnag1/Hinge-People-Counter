package com.example.peoplecounter; // Package name that uniquely identifies your app

import android.app.Application; // Base class for maintaining global application state
import android.content.SharedPreferences; // Used to store small key-value data persistently

import androidx.annotation.NonNull; // Indicates a parameter/return value should never be null
import androidx.lifecycle.AndroidViewModel; // ViewModel subclass with access to Application context
import androidx.lifecycle.LiveData; // Lifecycle-aware data holder that can be observed
import androidx.lifecycle.MutableLiveData; // A LiveData you can modify (mutable)

public class PeopleViewModel extends AndroidViewModel {

    // LiveData to store and observe the current number of people
    private final MutableLiveData<Integer> currentCount = new MutableLiveData<>();

    // LiveData to store and observe the total number of people counted
    private final MutableLiveData<Integer> totalCount = new MutableLiveData<>();

    // SharedPreferences object for persistent local storage
    private final SharedPreferences prefs;

    // Constants used as keys in SharedPreferences
    private static final String PREFS_NAME = "people_counter_prefs"; // Name of the SharedPreferences file
    private static final String CURRENT_KEY = "current"; // Key for current count
    private static final String TOTAL_KEY = "total";     // Key for total count

    // Constructor: initializes the ViewModel and loads saved values
    public PeopleViewModel(@NonNull Application application) {
        super(application); // Pass the application context to the parent ViewModel
        prefs = application.getSharedPreferences(PREFS_NAME, Application.MODE_PRIVATE); // Initialize SharedPreferences

        // Load saved current and total values from SharedPreferences (default to 0 if not found)
        currentCount.setValue(prefs.getInt(CURRENT_KEY, 0));
        totalCount.setValue(prefs.getInt(TOTAL_KEY, 0));
    }

    // Expose currentCount as read-only LiveData to the UI
    public LiveData<Integer> getCurrentCount() {
        return currentCount;
    }

    // Expose totalCount as read-only LiveData to the UI
    public LiveData<Integer> getTotalCount() {
        return totalCount;
    }

    // Increments both the current and total counters
    public void increment() {
        int current = currentCount.getValue() != null ? currentCount.getValue() : 0; // Get current value or 0
        int total = totalCount.getValue() != null ? totalCount.getValue() : 0;       // Get total value or 0
        int newCurrent = current + 1; // Increment current
        int newTotal = total + 1;     // Increment total
        currentCount.setValue(newCurrent); // Update LiveData
        totalCount.setValue(newTotal);     // Update LiveData
        saveCounts(newCurrent, newTotal);  // Save to SharedPreferences
    }

    // Decrements only the current counter (but not below 0)
    public void decrement() {
        int current = currentCount.getValue() != null ? currentCount.getValue() : 0; // Get current value or 0
        int newCurrent = Math.max(0, current - 1); // Decrease current but don't go below 0
        currentCount.setValue(newCurrent); // Update LiveData
        saveCounts(newCurrent, totalCount.getValue() != null ? totalCount.getValue() : 0); // Save new value
    }

    // Resets both current and total counters to 0
    public void reset() {
        currentCount.setValue(0); // Reset LiveData
        totalCount.setValue(0);   // Reset LiveData
        saveCounts(0, 0);         // Save reset values
    }

    // Saves the current and total count values to SharedPreferences
    private void saveCounts(int current, int total) {
        prefs.edit()
                .putInt(CURRENT_KEY, current) // Save current count
                .putInt(TOTAL_KEY, total)     // Save total count
                .apply();                     // Apply changes asynchronously
    }
}
