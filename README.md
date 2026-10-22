# Sandbox

**Minecraft 26.2 · NeoForge 26.2.0.93 · Java 25**

> 本模组是 [KevinT742MinecraftMod](../README.md) 家族仓库的成员 **sandbox**，
> 源码位于仓库的 `sandbox/` 目录。

Sandbox 是这个模组家族的**探针**。它故意做得很小，因为它只有一个目的：

> **证明"再开一个模组"真的只是照抄一个目录的事。**

它证明四件事：

1. 它是一个**独立的 git 仓库**，有自己的历史与发布节奏；
2. 它只靠 `implementation project(':base')` 就用上了 Genesis 的契约；
3. 它注册自己的方块与物品，**材质完全由 Genesis 的模板推导**；
4. 它在游戏里读到了 Genesis 的**魔力场**。

---

## 一、它怎么用上 Genesis

```groovy
dependencies {
    // ★ 这一行就是"家族"二字的全部含义
    implementation project(':base')
}
```

```java
// Genesis 把模板翻译成 Minecraft 的方块属性
public static final DeferredBlock<Block> PROBE_BLOCK_REG =
        BLOCKS.registerSimpleBlock("probe_block", GenesisApi.blockProperties(PROBE_BLOCK));

// Genesis 给出这个维度里的魔力
Optional<MagicConcentration> magic = GenesisApi.concentration(level, player.getX(), player.getZ());
```

**没有别的东西。** 探针不知道魔力是怎么算的、怎么存的，也不知道星球定义从哪来——
它只知道 `GenesisApi` 给它什么。

## 二、它注册了什么

| 内容 | 说明 |
|---|---|
| **探针方块** `probe_block` | 一块普通石头。硬度、爆炸抗性、挖掘速度、声音**全部**由 `MaterialFamily.rock(...)` 推导，探针一个字都没写 |
| **探针碎片** `probe_shard` | 一样魔力材料。Genesis 的魔法模型判定它是**魔力材料**、可炼药 |

两个东西的材质档案都注册进了 Genesis 的 `MaterialRegistry`，
所以探针的方块和 Genesis 的方块在同一个世界里被同样对待。

## 三、它在游戏里做什么

* 服务器启动时，把**主世界所在星球**和**出生点的魔力浓度**打进日志；
* 玩家每两秒在动作栏看到脚下的**魔力浓度**与**强度**。

如果这些数字会变（有人在旁边做了仪式），就说明魔力场是活的；
如果服务器重启后数字还在，就说明它被存下来了。

---

## 四、工程结构

```
sandbox/src/main/java/sandbox/
├── SandboxMod.java        模组入口：注册自己的方块与物品
├── SandboxBlocks.java     方块与物品（材质由 Genesis 的模板推导）
├── SandboxEvents.java     读 Genesis 的星球与魔力
└── dev/SandboxSelfTest.java  自检：证明契约可用
```

自检刻意**自己搭模板**而不是用模组里那一份，这样测的是**契约**而不是碰巧用了它的代码。

它用**窄类路径**编译（只有各成员的类，没有游戏），这机械地证明了
`api` / `core` 层不依赖 Minecraft。

---

## 五、构建与运行

命令在**家族根目录**执行，用 `:sandbox:` 指定成员：

```bash
./gradlew :sandbox:build         # 构建
./gradlew :sandbox:runClient     # 启动客户端
./gradlew :sandbox:runServer     # 启动服务端
./gradlew :sandbox:selfTest      # 运行自检
```

离线验证（够不到 `maven.neoforged.net` 时）：

```powershell
cd verification
pwsh -File fetch26.ps1         # 首次：下载 Minecraft 26.2
pwsh -File prep.ps1            # 解包并生成编译类路径
pwsh -File build-and-test.ps1  # 编译全族 + 跑全族自检
```

> 在成员目录里单独跑 Gradle 是构建不了的：成员依赖根 `gradle.properties` 的版本号
> 和根 `build.gradle` 的插件声明，所以**必须在装配好的母文件夹里构建**。

---

## 六、许可

MIT
