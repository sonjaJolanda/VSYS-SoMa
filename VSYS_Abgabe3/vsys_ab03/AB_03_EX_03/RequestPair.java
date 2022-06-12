package AB_03_EX_03;

public class RequestPair {
    public Integer sendPort;
    public Long requestValue;

    RequestPair(Integer sendPort, Long requestValue) {
        this.requestValue = requestValue;
        this.sendPort = sendPort;
    }
}
