public class RequestPair {
    public Integer sendPort;
    public Long requestValue;
    Boolean answer;

    private Long pStart;
    Long p;

    private Long wStart;
    Long w;

    private Long cStart;
    Long c;


    public RequestPair(Integer sendPort, Long requestValue) {
        this.requestValue = requestValue;
        this.sendPort = sendPort;
    }

    public long getProcessingTime() {
        return p;
    }

    public long getWaitingTime() {
        return w;
    }

    public long getCommunicationTime() {
        return c - p - w;
    }

    public void setcStart() {
        if (this.cStart != null)
            this.cStart = System.currentTimeMillis();
    }

    public void setcEnd() {
        if (this.c != null)
            this.c = System.currentTimeMillis() - cStart;
    }

    public void setpStart() {
        if (this.pStart != null)
            this.pStart = System.currentTimeMillis();
    }

    public void setwStart() {
        if (this.wStart != null)
            this.wStart = System.currentTimeMillis();
    }

    public void setpEnd() {
        if (this.p != null)
            this.p = System.currentTimeMillis() - pStart;
    }

    public void setwEnd() {
        if (this.w != null)
            this.w = System.currentTimeMillis() - wStart;
    }
}
