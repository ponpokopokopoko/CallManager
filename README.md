# Sales Call Management App

電話営業の記録と成績分析を行うWebアプリケーションです。  
営業結果の記録、日次・週次・月次の集計、オペレーター全体の比較などをサポートします。

## スクリーンショット

- ![ログイン画面](screenshot.png)
<img  height="250" alt="スクリーンショット 2025-10-07 152241" src="https://github.com/user-attachments/assets/445a4d74-9506-4473-a79e-a63be7ac2718" />

- ![管理者メニュー](screenshot.png)
<img height="250" alt="スクリーンショット 2025-10-07 152406" src="https://github.com/user-attachments/assets/db5f9383-bf32-4e20-973d-7255f3e075f6" />

- ![トップメニュー](screenshot.png)
<img height="250" alt="スクリーンショット 2025-10-07 152456" src="https://github.com/user-attachments/assets/f816c77b-0aaa-462e-a2f9-8e1e587aca3d" />

- ![データ入力画面](screenshot.png)
<img height="250" alt="スクリーンショット 2025-10-07 155942" src="https://github.com/user-attachments/assets/cb4fc193-fd5b-4604-91bb-ea82cbdf541c" />

- ![全体比較](screenshot.png)
<img height="250" alt="スクリーンショット 2025-10-07 160029" src="https://github.com/user-attachments/assets/47f16a9d-2902-4bfe-810a-ed2f48e2c596" />

- ![出退勤画面](screenshot.png)
<img height="350"  alt="スクリーンショット 2025-10-07 152543" src="https://github.com/user-attachments/assets/f674ebe1-d434-436e-b8cf-be07c72dd32d" />


## 主な機能
### 認証・ユーザー管理
- ユーザーと管理者のロール管理
- ログイン・ログアウト機能
- ユーザーの新規作成・削除

### 勤怠管理
- 出勤・退勤の打刻機能

### 営業活動管理
- 営業結果の入力・編集・削除

### 集計・分析
- ユーザーごとの営業結果の集計
- 全ユーザーの成績比較

## 使用技術
- フロントエンド：Thymeleaf, JavaScript, HTML, CSS
- バックエンド：Spring Boot
- データベース：MySQL
- 開発ツール：Eclipse (Windows環境)

## 工夫点
- 集計結果をDBに保存し、フロント表示を高速化
- ロール（ユーザー/管理者）を分けて権限管理を実装
- オペレーターごとの成績比較表にソート機能を実装し、各指標ごとに並べ替えが可能
