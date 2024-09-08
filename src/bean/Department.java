package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Department {
    private String id = null;
    private String deptName = null;
    private String deptDescription = null;
    private String deptStatus = null;

    public Department() {
    }

    public Department(String id, String deptName, String deptDescription, String deptStatus) {
        this.id = UtilityMethod.addPrefix("DPT", id);
        this.deptName = deptName;
        this.deptDescription = deptDescription;
        this.deptStatus = deptStatus;
    }

    public String getId() {
        return id;
    }

    public StringProperty idProperty(){
        return new SimpleStringProperty(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public StringProperty deptNameProperty(){
        return new SimpleStringProperty(deptName);
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptDescription() {
        return deptDescription;
    }

    public StringProperty deptDescriptionProperty(){
        return new SimpleStringProperty(deptDescription);
    }

    public void setDeptDescription(String deptDescription) {
        this.deptDescription = deptDescription;
    }

    public String getDeptStatus() {
        return deptStatus;
    }

    public StringProperty deptStatusProperty(){
        return new SimpleStringProperty(deptStatus);
    }

    public void setDeptStatus(String deptStatus) {
        this.deptStatus = deptStatus;
    }
}
