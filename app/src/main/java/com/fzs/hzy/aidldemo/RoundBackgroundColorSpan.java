package com.fzs.hzy.aidldemo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import com.zhy.autolayout.utils.AutoUtils;

public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int NormalSize;//textview设置的字体大小(单位px)
    private int bgColor;
    private int textColor;
    public RoundBackgroundColorSpan(int bgColor, int textColor, int textSize) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.NormalSize = textSize;
    }
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return ((int)paint.measureText(text, start, end))+ AutoUtils.getPercentWidthSize(44);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int color1 = paint.getColor();
        paint.setColor(this.bgColor);
        canvas.drawRoundRect(new RectF(x, top, x + ((int) paint.measureText(text, start, end)+ AutoUtils.getPercentWidthSize(30)), AutoUtils.getPercentWidthSize(NormalSize) + paint.descent()), AutoUtils.getPercentWidthSize(4), AutoUtils.getPercentWidthSize(4), paint);
        paint.setColor(this.textColor);
        canvas.drawText(text, start, end, x + AutoUtils.getPercentWidthSize(15), (AutoUtils.getPercentWidthSize(NormalSize) - paint.ascent())/2, paint);
        paint.setColor(color1);
    }
}
