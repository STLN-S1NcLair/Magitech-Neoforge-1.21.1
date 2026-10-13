# Magitech レシピツリーとゲームプレイ進行

## このメモの範囲

- `src/generated/resources/data/magitech/recipe` の生成済みレシピ 388 件、各機械の実装、ワールド生成、戦利品、Glistening Lexicon のガイド記述を確認した。
- ここでは装飾用の木材・石材の派生レシピを個別には列挙せず、サバイバルでの到達順、分岐、実際の入手経路を中心にまとめる。
- 数値はレシピ JSON 上の必要マナ量であり、注入祭壇は必要量を貯めてから開始し、完了時にまとめて消費する。通常の注入は 200 tick（10 秒）かかる。

| レシピ種別 | 件数 | ゲーム上の位置付け |
| --- | ---: | --- |
| 通常クラフト | 128 | 設備、マナ輸送、装飾、ブロック圧縮 |
| 石切り | 62 | 石材の装飾派生。進行の必須条件ではない |
| フィールド効果 | 51 | 熱・冷却環境内での変換 |
| 注入 | 50 | 中核素材、強化設備、アクセサリー、最終装備 |
| 部品素材 / 部品切削 / 組立 | 38 / 12 / 10 | 素材特性付きツールと杖 |
| るつぼ | 17 | ポーション、流体錬金、上位金属・防具素材 |
| かみ砕き / 圧縮 | 4 / 1 | 後半の自動資源加工 |
| 製錬・溶鉱炉 | 6 / 5 | 鉱石・アメジストの基本精錬 |
| 魔法変換 | 4 | 序盤の Magitech 素材への入口 |

## 全体ツリー

```text
初回ログイン
  └─ Glistening Lexicon（Enercrux + ランダムな追加スペルを所持）
       ├─ [通常素材] Engineering Workbench + Assembly Workbench
       │    └─ 部品を切削・組立 → 杖 / 近接武器 / 採掘・農業ツール
       │         └─ Enercrux で落ちた素材を魔法変換
       │              ├─ 原木 → Mystwood log
       │              ├─ 石 → Vesperite
       │              ├─ 蛍石 → Mana Charged Fluorite
       │              └─ ファントムの皮膜 → Alchaefabric
       │
       └─ Mystwood + Vesperite + 蛍石 → Infusion Altar
            ├─ 手動マナ供給（Enercrux / Mana Charged Fluorite）
            ├─ High Purity Fluorite / Aspect Crystal / Alchecrysite
            ├─ 鉄 + 亜鉛 + Weaver の凝集ストランド → Fluxium
            │    └─ Fluxium enclosure と高圧素材
            │         ├─ Infuser・強化マナ輸送・マナ生産
            │         ├─ Crusher / Compressor・アイテム搬送・フィールド加工
            │         └─ 指輪・検査ゴーグル・終盤装備
            └─ （並行）Tourmaline + 鉄 → Zardius Crucible
                 └─ Mana Potion / 各種ポーション / 絶縁ガラス / 上位素材
```

重要なのは、**Fluxium が「手動の魔法」から「マナ機械と自動化」へ移る境目**であることです。祭壇・るつぼ・ツール作成は Fluxium 前にも始められますが、Fluxium enclosure を要求する設備群からは戦闘と高圧素材の確保が必要になります。

## 0. 初期状態と探索

1. 初回ログイン時に `Glistening Lexicon` が 1 冊自動配布される。中には `Enercrux` と、Enercrux 以外から選ばれたランダムなスペルが登録済みである。通常クラフトの「本 + 蛍石」レシピは、紛失後などの再入手用である。
2. Lexicon を Threadbound スロットに装備し、スペルメニューから選択する。Enercrux を使うには、後述する杖またはスタッフを用意する。
3. 序盤に集める主な資源は、蛍石・トルマリン・亜鉛鉱石、鉄・金・銅、石・原木、アメジスト、石炭である。亜鉛は `raw_zinc` をかまどまたは溶鉱炉で精錬する。アメジストの欠片は精錬すると Citrine になる。
4. Mana Berry Bush は Mana Berry を供給し、後で Mana Potion の原料になる。オーバーワールドに湧く敵対 Mob の Weaver は、Fluxia / Luminis / Noctis のいずれかの凝集ストランドを落とす。これは Fluxium の必須素材であり、明確な戦闘ゲートになっている。

## 1. ツール作成と最初の魔法変換

### 1-1. 作業台を作る

