package de.haw.riddle.ui.water;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.haw.riddle.R;
import lombok.Getter;

public class TipsListAdapter extends RecyclerView.Adapter<TipsListAdapter.ViewHolder> {

    private final List<String> tips = new ArrayList<>(0);

    public void addTip(String tip){
        tips.add(tip);
        System.out.println("tip = " + tip);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return tips.get(position).hashCode();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tip, viewGroup, false);
        System.out.println("viewGroup = " + viewGroup + ", viewType = " + viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(tips.get(position));
        System.out.println("viewHolder = " + viewHolder + ", position = " + position);
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }


    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tvTip);
        }
    }
}


