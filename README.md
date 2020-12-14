# 録音アプリ

## アプリ概要
* 録音アプリ

音源リスト画面 | 録音画面 | 再生画面
---- | ---- | ----
<img src="https://user-images.githubusercontent.com/57245344/102087876-7e7e0200-3e5d-11eb-9fde-4153ef93a425.jpg" width="320"/>   | <img src="https://user-images.githubusercontent.com/57245344/102087904-850c7980-3e5d-11eb-8182-0631e16110bb.jpg" width="320"/>   | <img src="https://user-images.githubusercontent.com/57245344/102087918-8b025a80-3e5d-11eb-809d-36347f3f904d.jpg" width="320"/>

## 機能
* 録音機能(一時停止、再開)
* 録音ファイルの保存
* 録音ファイルのリスト表示

## 工夫した点
* 疎結合を意識した実装を行った。
* シングルトンの実装にLich Componentを用いた。

## やりきれなかった点

* 収録中音声の波形表示
* ExoPlayerの再生画面のカスタマイズ
* 保存したファイルの削除、編集
  
  

## アーキテクチャ
![スクリーンショット 2020-10-30 21 29 59](https://user-images.githubusercontent.com/57245344/97705276-29796d00-1af7-11eb-8a5b-6f8aa57c41be.png)

## ライブラリ
* Android Jetpack
  * Foundation
    * AppCompat
    * Android KTX
  * Architecture
    * Data Binding
    * Lifecycles
    * LiveData
    * Navigation
    * Room
    * ViewModel
  * UI
    * Fragment
    * ConstraintLayout
    * RecyclerView
    * Material Design
* Third party
  * Kotlin Coroutines
  * ExoPlayer
  * Lich Component

