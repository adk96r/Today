package adk.today;

/**
 * Created by Adu on 12/3/2016.
 */

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adu on 12/2/2016.
 */

public class UpdateClass extends DialogFragment implements AdapterView.OnItemSelectedListener {

    /*
    * DialogFragment created when a period is long pressed to be modified.
    */

    RecyclerView recyclerView;

    TextInputLayout ssub;
    TextInputLayout sprof;
    TextInputLayout sfr;
    TextInputLayout sto;
    TextInputLayout sro;
    Switch sw;
    String day;
    Context context;
    View rel;

    int valP = 0;

    ArrayList<String> SubNames;
    ArrayList<String> SubProfs;
    ArrayList<String> SubStats;
    ArrayList<String> SubRooms;

    String SSUB;
    String SPROF;
    String SFR;
    String STO;
    String SRO;
    Boolean SW;
    String DAY;
    int discardCheck = 0;

    public void init(String SSUB, String SPROF, String SFR, String STO, String SRO, Boolean SW, String DAY, View rel, RecyclerView rv) {
        this.SSUB = SSUB;
        this.SPROF = SPROF;
        this.SFR = SFR;
        this.STO = STO;
        this.SRO = SRO;
        this.SW = SW;
        this.DAY = DAY;
        this.rel = rel;
        this.recyclerView = rv;

        SubNames = new ArrayList<>(60);
        SubProfs = new ArrayList<>(60);
        SubRooms = new ArrayList<>(60);
        SubStats = new ArrayList<>(60);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_class, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        context = getActivity();

        ssub = (TextInputLayout) view.findViewById(R.id.editSubMaster);
        sprof = (TextInputLayout) view.findViewById(R.id.editSubProfMaster);
        sfr = (TextInputLayout) view.findViewById(R.id.editTimeFromMaster);
        sto = (TextInputLayout) view.findViewById(R.id.editTimeToMaster);
        sro = (TextInputLayout) view.findViewById(R.id.editRoomMaster);
        sw = (Switch) view.findViewById(R.id.labSwitch);

        ssub.getEditText().setText(SSUB);
        sprof.getEditText().setText(SPROF);
        sfr.getEditText().setText(SFR);
        sto.getEditText().setText(STO);
        sro.getEditText().setText(SRO);
        sw.setChecked(SW);

        setupSpinner(view);

        ////////////////////////////////////////////  CARD BUTTONS /////////////////////////////////////////

        view.findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UpdateClass()) {
                    recyclerView.setAdapter(new PeriodCardAdapter(recyclerView.getContext(), DAY, getFragmentManager(), recyclerView));
                    dismiss();
                }
            }
        });


        view.findViewById(R.id.Discard).setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   if (discardCheck == 0) {
                                                                       Toast.makeText(context, "Press Again To Detele.", Toast.LENGTH_SHORT).show();
                                                                       discardCheck++;
                                                                   } else if (discardCheck == 1) {
                                                                       Toast.makeText(v.getContext(), "Deleted.", Toast.LENGTH_SHORT).show();
                                                                       Storage storage = new Storage(context);

                                                                       String a = ssub.getEditText().getText().toString();
                                                                       String b = sprof.getEditText().getText().toString();
                                                                       String c = sfr.getEditText().getText().toString();
                                                                       String d = sto.getEditText().getText().toString();
                                                                       String e = sro.getEditText().getText().toString();

                                                                       storage.deleteData(DAY, a, b, c + " - " + d, e);
                                                                       recyclerView.setAdapter(new PeriodCardAdapter(recyclerView.getContext(), DAY, getFragmentManager(), recyclerView));
                                                                       getDialog().dismiss();
                                                                       discardCheck = 0;
                                                                   }
                                                               }
                                                           }

        );

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
                                             }

        );

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
                                             }

        );

        return view;
    }

    public boolean UpdateClass() {
        try {

            String a = ssub.getEditText().getText().toString();
            String b = sprof.getEditText().getText().toString();
            String c = sfr.getEditText().getText().toString();
            String d = sto.getEditText().getText().toString();
            String e = sro.getEditText().getText().toString();

            if (!a.equals("")) {
                Storage storage = new Storage(context);
                storage.updateData(DAY,
                        SSUB,
                        SPROF,
                        SFR + " - " + STO,
                        SRO,
                        a,
                        b,
                        c + " - " + d,
                        e,
                        sw.isChecked() ? "Lab" : "Theory");

            } else {
                Toast.makeText(context, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setupSpinner(View view) {

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
        if (valP++ != 0) {
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

    public void getClassNames(String day) {

        String sb, sp, ss, sr;

        Storage storage = new Storage(context);
        List<Period> p = storage.getData(day);

        for (int i = 0; i < p.size(); i++) {
            sb = p.get(i).getSubject();
            sp = p.get(i).getSubProf();
            ss = p.get(i).getSubStatus();
            sr = p.get(i).getRoom();

            if ((!this.SubNames.contains(sb)) || (!(this.SubStats.get(SubNames.indexOf(sb)).equals(ss)))) {
                this.SubNames.add(sb);
                this.SubProfs.add(sp);
                this.SubStats.add(ss);
                this.SubRooms.add(sr);
            }
        }
    }

    public void generateLists() {
        getClassNames("Monday");
        getClassNames("Tuesday");
        getClassNames("Wednesday");
        getClassNames("Thursday");
        getClassNames("Friday");
        getClassNames("Saturday");
    }
}

