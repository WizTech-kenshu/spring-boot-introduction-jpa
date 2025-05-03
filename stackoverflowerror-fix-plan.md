# StackOverflowError 解決計画

## 問題の概要

アプリケーションで`StackOverflowError`が発生しています。エラーのスタックトレースから、`Category`クラスと`Task`クラスの間の循環参照が原因であることがわかります。

```
java.lang.StackOverflowError: null
	at java.base/java.util.AbstractCollection.toString(AbstractCollection.java:449) ~[na:na]
	at org.hibernate.collection.spi.PersistentBag.toString(PersistentBag.java:590) ~[hibernate-core-6.2.6.Final.jar:6.2.6.Final]
	at java.base/java.lang.String.valueOf(String.java:4220) ~[na:na]
	at com.example.todo.entity.Category.toString(Category.java:9) ~[main/:na]
	at java.base/java.lang.String.valueOf(String.java:4220) ~[na:na]
	at com.example.todo.entity.Task.toString(Task.java:8) ~[main/:na]
```

## 問題の原因

1. `Category`クラスと`Task`クラスの間に双方向の関連があります：
   - `Category`クラスには`tasks`フィールド（`List<Task>`）があります（`@OneToMany`関係）
   - `Task`クラスには`category`フィールド（`Category`型）があります（`@ManyToOne`関係）

2. 両方のクラスにLombokの`@Data`アノテーションが付いています。このアノテーションは自動的に`toString()`、`equals()`、`hashCode()`などのメソッドを生成します。

3. 無限ループの発生プロセス：
   - `Category.toString()`が呼び出されると、`tasks`リストの各`Task`オブジェクトの`toString()`も呼び出されます
   - 各`Task.toString()`は`category`フィールドの`Category.toString()`を呼び出します
   - これが無限に繰り返され、最終的に`StackOverflowError`が発生します

4. 特に`CategoryController.showDetail`メソッドで`System.out.println(categoryEntity)`を実行した際に、このループが発生しています。

## 解決策

最も簡単で効果的な解決策は、Lombokの`@ToString.Exclude`アノテーションを使用して、循環参照を引き起こすフィールドを`toString()`メソッドから除外することです。

### 修正手順

1. `Category.java`の`tasks`フィールドに`@ToString.Exclude`アノテーションを追加：

```java
@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
@ToString.Exclude
private List<Task> tasks;
```

2. `Task.java`の`category`フィールドに`@ToString.Exclude`アノテーションを追加：

```java
@ManyToOne
@JoinColumn(name = "category_id")
@ToString.Exclude
private Category category;
```

これにより、`toString()`メソッドが循環参照を引き起こすことなく、エンティティの文字列表現を生成できるようになります。

## 代替解決策

1. カスタム`toString()`メソッドの実装：
   - Lombokの自動生成を使わず、循環参照を避けるカスタム`toString()`メソッドを実装する

2. `@Data`の代わりに個別のLombokアノテーションを使用：
   - `@Data`の代わりに、`@Getter`、`@Setter`、`@EqualsAndHashCode`などを必要に応じて使用し、`@ToString`は循環参照を避ける設定で使用する

3. DTOパターンの徹底：
   - エンティティを直接表示せず、常にDTOに変換してから表示する
   - 現在のコードでは一部でDTOを使用していますが、`CategoryController.showDetail`メソッドなど、一部でエンティティを直接使用している箇所があります