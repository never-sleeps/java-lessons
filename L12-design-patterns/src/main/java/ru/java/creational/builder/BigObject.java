package ru.java.creational.builder;


public class BigObject {
  private String param1;
  private String param2;
  private String param3;
  private String param4;
  private String param5;

  // Плохой конструктор.
  public BigObject(
      String param1,
      String param2,
      String param3,
      String param4,
      String param5)
  {
    this.param1 = param1;
    this.param2 = param2;
    this.param3 = param3;
    this.param4 = param4;
    this.param5 = param5;
  }

  private BigObject(BigObjectBuilder builder) {
    this.param1 = builder.param1;
    this.param2 = builder.param2;
    this.param3 = builder.param3;
    this.param4 = builder.param4;
    this.param5 = builder.param5;
  }


  @Override
  public String toString() {
    return "BigObject{" +
        "param1='" + param1 + '\'' +
        ", param2='" + param2 + '\'' +
        ", param3='" + param3 + '\'' +
        ", param4='" + param4 + '\'' +
        ", param5='" + param5 + '\'' +
        '}';
  }

  public static class BigObjectBuilder {
    private String param1; // обязательный
    private String param2;
    private String param3;
    private String param4;
    private String param5;

    BigObjectBuilder(String param1) {
      // Можем проверить обязательные аргументы
      if (param1 == null)
        throw new IllegalArgumentException("param1 cannot be null");

      this.param1 = param1;
    }

    BigObjectBuilder withParam2(String param2) {
      this.param2 = param2;
      return this; //fluent
    }

    BigObjectBuilder withParam3(String param3) {
      // Можем здесь также проверить аргумент
      if (param3.length() < 10)
        throw new IllegalArgumentException("length of param3 must be >= 10");

      this.param3 = param3;
      return this;
    }

    public BigObjectBuilder withParam4(String param4) {
      this.param4 = param4;
      return this;
    }

    BigObjectBuilder withParam5(String param5) {
      this.param5 = param5;
      return this;
    }

    BigObject build() {
      // или return new BigObject(param1, param2, param3, param4, param5);
      return new BigObject(this);
    }
  }
}
