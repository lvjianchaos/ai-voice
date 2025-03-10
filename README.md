# 前端项目文档

## 项目文档：AI语音合成教学软件

### 1. **项目概述**

本项目旨在开发一款基于语音合成和声音克隆技术的教学软件。通过结合AI驱动的语音合成技术和声音克隆，旨在提高教学效果、增强学习体验，并促进教育公平。该系统将提供个性化、自然的语音输出，帮助学习者，特别是在资源匮乏地区的学生或有特殊需求的学生，提升学习效果。

### 2. **用户期望**

- **提高教学效果**：通过灵活的语言风格和个性化的语音讲解，提升学生的学习体验，提高教学效果。
- **语言学习辅助**：帮助语言学习者练习发音和语言节奏，包括普通话、外语、特定方言等，提供高质量的语音样本供模仿和学习。
- **辅助特定教育**：为视障或阅读困难的学生提供文本转语音服务，帮助他们更好地获取信息和进行学习。
- **促进教育公平**：在教师资源不足的地区，通过声音克隆技术创建虚拟教师，为学生提供标准化的教学内容。

### 3. **功能需求**

1. 声音样本库：
   - 提供预设的标准声音库，支持音频文件上传或现场录制（录制时长为5至30秒的个体人声，如老师的声音）。
2. 个性化语音讲解：
   - 输入或上传一段教学内容（800~2000字），通过选择声音样本库中的声音生成语音讲解。
3. 标准语言输出：
   - 根据给定文本内容（800~2000字），输出普通话、英语等标准发音，支持语速、语调和节奏等参数的调整。扩展性要求：系统能够适应不同的语言和发音模式。
4. 课件制作与下载：
   - 支持上传小型教学课件（如PPT，大小为3MB至20MB），通过选择声音样本库中的声音生成有声课件，并允许用户下载生成的课件。
5. 可选功能：
   - **声音置换与字幕功能**：导入音频/视频文件后，系统能够更换声音并同步展示字幕。

### 4. **技术栈**

- **前端框架**：Vue3
- **状态管理**：Pinia
- **类型支持**：TypeScript
- **UI组件库**：Element Plus

### 5. **项目结构**

项目采用Vue 3 + Pinia + TypeScript构建，使用Element Plus组件库，文件结构如下：

- src：存放项目的主要代码和资源
  - **api**：与后端进行数据交互的接口
  - **assets**：存放静态资源，如图片、音频等
  - **components**：各个UI组件，如布局、头部、侧边栏等
  - **composables**：存放可复用的逻辑，如声音处理
  - **router**：前端路由配置
  - **stores**：Pinia状态管理配置
  - **utils**：工具函数，如数据格式化、网络请求等
  - **views**：各个页面的视图文件，如课程、注册、登录等

### 6. **开发步骤**

1. **主页**
   - 开发主页面，包含顶部导航栏，登录、注册等页面。
2. **系统页**
   - 开发主题布局，包含侧边栏、顶部导航栏、主题展示区。
3. **声音样本库**：
   - 开发音频上传和录制功能，支持用户上传音频文件或实时录制声音。
4. **个性化语音讲解**：
   - 开发文本输入框，允许用户输入教学内容并选择语音样本进行合成。
   - 实现标准化语音输出功能，并加入语速、语调、节奏等参数控制。
5. **课件制作与下载**：
   - 支持PPT文件上传，完成语音合成后提供下载链接。
6. **声音置换与字幕**：
   - 支持音视频文件导入，进行声音更换并同步显示字幕。

### 7. **安装与运行**

1. 克隆项目仓库：

   ```bash
   git clone git@github.com:lvjianchaos/ai-voice.git
   ```

2. 安装依赖：

   ```bash
   npm install
   ```

3. 启动开发服务器：

   ```bash
   npm run dev
   ```

### 8. **未来扩展**

- **多语言支持**：支持更多的语言合成和发音特性，如方言、外语等。
- **AI智能优化**：根据用户输入的语料进行智能优化，提供更自然的语音合成效果。
