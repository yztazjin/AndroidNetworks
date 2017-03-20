# JinNetwork
Network Tools By HttpClient Or HttpURLConnection Or OKHttpClient

# Https 
GET/POST请求
### GET
```java
Https.get(url)
     .addPathParam("page", currIndex+"")
     .build()
     .requestAsync();//进入请求队列
```
### POST
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

* POST
> addParam(String key, File file) 设置表单上传文件 

## Images 
图片加载
```Java
 Images.get().source(data.url)
             .useCache(false)
             .placeholder(R.drawable.shape_pre)
             .anim(android.R.anim.fade_in)
             .error(R.drawable.shape_err)
             .into(iv);
```

  
