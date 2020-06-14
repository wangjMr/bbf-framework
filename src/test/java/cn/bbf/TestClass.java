package cn.bbf;

/**
 * @author: WJ
 * @date 2020/5/28 12:27
 * @description: TODO
 */
public class TestClass {
    private String testClassId;
    private String name;
    private String age;

    public String getTestClassId() {
        return testClassId;
    }

    public void setTestClassId(String testClassId) {
        this.testClassId = testClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "testClassId='" + testClassId + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
