package org.study.chp5.bytebuf_operation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.util.Random;

public class ByteBufOperation {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(30);
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        int readableBytes = byteBuf.readableBytes();
        int writableBytes = byteBuf.writableBytes();
        System.out.println("initial readableBytes = " + readableBytes + " / writableBytes = " + writableBytes + "\n");

        System.out.println("Random Access Indexing");
        for (int i = 0; i < byteBuf.capacity() - 1; i++) {
            System.out.print(byteBuf.getByte(i) + " ");
        }
        assert byteBuf.readerIndex() == 0;
        assert byteBuf.writerIndex() == 10;
        System.out.println("\n");

        System.out.println("Readable Bytes");
        while (byteBuf.isReadable()) {
            System.out.print(byteBuf.readByte() + " ");
        }
        assert byteBuf.readerIndex() == 10;
        assert byteBuf.writerIndex() == 10;
        System.out.println("\n");

        System.out.println("writable Bytes");
        Random random = new Random();
        while (byteBuf.writableBytes() > 0) {
            byteBuf.writeInt(random.nextInt());
        }
        assert byteBuf.readerIndex() == 10;
        assert byteBuf.writerIndex() == 30;
        System.out.println();

        System.out.println("discard - mem copied 20~30 -> 10~20");
        byteBuf.discardReadBytes();
        for (int i = 0; i < byteBuf.capacity() - 1; i++) {
            System.out.print(byteBuf.getByte(i) + " ");
        }
        assert byteBuf.readerIndex() == 0;
        assert byteBuf.writerIndex() == 20;
        System.out.println("\n");

        System.out.println("mark/reset byteBuf");
        byteBuf.readByte();
        byteBuf.markReaderIndex();
        byteBuf.resetReaderIndex();
        byteBuf.resetWriterIndex();
        assert byteBuf.readerIndex() == 1;
        assert byteBuf.writerIndex() == 0;
        System.out.println("\n");


        System.out.println("clear");
        byteBuf.clear();
        assert byteBuf.readerIndex() == 0;
        assert byteBuf.writerIndex() == 0;
        System.out.println("\n");

        System.out.println("search");
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        while (byteBuf.writableBytes() > 0) {
            byteBuf.writeByte(bytes[random.nextInt(20)]);
        }
        for (int i = 0; i < byteBuf.capacity() - 1; i++) {
            System.out.print(byteBuf.getByte(i) + " ");
        }
        System.out.println();
        assert byteBuf.readerIndex() == 0;
        assert byteBuf.writerIndex() == 30;
        ByteBufProcessor processor = value -> value % 2 != 0; // false 면 중지
        int idx = byteBuf.forEachByte(processor);
        System.out.println("idx : " + idx + " / value : " + byteBuf.getByte(idx));
        assert byteBuf.getByte(idx) % 2 == 0;
        System.out.println();


        System.out.println("copy / derived");
        byteBuf.clear();
        byteBuf.writeBytes(new byte[]{1,2,3,4,5});
        ByteBuf copy = byteBuf.copy();
        copy.writeByte(5);
        ByteBuf duplicate = byteBuf.duplicate();
        duplicate.writeByte(5);
        assert copy.capacity() != duplicate.capacity();
        assert byteBuf.capacity() == duplicate.capacity();
    }
}
