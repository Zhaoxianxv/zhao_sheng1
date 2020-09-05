package com.yfy.app.info_submit.utils;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class KeyboardUtil {
	private KeyboardView keyboardView;
	private Keyboard k;//
	private EditText ed;

	public KeyboardUtil(KeyboardView keyboardView, Context ctx, EditText edit, int id) {
		this.ed = edit;
		k = new Keyboard(ctx, id);
		this.keyboardView = keyboardView;
		this.keyboardView.setKeyboard(k);
		this.keyboardView.setEnabled(true);
		this.keyboardView.setPreviewEnabled(false);
		this.keyboardView.setVisibility(View.VISIBLE);
		this.keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}


		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_DELETE) {
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}
}