- `Engineering Workbench` は、剥いだ原木・板材・石で作る部品切削用の作業台。
- `Assembly Workbench` は同じく通常素材で作る部品組立用の作業台。
- `Repairing Workbench` と `Tool Hanger` も同段階で作れる。修理は「そのツールで使用している素材のいずれか 1 個 + 鉄インゴット」を使い、5 回で耐久を完全回復する実装である。

### 1-2. 部品を選んで組み立てる

Engineering Workbench に対応素材を入れると、素材レシピが定義された 38 種から部品素材を選び、任意の部品を切り出せる。素材ごとの特性は完成ツール全体へ作用し、部品ごとに別の素材を使える。

| 部品 | 消費素材数 | 主な用途 |
| --- | ---: | --- |
| Catalyst / Handguard / Light Handle / Tool Binding | 1 | 杖・剣などの基礎部品 |
| Conductor / Heavy Handle / Light Blade / Plate / Reinforced Rod / Spike Head | 2 | 杖、ツール、武器 |
| Strike Head | 3 | 斧・ハンマー用 |

- 最初の杖は `Catalyst + Conductor + Light Handle + Tool Binding`。
- 杖以外は、短剣・軽剣・大剣・ピッケル・ハンマー・斧・シャベル・鎌・スタッフの 9 種。Assembly Workbench ではレシピで定められた部品を順不同で組み立てる。
- 手近な石、剥いだ原木、銅、鉄、蛍石だけでも最初の杖を構成できる。高級素材へ一気に進む必要はない。

### 1-3. Enercrux で序盤素材を作る

Enercrux を**地面に落としたアイテム**へ当てると、次の魔法変換が行われる。

| 入力 | 出力 | 用途 |
| --- | --- | --- |
| 任意の原木 | Mystwood Log | 祭壇、マナ容器、初期機械の木材 |
| `c:stones` | Vesperite | 祭壇・マナノード・台座の基本石材 |
| 蛍石 | Mana Charged Fluorite | プレイヤーまたはマナ設備へ 40,000 マナを充填する消耗品 |
| ファントムの皮膜 | Alchaefabric | 終盤の Aegis Weave 用 |

Enercrux をマナを受け取れるブロックへ照射すると、1 回で 10,000 マナを直接注入できる。このため、Mana Charged Fluorite を用意する前でも、プレイヤーのマナ回復を待ちながら初期の祭壇・るつぼを手動運用できる。

## 2. 注入祭壇：中盤の素材基盤

### 2-1. 祭壇と基本マナ網

- `Infusion Altar` は **Vesperite、Mystwood Planks、蛍石**でクラフトする。中央スロットがベース素材、半径 2 ブロック以内の `Pedestal Pylon` が追加素材置き場になる。
- 祭壇の下に `Mana Vessel` を置くと、祭壇が必要なマナを自動補充できる。Mana Vessel は Mystwood enclosure・蛍石ブロック・Mystwood・金塊から 2 個作れる。
- `Mana Node` は Mana Vessel 等の接続口と無線接続を橋渡しし、`Mana Relay` はより長距離の無線中継を担う。まずは祭壇へ手動充填し、必要になった時点で Vessel → Node / Relay を増やす流れになる。

### 2-2. 最優先の注入レシピ

| 生成物 | ベース / 追加素材 | マナ | 何を開くか |
| --- | --- | ---: | --- |
| Mana Charged Fluorite | 蛍石 | 4,000 | 40,000 マナの携帯充填源 |
| High Purity Fluorite | Mana Charged Fluorite x2 | 10,000 | 強化設備・自動化設備・アクセサリー |
| Vesperite | 石 | 2,000 | 魔法変換と同じ結果を祭壇で量産 |
| Mystwood Log | 原木 | 2,000 | 魔法変換と同じ結果を祭壇で量産 |
| Alchecrysite x8 | Vesperite x8、High Purity Fluorite x2、Citrine x1、Tourmaline x2、金塊 x6 | 10,000 | Fluxium 後の機械筐体 |
| Fluxium Ingot x16 | 鉄 x8、亜鉛 x8、凝集ストランド x1 | 40,000 | 高圧素材と全自動化の核 |

Aspect Crystal も祭壇の主要な並行目標である。ベースには **トルマリン、クォーツ、または Restraint Quartz** を使い、40,000 マナと素材を注入する。例として、石炭 / Citrine / ブレイズロッドから Ember、方解石 / 氷 / クォーツから Glace、銅 / レッドストーンから Surge、アメジスト / 金から Magic、苗木 / 水から Flow、エンダーパール等から Hollow を作れる。これらは機械、指輪、ポーション、最終装備の要求素材になる。

