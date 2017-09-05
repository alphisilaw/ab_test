package com.web.abt.m.util;

import java.io.UnsupportedEncodingException;


/**
 * @Title: Encryptstr.java
 * @Package com.medialab.quizup.api.common.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liuyang
 * @date 2014-1-20 下午6:12:10
 * @version V1.0
 */
public class Encryptstr {


    private static String sSkey = "201501118888";


    public static String encryptkey(String s, int i) {
        String s1 = s;
        int j = s.length();
        if (i <= j)
            s1 = s.substring(0, i);
        else
            for (; j < i; j = s1.length())
                if (j + s.length() >= i)
                    s1 = s1 + s.substring(0, i - j);
                else
                    s1 = s1 + s;

        return s1;
    }

    public static String encrypt(String s, String s1) {
        String s2 = "";
        if (s1 == "") {
            return s2;
        }
        if (s == "") {
            return s2;
        }
        try {
            s1 = Base64.encode(s1.getBytes("utf-8"));// base64加密
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int i = s1.length();
        String s3 = encryptkey(s, i);
        for (int k = 0; k < i; k++) {
            char c = s1.charAt(k);
            char c1 = s3.charAt(k);
            char c2 = c;
            char c3 = c1;
            int j = c2 ^ c3;
            j += 29;
            j = 1000 + ((j / 10) % 10) * 100 + (j / 100) * 10 + j % 10;
            s2 = s2 + Integer.toString(j).substring(1);
        }

        return s2;
    }

    public static String encrypt(String s) {
        return encrypt(sSkey, s);
    }

    public static String decode(String s, String s1) {
        String s2 = "";
        if (s1 == "") {
            return s2;
        }
        if (s == "") {
            return s2;
        }
        int i = s1.length() / 3;
        String s3 = encryptkey(s, i);
        for (int l = 0; l < i; l++) {
            String s4 = s1.substring(l * 3, (l + 1) * 3);
            char c1 = s3.charAt(l);
            int k = Integer.parseInt(s4);
            k = ((k / 10) % 10) * 100 + (k / 100) * 10 + k % 10;
            char c2 = c1;
            int j = k - 29 ^ c2;
            char c = (char) j;
            s2 = s2 + c;
        }
        try {
            s2 = new String(Base64.decode(s2), "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s2;
    }

    public static String decode(String s) {
        return decode(sSkey, s);
    }

    public static void main(String args[]) throws Exception {
        String strOri = "113";
        long t1 = System.currentTimeMillis();
        // String ivAndEncryptedMessageBase64 = Base64.encode(strOri.getBytes());

        String str = Encryptstr.encrypt(strOri);// 加密
        long t2 = System.currentTimeMillis();
        System.out.println(str);
        System.out.println("encrypt user time:" + (t2 - t1));

        str =
                "306513117301304514010412402415114412303315117506304313504217402116414514306214511412304019504304403412212015306304903904305511504209402415016410303512515214304019209216402402012514407304213904511306313516408402119408316304514908418303212908413404404904516305419110907303306904016402216309301304514212515303515908113402415110303304213312215305304012213";

        str = decode(sSkey, str); // 解密
        System.out.println("decode:" + str);
        // System.out.println(new String(Base64.decode(str)));
        long t3 = System.currentTimeMillis();
        System.out.println("decode user time:" + (t3 - t2));

        // // aes解密
        // String nnn = AES.encrypt(strOri, Constants.PASSWORD);
        // System.out.println(nnn);
        // long t4 = System.currentTimeMillis();
        // System.out.println("encrypt user time:" + (t4 - t3));
        // nnn =
        // AES.decrypt(
        // "pSmu4MRicGRNoYieVTlC67QzyGfnRLpKlB7b+UaQpU5fG0RZsxYgvWVqTo5R 3478RFMKTUIXMPOZLl3dhqFjEie5uefZkUnd0SA+hpaMCzHI19+AXum/VBFH RXsrVV9xA+EOqtqK0quZTUGRvHjhG7tEVvKisprjAC4isIJYLealPcuDXQUm CWSH1yDzaIgsg6FuW8MAYq85o3WUwcwrHFMoPvbN1M4N4RESPp4Ud7BT7CsA 1E93z7p9DMAvnoGdDlN99UpxCl1oDan/rHhfPM+vPWJ9aQzRHCAbmZ+EYeXP N2UpSSZir6ZGmYZXdpI1eaqKC5EHjZARWT6iRx58TrID+plQFIIjFboKYKmq y636FAqXxsC4GCHnQehTx7bRV1eyFLdWVtmemfKj/XqCarsYaYI5i436rswG 045YZro9FGFOcsBpvuwXUt0/awuiTeyRhYZrpODq+SbHBp0mttfT0RItdD5R sFchu5vkyW7S5xeLf7VCS1XYL8mGuMhUPKC2z0LTGVH97Vu0Y+kZTsnBIe8J heWoQU/ghmFVwwYIHk5VYSbv1WjEqt5ET0GHDKo1Mxx/TD1B9Tf1lpneRN0V N4OJ7ktcZwhKiy67XGVct7T+PoIx1Padh42ZcbuqqEJ7G2nA/UpOApKufMTJ rpGsvSSuT2dEd7qvuXNgl/3ib8zwUdJJnq7Xx+tB6qDQv8dlKhUxBps/Iu0e jDn+kU60w1PhQKPzDlJkuqGSvHY9FGLWMyWJxImP3+AzgfnuVT4ym8udqIZF hydNfiuVuCRi3IJkpaN0imeE6+jGarsX/2yeswTG+cMVBfqwHCzr9zpyfdEo 9MjzTJbHRMgl4DY/h2/DsD3D9Y6jhKEbH/csEXKKElCAZXwoFhUKftW1t8Yh J9tmem05JuW09uItB6LwWQS67Ex8ceL2QPT14/tHKpa7NtVIrO2M0QfKA1ax FbPjO4NeymI7awSjC3k3EEH3LWNfyuK9SdpzsENC/EGlaXtodagoBTTfPzkj mq3CGbp55H4icBvW/9V0/cK/IkXMrviWIQpJAZ+bpaPHjbKMyFn0rQrUdsoX 4V5MF6ptQn5wEkCB4/X/ad9ca0/NZEd3IxIgmpHQTV+oS7kKeOG9Bg7OBscP V6IbLiFzYYpflwaZ1Ib7wrX836HYWddb7uiVW18+qZF/npPxIyTCLNsx+e6k Fjj+5X4bFRdGguHxYVMu7IvYtxuDztR8BvyINA==",
        // Constants.PASSWORD);
        // System.out.println(nnn);
        // long t5 = System.currentTimeMillis();
        // System.out.println("encrypt user time:" + (t5 - t4));

    }
}
