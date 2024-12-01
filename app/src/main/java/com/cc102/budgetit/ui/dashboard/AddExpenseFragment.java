package com.cc102.budgetit.ui.dashboard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cc102.budgetit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddExpenseFragment extends Fragment {

    private EditText etExpenseName, etExpenseAmount, etCategory, etDate, etDescription;
    private Button btnSaveExpense;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_expense, container, false);

        // Initialize UI elements
        etExpenseName = root.findViewById(R.id.etExpenseName);
        etExpenseAmount = root.findViewById(R.id.etExpenseAmount);
        etCategory = root.findViewById(R.id.etCategory);
        etDate = root.findViewById(R.id.etDate);
        etDescription = root.findViewById(R.id.etDescription);
        btnSaveExpense = root.findViewById(R.id.btnSaveExpense);

        // Initialize Firebase references
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("expenses");

        // Set up DatePicker on Date EditText
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Save Expense Button Click Listener
        btnSaveExpense.setOnClickListener(view -> saveExpense());

        return root;
    }

    private void saveExpense() {
        String expenseName = etExpenseName.getText().toString().trim();
        String expenseAmount = etExpenseAmount.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(expenseName)) {
            etExpenseName.setError("Expense name is required!");
            return;
        }
        if (TextUtils.isEmpty(expenseAmount)) {
            etExpenseAmount.setError("Amount is required!");
            return;
        }
        if (TextUtils.isEmpty(category)) {
            etCategory.setError("Category is required!");
            return;
        }
        if (TextUtils.isEmpty(date)) {
            etDate.setError("Date is required!");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Description is required!");
            return;
        }

        try {
            double amount = Double.parseDouble(expenseAmount);

            // Create a unique key for the expense
            String expenseId = databaseReference.push().getKey();

            // Store data in Firebase
            Map<String, Object> expenseData = new HashMap<>();
            expenseData.put("id", expenseId);
            expenseData.put("name", expenseName);
            expenseData.put("amount", amount);
            expenseData.put("category", category);
            expenseData.put("date", date);
            expenseData.put("description", description);

            assert expenseId != null;
            databaseReference.child(expenseId).setValue(expenseData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Expense saved!", Toast.LENGTH_SHORT).show();
                        etExpenseName.setText("");
                        etExpenseAmount.setText("");
                        etCategory.setText("");
                        etDate.setText("");
                        etDescription.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } catch (NumberFormatException e) {
            etExpenseAmount.setError("Enter a valid number!");
        }
    }

    // Method to show the Date Picker dialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            // Format the date as YYYY-MM-DD
            String date = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            etDate.setText(date); // Set the selected date into the EditText
        }, year, month, day);

        datePickerDialog.show();
    }
}
