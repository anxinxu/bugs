package com.anxinxu.bugs.nowebview;

import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;

import androidx.annotation.Nullable;

class NoopWebBackForwardList extends WebBackForwardList {
    @Nullable
    @Override
    public WebHistoryItem getCurrentItem() {
        return null;
    }

    @Override
    public int getCurrentIndex() {
        return -1;
    }

    @Override
    public WebHistoryItem getItemAtIndex(int index) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    protected WebBackForwardList clone() {
        return new NoopWebBackForwardList();
    }
}
