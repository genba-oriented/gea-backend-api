# Good Enough Architecture ~ Backend API

## 本リポジトリについて

本リポジトリは、弊社([合同会社 現場指向](https://www.genba-oriented.com))が「十分に良い(Good Enough)」と考える、バックエンドAPIのアーキテクチャとそのサンプルプログラムを公開したものです。フリーマーケットを題材にしたサンプルプログラム（フリマアプリという名前）を実装しています。フリマアプリの要件定義は、 [こちらのリポジトリ](https://github.com/genba-oriented/gea-requirements) をご参照ください。

公開しているプログラムやドキュメントは [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) ライセンスとしますので、自由にご利用いただいて構いません。本リポジトリをベースにして、個別の開発プロジェクトで適宜カスタマイズして利用することを想定しています。ただし、ご利用によって何かしらの不都合が生じた場合は自己責任となることをご了承ねがいます。また、内容は時間とともに変更していく予定です。

もし、プログラムにおかしなところがありましたら、Issueでご連絡いただけると大変ありがたいです。

## コンセプト

アプリケーションを開発する際の設計思想や技術には、さまざまなものが存在します。新規の開発プロジェクトでは、高度な設計思想や最新の技術を採用したくなるものです。

しかし、高度な設計思想や最新の技術を採用すれば、プロジェクトが円滑に進むという訳ではありません。逆に学習コストがあがったり、ブラックボックスな部分が多くなって、問題が起きたときにハマるリスクがあります。

本リポジトリのアーキテクチャは、できるだけ分かりやすく、ハマりにくいアーキテクチャをコンセプトに作成しています。

## 実行方法

### Postgresの起動とテーブルの用意

    docker run --name postgres-fleamarket -p 5432:5432 -e POSTGRES_USER=fleamarket -e POSTGRES_PASSWORD=fleamarket -e POSTGRES_DB=fleamarket -d postgres

    docker exec -it postgres-fleamarket psql -U fleamarket

    # psqlの画面に、src/test/resouces/schema.sql の中身を貼り付けて実行

### アプリケーションのビルド

    # Java21以上が必要です
    ./mvnw package

### 環境変数の設定

Javaを実行する前に、以下の環境変数を設定します。

<table>
<colgroup>
<col style="width: 33%" />
<col style="width: 33%" />
<col style="width: 33%" />
</colgroup>
<thead>
<tr class="header">
<th style="text-align: left;">環境変数名</th>
<th style="text-align: left;">説明</th>
<th style="text-align: left;">サンプル値</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td style="text-align: left;"><p>SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_AUDIENCES</p></td>
<td style="text-align: left;"><p>JWTの「aud」の値。JWTを検証する際にチェックする。IdPとしてGoogleを使用する場合は、クライアントIDの値が該当します</p></td>
<td style="text-align: left;"><p>aud01</p></td>
</tr>
<tr class="even">
<td style="text-align: left;"><p>SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI</p></td>
<td style="text-align: left;"><p>JWTを発行するIdP(Issuer)のURL</p></td>
<td style="text-align: left;"><p>https://accounts.google.com</p></td>
</tr>
</tbody>
</table>

### Javaの実行

    java -jar target/fleamarket-api-0.0.1-SNAPSHOT.jar

## 特徴

### トランザクションスクリプトパターンを採用したオーソドックスな業務ロジック

昨今の業務ロジックの実装の方法論として、ドメインモデルパターンに属するドメイン駆動設計が人気です。しかし、学習コストが高くなったり、初学者にとって難解なソースコードになるリスクがあります。

本リポジトリは、業務ロジックの一連の処理を手続き的に記載する、いわゆるトランザクションスクリプトパターンを採用しています。これにより、ソースコードが取っつきやすく分かりやすくなります。 トランザクションスクリプトパターンのデメリットである、1メソッドの処理が長くなったり冗長な記述が多くなりがちなことについては、サービス層のプログラムを積極的に分割することで対応するスタンスです。

### Domaを使用した軽量なデータアクセス

昨今のデータアクセスの実装の手段として、Spring Data JPAが人気です。Spring Data JPAを使用すると、開発者のコードが劇的に少なくなります。しかし、内部の処理がブラックボックスになったり、JPA自体の学習コストが高くなるリスクもあります。

本リポジトリでは、日本産のライブラリである [Doma](https://github.com/domaframework/doma)を使用しています。Domaは、2Way-SQL [1] が有名ですが、本リポジトリは2Way-SQLの機能は使用せずCriteria APIと呼ばれる機能を使っています。Criteria APIは、JPAのCriteria APIと似ており、Entityクラスからメタモデルを生成してタイプセーフにSQLを記述できます。また、セレクトしたレコードをEntityオブジェクトに変換してくれます。ただ、JPAのようにEntityのライフサイクルの管理や、キャッシュの制御は行いません。JPAから難易度が高い機能を削ぎ落とした軽量なORマッパーのイメージです。

## アーキテクチャ

### ソフトウェアアーキテクチャ

![sw architecture.drawio](./doc/sw-architecture.drawio.svg)

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<thead>
<tr class="header">
<th style="text-align: left;">役割名</th>
<th style="text-align: left;">説明</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td style="text-align: left;"><p>Controller</p></td>
<td style="text-align: left;"><p>リクエストを受け付けて、レスポンスを返す</p></td>
</tr>
<tr class="even">
<td style="text-align: left;"><p>Service</p></td>
<td style="text-align: left;"><p>業務ロジックを行う</p></td>
</tr>
<tr class="odd">
<td style="text-align: left;"><p>Repository</p></td>
<td style="text-align: left;"><p>データアクセスを行う</p></td>
</tr>
<tr class="even">
<td style="text-align: left;"><p>Entity</p></td>
<td style="text-align: left;"><p>業務的なデータを保持。基本的にテーブルと1対1でクラスを作成する</p></td>
</tr>
<tr class="odd">
<td style="text-align: left;"><p>Input</p></td>
<td style="text-align: left;"><p>クライアントからの入力データを保持する</p></td>
</tr>
<tr class="even">
<td style="text-align: left;"><p>Dto</p></td>
<td style="text-align: left;"><p>都合の良い単位でデータを保持する。主に、クライアントに返すデータとしてEntityが適さない場合に作成する。※Dtoは、Data Transfer Objectの略</p></td>
</tr>
</tbody>
</table>

### ドメイン間の依存関係

ドメイン間の依存関係を一方通行にすることで、プログラムの複雑化を防ぎます。

![domain dependencies.drawio](./doc/domain-dependencies.drawio.svg)

なお、ドメイン間の依存関係の違反をチェックするため、 [ArchUnit](https://www.archunit.org) を使用しています。

### パッケージ構造について

各ドメインのパッケージの下に、役割ごとのパッケージを配置しています。以下のようなイメージです。

    ドメインA
    ├── controller
    ├── service
    ├── entity
    ...
    ドメインB
    ├── controller
    ├── service
    ├── entity
    ...

## 技術スタック

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<thead>
<tr class="header">
<th style="text-align: left;">ライブラリ名</th>
<th style="text-align: left;">説明</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td style="text-align: left;"><p><a href="https://spring.io/projects/spring-boot">Spring Boot</a></p></td>
<td style="text-align: left;"><p>アプリケーション全体のフレームワーク。Spring Framework, Spring MVC, Spring Securityを含む</p></td>
</tr>
<tr class="even">
<td style="text-align: left;"><p><a href="https://github.com/domaframework/doma">Doma</a></p></td>
<td style="text-align: left;"><p>データアクセスのためのライブラリ</p></td>
</tr>
</tbody>
</table>

## 認証について

OIDCのトークン(JWT)をリクエストで受け取って妥当性を検証し、ユーザを特定しています。

## テストについて

### テスト用のデータベース

[Testcontainers](https://testcontainers.com)を使用してデータベースを用意しています。

### アーキテクチャのテスト

ドメイン間の依存の方向や、役割間の依存の方向に違反していないかを、 [ArchUnit](https://www.archunit.org)を使ってチェックしています。

## API仕様書について

API仕様書が、doc/rest-api/index.html に格納されています。 [こちら](https://htmlpreview.github.io/?https://github.com/genba-oriented/gea-backend-api/blob/main/doc/rest-api/index.html)で見栄え良く表示できます。のこの仕様書は、 [Spring REST Docs](https://spring.io/projects/spring-restdocs)を使用して、テストプログラムから自動的に仕様書の断片を生成しています。生成された断片を、doc/rest-api ディレクトリ配下のadocファイルが取り込んでいます。※adocファイルからhtmlファイルへの変換はIntelliJ IDEAで行っています。

[1] プログラムが読み込むテンプレートとしても使えるし、開発者がpsqlのようなツールで実行することもできるSQL
