package com.manman.rpc.protocol;

import com.manman.rpc.config.RpcConfig;
import com.manman.rpc.message.Message;
import com.manman.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Title: MessageCodecSharable
 * @Author manman
 * @Description 消息编码/解码器
 * @Date 2021/8/26
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    /**
     * 编码
     * @param ctx
     * @param msg
     * @param list
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 字节的版本
        out.writeByte(1);
        // 3. 1 字节序列化方式 jdk 0，json 1，protobuf 2
        out.writeByte(RpcConfig.getSerializerAlogrithm().ordinal());
        // 4. 1 字节的指令类型
        out.writeByte(msg.getMessageType());
        // 5. 4 字节的序列号
        out.writeInt(msg.getSequenceId());
        // 6. 对其填充
        out.writeByte(0xff);
        // 7. 获取内容的字节数组
        byte[] bytes = RpcConfig.getSerializerAlogrithm().serialize(msg);
        // 8. 长度
        out.writeInt(bytes.length);
        // 9. 写入内容
        out.writeBytes(bytes);
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerAlgorithm = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerAlgorithm];
        // 确定具体消息类型
        Class<? extends Message> messageClass = Message.getMessageClass(messageType);
        Message message = algorithm.deserialize(messageClass, bytes);
        list.add(message);
    }
}
