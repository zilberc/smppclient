package org.bulatnig.smpp.pdu.udh;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 20:18:30
 */
public class IEI extends UDH {

    private static final short TOTAL_LENGTH = 4;

    private static final short IEIL = 3;

    private short msgRefNum;

    private short totalSegments;

    private short segmentSeqnum;

    public IEI() {
        super(UDHType.IEI);
    }

    public IEI(byte[] bytes) throws UDHException {
        super(bytes);
    }

    @Override
    protected void parseBody(byte[] bytes) throws UDHException {
        if (UDHType.IEI != type()) {
            throw new ClassCastException();
        }
        if (TOTAL_LENGTH == bytes.length) {
            SmppByteBuffer bb = new SmppByteBuffer(bytes);
            try {
                if (IEIL == bb.removeByte()) {
                    msgRefNum = bb.removeByte();
                    totalSegments = bb.removeByte();
                    segmentSeqnum = bb.removeByte();
                } else
                    throw new UDHException("IEIL field value should be " + IEIL);
            } catch (WrongLengthException e) {
                throw new UDHException("UDH parsing error", e);
            }
        } else
            throw new UDHException("IEI length should be " + TOTAL_LENGTH);
    }

    @Override
    protected byte[] getBodyBytes() throws UDHException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendByte(IEIL);
        bb.appendByte(msgRefNum);
        bb.appendByte(totalSegments);
        bb.appendByte(segmentSeqnum);
        return bb.array();
    }

    public short getMsgRefNum() {
        return msgRefNum;
    }

    public void setMsgRefNum(short msgRefNum) {
        this.msgRefNum = msgRefNum;
    }

    public short getTotalSegments() {
        return totalSegments;
    }

    public void setTotalSegments(short totalSegments) {
        this.totalSegments = totalSegments;
    }

    public short getSegmentSeqnum() {
        return segmentSeqnum;
    }

    public void setSegmentSeqnum(short segmentSeqnum) {
        this.segmentSeqnum = segmentSeqnum;
    }

    @Override
    public String toString() {
        return "IEI{" +
                "msgRefNum=" + msgRefNum +
                ", totalSegments=" + totalSegments +
                ", segmentSeqnum=" + segmentSeqnum +
                '}';
    }

}
