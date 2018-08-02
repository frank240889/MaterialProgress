package mx.dev.materialprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * TODO: document your custom view class.
 */
public class MaterialProgress extends FrameLayout {
    public interface CancellableAction{
        void onCancel();
    }

    private static final String TAG = MaterialProgress.class.getName();
    private TextView mTextView;
    private Snackbar mSnackbar;
    private View mViewToAttach;
    private int mParentId;

    public MaterialProgress(Context context) {
        super(context);
        init(null, 0);
    }

    public MaterialProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaterialProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MaterialProgress, defStyle, 0);
        mParentId = a.getResourceId(R.styleable.MaterialProgress_attachToParent, -1);

        LayoutInflater.from(getContext()).inflate(R.layout.material_progress_layout, this, true);

        a.recycle();
    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();

        if(mParentId == -1){
            mViewToAttach = this;
        }
        else {
            View parent = (View) getParent();
            mViewToAttach = parent.findViewById(mParentId);
        }
        mTextView = findViewById(R.id.progress_message);
        mTextView.setVisibility(GONE);

        setVisibility(GONE);
    }

    public void showWithSnackbar(String message, final CancellableAction cancellableAction){

        if(mTextView.getVisibility() == VISIBLE)
            return;

        if(isVisible() && mSnackbar != null){
            mSnackbar.setText(message);
        }
        else {
            setVisibility(VISIBLE);
            mSnackbar = getSnackbar();
            mSnackbar.setText(message);
            if(cancellableAction != null) {
                mSnackbar.setAction(R.string.material_progress_cancel, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancellableAction.onCancel();
                        hideProgress();
                    }
                });
            }
            mSnackbar.show();
        }
    }

    /**
     * Shows the progressmaterial view with no cancel action in snackbar,
     * or updates the actual message if progressmaterial view is showing.
     * To hide the progress call {@link #hideProgress}
     * @param message The message as android resource to show in snackbar.
     */
    public void showWithSnackbar(int message){
        showWithSnackbar(getContext().getString(message));
    }

    /**
     * Shows the progressmaterial view with no cancel action in snackbar,
     * or updates the actual message if progressmaterial view is showing.
     * To hide the progress call {@link #hideProgress}
     * @param message The message as string to show in snackbar
     */
    public void showWithSnackbar(String message){
        showWithSnackbar(message,null);
    }

    /**
     * Shows the progressmaterial view with the message behind of progressbar
     * and no snackbar, or updates the actual message if progressmaterial view is showing.
     * @param message The message as string
     */
    public void show(String message){
        if(mSnackbar != null)
            return;

        if(isVisible()){
            mTextView.setText(message);
        }
        else {
            setVisibility(VISIBLE);
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(message);
        }
    }

    /**
     * Shows the progressmaterial view with the message behind of progressbar
     * and no snackbar, or updates the actual message if progressmaterial view is showing.
     * @param message The message as android resource
     */
    public void show(int message){
        show(getContext().getString(message));
    }

    /**
     * Creates a snackbar without SwipeDismissBehaviour as false,
     * meaning that is not swipable.
     * @return The snackbar created.
     */
    private Snackbar getSnackbar(){
        mSnackbar = null;
        final Snackbar snackbar = Snackbar.make(mViewToAttach, "", Snackbar.LENGTH_INDEFINITE);
        //Remove the funcionality to avoid the snackbar be swipeable
        snackbar.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams lp = snackbar.getView().getLayoutParams();
                if (lp instanceof CoordinatorLayout.LayoutParams) {
                    ((CoordinatorLayout.LayoutParams) lp).
                            setBehavior(new SwipeDismissBehavior<Snackbar.SnackbarLayout>(){
                        @Override
                        public boolean canSwipeDismissView(@NonNull View view){
                            return false;
                        }
                    });
                    snackbar.getView().setLayoutParams(lp);
                }
                snackbar.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return snackbar;
    }

    /**
     * Exposes the hide functionaliy
     */
    public void hideProgress(){
        hide();
    }

    /**
     * Check if progressmaterial is showing it.
     * @return True if is showing  it.
     */
    private boolean isVisible(){
        return getVisibility() == VISIBLE ;
    }

    /**
     * Hides the progressmaterial view.
     */
    private void hide(){
        if(!isVisible())
            return;

        mTextView.setText("");
        mTextView.setVisibility(GONE);
        if(mSnackbar != null) {
            mSnackbar.setAction(null, null);
            mSnackbar.dismiss();
        }
        mSnackbar = null;

        setVisibility(GONE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onDetachedFromWindow(){
        hide();
        mTextView = null;
        mSnackbar = null;
        mViewToAttach = null;
        super.onDetachedFromWindow();
    }
}
