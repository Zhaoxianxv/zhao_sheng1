package com.yfy.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


@SuppressLint("ClickableViewAccessibility")
public class CalendarCard extends View {

	private static final int TOTAL_COL = 7; //
	private static final int TOTAL_ROW = 6; //

	private Paint mCirclePaint; //
	private Paint mTextPaint; //
	private int mViewWidth; //
	private int mViewHeight; //
	private int mCellSpace; //
	private Row rows[] = new Row[TOTAL_ROW]; //
	private static CustomDate mShowDate; //
	public static CustomDate mSelectedDate;

	private OnCellClickListener mCellClickListener; //
	private int touchSlop; //
	private boolean callBackCellSpace;

	private Cell mClickCell;
	private float mDownX;
	private float mDownY;

	/**
	 *
	 * 
	 * @author wuwenjie
	 * 
	 */
	public interface OnCellClickListener {
		void clickDate(CustomDate date); //

		void changeDate(CustomDate date); //
	}

	public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CalendarCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CalendarCard(Context context) {
		super(context);
		init(context);
	}

	public CalendarCard(Context context, OnCellClickListener listener) {
		super(context);
		this.mCellClickListener = listener;
		init(context);
	}

	private void init(Context context) {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setColor(Color.parseColor("#F24949")); //
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		initDate();
	}

	private void initDate() {
		mShowDate = new CustomDate();
		fillDate();//
	}

