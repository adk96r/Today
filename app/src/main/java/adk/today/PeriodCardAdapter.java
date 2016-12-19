package adk.today;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adu on 12/2/2016.
 */


public class PeriodCardAdapter extends RecyclerView.Adapter<PeriodCardAdapter.CardHolder> {

    FragmentManager fr;
    CardHolder cardHolder;
    String day;
    RecyclerView classesRecyclerView;
    private LayoutInflater inflater;
    private Context context;

    public PeriodCardAdapter(Context context, String day, FragmentManager fr, RecyclerView recyclerView) {
        inflater = LayoutInflater.from(context);
        this.day = day;
        this.context = context;
        this.fr = fr;
        this.classesRecyclerView = recyclerView;
    }

    @Override
    public CardHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        try {
            View periodView = inflater.inflate(R.layout.card_period, parent, false);
            final CardView rel = (CardView) periodView.findViewById(R.id.OnePeriod);

            rel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final TextView ssub = (TextView) rel.findViewById(R.id.subject);
                    final TextView sprof = (TextView) rel.findViewById(R.id.subProf);
                    final TextView tim = (TextView) rel.findViewById(R.id.Time);
                    final TextView sro = (TextView) rel.findViewById(R.id.room);
                    final Boolean sw = (((TextView) rel.findViewById(R.id.subStatus)).getText().toString().equals("Theory")) ? false : true;


                    // Initiate updateClass wih the all the current details of the period.
                    UpdateClass updateClass = new UpdateClass();
                    updateClass.init(ssub.getText().toString(),
                            sprof.getText().toString(),
                            tim.getText().toString().split(" - ")[0],
                            tim.getText().toString().split(" - ")[1],
                            sro.getText().toString(),
                            sw,
                            day, rel, classesRecyclerView);
                    updateClass.show(fr, "Update Class Dialog");
                    return true;
                }

            });

            cardHolder = new CardHolder(periodView, context);
        } catch (Exception e) {
        }
        return cardHolder;

    }


    @Override
    public void onBindViewHolder(CardHolder holder, int position) {

        // Create the holder for each period .
        Log.d("ADU ", "BINDING VIEW FOR DAY : " + day);
        Storage storage = new Storage(context);
        List<Period> periods = storage.getData(day);

        if (periods.size() != 0) {

            holder.subject.setText(periods.get(position).getSubject());
            holder.subTime.setText(periods.get(position).getSubTime());
            holder.subProf.setText(periods.get(position).getSubProf());
            holder.subStatus.setText(periods.get(position).getSubStatus());
            holder.room.setText(periods.get(position).getRoom());

        } else {
            Log.d("ADK--", "NO DATA");
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.up_from_bottom);
        holder.itemView.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        Storage storage = new Storage(context);
        switch (day) {
            case "Monday":
                return storage.getData("Monday").size();
            case "Tuesday":
                return storage.getData("Tuesday").size();
            case "Wednesday":
                return storage.getData("Wednesday").size();
            case "Thursday":
                return storage.getData("Thursday").size();
            case "Friday":
                return storage.getData("Friday").size();
            case "Saturday":
                return storage.getData("Saturday").size();
            default:
                return 0;
        }
    }

    @Override
    public void onViewDetachedFromWindow(CardHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public static class CardHolder extends RecyclerView.ViewHolder {
        public TextView subject, subTime, subStatus, room, subProf;

        public CardHolder(View itemView, final Context context) {
            super(itemView);

            try {
                subject = (TextView) itemView.findViewById(R.id.subject);
                subTime = (TextView) itemView.findViewById(R.id.Time);
                subProf = (TextView) itemView.findViewById(R.id.subProf);
                subStatus = (TextView) itemView.findViewById(R.id.subStatus);
                room = (TextView) itemView.findViewById(R.id.room);

            } catch (Exception e) {
                Log.d("ADK--", e.getMessage());
            }
        }
    }

}

