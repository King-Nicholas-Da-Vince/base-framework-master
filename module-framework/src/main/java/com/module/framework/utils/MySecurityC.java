package com.module.framework.utils;

import android.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 客户端加解密
 * @author x.j
 * @since 2022-05-28
 */
public class MySecurityC {
    public static final boolean debug=true;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static RSAPublicKey publicKey;

    public static void init() {
        try {
            String pubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApn59bjt0jnvInpfAbdMpmcvumUxklWN74Tv+XDveglhmgNsZo0tDCQ725floNrGaa0+6co+okgoIbqxmJggbIYK/X+zg3LM30TIGaA+qn8LF7OQrS+sIaHPpIDAG5X+dLLFwjX7gEJwiwqep0r9aZj5KWh8GWH1kuc47iFxAX9p8JeyDUWixE9cCmU6x71UjSROtKQJRmlIt9TFrArTkAJUTeMatqpAkVohTZL2LiYXwb5HZYVlRz5wDgDKh8QpMvUiw/T5TjODzjc2P7yEXlkzAm6IOhEv5uZaJ5C+aV3w3wLVoPTrKtH/skg4a75JWnPvrBZPvET3D/FpViZjrJwIDAQAB";
            publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(pubkey, Base64.DEFAULT)));
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES");
            byte[] v1 = new byte[16];
            new SecureRandom().nextBytes(v1);
            ivParameterSpec = new IvParameterSpec(v1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int RSA_BUFLEN = 117;

    private static byte[] encryptRSA(byte[] buf, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes;
        int blength = offset + length;
        for (int i = offset; i < blength; i += RSA_BUFLEN) {
            bytes = blength - i > RSA_BUFLEN ? cipher.doFinal(buf, i, RSA_BUFLEN) : cipher.doFinal(buf, i, blength - i);
            bos.write(bytes, 0, bytes.length);
        }
        bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }

    private static SecretKey secretKey;
    private static IvParameterSpec ivParameterSpec;

    private static byte[] encryptAES(byte[] buf, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(buf, offset, length);
    }

    private static byte[] decryptAES(byte[] buf, int offset, int length) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(buf, offset, length);
    }

    private static byte[] compress(byte[] buf, int offset, int length) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(bos);
        gos.write(buf, offset, length);
        gos.finish();
        return bos.toByteArray();
    }

    private static byte[] decompress(byte[] buf, int offset, int length) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(buf, offset, length));
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        byte[] bytes = new byte[1024];
        while (true) {
            int read = gis.read(bytes);
            if (read == -1)
                break;
            bos.write(bytes, 0, read);
        }
        bos.flush();
        return bos.toByteArray();
    }

    public static byte[] encodeCfg(byte[] buf)  {
        if (debug){
            return buf;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(buf.length + 70);
            bos.write(secretKey.getEncoded());
            bos.write(ivParameterSpec.getIV());
            byte[] b = bos.toByteArray();
            b = encryptRSA(b, 0, b.length);
            bos.reset();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.write(0x80);
            dos.writeShort(b.length);
            dos.write(b);
            dos.write(encode(buf));
            dos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decode(byte[] buf) {
        if (debug){
            return buf;
        }
        try {
            byte c = buf[0];
            if (c != 1 && c != 3)
                return buf;
            buf = decryptAES(buf, 1, buf.length - 1);
            if (c != 3)
                return buf;
            return decompress(buf, 0, buf.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encode(byte[] buf) {
        if (debug){
            return buf;
        }
        try {
            byte c = 1;
            if (buf.length > 512) {
                buf = compress(buf, 0, buf.length);
                c |= 2;
            }
            byte[] b = encryptAES(buf, 0, buf.length);
            byte[] tb = new byte[b.length + 1];
            tb[0] = c;
            System.arraycopy(b, 0, tb, 1, b.length);
            return tb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] decodeCfg(byte[] buf) throws Exception {
        if (debug){
            return buf;
        }
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buf, 1, buf.length - 1));
        dis.read(new byte[dis.readShort()]);
        buf = new byte[dis.available()];
        dis.read(buf);
        return decode(buf);
    }
}
