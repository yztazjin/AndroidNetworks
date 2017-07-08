# JinNetwork
Network Tools By HttpClient Or HttpURLConnection Or OKHttpClient

# Https 
GET/POST请求
### GET
#### HTTP GET
```java
Https.get(url)
     .addPathParam("page", currIndex+"")
     .build()
     .requestAsync();//进入请求队列
```
### POST
#### HTTP POST
```Java
Https.post(url)// 默认使用Content-Type "application/json"
     .build()
     .requestAsync();//进入请求队列
     
Https.post(url, PostContentType.ApplicationForm)// 指定Content-Type "application/x-www-form-urlencoded"
     .build()
     .requestAsync();
   
Https.post(url, PostContentType.MultipartFormadata)// 指定Content-Type "multipart/form-data"
     .build()
     .requestAsync();
```
### SPECIAL
#### SPECIAL 自定义HTTP 请求方式 
* 1. SPECIAL 对应Client接口的special方法
* 2. 需要自定义实现Client接口
* 3. builder.setClient(client);设置自定义实现的Client对象 

### 参数支持
* 通用参数
> addParam(String key, Object value)  
> removeParam(String key)  
> addHeader(String key, String value)  
> removeHeader(String key)  
> setClientType(ClientType type) 设置网络客户端类型 A. ApacheHttpClient B.OKHttpClient C.URLConnection  
> setRequestClient(Client client) 设置本次请求用户自定义的网络请求客户端  
> setResponseFile(File file) 设置请求响应的源，设置该选项后，不会去网络获取资源，直接从设置的file中读取数据流  
> ...  
* GET 定制参数 
> setDownloadMode(File file) 设置为下载文件的本地存储文件  
* POST 定制参数
> addParam(String key, File file) 设置表单上传文件  

## Images 
图片加载
```Java
 Images.get().source(data.url)// 图片资源地址
             .useCache(ImageCacheType.AllCache)// 同时启用 内存缓存 磁盘缓存
             .placeholder(R.drawable.shape_pre)// 加载前的预置图片
             .anim(android.R.anim.fade_in)// 加载成功后的过渡动画
             .error(R.drawable.shape_err)// 加载失败后的图片
             .into(iv); // 要加载的View ImageView setImageBitmap 非ImageView setBackgroundDrawable
```

#### Images Source
* File file Images.get().source(file)
* File String url = "file://"+file.getAbsoultPath() Images.get().source(url)
* Network Images.get().source(url)

## Reflect
运行时注解注入

### 默认提供注解：
* 1 Callback ElementType.PARAMETER
> 使用默认提供的注解注入转换工具，Callback注解类型必须为 HTTPCallback类型 
* 2 Header ElementType.PARAMETER
> Http请求Header 
* 3 MethodType ElementType.METHOD
> Http请求Method GET/POST/SPECIAL 
* 4 Param ElementType.PARAMETER
> Http请求参数 
* 5 PathParam ElementType.PARAMETER
> Http请求路径匹配参数 xxx/{key}/xxx <=> @param(key) 
* 6 URLPath ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE
> 取最近注解的为准


## Response StatusCode
* -1  出现异常
* 800 图片加载 加载磁盘thumb缓存的状态码
* 801 图片加载 加载内存缓存的状态码
* 802 从磁盘文件中获取数据的状态码
  
