package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndiaStateCodeCSV {
    @CsvBindByName(column = "SrNo", required = true)
    private String SrNo;

    @CsvBindByName(column = "State Name", required = true)
    private String stateName;

    @CsvBindByName(column = "TIN", required = true)
    private String tin;

    @CsvBindByName(column = "StateCode",required = true)
    public String stateCode;

    public String getSrNo() {
        return SrNo;
    }


    public void setSrNo(String srNo) {
        this.SrNo = srNo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String toString() {
        return "IndiaStateCodeCSV{" +
                "SrNo='" + SrNo + '\'' +
                ", stateName='" + stateName + '\'' +
                ", tin='" + tin + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }
}
