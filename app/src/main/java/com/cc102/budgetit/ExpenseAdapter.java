package com.cc102.budgetit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.cc102.budgetit.R;
import com.cc102.budgetit.Expense;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    // Modify constructor to accept a list of expenses
    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.expenseName.setText(expense.getName());
        holder.expenseAmount.setText(String.valueOf(expense.getAmount()));
        holder.expenseCategory.setText(expense.getCategory());
        holder.expenseDescription.setText(expense.getDescription());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount, expenseCategory, expenseDescription;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.tvExpenseName);
            expenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            expenseCategory = itemView.findViewById(R.id.tvExpenseCategory);
            expenseDescription = itemView.findViewById(R.id.tvExpenseDescription);
        }
    }
}
