package adk.today;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static adk.today.R.anim.rotate_backward;

/**
 * Created by Adu on 12/2/2016.
 */

public class NewClass extends DialogFragment implements AdapterView.OnItemSelectedListener {

    /*
    * Dialog Fragment which pops up when a period is long pressed or the floating action button is
    * tapped to create a new period.
    */

    TextInputLayout ssub;
    TextInputLayout sprof;
    TextInputLayout sfr;
    TextInputLayout sto;
    TextInputLayout sro;
    Switch sw;
    String day;
    int val = 0;
    Context context;
    FloatingActionButton fab;
    RecyclerView.Adapter adapter;

    ArrayList<String> SubNames;
    ArrayList<String> SubProfs;
    ArrayList<String> SubStats;
    ArrayList<String> SubRooms;


    public void setDay(String day) {
        this.day = day;
    }

    public void initFAB(FloatingActionButton fab) {
        this.fab = fab;
    }

    public void initAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fab.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), rotate_backward));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Used solely for the generateLists //
        SubNames = new ArrayList<>(60);
        SubProfs = new ArrayList<>(60);
        SubRooms = new ArrayList<>(60);
        SubStats = new ArrayList<>(60);
        //////////////////////////////////////

        View view = inflater.inflate(R.layout.new_class, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);

        context = getActivity();

        setupSpinner(view);


        ssub = (TextInputLayout) view.findViewById(R.id.editSubMaster);
        sprof = (TextInputLayout) view.findViewById(R.id.editSubProfMaster);
        sfr = (TextInputLayout) view.findViewById(R.id.editTimeFromMaster);
        sto = (TextInputLayout) view.findViewById(R.id.editTimeToMaster);
        sro = (TextInputLayout) view.findViewById(R.id.editRoomMaster);
        sw = (Switch) view.findViewById(R.id.labSwitch);

        // Save the details by calling the createClass() and animate the Floating Action Button.
        // Finally dismiss the Dialog Fragment.
        view.findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createClass()) {
                    fab.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), rotate_backward));
                    adapter.notifyDataSetChanged();
                    dismiss();
                }
            }
        });


        // Dismiss the Dialog Fragment.
        view.findViewById(R.id.Discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Discarded.", Toast.LENGTH_SHORT).show();
                ssub.getEditText().setText("");
                sprof.getEditText().setText("");
                sfr.getEditText().setText("");
                //  sto.getEditText().setText("");
                sro.getEditText().setText("");
                sw.setChecked(false);
                getDialog().dismiss();
                fab.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), rotate_backward));
            }
        });

        // Set the time properly in Hours and Minutes
        sto.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (0 == event.getAction()) {
                    TimePickerDialog tpd = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String m;
                            if (selectedMinute < 10) {
                                m = "0" + new Integer(selectedMinute).toString();
                            } else {
                                m = new Integer(selectedMinute).toString();
                            }
                            sto.getEditText().setText(new Integer(selectedHour).toString() + ":" + m);
                        }
                    }, 10, 0, true);
                    tpd.setCancelable(true);
                    tpd.setTitle("");
                    tpd.show();
                    return true;
                }
                return false;
            }
        });

        // Set the time properly in Hours and Minutes
        sfr.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (0 == event.getAction()) {
                    TimePickerDialog tpd = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String m;
                            if (selectedMinute < 10) {
                                m = "0" + new Integer(selectedMinute).toString();
                            } else {
                                m = new Integer(selectedMinute).toString();
                            }
                            sfr.getEditText().setText(new Integer(selectedHour).toString() + ":" + m);
                        }
                    }, 10, 0, true);
                    tpd.setCancelable(true);
                    tpd.setTitle("");
                    tpd.show();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public boolean createClass() {
        // Saves the period in database.

        try {

            String a = ssub.getEditText().getText().toString();
            String b = sprof.getEditText().getText().toString();
            String c = sfr.getEditText().getText().toString();
            String d = sto.getEditText().getText().toString();
            String e = sro.getEditText().getText().toString();

            if (!a.equals("")) {

                Storage storage = new Storage(context);
                storage.insertData(day,
                        ssub.getEditText().getText().toString(),
                        sprof.getEditText().getText().toString(),
                        sfr.getEditText().getText().toString() + " - " + sto.getEditText().getText().toString(),
                        sro.getEditText().getText().toString(),
                        sw.isChecked() ? "Lab" : "Theory"
                );

            } else {
                Toast.makeText(context, "Please fill the remaining fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(context, "You've just found a bug! ( Code - Da_S.L )", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setupSpinner(View view) {
        // Spinner for input of time.

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        generateLists();

        String[] SNP = this.SubNames.toArray(new String[this.SubNames.size()]);
        String[] SSP = this.SubStats.toArray(new String[this.SubStats.size()]);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, SNP, SSP);
        spinner.setAdapter(spinnerAdapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (val++ != 0) {
            this.ssub.getEditText().setText(this.SubNames.get(position));
            this.sprof.getEditText().setText(this.SubProfs.get(position));
            this.sro.getEditText().setText(this.SubRooms.get(position));
            this.sw.setChecked(this.SubStats.get(position).equals("Lab") ? true : false);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    /*
     * When a new period is being added or an existing period is being modified , the user can
     * choose to pre-fill some fields in the dialog using already existing periods in the database.
     *
     * Hence , generateLists() gets the data for everyday , and this data is not redundant ,i.e,
     * all the (Subject Name, Professor, Lab/Theory) tuples are unique.
     */

    public void generateLists() {
        getClassNames("Monday");
        getClassNames("Tuesday");
        getClassNames("Wednesday");
        getClassNames("Thursday");
        getClassNames("Friday");
        getClassNames("Saturday");
    }

    public void getClassNames(String day) {

        String sb, sp, ss, sr;

        Storage storage = new Storage(context);
        List<Period> p = storage.getData(day);

        for (int i = 0; i < p.size(); i++) {
            sb = p.get(i).getSubject();
            sp = p.get(i).getSubProf();
            ss = p.get(i).getSubStatus();
            sr = p.get(i).getRoom();

            // This removes any redundancy.
            if ((!this.SubNames.contains(sb)) || (!(this.SubStats.get(SubNames.indexOf(sb)).equals(ss)))) {
                this.SubNames.add(sb);
                this.SubProfs.add(sp);
                this.SubStats.add(ss);
                this.SubRooms.add(sr);
            }
        }
    }

}
