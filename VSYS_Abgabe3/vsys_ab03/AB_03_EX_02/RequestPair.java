package AB_03_EX_02;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class RequestPair implements Serializable {

    private static final long serialVersionUID = 10;

    public Integer sendPort;
    public Long requestValue;
    public Boolean answer;

    private Long pStart;
    private Long p;
    private Long pAverage;
    private static List<Long> ps = new LinkedList<>();

    private Long wStart;
    private Long w;
    private Long wAverage;
    private static List<Long> ws = new LinkedList<>();

    private Long cStart;
    private Long c;

    private Long cResult;
    private Long cResultAverage;
    private static List<Long> cResults = new LinkedList<>();


    public RequestPair(Integer sendPort, Long requestValue) {
        this.requestValue = requestValue;
        this.sendPort = sendPort;
    }

    public long getProcessingTime() {
        return (p != null) ? p : -10;
    }

    public long getWaitingTime() {
        return (w != null) ? w : -10;
    }

    public long getCommunicationTime() {
        //System.out.println("c:" + c + ", p:" + p + ", w:" + w);
        if ((c == null) || (p == null) || (w == null))
            return -10;
        return c - p - w;
    }

    private void calculateCResult() {
        if ((c == null) || (p == null) || (w == null) || (this.cResult != null))
            return;
        this.cResult = c - p - w;
        //System.out.println(">> added cResult:" + this.cResult);
        this.cResults.add(this.cResult);
    }

    public long getProcessingTimeAverage() {
        return this.pAverage;
    }

    public long getWaitingTimeAverage() {
        return this.wAverage;
    }

    public long getCommunicationTimeAverage() {
       return this.cResultAverage;
    }

    public void setcStart() {
        if (this.cStart == null)
            this.cStart = System.currentTimeMillis();
        //System.out.println("cStart:" + cStart);
    }

    public void setpStart() {
        if (this.pStart == null)
            this.pStart = System.currentTimeMillis();
        //System.out.println("pStart:" + pStart);
    }

    public void setwStart() {
        if (this.wStart == null)
            this.wStart = System.currentTimeMillis();
        //System.out.println("wStart:" + wStart);
    }

    public void setcEnd() {
        if (this.c != null)
            return;
        synchronized (cResults) {
            this.c = System.currentTimeMillis() - cStart;
            calculateCResult();

            Long sum = 0L;
            for (Long cResult : cResults) {
                sum += cResult;
            }
            this.cResultAverage = (cResults.isEmpty()) ? 0 : (sum / cResults.size());
        }
        //System.out.println("c:" + c + ", cStart:" + cStart);
    }

    public void setpEnd() {
        if (this.p != null)
            return;
        synchronized (ps) {
            this.p = System.currentTimeMillis() - pStart;
            this.ps.add(p);
            calculateCResult();
            Long sum = 0L;
            for (Long p : ps) {
                sum += p;
            }
            this.pAverage = (ps.isEmpty()) ? 0 : (sum / ps.size());
            System.out.println(">> added p:" + this.p + " (" + pAverage + ")");
        }
        //System.out.println("p:" + p + ", pStart:" + pStart);
    }

    public void setwEnd() {
        if (this.w != null)
            return;
        synchronized (ws) {
            this.w = System.currentTimeMillis() - wStart;
            this.ws.add(w);
            calculateCResult();
            Long sum = 0L;
            for (Long w : ws) {
                sum += w;
            }
            this.wAverage = (ws.isEmpty()) ? 0 : (sum / ws.size());
            System.out.println(">> added w:" + this.w + " (" + wAverage + ")");
        }
        //System.out.println("w:" + w + ", wStart:" + wStart);
    }
}
