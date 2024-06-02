# Netty-In-Action
네티 인 액션 책 읽으면서 따라한 예제 코드

### 2. Your first Netty application
- [Simple Netty Echo Client/Server](https://github.com/O0oO0Oo/Netty-In-Action/tree/main/netty/src/main/java/org/study/chp2)
  
### 4. Transports
- [Plain Nio/Oio Server](https://github.com/O0oO0Oo/Netty-In-Action/tree/main/netty/src/main/java/org/study/chp4/without_netty)
- [Netty Nio/Oio Server](https://github.com/O0oO0Oo/Netty-In-Action/tree/main/netty/src/main/java/org/study/chp4/with_netty)
- [Channel Operation](https://github.com/O0oO0Oo/Netty-In-Action/blob/main/netty/src/main/java/org/study/chp4/channel_ops/ChannelOps.java)
  
### 5. ByteBuf
- [ByteBuf Type](https://github.com/O0oO0Oo/Netty-In-Action/blob/main/netty/src/main/java/org/study/chp5/buffer_usage/Buffer.java) - Heap/Direct/Composite/Copied/Wrapper etc
- [ByteBuf Operation](https://github.com/O0oO0Oo/Netty-In-Action/blob/main/netty/src/main/java/org/study/chp5/bytebuf_operation/ByteBufOperation.java) - Writer/Reader indexing, management, operation
- [ByteBuf Reference Counting](https://github.com/O0oO0Oo/Netty-In-Action/blob/main/netty/src/main/java/org/study/chp5/bytebuf_reference_counting/ByteBufReferenceCounting.java)

### 6. ChannelHandler and ChannelPipeline
- [ChannelHandler, Pipeline, ChannelHandlerContext](https://github.com/O0oO0Oo/Netty-In-Action/tree/main/netty/src/main/java/org/study/chp6/channelpipeline/ctx)
- [Exception](https://github.com/O0oO0Oo/Netty-In-Action/tree/main/netty/src/main/java/org/study/chp6/exception)
- [Resource Leak](https://github.com/O0oO0Oo/Netty-In-Action/tree/main/netty/src/main/java/org/study/chp6/resourceleak) - <code>-Dio.netty.leakDetectionLevel=ADVANCED -mx{mem size}m</code> 설정
