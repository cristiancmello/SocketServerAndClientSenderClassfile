public class Person {
  private String name;
  private String surname;

  public Person(String name, String surname) {
    this.name = name;
    this.surname = surname;
  }

  public static void main(String[] args) {
    System.out.println("Hello, I'm Person!");
  }

  @Override
  public String toString() {
    return "{name: %s, surname: %s}".formatted(name, surname);
  }
}