### 2-3. Fluxium が作れた後

Fluxium Ingot と Mystwood Planks から `Fluxium Enclosure` を作る。以後の「Alchecrysite + High Purity Fluorite + Fluxium enclosure」を含むレシピ群が自動化・高出力設備の本体である。

- `Infuser`: 祭壇をベースに 40,000 マナで作る上位注入設備。容量・流量が大きく、強化マナ設備の入口になる。
- `Enhanced Mana Vessel`、`Enhanced Mana Node`、`Enhanced Mana Relay`: 既存設備をベースに 100,000 マナで強化する。Vessel は大容量化、Node / Relay は無線段階の上昇。
- `Mana Collector`: 空気中のマナを自動収集する。ただし近接配置で効率が競合する。
- `Thermal Mana Furnace`: 燃料を消費して高速にマナを生産する。大量注入・るつぼ処理用の発電源。
- `Mana Strander` / `Mana Receiver`: マナ弾を長距離へ射出・受信する。`Mana Junction` は全方向の配線分岐、`Mana Pump` は一方向への吸引・排出（レッドストーンで停止可能）。

## 3. るつぼによる流体錬金（Fluxium 前から並行可能）

`Zardius Crucible` は鉄インゴットとトルマリンで作れる。マナ接続を用意して水または指定流体と材料を入れ、以下を進める。

1. **Mana Potion**: 水 1,000 mB + Mana Berry x2 + 20,000 マナ。ポーション錬金と絶縁ガラスの基材。
2. **Mana Insulating Glass**: Mana Potion 1,000 mB + Mana Charged Fluorite x2 + Tourmaline x4 + ガラス x4 + 10,000 マナ → 6 個。これを通常クラフトして `Alchemical Flask` を 4 個作り、各種ポーションや硫酸を持ち運ぶ。
3. **各属性ポーション / Healing Potion / Sulfuric Acid**: Mana Potion または水 1,000 mB と対応する結晶・素材、10,000 マナ。属性ポーションは一時的な属性出力強化、Healing Potion は回復用、Sulfuric Acid は後述の電池素材となる。
4. **上位素材**: Hollow Potion を使う Ender Metal、Lava と多数の結晶を使う Aegis Weave、Nether Star Brilliance、Radiant Steel がある。大量の結晶、ネザースター、ダイヤ、グロウイカ墨を要求するため終盤目標である。

`Alchaefabric + Obsidian + Glace / Phantom Crystal + 金 / ダイヤ + Lava` から Aegis Weave を作り、さらに大量の High Purity Fluorite・Aspect Crystal・Fluxium と合わせることで、`Aether Lifter` / `Flamglide Strider`（各 500,000 マナ）へ到達する。

## 4. Fluxium 後の自動加工・搬送・環境加工

### 4-1. アイテム加工と搬送

- `Crusher`: Amethyst Block → Amethyst Shard x4、Redstone Crystal → Redstone x3、Quartz Plant x4 → Restraint Quartz Dust、Sulfur Block → Sulfur x3。
- `Compressor`: Restraint Quartz Dust x4 → Restraint Quartz。これは Aspect Crystal のベース素材の代替となる。
- `Entangler` / `Detangler`: マナ弾にアイテムを結び付け、接続コンテナから取り出し・格納する。
- `Item Collector`: 周囲のドロップを回収して接続コンテナへ入れる。稼働中はマナを消費する。

### 4-2. フィールド効果

`Heat Burner` は熱、`Chiller` は冷却の影響範囲を作る。単独の影響は `Heated` / `Cold`、同種を重ねると `Scorching` / `Freezing`、熱と冷却を重ねると `Thermal Shock` になる。対象アイテムまたは流体を稼働範囲に入れて処理する。

| 効果 | 代表的な変換 | 進行上の役割 |
| --- | --- | --- |
| Heated | 通常の精錬レシピ、Slime Ball → 緑の染料 | 簡易加熱 |
| Scorching | Stone → Lava 1,000 mB | 溶岩資源化 |
| Cold | 水 → 氷、土 → 粘土、Magma Cream → Slime Ball、Glowstone → Amethyst Block | 資源変換 |
| Freezing | Redstone x4 → Redstone Crystal、Magma Block → Obsidian、氷 → Packed / Blue Ice、Andesite → Calcite、Blaze Rod → Breeze Rod | 希少素材・結晶素材の獲得 |
| Thermal Shock | 酸化銅を段階的に脱酸化、Soul Sand ↔ Soul Soil、Prismarine Shard ↔ Crystal、水 → 雪ブロック | 銅修復・相互変換 |