	private void fillDate() {
		int monthDay = DateUtil.getCurrentMonthDay(); //
		int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1); //
		int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month); //
		int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);

		boolean isCurrentMonth = false;
		if (DateUtil.isCurrentMonth(mShowDate)) {
			isCurrentMonth = true;
		}
		boolean isSelectedMonth = false;
		if (DateUtil.isSameMonth(mSelectedDate, mShowDate)) {
			isSelectedMonth = true;
		}

		boolean isNextMonth = false;
		isNextMonth = DateUtil.isNextMonth(mShowDate);

		int day = 0;
		for (int j = 0; j < TOTAL_ROW; j++) {
			rows[j] = new Row(j);
			for (int i = 0; i < TOTAL_COL; i++) {
				int position = i + j * TOTAL_COL;
				if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
					day++;
					rows[j].cells[i] = new Cell(
							CustomDate.modifiDayForObject(mShowDate, day),
							State.CURRENT_MONTH_DAY,
							i,
							j
					);
					if (isCurrentMonth && day == monthDay) {
						CustomDate date = CustomDate.modifiDayForObject(
								mShowDate,
								day
						);
						rows[j].cells[i] = new Cell(
								date,
								State.TODAY,
								i,
								j
						);
					}
					if (isSelectedMonth && day == mSelectedDate.day) {
						if (rows[j].cells[i] != null && rows[j].cells[i].state == State.TODAY) {
							rows[j].cells[i].state = State.TODAY_SELECTED_DAY;
						} else {
							rows[j].cells[i] = new Cell(
									mSelectedDate,
									State.SELECTED_DAY,
									i,
									j
							);
						}
						mClickCell = rows[j].cells[i];
					} else if ((isCurrentMonth && day > monthDay) || isNextMonth) {
						rows[j].cells[i] = new Cell(
								CustomDate.modifiDayForObject(mShowDate, day),
								State.UNREACH_DAY,
								i,
								j
						);
					}
				} else if (position < firstDayWeek) {
					rows[j].cells[i] = new Cell(new CustomDate(
							mShowDate.year,
							mShowDate.month - 1,
							lastMonthDays - (firstDayWeek - position - 1)),
							State.PAST_MONTH_DAY,
							i,
							j
					);
				} else if (position >= firstDayWeek + currentMonthDays) {
					rows[j].cells[i] = new Cell(
							(new CustomDate(
									mShowDate.year,
									mShowDate.month + 1,
									position - firstDayWeek - currentMonthDays + 1
							)),
							State.NEXT_MONTH_DAY,
							i,
							j
					);
				}
			}
		}
		mCellClickListener.changeDate(mShowDate);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < TOTAL_ROW; i++) {
			if (rows[i] != null) {
				rows[i].drawCells(canvas);
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHeight = h;
		mCellSpace = Math.min(mViewHeight / TOTAL_ROW, mViewWidth / TOTAL_COL);
		if (!callBackCellSpace) {
			callBackCellSpace = true;
		}
		mTextPaint.setTextSize(mCellSpace / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float disX = event.getX() - mDownX;
			float disY = event.getY() - mDownY;
			if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
				int col = (int) (mDownX / mCellSpace);
				int row = (int) (mDownY / mCellSpace);
				if (isEffectClick(col, row)) {//
					measureClickCell(col, row);
				}

//				measureClickCell(col, row);
			}
			break;
		default:
			break;
		}

		return true;
	}
	//判断选着的日期范围
	private boolean isEffectClick(int col, int row) {
		if (col < 0 || col >= 7 || row < 0 || row >= 6) {
			return false;
		}
		State state = rows[row].cells[col].state;
//		return true;
//		return state == State.CURRENT_MONTH_DAY || state == State.TODAY;//可点击今天和今天以前时间
		return state == State.UNREACH_DAY||state == State.TODAY;//
	}

	/**
	 *
	 * 
	 * @param col
	 * @param row
	 */
	private void measureClickCell(int col, int row) {
		if (col >= TOTAL_COL || row >= TOTAL_ROW)
			return;
		if (mClickCell != null) {
			if (mClickCell.state == State.TODAY_SELECTED_DAY) {
				mClickCell.state = State.TODAY;
			} else {
				mClickCell.state = State.CURRENT_MONTH_DAY;
			}
			rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
		}

		if (rows[row] != null) {
			if (rows[row].cells[col].state == State.TODAY) {
				rows[row].cells[col].state = State.TODAY_SELECTED_DAY;
			} else {
				rows[row].cells[col].state = State.SELECTED_DAY;
			}
			mClickCell = new Cell(
					rows[row].cells[col].date,
					rows[row].cells[col].state,
					rows[row].cells[col].i,
					rows[row].cells[col].j
			);

			CustomDate date = rows[row].cells[col].date;
			date.week = col;

			mSelectedDate = new CustomDate(date);
			mCellClickListener.clickDate(date);

			//
			// update();

			invalidate();
		}
	}

	/**
	 *
	 * 
	 * @author wuwenjie
	 * 
	 */
	class Row {
		public int j;

		Row(int j) {
			this.j = j;
		}

		public Cell[] cells = new Cell[TOTAL_COL];
		public void drawCells(Canvas canvas) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null) {
					cells[i].drawSelf(canvas);
				}
			}
		}

	}

	/**
	 *
	 * 
	 * @author wuwenjie
	 * 
	 */
	class Cell {
		public CustomDate date;
		public State state;
		public int i;
		public int j;

		public Cell(CustomDate date, State state, int i, int j) {
			super();
			this.date = date;
			this.state = state;
			this.i = i;
			this.j = j;
		}



		public void drawSelf(Canvas canvas) {
			float centerX = mCellSpace * (i + 0.5f);
			float centerY = mCellSpace * (j + 0.5f);
			float radius = mCellSpace / 2f;
			switch (state) {
			case TODAY_SELECTED_DAY:
			case SELECTED_DAY:
				mTextPaint.setColor(Color.parseColor("#fffffe"));
				mCirclePaint.setColor(Color.parseColor("#87C126"));
				// canvas.drawCircle((float) (mCellSpace * (i + 0.5)),
				// (float) ((j + 0.5) * mCellSpace), mCellSpace / 3,
				// mCirclePaint);
				canvas.drawRoundRect(
						new RectF(
								centerX - radius,
								centerY - radius,
								centerX + radius,
								centerY + radius
						),
						10,
						10,
						mCirclePaint
				);
				break;
			case TODAY: //
				mTextPaint.setColor(Color.parseColor("#fffffe"));
				mCirclePaint.setColor(Color.parseColor("#F24949"));
				// canvas.drawCircle((float) (mCellSpace * (i + 0.5)),
				// (float) ((j + 0.5) * mCellSpace), mCellSpace / 3,
				// mCirclePaint);
				canvas.drawRoundRect(
						new RectF(
								centerX - radius,
								centerY - radius,
								centerX + radius,
								centerY + radius
						),
						10,
						10,
						mCirclePaint
				);
				break;
			case CURRENT_MONTH_DAY: //
				mTextPaint.setColor(Color.GRAY);
				break;
			case PAST_MONTH_DAY: //

			case NEXT_MONTH_DAY: //
				mTextPaint.setColor(Color.parseColor("#fffffe"));
				break;
			case UNREACH_DAY: //
				mTextPaint.setColor(Color.BLACK);
				break;
			default:
				break;
			}

			String content = date.day + "";
			canvas.drawText(
					content,
					(float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2),
					(float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(content, 0, 1) / 2),
					mTextPaint
			);
		}
	}

	enum State {
		TODAY,
		CURRENT_MONTH_DAY,
		PAST_MONTH_DAY,
		NEXT_MONTH_DAY,
		UNREACH_DAY,
		SELECTED_DAY,
		TODAY_SELECTED_DAY;
	}

	public void update() {
		fillDate();
		invalidate();
	}

	public void update(CustomDate date) {
		mShowDate = date;
		update();
	}

}
