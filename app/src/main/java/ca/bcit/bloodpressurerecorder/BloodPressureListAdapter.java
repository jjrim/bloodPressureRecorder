package ca.bcit.bloodpressurerecorder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class BloodPressureListAdapter extends ArrayAdapter<BloodPressure> {
    private Activity context;
    private List<BloodPressure> BPList;

    public BloodPressureListAdapter(Activity context, List<BloodPressure> BPList) {
        super(context, R.layout.blood_pressure_list_layout, BPList);
        this.context = context;
        this.BPList = BPList;
        }

    public BloodPressureListAdapter(Context context, int resource, List<BloodPressure> objects, Activity context1, List<BloodPressure> BPList) {
        super(context, resource, objects);
        this.context = context1;
        this.BPList = BPList;
        }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.blood_pressure_list_layout, null, true);

        TextView familyMember = listViewItem.findViewById(R.id.familyMember);
        TextView pressure = listViewItem.findViewById(R.id.pressure);
        TextView condition = listViewItem.findViewById(R.id.condition);


        BloodPressure bloodPressure = BPList.get(position);
        familyMember.setText(bloodPressure.getFamilyMember());
        pressure.setText("Systolic: " + bloodPressure.getDiastolicReading()
        + " Diastolic: " + bloodPressure.getSystolicReading());
        condition.setText(bloodPressure.getCondition());



        return listViewItem;
        }

        }
