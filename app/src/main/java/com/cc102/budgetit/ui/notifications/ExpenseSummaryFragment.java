package com.cc102.budgetit.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc102.budgetit.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cc102.budgetit.databinding.FragmentExpenseSummaryBinding;
import com.cc102.budgetit.ui.notifications.CustomPieChartView;
import java.util.ArrayList;

public class ExpenseSummaryFragment extends Fragment {

    private FragmentExpenseSummaryBinding binding;
    private CustomPieChartView pieChartView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentExpenseSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize PieChartView
        pieChartView = binding.getRoot().findViewById(R.id.customPieChartView);

        // Set the pie chart data
        setPieChartData();

        return root;
    }

    private void setPieChartData() {
        // Sample data for the pie chart
        ArrayList<CustomPieChartView.PieSlice> slices = new ArrayList<>();
        slices.add(new CustomPieChartView.PieSlice(30f, getResources().getColor(android.R.color.holo_red_dark)));
        slices.add(new CustomPieChartView.PieSlice(40f, getResources().getColor(android.R.color.holo_blue_dark)));
        slices.add(new CustomPieChartView.PieSlice(20f, getResources().getColor(android.R.color.holo_green_dark)));
        slices.add(new CustomPieChartView.PieSlice(10f, getResources().getColor(android.R.color.holo_orange_dark)));

        // Set the data for the PieChartView
        pieChartView.setPieChartData(slices);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
