package com.wdullaer.materialdatetimepicker.time;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wdullaer.materialdatetimepicker.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private static final String TAG = "GridAdapter";
    private final Drawable mDefaultDrawable;
    private final Drawable mPressedDrawable;
    private final Drawable mSelectedDrawable;
    private final int[] mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private View rootView;
    private int selectedPosition = -1;
    private ColorStateList mViewTextColor;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;

        public ViewHolder(View v, Drawable defaultDrawable, Drawable pressedDrawable, Drawable selectedDrawable, ColorStateList viewTextColor) {
            super(v);
            button = (Button) itemView.findViewById(R.id.item);

            //Create a dynamic state list drawable so we can use dynamically modified shape drawables
            // (already modified and passed in here). This allows us to use accentColor
            // and a darkened accent color.
            StateListDrawable sld = new StateListDrawable();
            if(pressedDrawable != null) {
                sld.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            }
            if(selectedDrawable != null) {
                sld.addState(new int[]{android.R.attr.state_selected}, selectedDrawable);
            }
            if(defaultDrawable != null) {
                sld.addState(new int[]{}, defaultDrawable);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable(sld);
            } else {
                button.setBackground(sld);
            }

            button.setTextColor(viewTextColor);
        }

        public View getButton() {
            return button;
        }
    }

    public GridAdapter(Drawable defaultDrawable, Drawable pressedDrawable, Drawable selectedDrawable, ColorStateList viewTextColor, int[] dataSet, int initialValue, OnItemClickListener onItemClickListener) {
        this.mDefaultDrawable = defaultDrawable;
        this.mPressedDrawable = pressedDrawable;
        this.mSelectedDrawable = selectedDrawable;
        this.mViewTextColor = viewTextColor;
        this.mDataSet = dataSet;
        for (int i = 0; i < dataSet.length; i++) {
            int value = dataSet[i];
            if(value == initialValue) {
                this.selectedPosition = i;
            }
        }
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_picker_item, viewGroup, false);
        return new ViewHolder(rootView, this.mDefaultDrawable, this.mPressedDrawable, this.mSelectedDrawable, mViewTextColor);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Button item = (Button) viewHolder.getButton();

        item.setSelected(position == selectedPosition);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the selected value immediately to avoid a delay causing the selection
                //to flash back to the normal state, which happens
                //if we wait for the onBindViewHolder to do it.
                v.setSelected(true);
                //Let the onBindViewHolder remove selected on the previously selected view since
                //there is no flashing.
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        int value = mDataSet[position];
        String formattedValue = String.valueOf(value);
        item.setText(formattedValue);

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}