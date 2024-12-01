package com.cc102.budgetit.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.cc102.budgetit.Expense;
import com.cc102.budgetit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class EditExpenseDialogFragment extends DialogFragment {

    private EditText etExpenseName, etExpenseAmount, etExpenseCategory, etExpenseDate, etExpenseDescription;
    private Button btnSaveExpense;
    private Expense expense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_expense_dialog, container, false);

        // Retrieve the Expense object passed from the adapter (Parcelable)
        expense = getArguments().getParcelable("expense");

        etExpenseName = view.findViewById(R.id.etExpenseName);
        etExpenseAmount = view.findViewById(R.id.etExpenseAmount);
        etExpenseCategory = view.findViewById(R.id.etExpenseCategory);
        etExpenseDate = view.findViewById(R.id.etExpenseDate);
        etExpenseDescription = view.findViewById(R.id.etExpenseDescription);
        btnSaveExpense = view.findViewById(R.id.btnSaveExpense);

        // Populate the fields with current expense values
        if (expense != null) {
            etExpenseName.setText(expense.getName());
            etExpenseAmount.setText(String.valueOf(expense.getAmount()));
            etExpenseCategory.setText(expense.getCategory());
            etExpenseDate.setText(expense.getDate());
            etExpenseDescription.setText(expense.getDescription());
        }

        btnSaveExpense.setOnClickListener(v -> {
            // Get updated values from EditText fields
            String updatedName = etExpenseName.getText().toString();
            String updatedAmountStr = etExpenseAmount.getText().toString();
            String updatedCategory = etExpenseCategory.getText().toString();
            String updatedDate = etExpenseDate.getText().toString();
            String updatedDescription = etExpenseDescription.getText().toString();

            if (updatedName.isEmpty() || updatedAmountStr.isEmpty() || updatedCategory.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double updatedAmount;
            try {
                updatedAmount = Double.parseDouble(updatedAmountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the expense object
            expense.setName(updatedName);
            expense.setAmount(updatedAmount);
            expense.setCategory(updatedCategory);
            expense.setDate(updatedDate);
            expense.setDescription(updatedDescription);

            // Get reference to the specific expense node in Firebase
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference expenseRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("expenses")
                    .child(expense.getId());

            // Update the expense in Firebase
            expenseRef.setValue(expense)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Expense updated", Toast.LENGTH_SHORT).show();
                        dismiss();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update expense", Toast.LENGTH_SHORT).show());
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}

