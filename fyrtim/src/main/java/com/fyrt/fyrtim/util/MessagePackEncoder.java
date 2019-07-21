package com.fyrt.fyrtim.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 编码器
 */
public class MessagePackEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf buf) throws Exception {
        // TODO Auto-generated method stub
        MessagePack messagePack = new MessagePack();
        buf.writeBytes(messagePack.write(msg));
    }

}
