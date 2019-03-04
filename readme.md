# 七牛token获取工具分享

> 以前写安卓使用到了七牛进行图片上传获取外链，在网上搜索token的获取方法，都没有一个可靠一点的，各种模拟获取token..连官方文档也写着token加密方法不属于androidSDK文档范畴...
>
> 于是这次，本着学习javaweb的心态，花了一晚上的时间把官方服务器端sdk看了下，了解了加密方式，制作出了java后端token获取工具。虽然只有最基础的功能，但也算够用了，以后如果有需求再增加功能，欢迎大家star,fork

我的服务器还没有支持https，Android Pie版本默认无法访问http，请设置一下

> 如果post请求失败，可以换成这样的形式试试
>
> http://zzzia.net:8080/qiniu/?accessKey=1&secretKey=2&bucket=3
>
> 直接把参数写在url里，请求方式设置为post

[Android Pie对http的支持](https://blog.csdn.net/chinaboyliusir/article/details/82760127)

[在线post地址](http://zzzia.net:8080/qiniu/)


![img](https://github.com/Zzzia/qiniuToken/blob/master/qiniu.png)


post参数：

accessKey,secretKey,bucket

post示例：

```
accessKey:iN7NgwM31j4-BZacMjPrOQBs34UG1maYCAQmhdCV
secretKey:6QTOr2Jg1gcZEWDQXKOGZh5PziC2MCV5KsntT70j
bucket:qtestbucket
```



返回示例：

```json
{
    "status": 200,
    "info": "success",
    "token": "iN7NgwM31j4-BZacMjPrOQBs34UG1maYCAQmhdCV:n2BishAy561IogFhWBFsTccCKYY=:eyJzY29wZSI6InF0ZXN0YnVja2V0IiwiZGVhZGxpbmUiOjE1MTkzODI4NTB9"
}
```
