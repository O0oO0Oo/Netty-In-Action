package org.study.chp5.buffer_usage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class Buffer {

    public void heapBuffer() {
        ByteBuf heapBuf = Unpooled.buffer();
        if (heapBuf.hasArray()){
            byte[] array = heapBuf.array(); // 배열에 대한 참조를 가져옴
            // hasArray() false 일 떄 접근하면 UnsupportedOperationException 발생, ByteBuffer 사용과 유사
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex(); // 첫 바이트 offset 계산
            int length = heapBuf.readableBytes(); // 읽을 수 있는 바이트 수를 가져온다.
            handleArray(array, offset, length); // 배열, 오프셋, 길이로 메서드 호출
        }
    }

    public void directBuffer() {
        ByteBuf directBuf = Unpooled.directBuffer();
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            // 읽을 쇼ㅜ 있는 바이트 만큼 새 배열 할당
            byte[] array = new byte[length];

            // 데이터를 힙 메모리의 배열로 복사
            directBuf.getBytes(directBuf.readerIndex(), array);
            handleArray(array, 0, length);
        }
    }

    public void compositeBuffer(){
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes(); // 읽을 수 있는 버퍼 가져옴
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(), array);
        handleArray(array, 0, array.length);
    }

    public void otherBuffers() {
        byte[] originData = "origin Data".getBytes();

        // 데이터 복사, 불변성 보장
        ByteBuf copiedBuf = Unpooled.copiedBuffer(originData);
        // 래핑, 원본 메모리 공유
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(originData);

        // 래핑, 참조 카운트 보호, 불변성 보장
        ByteBuf unreleasableBuf = Unpooled.unreleasableBuffer(copiedBuf);
        // 래핑, 읽기 전용, 불변성 보장
        ByteBuf unmodifiableBuf = Unpooled.unmodifiableBuffer(wrappedBuf);
    }

    private void handleArray(byte[] array, int offset, int length) {
    }
}
