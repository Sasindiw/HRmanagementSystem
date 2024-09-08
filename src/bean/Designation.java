package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Designation {
    private String id = null;
    private String name = null;
    private String description = null;
    private String status = null;
    private Department department = null;

    public Designation() {
    }

    public Designation(String id, String name, String description, String status, Department department) {
        this.id = UtilityMethod.addPrefix("DESIG", id);
        this.name = name;
        this.description = description;
        this.status = status;
        this.department = department;
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

    public String getName() {
        return name;
    }

    public StringProperty nameProperty(){
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public StringProperty descriptionProperty(){
        return new SimpleStringProperty(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public StringProperty statusProperty(){
        return new SimpleStringProperty(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public StringProperty deptIdProperty(){
        return department.idProperty();
    }

    public StringProperty deptNameProperty(){
        return department.deptNameProperty();
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
