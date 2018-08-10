参考
spring security oauth2.0 jwt
http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html
http://www.ruanyifeng.com/blog/2011/09/curl.html
http://www.ruanyifeng.com/blog/2018/03/http2_server_push.html

http://projects.spring.io/spring-security-oauth/docs/tutorial.html
http://projects.spring.io/spring-security-oauth/docs/oauth2.html

https://stackoverflow.com/questions/33638850/how-to-protect-spring-security-oauth-resources-using-preauthorize-based-on-scop
http://www.zakariaamine.com/
http://www.zakariaamine.com/2018-03-01/using-oauth2-in-spring-scopes
http://www.zakariaamine.com/2018-01-27/using-oauth2-in-spring
http://www.tinmegali.com/en/2017/06/25/oauth2-using-spring/
http://www.tinmegali.com/en/2017/07/18/oauth2-jwt-using-spring/

https://www.jianshu.com/p/68f22f9a00ee 
http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html

本demo分为两个部分， 服务授权采用oauth2.0 + jwt 的方案， 用户登录采用 jwt方案


服务授权地址
 /oauth/check_token token 检查服务
 /oauth/token 获取token 
 /oauth/confirm_access 确认授权页面展示
 /oauth/error 授权错误展示
 /oauth/authorize 主要用于简单模式获取token  http://localhost:9081/oauth/authorize?response_type=token&client_id=my-clien&state=1112222t&redirect_uri=https://cn.bing.com/
                        授权码模式获取授权码 http://localhost:9081/oauth/authorize?response_type=code&client_id=my-client&&state=djdkxkxk&redirect_uri=https://cn.bing.com/
 
 客户端授权,不会返回refresh_token
 curl localhost:9082/foo | jq
 {
  "error": "unauthorized",
  "error_description": "Full authentication is required to access this resource"
}

curl -X POST --user my-client:mysecret localhost:9081/oauth/token -d 'grant_type=client_credentials&client_id=my-client' -H "Accept: application/json" | jq
{
   "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNTMyNjIwMTMzLCJhdXRob3JpdGllcyI6WyJST0xFX1RSVVNURURfQ0xJRU5UIiwiUk9MRV9DTElFTlQiXSwianRpIjoiN2JjYmNmNTUtMjkxOS00ZGVhLTliMzEtODUyMGFjYjg0NWYxIiwiY2xpZW50X2lkIjoibXktY2xpZW50In0.AC5-SCayvRqHfR9PX-vfYJdbTjh7k0LCeJkOgTTz-qd7I_XEiRbfFRjcLWaD36h3BbOfCGWyCRrUaitAdVoOQ17tg6l989Iz3ZdU1ttX274TelkWbDCfgo8YjKBXcpc5B9ba6iaNIUQMOuivh97pVUvPgWx6mltdm8uLT1Fxbm4",
  "token_type": "bearer",
  "expires_in": 43199,
  "scope": "read write trust",
  "jti": "7bcbcf55-2919-4dea-9b31-8520acb845f1"
}

 curl -v localhost:9082/foo -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNTMyNjIwMTMzLCJhdXRob3JpdGllcyI6WyJST0xFX1RSVVNURURfQ0xJRU5UIiwiUk9MRV9DTElFTlQiXSwianRpIjoiN2JjYmNmNTUtMjkxOS00ZGVhLTliMzEtODUyMGFjYjg0NWYxIiwiY2xpZW50X2lkIjoibXktY2xpZW50In0.AC5-SCayvRqHfR9PX-vfYJdbTjh7k0LCeJkOgTTz-qd7I_XEiRbfFRjcLWaD36h3BbOfCGWyCRrUaitAdVoOQ17tg6l989Iz3ZdU1ttX274TelkWbDCfgo8YjKBXcpc5B9ba6iaNIUQMOuivh97pVUvPgWx6mltdm8uLT1Fxbm4" -H "Accept: application/json" | jq
 注意：客户端密码请勿加密
 
 
 密码授权
 curl -X POST --user my-client:mysecret localhost:9081/oauth/token -d 'grant_type=password&username=user&password=password' -H "Accept: application/json" | jq 

 {
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImV4cCI6MTUzMjYyMDIwNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiIxZThhZDY4ZS0yMzZhLTQwNzAtYTI3YS04YzYxYjUwYWVjNDAiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.Gm-cF1G98-S3shY7uNoapdosTn5htM5NNbBYPx3RiWipvEm-as-9buokfGMOtHn3VPsXGNscOXHV7bjSucKUZ2Iq8KY_63vy2kFpz1g0AXNdlXJLYaLXOczHSMmUfCohMmtI5z_6ZEYnobMsW-hU7hBhseG9S_6axCrcos-nrQ8",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImF0aSI6IjFlOGFkNjhlLTIzNmEtNDA3MC1hMjdhLThjNjFiNTBhZWM0MCIsImV4cCI6MTUzNTE2OTAwNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiIzOGI5ZDFmOC0yY2EyLTQ2ODQtOTVkNS1mYzQ1MTVmYjNiZWUiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.RjtiYh_RKe09_bnWkm7ZLvVqz7WVUM3MzW2m4b6X7jYZfG1l0NnhCJtii3QKvBm-KBrGxnI1epIAxFDGPiW36hLZgqd7kY8unS3Z3_VW5CfZlebtzjygFAl_8DGG-ibHSNavdDx1AXGo3kgLIQOIr61JBfJC9M2UsEeGtSeOWzk",
  "expires_in": 43199,
  "scope": "read write trust",
  "jti": "1e8ad68e-236a-4070-a27a-8c61b50aec40"
}


