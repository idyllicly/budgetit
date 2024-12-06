package com.cc102.budgetit.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.cc102.budgetit.Expense;
import com.cc102.budgetit.R;
import com.cc102.budgetit.ExpenseAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseOverviewFragment extends Fragment {

    private ExpenseAdapter expenseAdapter;
    private final List<Expense> expenseList = new ArrayList<>();
    private DatabaseReference expensesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_expense_overview, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.rvExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter
        expenseAdapter = new ExpenseAdapter(expenseList);
        recyclerView.setAdapter(expenseAdapter);

        // Set up Firebase reference
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses");

        // Fetch expenses from Firebase
        fetchExpensesFromFirebase();

        return rootView;
    }

    private void fetchExpensesFromFirebase() {
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expenseList.clear();  // Clear the current list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }
                // Notify the adapter to update the RecyclerView
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load expenses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
