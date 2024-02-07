const fs = require('fs');
const crypto = require('crypto');
 
function calculateFileMd5(filePath) {
    return new Promise((resolve, reject) => {
        const hash = crypto.createHash('md5'); // 创建MD5对象
        
        try {
            const stream = fs.createReadStream(filePath); // 读取文件流
            
            stream.on('data', (chunk) => {
                hash.update(chunk); // 更新hash对象
            });
            
            stream.on('end', () => {
                resolve(hash.digest('hex')); // 返回十六进制格式的MD5值
            });
            
            stream.on('error', (err) => {
                reject(err);
            });
        } catch (err) {
            reject(err);
        }
    });
}
 
// 调用函数并打印结果
//calculateFileMd5('./so/libTest.so')
//    .then((md5Value) => console.log(`文件的MD5值为：${md5Value}`))
//    .catch((err) => console.error(`发生错误：${err}`));

module.exports = calculateFileMd5;
