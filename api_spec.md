### api/case/submit

specification: 用户提交一份excel模板和需要如果操作excel的的指令文本说明，提交到后端

method:  POST

request Body:

````shell
multipart/form-data

file: file needed to upload

userId:"string"

instruction:"string"

````

response Body:

````shell
outputFileUrl: "String"
template_id: "string"
message: "string"
isNewConversation: "boolean" //是否是新的会话 不传任何东西的话,默认是flase,如果是true的话,会清空之前的会话记录.
````



###  api/case/status

specification: 用户预览excel文件没问题后，点击保留或者废弃，将正确的后端代码模板表记入库或者删除代码

method:  POST

request Body:

````shell
Json
templateId: "string",
status:"delete" \ "save"
````

response Body:

````shell
status: "ok"
````



### api/case/generate

specification: 用户使用已经存在的模板进行数据的解析

method:  POST

request Body:

````shell
multipart/form-data

file: file needed to upload

template_id:"string"

````

response Body:

````shell
binary Data
template_id: "string"
````



### api/case/list

specification: 用户查看已经能够正确解析Excel的代码列表

method:  POST

request Body:
Json
{
"status": "1"
}

response body:

````she
Json
{
data:[{"templateUd":"xxx"},{"templateUd":"xxx"}]
}
````









