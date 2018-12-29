package com.joe.commom_library.widget.curveview;

import android.database.Observable;

/**
 * Created by lixindong on 6/15/17.
 */

public class DataObservable extends Observable<DataObserver> {
    public void notifyChanged() {
        synchronized(mObservers) {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }
}
