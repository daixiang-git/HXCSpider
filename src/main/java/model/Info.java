package model;

/**
 * Created by 祥少 on 2017/7/26.
 */
public class Info {
    private String teacherId;
    private String teacherName;
    private String be;
    private String en;
    private String phone;
    private String carNumber;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getBe() {
        return be;
    }

    public void setBe(String be) {
        this.be = be;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    public String toString() {
        return "order{" +
                "teacherId='" + teacherId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", be='" + be + '\'' +
                ", en='" + en + '\'' +
                ", phone='" + phone + '\'' +
                ", carNumber='" + carNumber + '\'' +
                '}';
    }
}
