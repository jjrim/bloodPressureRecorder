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

import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends AppCompatActivity {

    DatabaseReference databaseBloodPressures;
    List<BloodPressure> BPFList;
    TextView father;
    TextView fatherSystolicAvg;
    TextView fatherDiastolicAvg;
    TextView fatherCondition;

    ListView lvBPs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        databaseBloodPressures = FirebaseDatabase.getInstance().getReference("bloodPressures");

//        lvBPs = findViewById(R.id.list);
        BPFList = new ArrayList<BloodPressure>();
        father = findViewById(R.id.father);
        fatherSystolicAvg = findViewById(R.id.fatherSystolicAvg);
        fatherDiastolicAvg = findViewById(R.id.fatherDiastolicAvg);
        fatherCondition = findViewById(R.id.fatherCondition);




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


                float fSAvg = 0;
                float fDAvg = 0;
                int fCount = 0;
                String fCondition;

                for (int i = 0; i < BPFList.size(); i++) {
                    if (BPFList.get(i).getFamilyMember().equals("father@home.com")) {
                        fCount++;
                        fSAvg = fSAvg + Float.parseFloat(BPFList.get(i).getSystolicReading());
                        fDAvg = fDAvg + Float.parseFloat(BPFList.get(i).getDiastolicReading());
                    }

                }

                father.setText("father@home.com");
                fatherSystolicAvg.setText("Father Average Systolic average reading is: " + fSAvg / fCount);
                fatherDiastolicAvg.setText("Father Average Diastolic average reading is: " + fDAvg / fCount);
                fatherCondition.setText(conditionDecider((fSAvg / fCount), (fDAvg / fCount)));
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

