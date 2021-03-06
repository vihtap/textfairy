package com.renard.documentview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.renard.ocr.R;
import com.renard.ocr.help.HelpActivity;

/**
 * Created by renard on 12/11/13.
 */
public class OCRResultDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = OCRResultDialog.class.getSimpleName();

    private final static String EXTRA_ACCURACY = "extra_ocr_accuracy";

    public static OCRResultDialog newInstance(int ocrAccuracy) {
        Bundle extra = new Bundle();
        extra.putInt(EXTRA_ACCURACY, ocrAccuracy);
        final OCRResultDialog ocrResultDialog = new OCRResultDialog();
        ocrResultDialog.setArguments(extra);
        return ocrResultDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setCancelable(true);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_ocr_result, null);
        TextView speech = (TextView) view.findViewById(R.id.help_header);
        final int accuracy = getArguments().getInt(EXTRA_ACCURACY);
        if (accuracy <= 75) {
            speech.setText(R.string.ocr_result_is_bad);
        } else if (accuracy < 83) {
            speech.setText(R.string.ocr_result_is_ok);
        } else {
            speech.setText(R.string.ocr_result_is_good);
            hideTips(view);
        }
        setButtonListeners(view);
        TextView accuracyView = (TextView) view.findViewById(R.id.accuracy_title);
        final String accuracyString = getActivity().getString(R.string.ocr_accuracy, accuracy);
        accuracyView.setText(accuracyString);
        builder.setNegativeButton(android.R.string.ok, null);
        builder.setView(view);
        return builder.create();
    }

    private void setButtonListeners(View view) {
        view.findViewById(R.id.button_show_tips).setOnClickListener(this);
        view.findViewById(R.id.button_copy_to_clipboard).setOnClickListener(this);
        view.findViewById(R.id.button_text_to_speech).setOnClickListener(this);
        view.findViewById(R.id.button_export_pdf).setOnClickListener(this);
        view.findViewById(R.id.button_share_text).setOnClickListener(this);
    }

    private void hideTips(View view) {
        view.findViewById(R.id.divider1).setVisibility(View.GONE);
        view.findViewById(R.id.button_show_tips).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        final DocumentActivity activity = (DocumentActivity) getActivity();
        if (activity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.button_show_tips:
                startActivity(new Intent(activity, HelpActivity.class));
                break;
            case R.id.button_copy_to_clipboard:
                activity.copyTextToClipboard();
                break;
            case R.id.button_text_to_speech:
                activity.startTextToSpeech();
                break;
            case R.id.button_export_pdf:
                activity.exportAsPdf();
                break;
            case R.id.button_share_text:
                activity.shareText();
                break;
        }
        final android.support.v4.app.Fragment fragmentByTag = activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragmentByTag != null) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragmentByTag).commitAllowingStateLoss();
        }
    }
}