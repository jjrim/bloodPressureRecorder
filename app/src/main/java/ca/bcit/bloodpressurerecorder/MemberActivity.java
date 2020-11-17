package ca.bcit.bloodpressurerecorder;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MemberActivity extends AppCompatActivity {

    DatabaseReference databaseBloodPressures;
    List<BloodPressure> BPFList;
    TextView month;
    TextView father;
    TextView fatherSystolicAvg;
    TextView fatherDiastolicAvg;
    TextView fatherCondition;
    TextView mother;
    TextView motherSystolicAvg;
    TextView motherDiastolicAvg;
    TextView motherCondition;
    TextView grandma;
    TextView grandmaSystolicAvg;
    TextView grandmaDiastolicAvg;
    TextView grandmaCondition;
    TextView grandpa;
    TextView grandpaSystolicAvg;
    TextView grandpaDiastolicAvg;
    TextView grandpaCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        databaseBloodPressures = FirebaseDatabase.getInstance().getReference("bloodPressures");

        BPFList = new ArrayList<BloodPressure>();
        month = findViewById(R.id.month);
        father = findViewById(R.id.father);
        fatherSystolicAvg = findViewById(R.id.fatherSystolicAvg);
        fatherDiastolicAvg = findViewById(R.id.fatherDiastolicAvg);
        fatherCondition = findViewById(R.id.fatherCondition);
        mother = findViewById(R.id.mother);
        motherSystolicAvg = findViewById(R.id.motherSystolicAvg);
        motherDiastolicAvg = findViewById(R.id.motherDiastolicAvg);
        motherCondition = findViewById(R.id.motherCondition);
        grandma = findViewById(R.id.grandma);
        grandmaSystolicAvg = findViewById(R.id.grandmaSystolicAvg);
        grandmaDiastolicAvg = findViewById(R.id.grandmaDiastolicAvg);
        grandmaCondition = findViewById(R.id.grandmaCondition);
        grandpa = findViewById(R.id.grandpa);
        grandpaSystolicAvg = findViewById(R.id.grandpaSystolicAvg);
        grandpaDiastolicAvg = findViewById(R.id.grandpaDiastolicAvg);
        grandpaCondition = findViewById(R.id.grandpaCondition);

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseBloodPressures.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BPFList.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    BloodPressure bloodPressure = studentSnapshot.getValue(BloodPressure.class);
                    BPFList.add(bloodPressure);
                }

                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int monthInt = cal.get(Calendar.MONTH);


                SimpleDateFormat sf = new SimpleDateFormat("MMMM");
                String strMonth= sf.format(new Date());


                float fSAvg = 0;
                float fDAvg = 0;
                int fCount = 0;
                float mSAvg = 0;
                float mDAvg = 0;
                int mCount = 0;
                float gmSAvg = 0;
                float gmDAvg = 0;
                int gmCount = 0;
                float gpSAvg = 0;
                float gpDAvg = 0;
                int gpCount = 0;



                for (int i = 0; i < BPFList.size(); i++) {
                    if (new Date().getMonth() == BPFList.get(i).getDateTime().getMonth()) {
                        if (BPFList.get(i).getFamilyMember().equals("father@home.com")) {
                            fCount++;
                            fSAvg = fSAvg + Float.parseFloat(BPFList.get(i).getSystolicReading());
                            fDAvg = fDAvg + Float.parseFloat(BPFList.get(i).getDiastolicReading());
                        } else if (BPFList.get(i).getFamilyMember().equals("mother@home.com")) {
                            mCount++;
                            mSAvg = mSAvg + Float.parseFloat(BPFList.get(i).getSystolicReading());
                            mDAvg = mDAvg + Float.parseFloat(BPFList.get(i).getDiastolicReading());
                        } else if (BPFList.get(i).getFamilyMember().equals("grandma@home.com")) {
                            gmCount++;
                            gmSAvg = gmSAvg + Float.parseFloat(BPFList.get(i).getSystolicReading());
                            gmDAvg = gmDAvg + Float.parseFloat(BPFList.get(i).getDiastolicReading());
                        } else if (BPFList.get(i).getFamilyMember().equals("grandpa@home.com")) {
                            gpCount++;
                            gpSAvg = gpSAvg + Float.parseFloat(BPFList.get(i).getSystolicReading());
                            gpDAvg = gpDAvg + Float.parseFloat(BPFList.get(i).getDiastolicReading());
                        }
                    }

                }

                month.setText("Month-to-date average readings for " + strMonth + "2020");
                father.setText("father@home.com");
                fatherSystolicAvg.setText("Father Average Systolic average reading is: " + fSAvg / fCount);
                fatherDiastolicAvg.setText("Father Average Diastolic average reading is: " + fDAvg / fCount);
                fatherCondition.setText(conditionDecider((fSAvg / fCount), (fDAvg / fCount)));
                mother.setText("mother@home.com");
                motherSystolicAvg.setText("Mother Average Systolic average reading is: " + mSAvg / mCount);
                motherDiastolicAvg.setText("Mother Average Diastolic average reading is: " + mDAvg / mCount);
                motherCondition.setText(conditionDecider((mSAvg / mCount), (mDAvg / mCount)));
                grandma.setText("grandma@home.com");
                grandmaSystolicAvg.setText("Grandma Average Systolic average reading is: " + gmSAvg / gmCount);
                grandmaDiastolicAvg.setText("Grandma Average Diastolic average reading is: " + gmDAvg / gmCount);
                grandmaCondition.setText(conditionDecider((gmSAvg / gmCount), (gmDAvg / gmCount)));
                grandpa.setText("grandpa@home.com");
                grandpaSystolicAvg.setText("Grandpa Average Systolic average reading is: " + gpSAvg / gpCount);
                grandpaDiastolicAvg.setText("Grandpa Average Diastolic average reading is: " + gpDAvg / gpCount);
                grandpaCondition.setText(conditionDecider((gpSAvg / gpCount), (gpDAvg / gpCount)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private String conditionDecider(float s, float d) {
        if (s > 180 || d > 120) {
            return "Warning!: Hypertensive Crisis";
        }
        else if (s > 140 || d > 90) {
            return "Stage 2";
        }
        else if (s > 130 || d > 80) {
            return "Stage 1";
        }
        else if (s > 120 && d < 80) {
            return "Elevated";
        }
        else {
            return "normal";
        }
    }
}

