# 🎤 AI语音合成教学软件

[![Vue Version](https://img.shields.io/badge/Vue-3.5%2B-brightgreen)](https://vuejs.org/)
[![TypeScript Version](https://img.shields.io/badge/TypeScript-5.6%2B-blue)](https://www.typescriptlang.org/)
[![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Java Version](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)

**🌍 基于AI语音合成与声音克隆技术的智能教学平台**

## 最开始练手写的Web项目，非常之垃圾 :(

## ✨ 项目简介

本项目是一个基于AI语音合成与声音克隆技术的智能教学平台，旨在通过先进的AI技术提供个性化的教学体验。项目采用前后端分离架构，前端使用Vue 3 + TypeScript开发，后端使用Spring Boot 3开发。

## 🎯 核心功能

### 🎙️ 声音管理
- **样本库管理**：支持音频上传/实时录制（5-30秒人声样本）
- **声音克隆**：基于短样本生成个性化语音模型
- **多语言支持**：支持普通话、英语等多种语言的声音克隆

### 🗣️ 语音合成
- **智能讲解**：输入文本自动生成带情感语音（800-2000字）
- **参数调节**：
  - 语速调节（0.5x - 2.0x）
  - 语调控制
  - 情感表达
- **实时预览**：支持语音合成结果的实时试听

### 📝 字幕生成
- **音视频字幕**：支持上传音频/视频文件自动生成字幕
- **双语字幕**：支持生成中英双语字幕
- **字幕编辑**：提供字幕时间轴调整和文本编辑功能
- **格式支持**：支持MP4、AVI、MOV、MP3、WAV等格式

### 📚 课件制作
- **PPT语音注入**：上传课件自动生成有声版本
- **音视频处理**：支持声音替换与字幕同步
- **多媒体整合**：支持音视频与PPT的智能融合

## 🛠️ 技术架构

### 💻 前端技术栈
- **框架**：Vue 3.5 + Composition API
- **状态**：Pinia 2.3
- **类型**：TypeScript 5.6
- **UI库**：Element Plus 2.9
- **构建**：Vite 6.0
- **动画**：GSAP 3.12 + VueUse Motion
- **样式**：SASS + Element Plus 主题定制
- **HTTP**：Axios 1.7
- **工具库**：st-common-ui-vue3 0.14

### 🔧 后端技术栈
- **框架**：Spring Boot 3.3.5
- **语言**：Java 21
- **数据库**：MySQL 8.0
- **工具库**：
  - Hutool 5.8.11
  - Lombok 1.18.32
  - Apache POI 5.2.0
  - JavaCV 1.5.5
  - FFmpeg 4.3.2
  - OpenCV 4.6.0
- **AI服务**：DashScope SDK 2.18.0

## 📂 项目结构

```
project/
├── frontend/                # 前端项目目录
│   ├── src/                # 源代码
│   │   ├── api/           # 接口服务
│   │   ├── assets/        # 静态资源
│   │   ├── components/    # 组件
│   │   ├── composables/   # 组合式函数
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # Pinia状态管理
│   │   ├── utils/         # 工具函数
│   │   └── views/         # 页面视图
│   └── README.md          # 前端说明文档
│
└── backend/               # 后端项目目录
    ├── common/           # 公共模块
    ├── pojo/            # 实体类
    ├── service/         # 服务模块
    └── pom.xml          # Maven配置文件
```

## ⚡ 快速启动

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

### 后端启动
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## 🔮 未来规划

1. **功能扩展**
   - 支持更多语言的声音克隆
   - 增加更多音视频处理功能
   - 优化AI模型性能

2. **性能优化**
   - 优化音视频处理速度
   - 提升AI模型响应速度
   - 优化系统资源占用

3. **用户体验**
   - 优化界面交互
   - 增加更多自定义选项
   - 提供更多教学场景支持

4. **技术升级**
   - 升级AI模型版本
   - 优化系统架构
   - 提升系统稳定性

## 📄 许可证


本项目采用 MIT 许可证 
