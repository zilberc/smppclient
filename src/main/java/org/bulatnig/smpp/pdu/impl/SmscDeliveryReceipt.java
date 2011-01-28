package org.bulatnig.smpp.pdu.impl;

import java.io.UnsupportedEncodingException;

/**
 * SMSC Delivery Receipt parser.
 * <p/>
 * Examples:
 * id:c449ab9744f47b6af1879e49e75e4f40 sub:001 dlvrd:0 submit date:0610191018 done date:0610191018 stat:ACCEPTD err:0 text:This is an Acti
 * id:7220bb6bd0be98fa628de66590f80070 sub:001 dlvrd:1 submit date:0610190851 done date:0610190951 stat:DELIVRD err:0 text:This is an Acti
 * id:b756c4f97aa2e1e67377dffc5e2f7d9b sub:001 dlvrd:0 submit date:0610191211 done date:0610191211 stat:REJECTD err:1 text:This is an Acti
 * id:bd778cd76ae9e79da2ddc8188c68f8c1 sub:001 dlvrd:0 submit date:0610191533 done date:0610191539 stat:UNDELIV err:1 text:This is an Acti
 * id:1661543146 sub:001 dlvrd:001 submit date:1101261110 done date:1101261110 stat:DELIVRD err:000 text:Hello, how are you?
 *
 *
 * @author Bulat Nigmatullin
 */
public class SmscDeliveryReceipt {

    private static final String CHARSET = "US-ASCII";
    private static final String NULL = "";

    private String id;
    private String sub;
    private String dlvrd;
    private String submitDate;
    private String doneDate;
    private String stat;
    private String err;
    private String text;

    /**
     * Empty construtor for creating SMSC Delivery Receipt.
     */
    public SmscDeliveryReceipt() {}

    /**
     * Parse SMSC Delivery Receipt from bytes ( proper esm_class should be set on PDU ).
     *
     * @param bytes
     */
    public SmscDeliveryReceipt(byte[] bytes) {
        try {
            String data = new String(bytes, CHARSET);
            for (int i = 0; i < data.length(); i++) {

            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("US-ASCII charset is not supported. Consult developer.", e);
        }
    }

    public byte[] toBytes() {
        StringBuilder builder = new StringBuilder();
        builder.append("id:");
        builder.append(id != null ? id : NULL);
        builder.append(" sub:");
        builder.append(sub != null ? sub : NULL);
        builder.append(" dlvrd:");
        builder.append(dlvrd != null ? dlvrd : NULL);
        builder.append(" submit date:");
        builder.append(submitDate != null ? submitDate : NULL);
        builder.append(" done date:");
        builder.append(doneDate != null ? doneDate : NULL);
        builder.append(" stat:");
        builder.append(stat != null ? stat : NULL);
        builder.append(" err:");
        builder.append(err != null ? err : NULL);
        builder.append(" text:");
        builder.append(text != null ? text : NULL);
        try {
            return builder.toString().getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("US-ASCII charset not supported by this JVM implementation. Consult developer.", e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getDlvrd() {
        return dlvrd;
    }

    public void setDlvrd(String dlvrd) {
        this.dlvrd = dlvrd;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
