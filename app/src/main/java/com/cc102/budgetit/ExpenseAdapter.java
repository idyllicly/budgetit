package com.cc102.budgetit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.cc102.budgetit.ui.home.EditExpenseDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expensesList;
    private DatabaseReference expensesRef;
    private Context context;

    public ExpenseAdapter(Context context, List<Expense> expensesList, String userId) {
        this.context = context;
        this.expensesList = expensesList;
        this.expensesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("expenses");
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false); // Use the new expense_item layout with CardView
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expensesList.get(position);

        holder.name.setText(expense.getName());
        holder.amount.setText(String.format("$%.2f", expense.getAmount())); // Format the amount
        holder.category.setText(expense.getCategory());
        holder.date.setText(expense.getDate());
        holder.description.setText(expense.getDescription());

        // Handle delete button click
        holder.deleteButton.setOnClickListener(view -> deleteExpense(expense.getId(), view));

        // Handle edit button click
        holder.editButton.setOnClickListener(v -> {
            // Pass the expense data to the EditExpenseDialogFragment (using Parcelable)
            Bundle bundle = new Bundle();
            bundle.putParcelable("expense", expense); // Pass the expense object as Parcelable
            EditExpenseDialogFragment fragment = new EditExpenseDialogFragment();
            fragment.setArguments(bundle);

            // Show the fragment
            fragment.show(((FragmentActivity) context).getSupportFragmentManager(), "EditExpenseDialogFragment");
        });
    }

    private void deleteExpense(String expenseId, View view) {
        expensesRef.child(expenseId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(view.getContext(), "Expense deleted", Toast.LENGTH_SHORT).show();
                    expensesList.removeIf(expense -> expense.getId().equals(expenseId)); // Remove the deleted item from the list
                    notifyDataSetChanged(); // Notify adapter that data has changed
                })
                .addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to delete expense", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, category, date, description;
        public Button deleteButton;
        public Button editButton;

        public ExpenseViewHolder(View view) {
            super(view);
            // Initialize views
            name = view.findViewById(R.id.tvExpenseName);
            amount = view.findViewById(R.id.tvExpenseAmount);
            category = view.findViewById(R.id.tvExpenseCategory);
            date = view.findViewById(R.id.tvExpenseDate);
            description = view.findViewById(R.id.tvExpenseDescription);
            deleteButton = view.findViewById(R.id.btnDeleteExpense);
            editButton = view.findViewById(R.id.btnEditExpense);
        }
    }
}
