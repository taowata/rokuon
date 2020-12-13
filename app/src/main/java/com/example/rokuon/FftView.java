package com.example.rokuon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class FftView extends View {
    // ----------------------------
    // 定数
    // ----------------------------
    // ピーク値
    private static float FFT_PEAK_VALUE = (float) (128 * Math.sqrt(2));
    // 表示デシベル数の下限
    private static float DISPLAY_MINIMUM_DB = -30;
    // 表示する最小周波数
    private static float DISPLAY_MINIMUM_HZ = 35;
    // 表示する最大周波数
    private static float DISPLAY_MAXIMUM_HZ = 30000;
    // バンド表示の最小周波数
    private static float BAND_MINIMUM_HZ = 40;
    // バンド表示の最大周波数
    private static float BAND_MAXIMUM_HZ = 28000;
    // バンドのデフォルト数
    private static int BAND_DEFAULT_NUMBER = 16;
    // バンドの内側の表示オフセット
    private static float BAND_INNER_OFFSET = 4;
    // FFTデータの描画色ID
    private static int FFT_DATA_SHADER_START_COLOR_ID = android.R.color.holo_blue_dark;
    private static int FFT_DATA_SHADER_END_COLOR_ID = android.R.color.white;
    // 対数グリッドの色ID
    private static int LOG_GRID_COLOR_ID = android.R.color.secondary_text_light;

    // ----------------------------
    // 変数
    // ----------------------------
    // Viewのサイズ
    private int currentWidth_;
    private int currentHeight_;
    // FFTデータ
    private byte[] fftData_;
    // FFTデータの色
    private Paint fftDataPaint_;
    // 対数グリッドの座標データ
    private float[] logGridDataX_;
    private float[] logGridDataY_;
    // 対数グリッドの色
    private Paint logGridPaint_;
    // サンプリングレート
    private int samplingRate_;
    // 表示するバンドの数
    private int bandNumber_;
    // バンドの矩形
    private RectF[] bandRects_;
    // バンドを表示するか(非表示でパルスを描画)
    private boolean isBandEnabled_;
    // 対数の範囲 (10^xでいうxの数)
    private int minLogarithm_;
    private int maxLogarithm_;
    // 対数の区間あたりの幅 (e.g. 10^1から10^2と，10^2から10^3の描画幅は一緒)
    private float logBlockWidth_;
    // X方向の表示オフセット
    private float logOffsetX_;
    // バンド全体のX方向の表示域
    private int bandRegionMinX_;
    private int bandRegionMaxX_;
    // 個々のバンドの幅
    private int bandWidth_;
    // バンドのデータ
    private float[] bandFftData_;
    // データ表示用のシェーダ
    private LinearGradient fftDataShader_;

    // コンストラクタ
    public FftView(Context context) {
        super(context);
        initialize();
    }
    public FftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    public FftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    // 初期化
    private void initialize() {
        // 変数設定
        bandNumber_ = BAND_DEFAULT_NUMBER;
        isBandEnabled_ = true;
        // バーの領域確保
        bandRects_ = new RectF[bandNumber_];
        for(int i = 0; i < bandNumber_; ++i){
            bandRects_[i] = new RectF();
        }
        // データを格納する配列
        bandFftData_ = new float[bandNumber_];
        // ペイントの設定
        fftDataPaint_ = new Paint();
        fftDataPaint_.setStrokeWidth(1f);
        fftDataPaint_.setAntiAlias(true);
        logGridPaint_ = new Paint();
        logGridPaint_.setStrokeWidth(1f);
        logGridPaint_.setAntiAlias(true);
        logGridPaint_.setColor(getResources().getColor(LOG_GRID_COLOR_ID));
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        // Viewの高さ，幅が取れるのでそれらに依存した計算を行う
        currentHeight_ = getHeight();
        currentWidth_ = getWidth();
        calculateViewSizeDependedData();
    }

    // Viewのサイズを基に対数グリッドとバーの座標を計算
    private void calculateViewSizeDependedData() {
        // 対数の範囲を計算
        minLogarithm_ = (int) Math.floor(Math.log10(DISPLAY_MINIMUM_HZ));
        maxLogarithm_ = (int) Math.ceil(Math.log10(DISPLAY_MAXIMUM_HZ));
        // 対数の区間あたりの幅
        logBlockWidth_ = (float) (getWidth() / (Math.log10(DISPLAY_MAXIMUM_HZ) - Math.log10(DISPLAY_MINIMUM_HZ)));
        // X方向の表示オフセット
        logOffsetX_ = (float) (Math.log10(DISPLAY_MINIMUM_HZ) * logBlockWidth_);

        // グリッドの線の数を数えて領域を確保，座標を計算して格納
        // 縦
        int lineNumberX = 10 - (int) (DISPLAY_MINIMUM_HZ / Math.pow(10, minLogarithm_));
        lineNumberX += 9 * (maxLogarithm_ - minLogarithm_ - 2);
        lineNumberX += (int) (DISPLAY_MAXIMUM_HZ / Math.pow(10, maxLogarithm_ - 1));
        logGridDataX_ = new float[lineNumberX];
        int logGridDataCounterX = 0;
        int left = getLeft();
        int right = getRight();
        for(int i = minLogarithm_; i < maxLogarithm_; ++i){
            for(int j = 1; j < 10; ++j){
                float x = (float) Math.log10(Math.pow(10, i)*j) * logBlockWidth_ - logOffsetX_;
                if(x >= left && x <= right){
                    logGridDataX_[logGridDataCounterX] = x;
                    logGridDataCounterX++;
                }
            }
        }
        // 横
        int lineNumberY = (int) (Math.ceil(-DISPLAY_MINIMUM_DB / 10));
        float deltaY = (float) (getHeight() / -DISPLAY_MINIMUM_DB * 10);
        logGridDataY_ = new float[lineNumberY];
        int top = getTop();
        for(int i = 0; i < lineNumberY; ++i){
            logGridDataY_[i] = top + deltaY * i;
        }
        // 各々のバンドの座標を計算
        bandRegionMinX_ = (int) (Math.log10(BAND_MINIMUM_HZ) * logBlockWidth_ - logOffsetX_);
        bandRegionMaxX_ = (int) (Math.log10(BAND_MAXIMUM_HZ) * logBlockWidth_ - logOffsetX_);
        bandWidth_ = (int) (bandRegionMaxX_ - bandRegionMinX_) / bandNumber_;
        int bottom = getBottom();
        for(int i = 0; i < bandNumber_; ++i){
            bandRects_[i].bottom = bottom;
            bandRects_[i].top = bottom;	// バーが表示されないように
            bandRects_[i].left = bandRegionMinX_ + (bandWidth_ * i) + BAND_INNER_OFFSET;
            bandRects_[i].right = bandRects_[i].left + bandWidth_ - BAND_INNER_OFFSET;
        }

        // シェーダーを設定
        int color0 = getResources().getColor(FFT_DATA_SHADER_START_COLOR_ID);
        int color1 = getResources().getColor(FFT_DATA_SHADER_END_COLOR_ID);
        fftDataShader_ = new LinearGradient(0, bottom, 0, top, color0, color1, Shader.TileMode.CLAMP);
        fftDataPaint_.setShader(fftDataShader_);
    }

    // サンプリングレート
    public void setSamplingRate(int samplingRateInMilliHz) {
        samplingRate_ = samplingRateInMilliHz / 1000;
    }
    public int getSamplingRate() {
        return samplingRate_;
    }

    // 更新
    public void update(byte[] bytes) {
        fftData_ = bytes;
        invalidate();
    }

    // 描画
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Viewのサイズ変更があった場合，再計算
        if(currentWidth_ != getWidth() || currentHeight_ != getHeight()){
            calculateViewSizeDependedData();
        }
        // グリッド描画
        drawLogGrid(canvas);
        // 波形データがない場合には処理を行わない
        if (fftData_ == null){
            return;
        }
        // FFTデータの描画
        drawFft(canvas);
    }

    // FFTの内容を描画
    private void drawFft(Canvas canvas) {
        // Viewのサイズ情報
        int top = getTop();
        int height = getHeight();
        // データの個数
        int fftNum = fftData_.length / 2;
        // データをバンドに加工して表示
        if(isBandEnabled_){
            // データの初期化
            for(int i = 0; i < bandNumber_; ++i){
                bandFftData_[i] = 0;
            }
            // データを順に見ていく
            for(int i = 1; i < fftNum; ++i){
                // 注目しているデータの周波数
                float frequency = (float) (i * samplingRate_ / 2) / fftNum;
                // 表示位置から対応するバンドのインデックスを計算
                float x = (float) (Math.log10(frequency) * logBlockWidth_) - logOffsetX_;
                int index = (int) (x - bandRegionMinX_) / bandWidth_;
                if(index >= 0 && index < bandNumber_){
                    // 振幅スペクトルを計算
                    float amplitude = (float) Math.sqrt(Math.pow((float)fftData_[i * 2], 2) + Math.pow((float)fftData_[i * 2 + 1], 2));
                    if(amplitude > 0 ){
                        // 対応する区間で一番大きい値を取っておく
                        if(bandFftData_[index] < amplitude) {
                            bandFftData_[index] = amplitude;
                        }
                    }
                }
            }
            // バーの高さを計算して描画
            for(int i = 0; i < bandNumber_; ++i){
                float db = (float) (20.0f * Math.log10(bandFftData_[i]/FFT_PEAK_VALUE));
                float y = (float) (top - db / -DISPLAY_MINIMUM_DB * height );
                bandRects_[i].top = y;
                canvas.drawRect(bandRects_[i], fftDataPaint_);
            }
            // データをそのまま線分で表示
        }else{
            int bottom = getBottom();
            int right = getRight();
            int left = getLeft();
            // 直流成分(0番目)は計算しない
            for(int i = 1; i < fftNum; ++i){
                // 注目しているデータの周波数
                float frequency = (float) (i * samplingRate_ / 2) / fftNum;
                // 振幅スペクトルからデシベル数を計算
                float amplitude = (float) Math.sqrt(Math.pow((float)fftData_[i * 2], 2) + Math.pow((float)fftData_[i * 2 + 1], 2));
                float db = (float) (20.0f * Math.log10(amplitude/FFT_PEAK_VALUE));
                // 描画
                float x = (float) (Math.log10(frequency) * logBlockWidth_) - logOffsetX_;
                if(x >= left && x <= right){
                    float y = (float) (top - db / -DISPLAY_MINIMUM_DB * height );
                    canvas.drawLine(x, bottom, x, y, fftDataPaint_);
                }
            }
        }
    }

    // グリッド描画
    private void drawLogGrid(Canvas canvas) {
        // 横方向
        int bottom = getBottom();
        int top = getTop();
        for(float x: logGridDataX_) {
            canvas.drawLine(x, bottom, x, top, logGridPaint_);
        }
        // 縦方向
        int width = getWidth();
        for(float y: logGridDataY_) {
            canvas.drawLine(0, y, width, y, logGridPaint_);
        }
    }
}