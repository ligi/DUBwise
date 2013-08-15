package org.ligi.android.dubwise_mk.balance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.VesselData;

public class BalanceView extends View {


    private Paint blockPaint = new Paint();
    private Paint blockPaintGreen = new Paint();

    private final static int BLOCK_COUNT = 32;
    private int[][] heatmap = new int[BLOCK_COUNT][BLOCK_COUNT];

    int heatmap_max = 0;

    public BalanceView(Context context) {
        super(context);
        blockPaint.setStyle(Paint.Style.FILL);
        blockPaint.setColor(Color.BLUE);

        blockPaintGreen.setStyle(Paint.Style.FILL);
        blockPaintGreen.setColor(Color.GREEN);

        blockPaint.setTextSize(64);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);
        int block_size = canvas.getWidth() / BLOCK_COUNT;

        float multiply = (BLOCK_COUNT / 2f) / 128f;

        for (int x = 0; x < BLOCK_COUNT; x++)
            for (int y = 0; y < BLOCK_COUNT; y++) {

                if ((x - (BLOCK_COUNT / 2) == (int) (VesselData.centroid.getNick() * multiply)) &&
                        (y - (BLOCK_COUNT / 2) == (int) (VesselData.centroid.getRoll() * multiply))) {
                    heatmap[x][y]++;
                    if (heatmap[x][y] > heatmap_max)
                        heatmap_max = heatmap[x][y];

                }

                if (heatmap[x][y] == 0)
                    blockPaint.setColor(Color.BLUE);
                else
                    blockPaint.setColor(Color.rgb(255, 255 - (int) (heatmap[x][y] * (255f / heatmap_max)), 0));
                canvas.drawRect(new Rect(x * block_size + 1, y * block_size + 1, (x + 1) * block_size - 1, (y + 1) * block_size - 1), blockPaint);
            }


        int offset = block_size * BLOCK_COUNT;

        canvas.drawText("N: " + VesselData.centroid.getNick(), 10, offset + blockPaint.getTextSize(), blockPaint);
        canvas.drawText("R: " + VesselData.centroid.getRoll(), 10, offset + 2 * blockPaint.getTextSize(), blockPaint);
        canvas.drawText("Y: " + VesselData.centroid.getYaw(), 10, offset + 3 * blockPaint.getTextSize(), blockPaint);
        canvas.drawText("D: " + MKProvider.getMK().stats.threeD_data_count, 10, offset + 4 * blockPaint.getTextSize(), blockPaint);
        super.onDraw(canvas);

        postInvalidateDelayed(20);

    }

}
