package com.cc102.budgetit.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cc102.budgetit.Expense;
import com.cc102.budgetit.ExpenseAdapter;
import com.cc102.budgetit.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseOverviewFragment extends Fragment {

    private ExpenseAdapter expenseAdapter;
    private List<Expense> expensesList;
    private DatabaseReference expensesRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_expense_overview, container, false);

        // Initialize RecyclerView
        RecyclerView rvExpenses = root.findViewById(R.id.rvExpenses);
        rvExpenses.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("expenses");

        expensesList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(requireContext(), expensesList, userId); // Pass Context correctly
        rvExpenses.setAdapter(expenseAdapter);

        // Fetch data from Firebase
        fetchExpenses();

        return root;
    }

    private void fetchExpenses() {
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expensesList.clear();
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    if (expense != null) {
                        expensesList.add(expense);
                    }
                }
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load expenses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
