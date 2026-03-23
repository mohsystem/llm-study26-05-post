package com.um.springbootprojstructure.dto;

import java.util.ArrayList;
import java.util.List;

public class UserXmlImportResponse {

    public static class RecordResult {
        private int index;
        private String email;
        private String status;
        private String reason;

        public RecordResult() {}

        public RecordResult(int index, String email, String status, String reason) {
            this.index = index;
            this.email = email;
            this.status = status;
            this.reason = reason;
        }

        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    private int total;
    private int imported;
    private int skipped;
    private int rejected;

    private List<RecordResult> results = new ArrayList<>();

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getImported() { return imported; }
    public void setImported(int imported) { this.imported = imported; }

    public int getSkipped() { return skipped; }
    public void setSkipped(int skipped) { this.skipped = skipped; }

    public int getRejected() { return rejected; }
    public void setRejected(int rejected) { this.rejected = rejected; }

    public List<RecordResult> getResults() { return results; }
    public void setResults(List<RecordResult> results) { this.results = results; }
}
