//package com.example.sw_project.Activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.sw_project.R;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
//public class MyPageActivity extends AppCompatActivity {
//
//    PieChart pieChartTotal;
//    PieChart pieChartCollege;
//
//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private static final String TAG = "MyPageData";
//    private RelativeLayout loaderLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_page);
//        findViewById(R.id.contestScrapButton).setOnClickListener(onClickListener);
//        findViewById(R.id.recruitButton).setOnClickListener(onClickListener);
//
//        ScrollView scrollView = findViewById(R.id.scrollView);
//        loaderLayout = findViewById(R.id.loaderLayout);
//
//        userParticipateQuery();
//
//    }
//
//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.contestScrapButton:
//                    moveContestActivity();
//                    break;
//                case R.id.recruitButton:
//                    moveRecruitActivity();
//                    break;
//            }
//
//        }
//    };
//
//    private void collegeStatisticQuery(final int userCount, final String userCollege){
//        final double[] tem = new double[1];
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        double totalAverage = 0;
//                        int totalUserNumber = 0;
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData().get("contestParticipate"));
//                                totalAverage += Integer.parseInt(String.valueOf(document.getData().get("contestParticipate")));
//                                totalUserNumber++;
//                            }
//                            System.out.println(totalUserNumber + " " + totalAverage);
//                            totalAverage /= totalUserNumber;
//                            tem[0] = totalAverage;
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//        db.collection("users")
//                .whereEqualTo("college",userCollege)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        double collegeAverage = 0;
//                        int collegeUserNumber = 0;
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData().get("contestParticipate"));
//                                collegeAverage += Integer.parseInt(String.valueOf(document.getData().get("contestParticipate")));
//                                collegeUserNumber++;
//                            }
//                            System.out.println("college "+ collegeUserNumber + " " + collegeAverage);
//                            collegeAverage /= collegeUserNumber;
//                            setCollegeText(collegeAverage,tem[0],userCount, userCollege);
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//    }
//
//    public void userParticipateQuery(){
//        loaderLayout.setVisibility(View.VISIBLE);
//        String userUid = user.getUid();
//
//        TextView myPageUserText = findViewById(R.id.myPageUserText);
//        TextView userInformationText = findViewById(R.id.userInformationText);
//
//        DocumentReference docRef = db.collection("users").document(userUid);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    int userParticipateCount = 0;
//                    String userCollege = "";
//                    String userDepartment;
//                    String userStudentId;
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("contestParticipate"));
//                        userParticipateCount = Integer.parseInt(String.valueOf(document.getData().get("contestParticipate")));
//                        myPageUserText.setText(document.getData().get("userName").toString() + "의 공모전 현황");
//                        userCollege = document.getData().get("college").toString();
//                        userDepartment = document.getData().get("department").toString();
//                        userStudentId = document.getData().get("studentId").toString();
//                        userInformationText.setText(userCollege + " " + userDepartment + " " + userStudentId+"학번");
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                    collegeStatisticQuery(userParticipateCount, userCollege);
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//    }
//
//    private void setScrapText(){
//        int scrapCount = 0;
//        TextView textScrap = findViewById(R.id.statistic_scrap);
//        String setScrap = String.format(Locale.KOREA,"나의 공모전 스크랩 횟수는 %d회입니다.",scrapCount);
//        textScrap.setText(setScrap);
//    }
//
//    private void setCollegeText(final double collegeAverage,final double average, final int userCount, final String userCollege){
//
//        TextView textCollege = findViewById(R.id.statistic_college);
//        String upDown = (userCount > average) ? "많이" : "적게";
//        String setCollege = String.format(Locale.KOREA,"%s 학생들의 평균 공모전 참여 횟수는 %.1f회, 성신여대 학생들의 평균 공모전 참여 횟수는 %.1f회이며, " +
//                        "나의 공모전 참여 횟수는 %d회로, 다른 수정이들보다 %s 참여했습니다.", userCollege, collegeAverage,average,userCount,upDown);
//        textCollege.setText(setCollege);
//        System.out.println("text set clear");
//
//        //first pieChart
//        pieChartTotal = (PieChart)findViewById(R.id.totalStatisticGraph);
//        pieChartTotal.setUsePercentValues(false);
//        pieChartTotal.getDescription().setEnabled(false);
//        pieChartTotal.setExtraOffsets(5,10,5,5);
//        pieChartTotal.setDragDecelerationFrictionCoef(0.95f);
//        // 안에 비어있냐 채워져있냐 여
//        pieChartTotal.setDrawHoleEnabled(true);
//        pieChartTotal.setHoleColor(Color.WHITE);
//        pieChartTotal.setTransparentCircleRadius(61f);
//
//        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
//        yValues.add(new PieEntry(userCount,"My"));
//        yValues.add(new PieEntry((int)average,"SSWU"));
//
//        PieDataSet dataSet = new PieDataSet(yValues,"Count");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        dataSet.setLabel(null);
//
//        PieData data = new PieData((dataSet));
//        pieChartTotal.setData(data);
//        pieChartTotal.invalidate();
//
//        //second pieChart
//        pieChartCollege = (PieChart)findViewById(R.id.collegeStatisticGraph);
//        pieChartCollege.setUsePercentValues(false);
//        pieChartCollege.getDescription().setEnabled(false);
//        pieChartCollege.setExtraOffsets(5,10,5,5);
//        pieChartCollege.setDragDecelerationFrictionCoef(0.95f);
//        // 안에 비어있냐 채워져있냐 여
//        pieChartCollege.setDrawHoleEnabled(true);
//        pieChartCollege.setHoleColor(Color.WHITE);
//        pieChartCollege.setTransparentCircleRadius(61f);
//
//        ArrayList<PieEntry> yValuesSecond = new ArrayList<PieEntry>();
//        yValuesSecond.add(new PieEntry(userCount,"My"));
//        yValuesSecond.add(new PieEntry((int)collegeAverage,"College"));
//
//        PieDataSet dataSetSecond = new PieDataSet(yValuesSecond,"Count");
//        dataSetSecond.setSliceSpace(3f);
//        dataSetSecond.setSelectionShift(5f);
//        dataSetSecond.setColors(ColorTemplate.MATERIAL_COLORS);
//        dataSetSecond.setLabel(null);
//
//        PieData dataSecond = new PieData((dataSetSecond));
//        pieChartCollege.setData(dataSecond);
//        pieChartCollege.invalidate();
//
//        loaderLayout.setVisibility(View.INVISIBLE);
//
//    }
//
//    private void moveContestActivity(){
//        Intent intent = new Intent(this, ContestScrapActivity.class);
//        startActivity(intent);
//    }
//
//    private void moveRecruitActivity(){
//        Intent intent = new Intent(this, RecruitScrapActivity.class);
//        startActivity(intent);
//    }
//
//}
