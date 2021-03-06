// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.jsdialog;

import org.chromium.base.VisibleForTesting;
import org.chromium.base.annotations.CalledByNative;
import com.us.kinscape.R;
import org.chromium.chrome.browser.ChromeActivity;
import org.chromium.ui.base.WindowAndroid;
import org.chromium.ui.modaldialog.DialogDismissalCause;
import org.chromium.ui.modaldialog.ModalDialogManager;

/**
 * A dialog shown via JavaScript. This can be an alert dialog, a prompt dialog, a confirm dialog,
 * or an onbeforeunload dialog.
 */
public class JavascriptAppModalDialog extends JavascriptModalDialog {
    private long mNativeDialogPointer;

    /**
     * Constructor for initializing contents to be shown on the dialog.
     */
    private JavascriptAppModalDialog(String title, String message, String promptText,
            boolean shouldShowSuppressCheckBox, int positiveButtonTextId,
            int negativeButtonTextId) {
        super(title, message, promptText, shouldShowSuppressCheckBox, positiveButtonTextId,
                negativeButtonTextId);
    }

    @CalledByNative
    public static JavascriptAppModalDialog createAlertDialog(
            String title, String message, boolean shouldShowSuppressCheckBox) {
        return new JavascriptAppModalDialog(
                title, message, null, shouldShowSuppressCheckBox, R.string.ok, 0);
    }

    @CalledByNative
    public static JavascriptAppModalDialog createConfirmDialog(
            String title, String message, boolean shouldShowSuppressCheckBox) {
        return new JavascriptAppModalDialog(
                title, message, null, shouldShowSuppressCheckBox, R.string.ok, R.string.cancel);
    }

    @CalledByNative
    public static JavascriptAppModalDialog createBeforeUnloadDialog(
            String title, String message, boolean isReload, boolean shouldShowSuppressCheckBox) {
        return new JavascriptAppModalDialog(title, message, null, shouldShowSuppressCheckBox,
                isReload ? R.string.reload : R.string.leave, R.string.cancel);
    }

    @CalledByNative
    public static JavascriptAppModalDialog createPromptDialog(String title, String message,
            boolean shouldShowSuppressCheckBox, String defaultPromptText) {
        return new JavascriptAppModalDialog(title, message, defaultPromptText,
                shouldShowSuppressCheckBox, R.string.ok, R.string.cancel);
    }

    @CalledByNative
    void showJavascriptAppModalDialog(WindowAndroid window, long nativeDialogPointer) {
        assert window != null;
        ChromeActivity activity = (ChromeActivity) window.getActivity().get();
        // If the activity has gone away, then just clean up the native pointer.
        if (activity == null) {
            nativeDidCancelAppModalDialog(nativeDialogPointer, false);
            return;
        }

        // Cache the native dialog pointer so that we can use it to return the response.
        mNativeDialogPointer = nativeDialogPointer;
        show(activity, ModalDialogManager.ModalDialogType.APP);
    }

    @CalledByNative
    private void dismiss() {
        dismiss(DialogDismissalCause.DISMISSED_BY_NATIVE);
        mNativeDialogPointer = 0;
    }

    @Override
    protected void accept(String promptResult, boolean suppressDialogs) {
        if (mNativeDialogPointer != 0) {
            nativeDidAcceptAppModalDialog(mNativeDialogPointer, promptResult, suppressDialogs);
        }
    }

    @Override
    protected void cancel(boolean buttonClicked, boolean suppressDialogs) {
        if (mNativeDialogPointer != 0) {
            nativeDidCancelAppModalDialog(mNativeDialogPointer, suppressDialogs);
        }
    }

    /**
     * Returns the currently showing dialog, null if none is showing.
     */
    @VisibleForTesting
    public static JavascriptAppModalDialog getCurrentDialogForTest() {
        return nativeGetCurrentModalDialog();
    }

    private native void nativeDidAcceptAppModalDialog(
            long nativeJavascriptAppModalDialogAndroid, String prompt, boolean suppress);
    private native void nativeDidCancelAppModalDialog(
            long nativeJavascriptAppModalDialogAndroid, boolean suppress);
    private static native JavascriptAppModalDialog nativeGetCurrentModalDialog();
}
