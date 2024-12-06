package com.cc102.budgetit.ui.notifications;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cc102.budgetit.R;
import com.cc102.budgetit.ui.notifications.CustomPieChartView;

public class ExpenseSummaryFragment extends Fragment {

    private CustomPieChartView customPieChartView;

    public ExpenseSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_expense_summary, container, false);

        // Initialize PieChart
        customPieChartView = rootView.findViewById(R.id.customPieChartView);

        // TODO: Update PieChart with data

        return rootView;
    }
}
