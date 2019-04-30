package eu.faircode.email;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class SwipeListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public SwipeListener(Context context, final ISwipeListener listener) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            private static final int MOVE_THRESHOLD = 100;
            private static final int SPEED_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent me1, MotionEvent me2, float vx, float vy) {
                if (me1 == null || me2 == null)
                    return false;

                boolean consumed = false;
                float dx = me2.getX() - me1.getX();
                float dy = me2.getY() - me1.getY();
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (Math.abs(dx) > MOVE_THRESHOLD && Math.abs(vx) > SPEED_THRESHOLD)
                        try {
                            if (dx > 0)
                                consumed = listener.onSwipeRight();
                            else
                                consumed = listener.onSwipeLeft();
                        } catch (Throwable ex) {
                            Log.e(ex);
                        }
                }
                return consumed;
            }
        });
    }

    public boolean onTouch(@NonNull View view, @NonNull MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    interface ISwipeListener {
        boolean onSwipeRight();

        boolean onSwipeLeft();
    }
}