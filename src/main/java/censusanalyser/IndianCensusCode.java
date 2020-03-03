package censusanalyser;

import com.opencsv.bean.CsvBindByName;

//SrNo,State Name,TIN,StateCode

public class IndianCensusCode {

    @CsvBindByName(column = "State Name", required = true)
    public String stateName;

    @CsvBindByName(column = "StateCode", required = true)
    public String stateCode;

    @Override
    public String toString() {
        return "IndianCensusCode{" +
                "stateName='" + stateName + '\'' +
                ", stateCode=" + stateCode +
                '}';
    }
}
