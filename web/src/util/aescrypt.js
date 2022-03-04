// AES加密解密
import CryptoJS from 'crypto-js'
const keyStr = 'e2#5!01p#KItNQ12'

export function encrypt (word) {
    var key = CryptoJS.enc.Utf8.parse(keyStr)
    var srcs = CryptoJS.enc.Utf8.parse(word)
    var encrypted = CryptoJS.AES.encrypt(srcs, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 })
    return encrypted.toString()
}
// 解密
export function decrypt (word) {
    var key = CryptoJS.enc.Utf8.parse(keyStr)
    var decrypt = CryptoJS.AES.decrypt(word, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 })
    return CryptoJS.enc.Utf8.stringify(decrypt).toString()
}
