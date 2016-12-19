package adk.today;

/**
 * Created by Adu on 12/3/2016.
 */

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Adu on 12/2/2016.
 */

public class UserNameUpdation extends DialogFragment {

    /*
    * A simple dialog fragment , for entering and saving the User's Name which
    * is displayed in the Navigation Drawer .
    */

    Context context;
    View rel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.username_updation_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        context = getActivity();

        ////////////////////////////////////////////  CARD BUTTONS /////////////////////////////////////////

        view.findViewById(R.id.UserNameSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String n = ((TextInputLayout) view.findViewById(R.id.UserNameEdit)).getEditText().getText().toString();
                editor.putString("UserName", n);
                Toast.makeText(context, "How do you do" + n + "!", Toast.LENGTH_SHORT);
                editor.commit();
                dismiss();
            }
        });


        view.findViewById(R.id.UserNameDiscard).setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           dismiss();
                                                                       }
                                                                   }

        );

        return view;
    }


}