简单授权 
curl -i http://localhost:9081/oauth/authorize?response_type=token&client_id=my-client&state=1112222t&redirect_uri=cn.bing.com

http://localhost:9081/oauth/cn.bing.com#access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImV4cCI6MTUzMjYzMDQ3MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiJjZGVhMzIzZS0wYTJjLTRlZmItOWQwNC02MjJjODdhMWIwNmQiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.FNqEqsBlQHhia34m4HMq6rVf57OK_gzBy97FitgMbpv8E0HBgpKHIqZ5IKwIU2dYrkNfQ-HM89j73q0jl6oTb62wqx7AIyY8uWFqHWjw_df9uifAsryFqWlrTxkYORU1s3wvV_imzB9bLolBuzJkPcIpaa6DB1k4u5NtLc_8pJg&token_type=bearer&state=1112222t&expires_in=43199&scope=read%20write%20trust&jti=cdea323e-0a2c-4efb-9d04-622c87a1b06d

 curl -v localhost:9082/foo -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImV4cCI6MTUzMjYzMDQ3MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiJjZGVhMzIzZS0wYTJjLTRlZmItOWQwNC02MjJjODdhMWIwNmQiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.FNqEqsBlQHhia34m4HMq6rVf57OK_gzBy97FitgMbpv8E0HBgpKHIqZ5IKwIU2dYrkNfQ-HM89j73q0jl6oTb62wqx7AIyY8uWFqHWjw_df9uifAsryFqWlrTxkYORU1s3wvV_imzB9bLolBuzJkPcIpaa6DB1k4u5NtLc_8pJg" -H "Accept: application/json" | jq
 
 授权码授权
 curl -i http://localhost:9081/oauth/authorize?response_type=code&ClientRegistration=my-client&&state=djdkxkxk&redirect_uri=https://cn.bing.com/
 
 https://cn.bing.com/?code=d8c9zp&state=djdkxkxk
 
 curl -X POST --user my-client:mysecret localhost:9081/oauth/token -d 'grant_type=authorization_code&code=d8c9zp&redirect_uri=https://cn.bing.com/' -H "Accept: application/json" | jq
  
 {
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImV4cCI6MTUzMjYzMTEyMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiJiY2I5MmY0Yi0wOTJmLTRlODQtYWNmMi02YzIwYzgxMzlmN2EiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.lnBFwUVC3QQ16ugPl7TCAAh8R5Tp_gd8tWtilZuJlSAKKMyzl0lXiHfYRC2XKlJWCC6GI_yIO8HYPl3TsxLBO2kOUCJlnzL_UcJ1pbZZv8RY4i9tBSWbxj-Eu2hOhOG6ttpkDXm_YDVqOmSo38NXJQo2KidBeOigZ5YTz0Optrk",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImF0aSI6ImJjYjkyZjRiLTA5MmYtNGU4NC1hY2YyLTZjMjBjODEzOWY3YSIsImV4cCI6MTUzNTE3OTkyMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiJkNDNhZWFmOC1lNzkwLTRmMjctYjBjYi0xYWIyYWRlZDk5OTgiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.lQynvcwgZ2gK4JG3s_sQP2fB1n2343qkXvIObxU42Jzmd6u7N0ZCliqPr6TaGL3afmunO3hGKkLgqRjj8c1eO2hg5bZAOrK7lIPbStY1IpX4V23XMXg9XDtOfB90AykiO-ZIVOYB6Xqo_qpiMSeRxNbceMzuJ046EnXHpZQh300",
  "expires_in": 43199,
  "scope": "read write trust",
  "jti": "bcb92f4b-092f-4e84-acf2-6c20c8139f7a"
}


curl -v localhost:9082/foo -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImV4cCI6MTUzMjYzMTEyMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UUlVTVEVEX0NMSUVOVCIsIlJPTEVfVVNFUiJdLCJqdGkiOiJiY2I5MmY0Yi0wOTJmLTRlODQtYWNmMi02YzIwYzgxMzlmN2EiLCJjbGllbnRfaWQiOiJteS1jbGllbnQifQ.lnBFwUVC3QQ16ugPl7TCAAh8R5Tp_gd8tWtilZuJlSAKKMyzl0lXiHfYRC2XKlJWCC6GI_yIO8HYPl3TsxLBO2kOUCJlnzL_UcJ1pbZZv8RY4i9tBSWbxj-Eu2hOhOG6ttpkDXm_YDVqOmSo38NXJQo2KidBeOigZ5YTz0Optrk" -H "Accept: application/json" | jq


用户授权地址
uauth/register 用户注册
uauth/login 用户登录
uauth/token_refresh jwt token 刷新