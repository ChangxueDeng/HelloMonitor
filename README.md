# Hello服务器监控
实现客户端上报数据，服务端进行数据监控，并通过Web端展示，一键SSH登录、子用户管理。

----

### 客户端
- 启动时向服务端注册
- 启动时获取机器基本信息并上报
- 半秒钟获取一次实时信息
- 定时10秒钟上报至服务端
- ....

### 服务端
- 客户端对服务端注册
- 监控客户端上报数据
- 将实时监控数据存入influxdb数据库
- 响应Web端请求
- ....
### Web端
- 展示客户端列表
  ![image](/png/index1.png "管理面板")
  ![image](/png/dark.png "夜间模式")
- 显示客户端详细信息
  ![image](/png/detail1.png "客户端详细")
- 远程SSH连接
  ![image](/png/ssh1.png "远程SSH1")
  ![image](/png/ssh2.png "远程SSH2")
- 添加子用户并分配机器
  ![image](/png/sub.png "子用户管理")
- ....


