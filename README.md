王仔的淘淘商城
==========

### 商城的模块


```
├─paren
│  ├─common                 工具类
│  ├─manger-server          后台管理服务
│  │  ├─pojo                数据库实体类
│  │  ├─dao                 数据持久化
│  │  │  ├─interface        数据接口
│  │  │  └─mapper           mapper文件
│  │  ├─interface           发布接口
│  │  └─service             实现类
│  ├─manger-page            后台管理页面
│  ├─content-server         信息管理服务
│  │  ├─interface           发布接口
│  │  └─service             实现类
│  ├─portal-page            门户页面
│  ├─search-server          搜索服务
│  │  ├─interface           发布接口
│  │  └─service             实现类
│  ├─search-page            搜索页面
│  ├─itemDetails-page       商品详情页面
│  ├─sso-server             单点登录服务
│  │  ├─interface           发布接口
│  │  └─service             实现类
│  ├─login-page             登录页面
│  ├─cart                   购物车
│  ├─order                  拦截器服务
│  │  ├─interface           发布接口
│  │  └─service             实现类
│  └─order-page             拦截页面
└─pagehelper                分页插件
```


### 商城使用技术


> - 基本架构：`spring+springmvc+mybatis`
> - 项目构建：maven
> - 实现了SOA:dubbo+zookeeper注册中心
> - 缓存银弹：redis减轻门户网站缓存、部分商品详情的信息缓存
> - 单点登录：TOKEN+redis.实现登录,查询用户信息。
> - 商品搜索服务：SolrCloud+IKAnalyzer分词器
> - 消息详情异步上传solr中间件: activeMQ
> - 文件存存储：
        - 服务端
             - FastDFS_server 文件资源存储
             - Nginx 静态资源映射
             - FastDFS Nginx Moudle(解决集群环境下文件同步未完成问题)
        - 客户端
             - FastDFS_Client
> - 后台管理界面设计:easyUI
> - 前端JS框架：Jquery

### 环境搭建可以参考我CSDN的文章
http://my.csdn.net/qq_35101463

### 关于作者
mail:1103836847@qq.com

### 思维导图：
![cmd-markdown-logo](http://118.31.42.117/group1/M00/00/00/rBBSRFpnNhOACgKrAAGp0K_22DU094.png)

