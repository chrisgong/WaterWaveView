/**
 * 
 */
package com.cim120.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

/**
 * 水波动画控件
 */
@SuppressLint({ "NewApi", "HandlerLeak" })
public class WaterWaveView extends View {

	private Context mContext;

	private int mScreenWidth;
	private int mScreenHeight;

	private Paint mRingPaint;
	private Paint mRingPaintBottom;
	private Paint mWavePaint;
	private Paint mWavePaintBottom;

	private int mRingSTROKEWidth = 15;

	private int mRingColor = Color.WHITE;
	private int mWaveColor = Color.WHITE;

	private Handler mHandler;
	private long c = 0L;
	private boolean mStarted = false;
	private final float f = 0.033F;
	private int mAlpha = 50;// 透明度
	private float mAmplitude = 20.0F; // 振幅
	private float mWaterLevel = 0.45F;// 水高(0~1)
	private Path mPath;

	/**
	 * @param context
	 */
	public WaterWaveView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(mContext);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public WaterWaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(mContext);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(mContext);
	}

	public void setmWaterLevel(float mWaterLevel) {
		this.mWaterLevel = mWaterLevel;
	}

	private void init(Context context) {
		mRingPaint = new Paint();
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.FILL);
		mRingPaint.setAntiAlias(true);
		mRingPaint.setAlpha(mAlpha);
		mRingPaint.setStrokeWidth(mRingSTROKEWidth);

		mRingPaintBottom = new Paint();
		mRingPaintBottom.setColor(mRingColor);
		mRingPaintBottom.setStyle(Paint.Style.FILL);
		mRingPaintBottom.setAntiAlias(true);
		mRingPaintBottom.setAlpha(70);
		mRingPaintBottom.setStrokeWidth(mRingSTROKEWidth);

		mWavePaint = new Paint();
		mWavePaint.setStrokeWidth(1.0F);
		mWavePaint.setColor(mWaveColor);
		mWavePaint.setAlpha(mAlpha);
		mWavePaint.setAntiAlias(true);

		mWavePaintBottom = new Paint();
		mWavePaintBottom.setStrokeWidth(1.0F);
		mWavePaintBottom.setColor(mWaveColor);
		mWavePaintBottom.setAlpha(70);
		mWavePaintBottom.setAntiAlias(true);

		mPath = new Path();

		mHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0) {
					invalidate();
					if (mStarted) {
						// 不断发消息给自己，使自己不断被重绘
						mHandler.sendEmptyMessageDelayed(0, 100L);
					}
				}
			}
		};
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measure(widthMeasureSpec, true);
		int height = measure(heightMeasureSpec, false);
		setMeasuredDimension(width, height);
	}

	/**
	 * @category 测量
	 * @param measureSpec
	 * @param isWidth
	 * @return
	 */
	private int measure(int measureSpec, boolean isWidth) {
		int result;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		int padding = isWidth ? getPaddingLeft() + getPaddingRight()
				: getPaddingTop() + getPaddingBottom();
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else {
			result = isWidth ? getSuggestedMinimumWidth()
					: getSuggestedMinimumHeight();
			result += padding;
			if (mode == MeasureSpec.AT_MOST) {
				if (isWidth) {
					result = Math.max(result, size);
				} else {
					result = Math.min(result, size);
				}
			}
		}
		return result;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mScreenWidth = w;
		mScreenHeight = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 得到控件的宽高
		int width = getWidth();
		if (this.c >= 8388607L) {
			this.c = 0L;
		}
		// 每次onDraw时c都会自增
		c = (1L + c);
		float f1 = mScreenHeight * (1.0F - (0.25F + mWaterLevel / 2))
				- mAmplitude;
		// 与圆半径的偏移量
		float offsetWidth = mScreenWidth / 16 - width;
		int top = (int) (f1 + mAmplitude);
		mPath.reset();
		// 起始振动X坐标，结束振动X坐标
		int startX, endX;
		if (mWaterLevel > 0.50F) {
			startX = (int) (mScreenWidth / 16 + offsetWidth);
			endX = (int) (mScreenWidth / 2 + mScreenWidth / 4 - offsetWidth);
		} else {
			startX = (int) (mScreenWidth / 16 + offsetWidth - mAmplitude);
			endX = (int) (mScreenWidth / 2 + mScreenWidth / 4 - offsetWidth + mAmplitude);
		}
		// 波浪效果
		while (startX < endX) {
			int startY = (int) (f1 - mAmplitude
					* Math.sin(Math.PI
							* (2.0F * (startX + this.c * width * this.f))
							/ width));
			int startY2 = (int) (f1 - mAmplitude
					* Math.cos(Math.PI
							* (2.0F * (startX + this.c * width * this.f))
							/ width));
			canvas.drawLine(startX, startY, startX, top, mWavePaint);
			canvas.drawLine(startX, top + 100, startX, startY2 + 100,
					mWavePaintBottom);
			startX++;
		}
		/**
		 * 底部水区域
		 */
		canvas.drawRect(0, top, mScreenWidth, mScreenHeight, mRingPaint);
		canvas.drawRect(0, top + 100, mScreenWidth, mScreenHeight + 100,
				mRingPaintBottom);
		canvas.restore();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// Force our ancestor class to save its state
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.progress = (int) c;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		c = ss.progress;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		int _version = getSDKVersionNumber();

		if (_version >= 14) {
			// 关闭硬件加速，防止异常unsupported operation exception
			this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

	}

	public int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	/**
	 * @category 开始波动
	 */
	public void startWave() {
		if (!mStarted) {
			mStarted = true;
			this.mHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * @category 停止波动
	 */
	public void stopWave() {
		if (mStarted) {
			mStarted = false;
			this.mHandler.removeMessages(0);
		}
	}

	public boolean isStarted() {
		return mStarted;
	}

	/**
	 * @category 保存状态
	 */
	static class SavedState extends BaseSavedState {
		int progress;

		/**
		 * Constructor called from {@link ProgressBar#onSaveInstanceState()}
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			progress = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(progress);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
