package com.od.msbte_exam_pre;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Contact Us / Feedback")
                .setMessage("This App is made by \uD835\uDDE3\uD835\uDDF5\uD835\uDDFC\uD835\uDDFB\uD835\uDDF6\uD835\uDE05 \uD835\uDDD7\uD835\uDDF2\uD835\uDE03 for contributing and helping people those who want apps for improving there knowledge.\n\n \uD835\uDC02\uD835\uDC28\uD835\uDC27\uD835\uDC2D\uD835\uDC1A\uD835\uDC1C\uD835\uDC2D \uD835\uDC14\uD835\uDC2C \uD835\uDC0E\uD835\uDC27 Mail ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