`Envirometer` は周囲のフィールド状態を確認するための観測機器であり、複数機の配置調整に使う。

## 5. 戦闘・魔法・装備の伸ばし方

- **スペル運用**: 初期 Lexicon にある Enercrux が加工・設備充填を兼ねる。追加で配布されるランダムページと、入手した Thread Page を選択して杖 / スタッフで使う。スタッフは杖より強力だが詠唱が遅い。
- **指輪**: `Fluxium Ring` を通常クラフトし、High Purity Fluorite、各 Aspect Crystal、または Weaver の凝集ストランド 4 個を祭壇で注入する（40,000 マナ）。マナ量、属性魔法、移動、防御などの特化を選べる。
- **Spectacles of Inspection**: Mana Insulating Glass x2、High Purity Fluorite x4、Fluxium x4、革 x4、Phantom Crystal と 500,000 マナを必要とする。設備の状態確認を楽にするため、安定発電後の優先度が高い。
- **魔導書系**: `Materials and Toolcraft Design` は部品・強化向け、`The Fire That Thinks` と `Applied Arcane Circuitry` は高いマナ属性を持つ Threadbound 装備である。後者は Weaver の 3 種すべての凝集ストランドを要求するため、明確に終盤寄りとなる。
- **装飾・建築**: Fluorite / Tourmaline / Vesperite / Alchecrysite と 3 種の木材は、通常クラフトおよび石切りでブロック・階段・ハーフ・壁へ派生する。これらは設備進行とは独立した建築分岐である。

## 実際にプレイする際の推奨順

1. 初期本を受け取り、石・原木・鉄・銅・蛍石を集める。
2. Engineering Workbench と Assembly Workbench を作り、石 / 木 / 銅 / 鉄などで杖を組む。
3. Enercrux で原木・石・蛍石を変換し、Mystwood・Vesperite・Mana Charged Fluorite を確保する。
4. Infusion Altar、Pedestal Pylon、Mana Vessel / Node を作る。Enercrux と Mana Charged Fluorite で祭壇を手動充填する。
5. Tourmaline・Zinc・Citrine・Weaver の凝集ストランドを集める。祭壇で High Purity Fluorite、Aspect Crystal、Alchecrysite、Fluxium を作る。
6. Fluxium Enclosure を起点に、Mana Collector または Thermal Mana Furnace、Infuser、強化 Vessel / Node / Relay を設置してマナ基盤を自動化する。
7. 目的に応じて、Crusher / Compressor とアイテム搬送、Heat Burner / Chiller のフィールド加工、Zardius Crucible のポーション・上位素材へ枝分かれする。
8. 指輪・検査ゴーグル・Radiant Steel・Aegis Weave・特殊ブーツへ進み、戦闘と移動性能を強化する。

## データ上で確認できた未接続・注意点

- Lexicon の「Repairing」項目は `magitech:crafting/upgrade_workbench` を参照するが、生成済みレシピに Upgrade Workbench は存在しない。そのため、ツール**修理**はできても、ガイドにあるツール**強化**は現状のサバイバルレシピでは開始できない。
- Lexicon の魔法変換ページは `magitech:spell_conversion/mystwood` を参照するが、実際のレシピ ID は `magitech:spell_conversion/mystwood_log`。表示リンクは修正が必要である。
- Weaver の凝集ストランドにはレシピによる代替入手経路がなく、Fluxium と Applied Arcane Circuitry を作るには戦闘ドロップが必須である。
- 388 件のうち、石切り 62 件と通常クラフト内の木材・石材派生の大半は建築用であり、上の主進行を飛ばす近道にはならない。

## 参照した主な実装・データ

- レシピ: `src/generated/resources/data/magitech/recipe/`
- 注入祭壇とマナ供給: `InfusionAltarBlockEntity.java`、`ManaChargedFluoriteItem.java`、`Enercrux.java`
- 初期配布: `PlayerFirstSpawnEvent.java`
- ガイド上の想定進行: `src/main/resources/data/magitech/modonomicon/books/glistening_lexicon/`
- Weaver のスポーン・ドロップ: `src/main/resources/data/magitech/neoforge/biome_modifier/weaver_spawn.json`、`src/generated/resources/data/magitech/loot_table/entities/weaver.json`
