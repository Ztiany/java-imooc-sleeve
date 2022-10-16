# CMS 项目

## 1 CMS 介绍

**CMS 的必要性**：

- 针对运营人员：方便操作
- 数据安全：包装数据的完整性

**动态分配权限是最核心能力**：不像 TO C 的产品，CMS 端的角色和分组在一开始时何难确定下来，比如有运营、产品、人力、财务、开发等各个角色。

**如何开发 CMS 系统**：最高效的方式不是从 0 开始开发，而是选择一个优秀的 CMS 框架。

有很多基础功能：

- 权限管理
- 运行时异常日志/操作日志/行为日志
- 图片上传：扩展名限制、文件大小限制、重复文件限制等等

## 2 Lin CMS 介绍

Lim CMS 的特点：

- 大部分 CMS 框架还是后端模板渲染的，而 Lin CMS 是前后端分离的
- 分为两个项目：后端（支持多种开发语言） + 前端 vue

具体可以参考 [Lim CMS](https://doc.cms.talelin.com/)，在[这里](http://sleeve.talelin.com/#/about)可以体现 sleeve 的 CMS 系统。

单页面应用程序：

- vue/react 就是用来支持单页面应用
- 但是单页面应用程序对 seo 的支持并不好

vue/react 适用于 CMS、WebAPP、H5，而对于传统的网站，并不适用于 vue/react，因为对 SEO 支持不好。当然使用 nuxt.js，可以让我们即使用 vue/react 开发传统网站，又能支持 seo，但是增加了复杂性。

### 配置 Lin CMS 后端

在 [lin-cms-spring-boot](https://github.com/TaleLin/lin-cms-spring-boot/tags) 页面，选择以 sleeve 开头的 tag。

对比：lin-ui 是一个纯粹的 sdk，而 lin-cms-spring-boot 一个 sdk + 具体的工程。

- 总有一些基础功能可以抽象为 sdk
- 但是全部做成 sdk 又很难用

但是，像 lin-cms-spring-boot 这样的既有 sdk 又是工程化的项目，是很难做到无缝升级的。

初始化 lin-cms-spring-boot：

1. 配置好 MySQL 数据库（直接选择上一个模块的 sleeve-official 数据库即可），直接打开 `resource/schema.sql`，右键，选择 run.schema.sql。
2. 所有表都以 lin 开头，lin_user 存储的是用户基础信息，用户的验证信息存储在 lin_user_identity 中。
3. 修改数据库配置文件 application-dev.yml

完成以上配置，运行项目，接口访问 <http://localhost:5000/>。

### 配置 Lin CMS 前端

等待 vue3 进入稳定版。

### Lin CMS 使用的 ORM

Lin CMS 使用的 MyBatis + MyBatisPlus。同时也可以使用 SpringBoot 自带的 JPA。

**MyBatis 对比 JPA**：

- MyBatis 是半自动化的 ORM，很多细节都交给开发者处理。
- JPA 的自动化的 ORM，已经处理了很多细节。

MyBatis 也支持使用注解编写 SQL。

## 3 MyBatisPlus入门与进阶

### 三种返回格式

- 直接返回 Json 数据
- 返回统一的格式：UnifiedResponse【方便前端处理】

### 数据组装：BO 的意义

Service 层返回 BO，Controller 层返回 VO。

### 一对多的删除

一对多的删除是否应该级联删除？

这个需要根据业务需求来决定，而不是技术层面。
   1. 比如如果 BannerItem 是不能独立存在的，它必须依附于一个 Banner，那么它应该被删除，这同时也决定了其不能被共享。
   2. 比如如果 BannerItem 是能独立存在的，不一定依附于一个 Banner，那么它不应该被删除，则 BannerItem 可以在 Banner 之间共享。
   3. 另外如果 BannerItem 是不能独立存在的，它必须依附于一个 Banner，但是考虑到以后还需要用到，也可以不删除。

### 带文件的表单

带文件的表单的提交应该分为两步：

1. 先提交文件到资源服务器生成 URL【也在前端完成】
2. 再提交表单

完整图片管理业务：业务服务器应该保存图片的各种信息，比如扩展名、大小、分辨率等等。比如云存储功能，只给用户 1 G 的免费空间。

提交文件到资源服务器生成 URL 的流程：

1. 前端提交文件给业务服务器，业务服务器做好相关校验。【避免暴露资源服务器的地址】
2. 业务服务器将图片提价到资源服务器，保留图片的 md5 值和相关信息。
3. 客户端访问图片则可以自己访问资源服务器。