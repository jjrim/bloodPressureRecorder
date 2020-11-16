package ca.bcit.bloodpressurerecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText systolic;
    EditText diastolic;
    Button addBloodPressure;
    Button moveToReport;
    Spinner familyMember;

    DatabaseReference databaseBloodPressures;

    ListView lvBPs;
    List<BloodPressure> BPlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseBloodPressures = FirebaseDatabase.getInstance().getReference("bloodPressures");


        systolic = findViewById(R.id.systolic);
        diastolic = findViewById(R.id.diastolic);
        addBloodPressure = findViewById(R.id.addBloodPressure);
        familyMember = findViewById(R.id.familyMember);
        moveToReport = findViewById(R.id.report);

        moveToReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReport();
            }
        });


        addBloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBloodPressure();
            }
        });

        lvBPs = findViewById(R.id.lvBPs);
        BPlist = new ArrayList<BloodPressure>();

        lvBPs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BloodPressure bloodPressure = BPlist.get(position);

                showUpdateDialog(
                        bloodPressure.getBloodPressureId(),
                        bloodPressure.getSystolicReading(),
                        bloodPressure.getDiastolicReading(),
                        bloodPressure.getFamilyMember(),
                        bloodPressure.getDateTime(),
                        bloodPressure.getCondition());

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseBloodPressures.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BPlist.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    BloodPressure bloodPressure = studentSnapshot.getValue(BloodPressure.class);
                    BPlist.add(bloodPressure);
                }

                BloodPressureListAdapter adapter = new BloodPressureListAdapter(MainActivity.this, BPlist);
                lvBPs.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private String conditionDecider(String systolicReading, String diastolicReading) {
        int s = Integer.parseInt(systolicReading);
        int d = Integer.parseInt(diastolicReading);
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

    private void addBloodPressure() {
        String systolicString = systolic.getText().toString().trim();
        String diastolicString = diastolic.getText().toString().trim();
        String family = familyMember.getSelectedItem().toString().trim();
        final String condition = conditionDecider(systolicString, diastolicString);
        Date dateTime = new Date();

        if (TextUtils.isEmpty(systolicString)) {
            Toast.makeText(this, "Systolic Pressure is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(diastolicString)) {
            Toast.makeText(this, "Diastolic Pressure is empty.", Toast.LENGTH_LONG).show();
            return;
        }



        String id = databaseBloodPressures.push().getKey();
        BloodPressure bloodPressure = new BloodPressure(id, systolicString, diastolicString, family, dateTime, condition);

        Task setValueTask = databaseBloodPressures.child(id).setValue(bloodPressure);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (condition.equals("Warning!: Hypertensive Crisis")) {
                    Toast.makeText(MainActivity.this,"Warning.",Toast.LENGTH_LONG).show();

                }
                Toast.makeText(MainActivity.this,"Blood Pressure added.",Toast.LENGTH_LONG).show();

                systolic.setText("");
                diastolic.setText("");
                familyMember.setSelection(0);
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,
                        "something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePressures(String id) {
        DatabaseReference dbRef = databaseBloodPressures.child(id);

        Task setRemoveTask = dbRef.removeValue();
        setRemoveTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this,
                        "Blood Preesure Deleted.",Toast.LENGTH_LONG).show();
            }
        });

        setRemoveTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updatePressures(String bloodPressureId, String systolicReading, String diastolicReading, String familyMember, Date dateTime, String condition) {
        DatabaseReference dbRef = databaseBloodPressures.child(bloodPressureId);

        BloodPressure bloodPressure = new BloodPressure(bloodPressureId, systolicReading, diastolicReading, familyMember, dateTime, condition);

        Task setValueTask = dbRef.setValue(bloodPressure);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this,
                        "Blood Pressure Updated.",Toast.LENGTH_LONG).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(final String bloodPressureId,
                                  String systolicReading,
                                  String diastolicReading,
                                  String family,
                                  Date dateTime,
                                  String condition) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText systolic = dialogView.findViewById(R.id.systolic);
        systolic.setText(systolicReading);

        final EditText editTextDiastolic = dialogView.findViewById(R.id.diastolic);
        editTextDiastolic.setText(diastolicReading);

        final Spinner familyMember = dialogView.findViewById(R.id.familyMember);
        familyMember.setSelection(((ArrayAdapter<String>)familyMember.getAdapter()).getPosition(family));


        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Update Pressure: " + systolicReading + " " + diastolicReading);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String systolicReading = systolic.getText().toString().trim();
                String diastolicReading = editTextDiastolic.getText().toString().trim();
                String family = familyMember.getSelectedItem().toString().trim();

//                String systolicString = systolic.getText().toString().trim();
//                int systolicPressure = Integer.parseInt(systolicString);
//                String diastolicString = diastolic.getText().toString().trim();
//                int diastolicPressure = Integer.parseInt(diastolicString);
//                String family = familyMember.getSelectedItem().toString().trim();

                String condition = conditionDecider(systolicReading, diastolicReading);
                Date dateTime = new Date();

//                if (TextUtils.isEmpty(firstName)) {
//                    editTextFirstName.setError("First Name is required");
//                    return;
//                } else if (TextUtils.isEmpty(lastName)) {
//                    editTextLastName.setError("Last Name is required");
//                    return;
//                }

                updatePressures(bloodPressureId, systolicReading, diastolicReading, family, dateTime, condition);

                alertDialog.dismiss();
            }
        });

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePressures(bloodPressureId);

                alertDialog.dismiss();
            }
        });

    }


    private void toReport() {
        Intent intent = new Intent(this, MemberActivity.class);
        startActivity(intent);
    }


}