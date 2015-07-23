###outline###

* 需要实现`WebViewClient`不然会redirect到手机自带浏览器
* 在WebViewClient里重载方法`shouldOverrideUrlLoading(WebView view, String url)`，判断url是否是在Dribbble注册的callback，如果是就拦截，说明用户授权已经完成，并获取access_token
* 设置webview的url的时候在url后面要跟上必要的参数，详情可以参考Dribbble的官方开发者文档。
* 已经获取到可以使用的access token，并能得到正确的用户信息，但是在出错处理这块，后续要需要再自定义一个适合自己的Request对象，和Gson库一起。

---
**REFERENCE:**  

* [OAuth api on dribbble.com](http://developer.dribbble.com/v1/oauth/)
* [Implementing OAuth 2.0 in an Android app](http://techblog.constantcontact.com/software-development/implementing-oauth2-0-in-an-android-app/)