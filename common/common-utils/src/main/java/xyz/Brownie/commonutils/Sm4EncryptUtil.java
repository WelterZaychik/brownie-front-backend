package xyz.Brownie.commonutils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import static cn.hutool.crypto.Mode.CBC;
import static cn.hutool.crypto.Padding.ZeroPadding;

public class Sm4EncryptUtil {

    public static String encrypt(String plainTxt){
        String cipherTxt = "";
        SymmetricCrypto sm4 = new SM4(CBC, ZeroPadding, "browniebypanwelt".getBytes(CharsetUtil.CHARSET_UTF_8), "iviviviviviviviv".getBytes(CharsetUtil.CHARSET_UTF_8));
        byte[] encrypHex = sm4.encrypt(plainTxt);
        cipherTxt = Base64.encode(encrypHex);
        return "{SM4}" + cipherTxt;
    }

    public static String decrypt(String cipherTxt){
        if(!cipherTxt.startsWith("{SM4}")){
            return cipherTxt;
        }
        cipherTxt = cipherTxt.substring(5);
        String plainTxt = "";
        SymmetricCrypto sm4 = new SM4(CBC, ZeroPadding, "browniebypanwelt".getBytes(CharsetUtil.CHARSET_UTF_8), "iviviviviviviviv".getBytes(CharsetUtil.CHARSET_UTF_8));
        byte[] cipherHex = Base64.decode(cipherTxt);
        plainTxt = sm4.decryptStr(cipherHex, CharsetUtil.CHARSET_UTF_8);
        return plainTxt;
    }

    public static void main(String[] argc){
        String originTxt = "abc123";
        System.out.println("原文: " + originTxt);
        String cipherTxt = encrypt(originTxt);
        System.out.println("密文: " + cipherTxt);
        String plainTxt = decrypt("{SM4}JDNCALgzBDBmE+MAZqAGlQ==");
        System.out.println("解密结果: " + plainTxt);
    }
}
