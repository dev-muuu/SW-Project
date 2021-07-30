package com.example.sw_project.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sw_project.Activity.ContestScrapActivity;
import com.example.sw_project.Activity.MainActivity;
import com.example.sw_project.Activity.RecruitScrapActivity;
import com.example.sw_project.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class Fragment_Tab_3 extends Fragment {

    View view;
    TextView totalStatisticNumber, totalStatisticsText, collegeStatisticsNumber, collegeStatisticsText;
    PieChart pieChartTotal, pieChartCollege;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyPageData";
    private RelativeLayout loaderLayout;

    public static Fragment_Tab_3 newInstance() {
        return new Fragment_Tab_3();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_my_page,container,false);

        view.findViewById(R.id.contestScrapButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.recruitButton).setOnClickListener(onClickListener);

        loaderLayout = view.findViewById(R.id.loaderLayout);

        userParticipateQuery();

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.contestScrapButton:
                    ((MainActivity)getActivity()).replaceFragment(ContestScrapActivity.newInstance());
                    break;
                case R.id.recruitButton:
                    ((MainActivity)getActivity()).replaceFragment(RecruitScrapActivity.newInstance());
                    break;
            }

        }
    };


    private void collegeStatisticQuery(final int userCount, final String userCollege){

        totalStatisticNumber = view.findViewById(R.id.totalStatisticNumber);
        totalStatisticsText = view.findViewById(R.id.totalStatisticsText);
        collegeStatisticsNumber = view.findViewById(R.id.collegeStatisticsNumber);
        collegeStatisticsText = view.findViewById(R.id.collegeStatisticsText);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double totalAverage = 0;
                        int totalUserNumber = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("contestParticipate"));
                                totalAverage += Integer.parseInt(String.valueOf(document.getData().get("contestParticipate")));
                                totalUserNumber++;
                            }
                            totalAverage /= totalUserNumber;
                            String number = String.format("%.1f vs %d",totalAverage,userCount);
                            String what = totalAverage <= userCount ? "많이" : "적게";
                            String text = String.format("수정이들보다 평균적으로 %s 참여했습니다",what);
                            totalStatisticNumber.setText(number);
                            totalStatisticsText.setText(text);
                            setTotalGraph(userCount, totalAverage);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("users")
                .whereEqualTo("college",userCollege)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double collegeAverage = 0;
                        int collegeUserNumber = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("contestParticipate"));
                                collegeAverage += Integer.parseInt(String.valueOf(document.getData().get("contestParticipate")));
                                collegeUserNumber++;
                            }
                            collegeAverage /= collegeUserNumber;
                            String number = String.format("%.1f vs %d",collegeAverage,userCount);
                            String what = collegeAverage <= userCount ? "많이" : "적게";
                            String text = String.format("%s 수정이들보다 평균적으로 %s 참여했습니다",userCollege,what);
                            collegeStatisticsNumber.setText(number);
                            collegeStatisticsText.setText(text);

                            setCollegeGraph(userCount, collegeAverage);
                            loaderLayout.setVisibility(View.INVISIBLE);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void userParticipateQuery(){
        loaderLayout.setVisibility(View.VISIBLE);
        String userUid = user.getUid();

        TextView myPageUserText = view.findViewById(R.id.myPageUserText);
        TextView userInformationText = view.findViewById(R.id.userInformationText);

        DocumentReference docRef = db.collection("users").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int userParticipateCount = 0;
                    String userCollege = "";
                    String userDepartment;
                    String userStudentId;
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("contestParticipate"));
                        userParticipateCount = Integer.parseInt(String.valueOf(document.getData().get("contestParticipate")));
                        myPageUserText.setText(document.getData().get("userName").toString() + "의 공모전 현황");
                        userCollege = document.getData().get("college").toString();
                        userDepartment = document.getData().get("department").toString();
                        userStudentId = document.getData().get("studentId").toString();
                        userInformationText.setText(userCollege + " " + userDepartment + " " + userStudentId+"학번");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                    collegeStatisticQuery(userParticipateCount, userCollege);
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setTotalGraph(final int userCount, final double average){

        //first pieChart
        pieChartTotal = (PieChart)view.findViewById(R.id.totalStatisticGraph);
        pieChartTotal.setUsePercentValues(false);
        pieChartTotal.getDescription().setEnabled(false);
        pieChartTotal.setExtraOffsets(5,10,5,5);
        pieChartTotal.setDragDecelerationFrictionCoef(0.95f);
        pieChartTotal.setDrawHoleEnabled(true);
        pieChartTotal.setHoleColor(Color.WHITE);
        pieChartTotal.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        yValues.add(new PieEntry(userCount,"My"));
        yValues.add(new PieEntry((int)average,"SSWU"));

        PieDataSet dataSet = new PieDataSet(yValues,"Count");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setLabel(null);

        PieData data = new PieData((dataSet));
        pieChartTotal.setData(data);
        pieChartTotal.invalidate();

    }

    private void setCollegeGraph(final int userCount, final double average){

        //second pieChart
        pieChartCollege = (PieChart)view.findViewById(R.id.collegeStatisticGraph);
        pieChartCollege.setUsePercentValues(false);
        pieChartCollege.getDescription().setEnabled(false);
        pieChartCollege.setExtraOffsets(5,10,5,5);
        pieChartCollege.setDragDecelerationFrictionCoef(0.95f);
        pieChartCollege.setDrawHoleEnabled(true);
        pieChartCollege.setHoleColor(Color.WHITE);
        pieChartCollege.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValuesSecond = new ArrayList<PieEntry>();
        yValuesSecond.add(new PieEntry(userCount,"My"));
        yValuesSecond.add(new PieEntry((int)(average),"College"));

        PieDataSet dataSetSecond = new PieDataSet(yValuesSecond,"Count");
        dataSetSecond.setSliceSpace(3f);
        dataSetSecond.setSelectionShift(5f);
        dataSetSecond.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSetSecond.setLabel(null);

        PieData dataSecond = new PieData((dataSetSecond));
        pieChartCollege.setData(dataSecond);
        pieChartCollege.invalidate();

    }

}