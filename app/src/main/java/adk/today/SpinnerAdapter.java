package adk.today;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Adu on 12/5/2016.
 */

public class SpinnerAdapter extends BaseAdapter {

    /*
    * Whenever a new period is being created or a period is being updated , the user can select
    * pre-existing period to pre-fill some fields . This spinner shows all such periods from which
    * the user can choose one.
    */

    String[] periods;
    String[] periodStats;
    Context context;
    LayoutInflater layoutInflater;

    public SpinnerAdapter(Context context, String[] periods, String[] periodStats) {
        this.context = context;
        this.periods = periods;
        this.periodStats = periodStats;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return periods.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = this.layoutInflater.inflate(R.layout.spinner_item, null);
        ((TextView) view.findViewById(R.id.spinName)).setText(periods[position]);
        ((TextView) view.findViewById(R.id.spinStat)).setText(periodStats[position]);
        return view;
    }
}
