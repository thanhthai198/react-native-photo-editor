package com.reactnativephotoeditor.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.reactnativephotoeditor.R;

public class EraserBSFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {

    public EraserBSFragment() {
        // Required empty public constructor
    }

    private Properties mProperties;

    public interface Properties {
        void onEraserSizeChanged(int eraserSize);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_eraser_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SeekBar sbEraserSize = view.findViewById(R.id.sbEraserSize);
        sbEraserSize.setOnSeekBarChangeListener(this);
    }

    public void setPropertiesChangeListener(Properties properties) {
        mProperties = properties;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int id = seekBar.getId();
        if (id == R.id.sbEraserSize) {
            if (mProperties != null) {
                mProperties.onEraserSizeChanged(i);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

