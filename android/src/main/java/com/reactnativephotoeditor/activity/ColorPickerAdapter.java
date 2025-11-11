package com.reactnativephotoeditor.activity;

import com.reactnativephotoeditor.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private final List<Integer> colorPickerColors;
    private OnColorPickerClickListener onColorPickerClickListener;
    private int selectedPosition = 0; // Default to first color (white)

    ColorPickerAdapter(@NonNull Context context, @NonNull List<Integer> colorPickerColors) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = colorPickerColors;
    }

    ColorPickerAdapter(@NonNull Context context) {
        this(context, getDefaultColors(context));
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.color_picker_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int color = colorPickerColors.get(position);
        
        if (position == selectedPosition) {
            // Highlight selected color with a border
            buildColorPickerView(holder.colorPickerView, color, true);
        } else {
            // Normal color without border
            buildColorPickerView(holder.colorPickerView, color, false);
        }
    }

    @Override
    public int getItemCount() {
        return colorPickerColors.size();
    }

    private void buildColorPickerView(View view, int colorCode, boolean isSelected) {
        view.setVisibility(View.VISIBLE);

        // Create gray border circle - always same size
        ShapeDrawable borderCircle = new ShapeDrawable(new OvalShape());
        borderCircle.setIntrinsicHeight(24);
        borderCircle.setIntrinsicWidth(24);
        borderCircle.setBounds(new Rect(0, 0, 24, 24));
        borderCircle.getPaint().setColor(Color.parseColor("#9E9E9E")); // Gray border
        borderCircle.getPaint().setStyle(android.graphics.Paint.Style.FILL);
        
        // Create color circle
        ShapeDrawable colorCircle = new ShapeDrawable(new OvalShape());
        colorCircle.setIntrinsicHeight(18);
        colorCircle.setIntrinsicWidth(18);
        colorCircle.setBounds(new Rect(0, 0, 18, 18));
        colorCircle.getPaint().setColor(colorCode);
        
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{borderCircle, colorCircle});
        
        if (isSelected) {
            // Selected: shrink the inner color circle (6px padding from border)
            layerDrawable.setLayerInset(1, 9, 9, 9, 9); // 9px from edge = 6px padding + 3px for smaller circle
        } else {
            // Not selected: normal color circle (3px border)
            layerDrawable.setLayerInset(1, 3, 3, 3, 3);
        }
        
        view.setBackgroundDrawable(layerDrawable);
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    public void setSelectedColor(int colorCode) {
        // Find the position of the color in the list
        for (int i = 0; i < colorPickerColors.size(); i++) {
            if (colorPickerColors.get(i) == colorCode) {
                int previousPosition = selectedPosition;
                selectedPosition = i;
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View colorPickerView;

        public ViewHolder(View itemView) {
            super(itemView);
            colorPickerView = itemView.findViewById(R.id.color_picker_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Update selected position
                        int previousPosition = selectedPosition;
                        selectedPosition = position;
                        
                        // Notify adapter to refresh the items
                        notifyItemChanged(previousPosition);
                        notifyItemChanged(selectedPosition);
                        
                        // Call listener
                        if (onColorPickerClickListener != null)
                            onColorPickerClickListener.onColorPickerClickListener(colorPickerColors.get(position));
                    }
                }
            });
        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }

    public static List<Integer> getDefaultColors(Context context) {
        ArrayList<Integer> colorPickerColors = new ArrayList<>();
        // Basic colors
        colorPickerColors.add(ContextCompat.getColor(context, R.color.white));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.black));
        
        // Red tones
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_orange_color_picker));
        
        // Orange & Yellow
        colorPickerColors.add(ContextCompat.getColor(context, R.color.orange_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_color_picker));
        
        // Green tones
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_green_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.light_green_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.green_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.teal_color_picker));
        
        // Blue tones
        colorPickerColors.add(ContextCompat.getColor(context, R.color.sky_blue_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.blue_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.indigo_color_picker));
        
        // Purple & Pink
        colorPickerColors.add(ContextCompat.getColor(context, R.color.violet_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.deep_purple_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.pink_color_picker));
        
        // Brown & Grey
        colorPickerColors.add(ContextCompat.getColor(context, R.color.brown_color_picker));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.grey_color_picker));
        
        return colorPickerColors;
    }
}
