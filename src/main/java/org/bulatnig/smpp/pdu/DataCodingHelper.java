package org.bulatnig.smpp.pdu;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 03.05.2009
 * Time: 15:06:14
 */
public enum DataCodingHelper implements DataCodingStrategy {
    INSTANCE;

    public DataCodingStrategy dataCodingStrategy = new DataCodingImpl();

    public DataCodingStrategy getDataCodingStrategy() {
        return dataCodingStrategy;
    }

    public void setDataCodingStrategy(DataCodingStrategy dataCodingStrategy) {
        this.dataCodingStrategy = dataCodingStrategy;
    }

    @Override
    public String getCharsetName(int dataCoding) {
        return dataCodingStrategy.getCharsetName(dataCoding);
    }

}
