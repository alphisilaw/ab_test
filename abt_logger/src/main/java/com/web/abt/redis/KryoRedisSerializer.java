package com.web.abt.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoRedisSerializer<T> implements RedisSerializer<T> {
	
	
    @Override  
    public byte[] serialize(Object t) throws SerializationException {
    	byte[] result = null;
    	Kryo kryo = new Kryo();  
        Output output = new Output(1024*4, 1024*1024);  
        kryo.writeClassAndObject(output, t);
        output.flush();
        result = output.toBytes();
        output.close();
        return result;  
    }  
  
    @Override  
    public T deserialize(byte[] bytes) throws SerializationException {
    	Kryo kryo = new Kryo();  
    	if (bytes == null) {
    		return null;
    	}
        Input input = new Input(bytes);  
        @SuppressWarnings("unchecked")  
        T t = (T) kryo.readClassAndObject(input);
        input.close();
        return t;  
    }  
  
}  