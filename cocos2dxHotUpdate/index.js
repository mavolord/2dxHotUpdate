const express = require("express");
const fs = require("fs");
const calculateFileMd5 = require("./calcMd5");
const app = express();
const port = 5684;

app.get("/", (req, res)=>{
    res.send("大噶后，喔系渣渣辉");
});

app.listen(port, ()=>{
    console.log("express服务启动:" + port);
});

app.use(express.static("so"));

//监听文件变化
const filePath = "./so/libTest.so";
var soMd5 = "";

function calcSoMd5(){
	calculateFileMd5(filePath).then((md5)=>{
		console.log("文件的md5值是:"+md5);
		soMd5 = md5;
	});
}

calcSoMd5();

fs.watch(filePath, (e, file)=>{
	if(file && e == "change"){
		console.log("so file has changeed!");
		calcSoMd5();
	}
});

app.get("/soMd5",(req, res)=>{
	res.send(soMd5);
});
