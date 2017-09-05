package com.zhenai.channel_manager.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopy {  

    /** 
     * 深层拷贝 - 需要类继承序列化接口 
     * @param <T> 
     * @param obj 
     * @return 
     * @throws Exception 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T copyImplSerializable(T obj) {  
        ByteArrayOutputStream baos = null;  
        ObjectOutputStream oos = null;  

        ByteArrayInputStream bais = null;  
        ObjectInputStream ois = null;  

        Object o = null;  
        //如果子类没有继承该接口，这一步会报错  
        try {  
            baos = new ByteArrayOutputStream();  
            oos = new ObjectOutputStream(baos);  
            oos.writeObject(obj);  
            bais = new ByteArrayInputStream(baos.toByteArray());  
            ois = new ObjectInputStream(bais);  

            o = ois.readObject();  
            return (T) o;  
        } catch (Exception e) {  
            //throw new Exception("对象中包含没有继承序列化的对象");  
        } finally{  
            try {  
                baos.close();  
                oos.close();  
                bais.close();  
                ois.close();  
            } catch (Exception e2) {  
                //这里报错不需要处理  
            }  
        }
        return null;
    }  
}