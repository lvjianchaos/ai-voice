# 前端设计大纲

## 项目与环境说明

### 项目名称

基于AI语音合成的教学声音处理软件

### 功能

**（1）声音样本库：**可以预设一些标准声音，也可以通过上传音频文件，或现场录制，如录制老师的声音；音频文件和现场录制建议为5s到30s的单独人声。

**（2）个性化语音讲解：**上传或输入一段文本教学内容，长度在800~2000字，选择声音样本库的声音进行语音讲解；

**（3）标准语言输出：**根据给定的文本内容，长度在800~2000字，输出普通话、英文等标准发音。扩展性要求，可控制语速、语气和节奏等；

**（4）课件制作下载：**上传一个小型的教学课件(比如一个PPT)，大小在3M~20M，通过选择声音样本库声音，完成有声课件的制作，可下载；

**（5）可选功能：**声音置换及加字幕功能，导入一段音视频，通过更换声音，实现变声，同步展示字幕。

#### 归类

1. **声音管理**

- **声音样本库**：预设标准声音、上传音频或现场录制。
- **个性化语音讲解**：上传或输入文本，选择样本库声音进行语音讲解。
- **标准语言输出**：根据文本内容输出普通话、英文等标准发音，支持调整语速、语气、节奏等。

2. **课件制作**

- **课件制作下载**：上传教学课件（如PPT），通过选择声音样本库声音，生成有声课件并提供下载。

3. **音视频处理**

- 可选功能：
  - **声音置换功能**：更换音视频中的声音，实现变声效果。
  - **加字幕功能**：导入音视频，通过更换声音，生成同步的字幕。

##### 结构

```bash
声音管理
  ├─ 声音样本库
  ├─ 个性化语音讲解
  └─ 标准语言输出

课件制作
  └─ 课件制作下载

音视频处理
  ├─ 声音置换功能
  └─ 加字幕功能

```

### 环境

$Vue3+Ts+Pinia$

$node.js$ 版本：$v20.18.0$
$npm$ 版本：$10.8.2$

确认版本：

```shell
node --version
npm --version
```

## 项目创建与说明

```shell
npm init vue@latest // 创建项目

> npx
> create-vue


Vue.js - The Progressive JavaScript Framework

√ 请输入项目名称： ... frontend
√ 是否使用 TypeScript 语法？ ... 否 / 是
√ 是否启用 JSX 支持？ ... 否 / 是
√ 是否引入 Vue Router 进行单页面应用开发？ ... 否 / 是
√ 是否引入 Pinia 用于状态管理？ ... 否 / 是
√ 是否引入 Vitest 用于单元测试？ ... 否 / 是
√ 是否要引入一款端到端（End to End）测试工具？ » 不需要
√ 是否引入 ESLint 用于代码质量检测？ » 是
√ 是否引入 Prettier 用于代码格式化？ ... 否 / 是

正在初始化项目 D:\Desktop\ai-voice\frontend...

项目初始化完成，可执行以下命令：

  cd frontend
  npm install
  npm run format
  npm run dev
```

### 文件说明

#### 根目录

- .vscode：这个文件夹包含与 Visual Studio Code 编辑器相关的配置文件，如设置、调试配置等。用于在该编辑器中优化开发体验。

- node_modules：包含项目所依赖的所有第三方库和包。由 npm 或 yarn 自动生成和管理。

- public：这个文件夹通常包含静态资源文件（如图片、字体、favicon等），这些文件会被直接部署到服务器上，并不经过Webpack处理。

- .gitignore：这是Git的配置文件，指定哪些文件或文件夹应被Git忽略，例如 node_modules 或临时文件。

- index.html：应用的HTML模板文件，通常是单页应用（SPA）的入口文件，里面会包含`<div id="app"></div>`的挂载点，以及一些公共的HTML元素。

- package.json：包含项目的元数据（如名称、版本、描述等），同时列出所有依赖包、脚本命令等。npm 用这个文件来管理项目的依赖和配置。

- tsconfig.json：TypeScript配置文件，包含TypeScript编译器的选项。

- env.d.ts：声明文件，用于为TypeScript项目提供环境变量类型定义。

- eslint.config.js：配置文件，用于设置 ESLint 的规则，帮助检查代码质量和一致性。

- .prettierrc.json：Prettier配置文件，用于设置代码格式化规则，保证代码风格的一致性。

- package-lock.json：锁定依赖版本的文件，确保每次安装依赖时使用相同版本的库。

- tsconfig.app.json：TypeScript配置文件，可能是项目中专门为应用程序部分设置的配置。

- tsconfig.node.json：TypeScript配置文件，专门为Node.js环境设置的配置文件。

#### src 文件夹：源代码文件夹，包含实际的应用逻辑

- assets：存放应用中的静态资源文件，如图片、样式、字体等。
- components：存放Vue组件（假设这是一个Vue项目），每个组件通常包含模板、样式和脚本部分，组成可复用的UI元素。
- router：包含路由配置文件，用于设置不同URL与页面组件的映射关系。
- stores：如果使用状态管理库（如 Vuex 或 Pinia），那么这个文件夹存储着与状态管理相关的文件，管理应用的全局状态。
- views：存放视图组件，每个视图组件通常对应一个页面，包含从路由中映射的内容。
主要的代码文件：
- App.vue：Vue应用的根组件，通常用于定义布局和渲染子组件。
- main.ts：应用的入口文件，通常用来初始化应用程序（例如，挂载Vue实例，配置插件等）。

#### 配置文件

- vite.config.ts：Vite配置文件。Vite是一个现代前端构建工具，用来配置开发和生产环境的构建行为，例如路径别名、插件配置等。
- .editorconfig：编辑器配置文件，用于统一不同开发者和编辑器之间的代码风格（如缩进方式、字符编码等）。
- tsconfig.app.json 和 tsconfig.node.json：分别为应用程序和Node环境的TypeScript配置，帮助TypeScript进行正确的编译和类型检查。

### 插件及其配置

1. 安装 `Vue-Official` 以及 `ESlint`扩展
2. 使用`element-ui组件库`
   1. 安装：`npm install element-plus --save`
   2. 自动导入插件：`npm install -D unplugin-vue-components unplugin-auto-import`（原因：按需导入，项目工程会小很多）

**vite**配置添加：

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  // ...
  plugins: [
    // ...
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
})
```

解决一些编辑器的**报错**
> 我超，解决不来，就这样吧。

关于`Auto-import`的其他配置：

```ts
// vite.config.ts
  AutoImport({
      imports: ['vue', 'vue-router'],
      resolvers: [ElementPlusResolver()],
      eslintrc: { enabled: true },
    }),
```

**图标icon**导入

```shell
npm install @element-plus/icons-vue
```

自动导入安装：
```shell
npm i -D unplugin-icons
```

在`vite.config.ts`中添加以下代码：
```ts
import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'
···
    AutoImport({
      imports: ['vue', 'vue-router'],
      resolvers: [ElementPlusResolver(), IconsResolver()],
      eslintrc: { enabled: true },
    }),
    Components({
      resolvers: [ElementPlusResolver(), IconsResolver({ enabledCollections: ['ep'] })],
    }),
    Icons({ autoInstall: true }),
···
```

安装sass，样式

```shell
mpn install sass
```



具体的首页显示：

// 本人或采用 https://tongchaowei.github.io/small-tail-common-doc/ 组件 （正在抉择）
