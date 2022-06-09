package de.haw.riddle.ui.water;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.haw.riddle.R;
import lombok.Getter;

public class TipsList extends RecyclerView.Adapter<TipsList.ViewHolder> {

    private final List<String> tips = new ArrayList<>(0);

    public void addTip(String tip){
        tips.add(tip);
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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(tips.get(position));
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


