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

### 代码生成器

运行 test 目录下的 CodeGenerator，然后输入对应的表名，即可生成对应的 Controller/Service/Mapper/DO。

### 带文件的表单

带文件的表单的提交应该分为两步：

1. 先提交文件到资源服务器生成 URL【也在前端完成】
2. 再提交表单

完整图片管理业务：业务服务器应该保存图片的各种信息，比如扩展名、大小、分辨率等等。比如云存储功能，只给用户 1 G 的免费空间。

提交文件到资源服务器生成 URL 的流程：

1. 前端提交文件给业务服务器，业务服务器做好相关校验。【避免暴露资源服务器的地址】
2. 业务服务器将图片提价到资源服务器，保留图片的 md5 值和相关信息。
3. 客户端访问图片则可以自己访问资源服务器。

## 4 CMS 权限、双令牌机制解析

### 权限的重要性

对于 CMS 来讲，权限是非常重要的，权限有以下概念：

- 用户
- 分组
- 权限

设计思想：RBAC。

Lin CMS 里，用户必须属于一个分组，这样就简化了设计。Lin CMS 默认有一个 root 和 guest 分组。guest 和其他普通分组没有区别，就只是一个默认的普通分组而已。

guest 用于在管理员创建用用户时没有指定其分组的情况下，为用于指定默认属于 guest 分组。

权限必须分配给分组，不能直接分配给用户。

优秀的权限管理控制应该是具体到按钮级别的，而不是隐藏或展示某一个功能。

Lin CMS 里默认也有一个 root 用户，密码为 123456。

### Lin CMS 里的权限设计

有五种注解：

- LoginRequired：要求登录，即访问数据带有有效令牌。【针对那些任何用户都可以访问的接口】
- AdminRequired：要求用户登录，且是 属于 root 分组。
- PermissionMeta：用于标识接口对应的权限的元信息，比如权限名与所属权限模块。mount 字段表示权限元信息是否记录到数据库。
- GroupRequired：GroupRequired 和 PermissionMeta 应该结合使用。
- PermissionModule：用于加在 Controller 上，表示该 Controller 下所有的方法对于权限的权限模块，这样方法上的 PermissionMeta 注解就可以省去 module 字段，当然如果加上了，就具有更好的优先级。

Lin CMS 里的权限设计是自己实现的，没有使用 SpringSecurity 和 Shiro，这些框架过于庞杂，而其实自己实现一套权限管理并不是非常复杂的事。

**查看数据是否应该作为一项权限**？其实是应该的，比如某些机密数据不希望被所有用户看到。 Lin CMS 的默认是让用户可以看到数据的。

**更细粒度的控制**：数据库字段层面的控制，一般使用动态 SQL 实现，但是会导致代码非常复杂。Lin CMS 中并没有开放出来。

### 双令牌机制

```json
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjY2NDI2NTkxfQ.Z4ndw6vyezblNL32N5HPEWNb1tOUGNhL1UZvkKdqZmc",
    "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6MSwic2NvcGUiOiJsaW4iLCJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTY2OTAxNDk5MX0.tcBcnxwH_qGLC5TxlOfb6_eI357FnJ_bKipANPjXwY0"
}
```

单令牌机制的有效期问题，比如有效期只有 1 天，用操在即将到期时登录，然后操作了很久提交，发现过期了，需要重新登录，之前的操作都丢失了。这个体验就很不好。

双令牌就可以实现一个不打扰用户的体验。

- access_token 2 小时
- refresh_token 7 天

使用 refresh_token 可以在不登录的情况下获取新的 access_token。但是 refresh_token 也会到期啊，其实每次一个用 refresh_token 获取 access_token，refresh_token 自身的有效期也会延长。

使用 PostMan 测试 Lin CMS 中的权限：登录获取到 access_token 后，填入 Authorization 中。

![](imgs/token.png)

### JWT 令牌全球性问题

JWT 并不是为了解决安全问题，而是为了解决前后端分离，用户登录的问题。

需要在安全和用户体验之间找到一个平衡。

### 系统日志与行为日志

- 系统日志：常规意义上的日志，比如应用程序抛出的日志等。【Lin CMS 默认开启】
- 行为日志：用于记录用户的操作，回溯操作行为。【可在 Lin CMS 中配置，使用 `io.github.talelin.core.annotation.Logger` 注解】

```java
/**
* 虽然返回 BO 到前端不规范，但是也没必要死守规范。
*/
@GetMapping("/{id}")
@LoginRequired
@PermissionMeta("查询 Banner 及其 Item 数据")
//行为日志，目前需要配合 PermissionMeta 注解才生效（后续 lin cms 会更新，不需要也可以），行为存储在 lin_log 表中
@Logger(template = "{user.username} 查询了 Banner 及其 Item 数据。")
public BannerWithItemsBO getWithItems(@PathVariable @Positive Long id) {
    return bannerService.getWithItems(id);
}
```

### MyBatis 实体映射到 DO 的意义

不同于 JPA 有属性导航，MyBatis 中的关联查询如何映射到实体呢？这个时候就不要要求 DO 必须映射到某张表了，可以根据都关联查询的结果定义出一个 DO 出来，灵活处理。比如 SupDetailDO。

### MyBatis 关联查询

如果使用原生的 SQL，那么 MyBatisPlus 的 TableLogic 等注解就不生效了。

## 5 MyBatisPlus高级技巧

### 即时搜索的意义

这里的即时搜索其实就是延迟加载，按需加载，目的的为了缓解服务器压力。

### MyBatis 和 JPA

JPA 和 MyBatis 的查询差不多，JPA 的优点在于增删改上，JPA 支持级联操作，只要在对象上建立好映射关系，属于不同表的字段都会被 JPA 进行正确的处理。

### 自定义注解实现集合类型字段校验

对于前端提交的集合类型的数据，Spring 内置的校验没有提供支持，可以自定义校验注解。
