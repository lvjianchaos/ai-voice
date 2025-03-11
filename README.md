# 🎤 AI语音合成教学软件 

[![Vue Version](https://img.shields.io/badge/Vue-3.3%2B-brightgreen)](https://vuejs.org/)[![TypeScript Version](https://img.shields.io/badge/TypeScript-5.0%2B-blue)](https://www.typescriptlang.org/)

**🌍 基于AI语音合成与声音克隆技术的智能教学平台**

## 📚 目录

- [✨ 核心功能](#✨ 核心功能)
- [🎯 用户价值](#🎯 用户价值)
- [🛠️ 技术架构](#️🛠️ 技术架构)
- [📂 项目结构](#📂 项目结构)
- [🚀 开发路线](#🚀 开发路线)
- [⚡ 快速启动](#⚡ 快速启动)
- [🔮 未来规划](#🔮 未来规划)

## ✨ 核心功能

### 🎙️ 声音管理
- **样本库管理**：支持音频上传/实时录制（5-30秒人声样本）
- **声音克隆**：基于短样本生成个性化语音模型

### 🗣️ 语音合成
- **智能讲解**：输入文本自动生成带情感语音（800-2000字）
- **参数调节**：实时调整语速/语调/节奏
- **多语言支持**：普通话/英语标准发音（可扩展方言）

### 📚 课件制作
- **PPT语音注入**：上传课件自动生成有声版本（3-20MB）
- **音视频处理**：支持声音替换与字幕同步

## 🎯 用户价值

- 🌟 **教育公平**：虚拟教师覆盖资源匮乏地区
- 🔍 **特殊教育**：为视障/阅读障碍者提供语音辅助
- 🎧 **语言学习**：提供标准发音示范与对比
- ⚡ **教学增效**：快速生成个性化教学素材

## 🛠️ 技术架构

### 💻 前端技术栈
- **框架**：Vue 3 + Composition API
- **状态**：Pinia 状态管理
- **类型**：TypeScript 5
- **UI库**：Element Plus
- **构建**：Vite 4

### 🌐 核心技术
```mermaid
graph TD
  A[用户界面] --> B(语音合成引擎)
  A --> C(声音克隆服务)
  B --> D[AI模型]
  C --> D
  D --> E{教学输出}
```

## 📂 项目结构

```bash
src/
├── 📁 api    # 接口服务
│	├── sound.ts    # 声音样本相关的API接口 
│	├── user.ts    # 用户相关的API接口 
├── 📁 assets    # 静态资源
│	├── favicon.ico    # 网站logo
├── 📦 components 
│   ├── 📁 layout	# 首页布局组件
│   │	├── AppHeader.vue    # 应用头部组件
│   │	├── AppHeader.vue    # 应用布局组件(头部+主要区域)
│   ├── 📁 layout2    # 系统页布局组件2
│   │	├── isCollapse.ts    # 控制组件的折叠状态
│   │	├── MainAside.vue    # 侧边栏组件
│   │	├── MainHeader.vue    # 顶部栏组件 
│   │	├── MainLayout.vue    # 应用布局组件(主要区域) 
├── 📝 composables    
│   ├── useSound.ts    # 集成声音样本相关操作
├── 🗺️ router    # 路由配置
│   ├── index.ts    # 配置Vue Router, 定义应用的路由规则和导航守卫
├── 📦 stores    # Pinia状态库
│   ├── mytoken.ts    # 管理用户的 token 信息
├── 🛠️ utils    # 工具函数
│   ├── request.ts    # 封装基于axios的HTTP请求，并添加了请求和响应拦截器
├── 🖼️ views    # 页面视图
│   ├── 📁 audio	# 音视频界面
│   │	├── AudioSubtitlesIndex.vue    # 声音置换+字幕组件
│   ├── 📁 courseware    # 课件制作界面
│   │	├── CoursewareIndex.vue    # 课件制作组件
│   ├── 📁 login	# 登陆界面
│   │	├── LoginView.vue    # 登陆组件
│   ├── 📁 register    # 注册界面
│   │	├── RegisterView.vue    # 注册组件
│   ├── 📁 sample	# 声音样本界面
│   │	├── DlgSampleCreateOrEdit.vue    # 对话框组件(新建/编辑)
│   │	├── SampleIndex.vue    # 声音样本组件
│   ├── 📁 voice    # 个性化语言输出界面
│   │	├── VoiceGuideIndex.vue    # 个性化语言合成组件
│   ├── AboutView.vue	# 关于界面
│   ├── AppIndexView.vue	# 主页默认页
│   ├── ErrorPage.vue	# 出错界面
│   ├── indexView.vue	# 系统默认页 
├── App.vue    # 根组件
└── main.ts    # 入口文件
```

## 🚀 开发路线

1. **🏠 主站开发**
   - 导航栏与基础布局
   - 用户登录/注册系统
2. **💡 核心功能**
   - 音频录制组件开发
   - 语音合成参数控制面板
   - PPT解析与语音注入模块
3. **🎨 增强功能**
   - 音视频编辑器（声音替换+字幕同步）

## ⚡ 快速启动

```bash
# 克隆仓库
git clone git@github.com:lvjianchaos/ai-voice.git

# 安装依赖
npm install

# 启动开发环境
npm run dev

# 构建生产版本
npm run build
```

## 🔮 未来规划

- 🌐 **多方言支持**：增加粤语/吴语等方言合成
- 🤖 **AI优化**：基于用户反馈的语音自适应优化
- 📱 **移动适配**：开发PWA渐进式Web应用
- 🎓 **教育生态**：接入在线教育平台API

> 🙌 欢迎通过Issue提交功能建议或通过PR参与贡献！
